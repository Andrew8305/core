package io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.protocol.core.TypeDescriptor;
import io.tetrapod.protocol.core.StructDescription;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * notification of an existing tetrapod cluster member
 */

@SuppressWarnings("unused")
public class ClusterMemberMessage extends Message {
   
   public static final int STRUCT_ID = 1076508;
   public static final int CONTRACT_ID = TetrapodContract.CONTRACT_ID;
    
   public ClusterMemberMessage() {
      defaults();
   }

   public ClusterMemberMessage(int entityId, ServerAddress[] cluster) {
      this.entityId = entityId;
      this.cluster = cluster;
   }   
   
   public int entityId;
   public ServerAddress[] cluster;

   public final Structure.Security getSecurity() {
      return Security.INTERNAL;
   }

   public final void defaults() {
      entityId = 0;
      cluster = null;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.entityId);
      if (this.cluster != null) data.write(2, this.cluster);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.entityId = data.read_int(tag); break;
            case 2: this.cluster = data.read_struct_array(tag, new ServerAddress()); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   public final int getContractId() {
      return ClusterMemberMessage.CONTRACT_ID;
   }

   public final int getStructId() {
      return ClusterMemberMessage.STRUCT_ID;
   }
   
   @Override
   public final void dispatch(SubscriptionAPI api, MessageContext ctx) {
      if (api instanceof Handler)
         ((Handler)api).messageClusterMember(this, ctx);
      else
         api.genericMessage(this, ctx);
   }
   
   public static interface Handler extends SubscriptionAPI {
      void messageClusterMember(ClusterMemberMessage m, MessageContext ctx);
   }
   
   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[2+1];
      result[1] = "entityId";
      result[2] = "cluster";
      return result;
   }
   
   public final Structure make() {
      return new ClusterMemberMessage();
   }
   
   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_STRUCT_LIST, ServerAddress.CONTRACT_ID, ServerAddress.STRUCT_ID);
      return desc;
   }
}