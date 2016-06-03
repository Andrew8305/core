package io.tetrapod.core.rpc;

import static io.tetrapod.protocol.core.CoreContract.*;

import io.tetrapod.core.Session;
import io.tetrapod.core.rpc.Structure.Security;
import io.tetrapod.core.utils.*;
import io.tetrapod.core.utils.LoginAuthToken.DecodedSession;
import io.tetrapod.protocol.core.*;

public class SessionRequestContext extends RequestContext {

   private final static boolean USE_SECURITY = true;

   public final Session         session;

   public SessionRequestContext(RequestHeader header, Session session) {
      super(header);
      this.session = session;
   }

   @Override
   public void handlePendingResponse(Response res, int originalRequestId) {
      assert res != Response.PENDING;
      session.sendResponse(res, originalRequestId);
   }

   @Override
   public Response securityCheck(Request request) {
      if (USE_SECURITY) {
         Security mine = request.getSecurity();
         Security theirs = getSenderSecurity();
         if (theirs.ordinal() < mine.ordinal())
            return Response.error(ERROR_INVALID_RIGHTS);
      }
      return null;
   }

   @Override
   public Response securityCheck(Request request, int accountId, String authToken) {
      if (USE_SECURITY) {
         Value<Integer> error = new Value<>(ERROR_INVALID_RIGHTS);
         Security mine = request.getSecurity();
         Security theirs = getSenderSecurity(accountId, authToken, error);

         if (mine == Security.ADMIN) {
            AdminAuthToken.validateAdminToken(accountId, authToken, request.getRequiredAdminRights());
            theirs = Security.ADMIN;         
         }
         if (theirs.ordinal() < mine.ordinal())
            return new Error(error.get());
      }
      return null;
   }

   private Security getSenderSecurity() {
      if (header.fromType == Core.TYPE_TETRAPOD || header.fromType == Core.TYPE_SERVICE)
         return Security.INTERNAL;
      return Security.PUBLIC;
   }

   private Security getSenderSecurity(int accountId, String authToken, Value<Integer> errorCode) {
      Security senderSecurity = getSenderSecurity();
      if (senderSecurity == Security.PUBLIC) {
         // upgrade them to protected if their token is good
         DecodedSession d = LoginAuthToken.decodeSessionToken(authToken, accountId, header.fromParentId);
         if (d != null && d.timeLeft >= 0) {
            senderSecurity = Security.PROTECTED;
         } else {
            errorCode.set((d != null && d.timeLeft < 0) ? ERROR_RIGHTS_EXPIRED : ERROR_SECURITY);
         }
      }
      return senderSecurity;
   }

}
