package  io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import java.util.*;
import io.tetrapod.core.*;

/**
 * The core tetrapod service
 */

@SuppressWarnings("unused")
public class TetrapodContract extends Contract {
   public static final int VERSION = 1;
   public static final String NAME = "Tetrapod";
   
   /**
    * hardcoded contract id, < 20 is reserved for core contracts
    */
   public static final int CONTRACT_ID = 1; 
   
   public static interface API extends
      RegisterRequest.Handler
      {}
   
   public void addRequests(StructureFactory factory, int dynamicId) {
      factory.add(dynamicId, RegisterRequest.STRUCT_ID, RegisterRequest.getInstanceFactory());
   }
   
   public void addResponses(StructureFactory factory, int dynamicId) {
      factory.add(dynamicId, RegisterResponse.STRUCT_ID, RegisterResponse.getInstanceFactory());
   }
   
   public void addMessages(StructureFactory factory, int dynamicId) {
      
   }
   
   public String getName() {
      return TetrapodContract.NAME;
   } 

   public static class ServiceInfo extends Contract {
      public static interface API extends
         ServiceAddedMessage.Handler
         {}
         
      public void addMessages(StructureFactory factory, int dynamicId) {
         factory.add(dynamicId, ServiceAddedMessage.STRUCT_ID, ServiceAddedMessage.getInstanceFactory());
      }
      
      public String getName() {
         return TetrapodContract.NAME;
      } 
   }
      
}