package io.tetrapod.core.rpc;

import io.tetrapod.protocol.core.*;

abstract public class RequestContext {

   public final RequestHeader header;

   public RequestContext(RequestHeader header) {
      this.header = header;
   }

   abstract public Response securityCheck(Request request, int accountId, String authToken, int adminRightsRequired);

   abstract public Response securityCheck(Request request);

   abstract public void handlePendingResponse(Response res, RequestHeader header);

   public boolean isFromClient() {
      return header.fromType == Core.TYPE_CLIENT;
   }

   public boolean isFromService() {
      return header.fromType == Core.TYPE_SERVICE;
   }
   
   public void respondWith(Response res) {
      assert res != Response.PENDING;
      handlePendingResponse(res, header);
   }
   
   public void respondWith(int errorCode) {
      handlePendingResponse(Response.error(errorCode), header);
   }

}
