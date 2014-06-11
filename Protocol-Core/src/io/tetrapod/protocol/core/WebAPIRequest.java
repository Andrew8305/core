package io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.protocol.core.TypeDescriptor;
import io.tetrapod.protocol.core.StructDescription;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class WebAPIRequest extends Request {

   public static final int STRUCT_ID = 9321342;
   public static final int CONTRACT_ID = CoreContract.CONTRACT_ID;
   
   public WebAPIRequest() {
      defaults();
   }

   public WebAPIRequest(String route, String params, String body) {
      this.route = route;
      this.params = params;
      this.body = body;
   }   

   /**
    * route name
    */
   public String route;
   
   /**
    * json string
    */
   public String params;
   
   /**
    * json string
    */
   public String body;

   public final Structure.Security getSecurity() {
      return Security.PUBLIC;
   }

   public final void defaults() {
      route = null;
      params = null;
      body = null;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.route);
      data.write(2, this.params);
      data.write(3, this.body);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.route = data.read_string(tag); break;
            case 2: this.params = data.read_string(tag); break;
            case 3: this.body = data.read_string(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   public final int getContractId() {
      return WebAPIRequest.CONTRACT_ID;
   }

   public final int getStructId() {
      return WebAPIRequest.STRUCT_ID;
   }
   
   @Override
   public final Response dispatch(ServiceAPI is, RequestContext ctx) {
      if (is instanceof Handler)
         return ((Handler)is).requestWebAPI(this, ctx);
      return is.genericRequest(this, ctx);
   }
   
   public static interface Handler extends ServiceAPI {
      Response requestWebAPI(WebAPIRequest r, RequestContext ctx);
   }
   
   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[3+1];
      result[1] = "route";
      result[2] = "params";
      result[3] = "body";
      return result;
   }
   
   public final Structure make() {
      return new WebAPIRequest();
   }
   
   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      desc.types[3] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      return desc;
   }

}
