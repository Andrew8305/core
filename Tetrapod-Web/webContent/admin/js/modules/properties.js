define(function(require) {
   var $ = require("jquery");
   var ko = require("knockout");
   var Alert = require("alert");
   return Properties; // not using new means this returns a constructor function (ie class)

   function Properties(app) {
      var self = this;

      self.props = ko.observableArray([]);
      self.importText = ko.observable("");
      self.importSecret = ko.observable(false);

      self.findProperty = findProperty;
      self.clear = clear;
      self.addClusterProperty = addClusterProperty;
      self.addProperty = addProperty;
      self.showImportClusterPropertiesDialog = showImportClusterPropertiesDialog;
      self.importClusterProperties = importClusterProperties;

      function clear() {
         self.props.removeAll();
      }

      function findProperty(key) {
         var arr = self.props();
         for (var i = 0; i < arr.length; i++) {
            if (arr[i].key == key) {
               return arr[i];
            }
         }
         return null;
      }

      function addClusterProperty(secret) {
         Alert.prompt("Enter a new key name", function(key) {
            if (key && key.trim().length > 0) {
               addProperty(key, '', secret)
            }
         });
      }

      function addProperty(key, val, secret) {
         app.sendAny("SetClusterProperty", {
            property: {
               key: key,
               val: val,
               secret: secret
            }
         }, app.alertResponse);
      }

      function showImportClusterPropertiesDialog() {
         self.importText("");
         self.importSecret();
         $('#importPropertiesModal').modal('show');
      }

      function importClusterProperties() {
         console.log("importing cluster properties....");
         var res = self.importText().split("\n");
         var secret = self.importSecret();
         for (var i = 0; i < res.length; i++) {
            if (!res[i].startsWith("#")) {
               var item = res[i].split("=");
               if (item.length == 2 && item[0].length > 0) {
                  //console.log("IMPORTING: " + item[0] + " = " + item[1]);
                  if (item[0].startsWith("*") && item[0].length > 1) {
                     addProperty(item[0].substring(1), item[1], true);
                  } else {
                     addProperty(item[0], item[1], secret);
                  }
               }
            }
         }
         $('#importPropertiesModal').modal('hide');
      }

      app.server.addMessageHandler("ClusterPropertyAdded", function(msg) {
         var p = self.findProperty(msg.property.key);
         if (p) {
            self.props.remove(p);
         }
         p = new Property(msg.property);
         self.props.push(p);
         self.props.sort(compareProperties);

         if (msg.property.key == "maintenanceMode") {
            app.cluster.hosts.maintenanceMode(true);
         } else if (msg.property.key == "identity.valid.builds") {
            app.cluster.hosts.currentClientBuild(msg.property.val);
         }
      });

      app.server.addMessageHandler("ClusterPropertyRemoved", function(msg) {
         var p = self.findProperty(msg.key);
         if (p) {
            self.props.remove(p);
         }
         if (p.key == "maintenanceMode") {
            app.cluster.hosts.maintenanceMode(false);
         }
      });

      function compareProperties(a, b) {
         return a.key.localeCompare(b.key);
      }

      // Property Model
      function Property(prop) {
         var self = this;

         self.key = prop.key;
         self.secret = prop.secret;
         self.val = ko.observable(prop.val);

         self.editValue = editValue;
         self.deleteProp = deleteProp;
         self.deletePropNoConfirm = deletePropNoConfirm;

         function editValue() {
            Alert.prompt("Enter a new value", function(val) {
               if (val && val.trim().length > 0) {
                  app.sendAny("SetClusterProperty", {
                     property: {
                        key: self.key,
                        val: val,
                        secret: self.secret
                     }
                  }, app.alertResponse);
               }
            }, self.val());
         }

         function deletePropNoConfirm() {
            app.sendAny("DelClusterProperty", {
               key: self.key
            }, app.alertResponse);
         }

         function deleteProp() {
            Alert.confirm("Are you sure you want to delete '" + self.key + "'?", function() {
               deletePropNoConfirm();
            });
         }
      }

   }

});
