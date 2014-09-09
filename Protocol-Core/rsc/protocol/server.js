define([], function() {
   return TP_Server;
});

function TP_Server() {
   var self = this;

   // private vars
   var protocol = {
      request : {},
      response : {},
      struct : {},
      message : {},
      consts : {},
      reverseMap : {}
   };
   var requestCounter = 0;
   var requestHandlers = [];
   var messageHandlers = [];
   var openHandlers = [];
   var closeHandlers = [];
   var socket;
   var simulator = null;
   var lastHeardFrom = 0;
   var lastSpokeTo = 0;
   var keepAliveRequestId;

   // public interface
   self.forceLongPolling = false;
   self.commsLog = false;
   self.commsLogKeepAlives = false;
   self.register = register;
   self.registerConst = registerConst;
   self.addMessageHandler = addMessageHandler;
   self.send = send;
   self.sendRequest = sendRequest;
   self.connect = connect;
   self.disconnect = disconnect;
   self.nameOf = nameOf;
   self.consts = protocol.consts;
   self.connected = false;
   self.polling = false;
   self.setSimulator = function(s) {
      simulator = s;
   };

   for (var i = 0; i < arguments.length; i++) {
      new arguments[i](self);
   }

   function register(type, contractName, structName, contractId, structId) {
      var map = protocol[type];
      var val = {
         contractId : contractId,
         structId : structId
      };
      map[contractName + "." + structName] = val;
      if (map[structName]) {
         map[structName] = 0;
      } else {
         map[structName] = val;
      }
      protocol.reverseMap["" + contractId + "." + structId] = contractName + "." + structName;
   }

   function registerConst(contractName, structName, constName, constValue) {
      var map = protocol["consts"];
      var o;
      if (structName != null && structName != "null") {
         o = map[contractName + "." + structName] || {};
         map[contractName + "." + structName] = o;
      } else {
         o = map[contractName] || {};
         map[contractName] = o;
      }
      o[constName] = constValue;
   }

   function consts(name) {
      return protocol["consts"][name];
   }

   function nameOf(arg1, arg2) {
      // call with either a single arg = object with _contractId and _structId
      // or with explicit contractId, structId
      var key;
      if (typeof arg2 !== 'undefined') {
         key = "" + arg1 + "." + arg2;
      } else {
         key = "" + arg1._contractId + "." + arg1._structId;
      }
      return protocol.reverseMap[key];
   }

   function addMessageHandler(message, handler) {
      var val = protocol.message[message];
      if (!val) {
         console.log("unknown message: " + message);
         return;
      }
      if (val === 0) {
         console.log("ambiguous message: " + message);
         return;
      }
      messageHandlers[val.contractId + "." + val.structId] = handler;
   }

   function send(request, args, toId) {
      var val = protocol.request[request];
      if (!val) {
         console.log("unknown request: " + request);
         return;
      }
      if (val === 0) {
         console.log("ambiguous request: " + request);
         return;
      }
      return sendRequest(val.contractId, val.structId, args, toId);
   }

   function sendRequest(contractId, structId, args, toId) {
      toId = typeof toId !== 'undefined' ? toId : 0;
      var requestId = requestCounter++;
      args._requestId = requestId;
      args._contractId = contractId;
      args._structId = structId;
      args._toId = toId;
      if (isKeepAlive(contractId, structId) && !self.commsLogKeepAlives) {
         keepAliveRequestId = requestId;
      } else {
         if (self.commsLog)
            logRequest(args);
      }

      if (simulator != null) {
         var resp = simulator.request(request, args, toId);
         var i;
         for (i = 0; i < resp.messages.length; i++) {
            var mess = resp.messages[i];
            handleMessage(mess);
         }
         return {
            handle : function(func) {
               requestHandlers[requestId] = func;
               handleResponse(resp.response);
            }
         }
      }

      if (self.polling) {
         lastSpokeTo = Date.now();
         sendRPC(args);
      } else {
         if (isConnected() || self.polling) {
            var data = JSON.stringify(args, null, 3);
            if (data.length < 1024 * 128) {
               lastSpokeTo = Date.now();
               socket.send(data);
            } else {
               console.log("RPC too big : " + data.length + "\n" + data);
               // FIXME: return an error
            }
         }
      }

      return {
         handle : function(func) {
            requestHandlers[requestId] = func;
         }
      }
   }

   function isConnected() {
      return (socket && socket.readyState == WebSocket.OPEN);
   }

   function connect(server, secure, port) {
      port = typeof port !== 'undefined' ? port : window.location.port;
      if (!window.WebSocket) {
         window.WebSocket = window.MozWebSocket;
      }

      // fall back to long polling if we have no WebSocket support
      if (!window.WebSocket || self.forceLongPolling) {
         return startPollingSession(server, secure);
      }

      if (simulator != null) {
         return {
            listen : function(onOpen, onClose) {
               onOpen();
            }
         }
      }

      // support for websocket spec 76 and make sure we're closed before unloading the page
      // or else we crash on iOS
      window.onbeforeunload = function() {
         disconnect();
      };

      var url = (secure ? "wss:" : "ws:") + "//" + server + (port ? ":" + port : "") + "/sockets";
      console.log("Connecting to: " + url);
      socket = new WebSocket(url);
      socket.onopen = onSocketOpen;
      socket.onmessage = onSocketMessage;
      socket.onclose = onSocketClose;
      socket.onerror = onSocketError;
      return {
         listen : function(onOpen, onClose) {
            openHandlers = [];
            closeHandlers = [];
            openHandlers.push(onOpen);
            closeHandlers.push(onClose);
         }
      }
   }

   function disconnect() {
      if (socket) {
         if (self.commsLog)
            console.log("Disconnecting... " + socket.readyState);
         var closeHack = (socket.readyState == WebSocket.CLOSING);
         if (closeHack) {
            // some sort of bug here when waking from sleep, it can take a very long long time
            // to transition from CLOSING to CLOSED and call our close handler, so we
            // take away the onclose handler and call it manually
            socket.onclose = null;
         }
         socket.close();
         if (self.commsLog)
            console.log("socket.close() called: " + socket.readyState);
         if (closeHack) { // call onSocketClose manually
            onSocketClose();
         }
      }
   }

   function logResponse(result) {
      var str = '[' + result._requestId + '] <- ' + nameOf(result) + ' ' + JSON.stringify(result, dropUnderscored);
      if (result.isError()) {
         console.warn(str);
      } else {
         console.debug(str);
      }
   }

   function logRequest(result) {
      console.debug('[' + result._requestId + '] => ' + nameOf(result) + ' ' + JSON.stringify(result, dropUnderscored));
   }

   function logMessage(result) {
      console.debug('[M:' + result._topicId + '] <- ' + nameOf(result) + ' ' + JSON.stringify(result, dropUnderscored));
   }

   function dropUnderscored(key, value) {
      if (typeof key == 'string' || key instanceof String) {
         if (key.indexOf("_") == 0)
            return undefined;
      }
      return value;
   }

   function handleResponse(result) {
      result.isError = function() {
         return result._contractId == 1 && result._structId == 1;
      };
      if (self.commsLog && result._requestId != keepAliveRequestId)
         logResponse(result);
      var func = requestHandlers[result._requestId];
      if (func) {
         func(result);
      }
   }

   function handleMessage(result) {
      if (self.commsLog)
         logMessage(result);
      var func = messageHandlers[result._contractId + "." + result._structId];
      if (func) {
         func(result);
      }
   }

   // --- socket methods

   function onSocketOpen(event) {
      lastHeardFrom = Date.now();
      self.connected = true;
      if (self.commsLog)
         console.log("[socket] open: " + socket.URL);
      var i, array = openHandlers;
      for (i = 0; i < array.length; i++)
         array[i]();

      self.keepAlive = setInterval(function() {
         var elapsedHeard = Date.now() - lastHeardFrom;
         var elapsedSpoke = Date.now() - lastSpokeTo;
         if (elapsedSpoke > 6000) {
            // this keep alive is a backup
            send("KeepAlive", {}, 1/* Core.DIRECT */);
         }
         if (elapsedHeard > 6000) {
            console.debug("We haven't heard from the server in " + elapsedHeard + " ms")
         }
         if (elapsedHeard > 20000) {
            disconnect();
         }
      }, 5000);

   }

   function onSocketClose(event) {
      self.connected = false;
      if (self.keepAlive != null) {
         clearInterval(self.keepAlive);
         self.keepAlive = null;
      }
      if (self.commsLog)
         console.log("[socket] closed")
      var i, array = closeHandlers;
      for (i = 0; i < array.length; i++)
         array[i]();

      // terminate all pending requests (only if using websockets I think...)      
      //      for ( var requestId in requestHandlers) {
      //         handleResponse({
      //            _requestId : requestId,
      //            _contractId : 1,
      //            _structId : 1,
      //            code : 7
      //         });
      //      }
   }

   function onSocketMessage(event) {
      lastHeardFrom = Date.now();
      var result = null;
      if (event.data.indexOf("{") == 0) {
         result = JSON.parse(event.data);
      } else {
         // TODO figure out how to communicate errors
         result = parseInt(event.data);
      }
      if (result._requestId != null) {
         handleResponse(result);
      } else if (result._topicId != null) {
         handleMessage(result);
      }
   }

   function onSocketError(event) {
      if (self.commsLog)
         console.log("[socket] error: " + JSON.stringify(event));
   }

   function isKeepAlive(contractId, structId) {
      return (contractId == 1 && structId == 5512920) || (contractId == 10 && structId == 15966706);
   }

   // ------------------------ long polling fall-back ----------------------------- //

   function startPollingSession(host, secure) {
      self.polling = true;
      self.pollPending = false;
      self.connected = true;
      self.entityInfo = null;
      lastHeardFrom = Date.now();
      if (self.commsLog)
         console.log("[poller] open: " + host);
      return {
         listen : function(onOpen, onClose) {
            onOpen();
         }
      }
   }

   function sendRPC(data) {
      if (self.entityInfo != null) {
         data._token = self.entityInfo.token;
      }
      //console.debug("SEND RPC: " + JSON.stringify(data));
      $.ajax({
         type : "POST",
         url : "/poll",
         timeout : 24000,
         data : JSON.stringify(data),
         dataType : 'json',
         success : function(data) {
            self.connected = true;
            handleResponse(data);
            schedulePoll(100);
         },
         error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.error(textStatus + " (" + errorThrown + ")");
            if (textStatus == 'timeout') {
               handleResponse({
                  _requestId : data._requestId,
                  _contractId : 1,
                  _structId : 1,
                  code : 3
               });
            } else {
               handleResponse({
                  _requestId : data._requestId,
                  _contractId : 1,
                  _structId : 1,
                  code : 1
               });
            }
            onSocketClose(); // do a fake socket close event
         }
      });
   }

   function schedulePoll(millis) {
      setTimeout(function() {
         poll()
      }, millis);
   }

   function poll() {
      if (!self.polling)
         return;
      if (self.entityInfo != null && self.entityInfo.entityId != 0) {
         if (self.pollPending == false) {
            var data = {
               _token : self.entityInfo.token
            };
            console.debug("POLL -> " + JSON.stringify(data));
            self.pollPending = true;
            $.ajax({
               type : "POST",
               url : "/poll",
               timeout : 12000,
               data : JSON.stringify(data),
               dataType : 'json',
               success : function(data) {
                  self.connected = true;
                  self.pollPending = false;
                  if (data.error) {
                     console.debug("POLL <- ERROR: " + data.error);
                     schedulePoll(1000);
                  } else {
                     console.debug("POLL <- " + data.messages.length + " items");
                     $.each(data.messages, function(i, m) {
                        handleMessage(m)
                     });
                     schedulePoll(100);
                  }
               },
               error : function(XMLHttpRequest, textStatus, errorThrown) {
                  console.error(textStatus + " (" + errorThrown + ")");
                  self.pollPending = false;
                  onSocketClose(); // fake socket close event 
               }
            });
         }
      }
   }

}
