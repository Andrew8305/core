package io.tetrapod.protocol.web;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.protocol.core.TypeDescriptor;
import io.tetrapod.protocol.core.StructDescription;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("all")
public class ClientSessionsResponse extends Response {
   
   public static final int STRUCT_ID = 2637706;
   public static final int CONTRACT_ID = WebContract.CONTRACT_ID;
   public static final int SUB_CONTRACT_ID = WebContract.SUB_CONTRACT_ID;

   public ClientSessionsResponse() {
      defaults();
   }

   public ClientSessionsResponse(int[] clientSessions) {
      this.clientSessions = clientSessions;
   }   
   
   public int[] clientSessions;

   public final Structure.Security getSecurity() {
      return Security.INTERNAL;
   }

   public final void defaults() {
      clientSessions = null;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      if (this.clientSessions != null) data.write(1, this.clientSessions);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.clientSessions = data.read_int_array(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
  
   public final int getContractId() {
      return ClientSessionsResponse.CONTRACT_ID;
   }

   public final int getSubContractId() {
      return ClientSessionsResponse.SUB_CONTRACT_ID;
   }

   public final int getStructId() {
      return ClientSessionsResponse.STRUCT_ID;
   }

   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[1+1];
      result[1] = "clientSessions";
      return result;
   }

   public final Structure make() {
      return new ClientSessionsResponse();
   }

   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();      
      desc.name = "ClientSessionsResponse";
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_INT_LIST, 0, 0);
      return desc;
   }
 }
