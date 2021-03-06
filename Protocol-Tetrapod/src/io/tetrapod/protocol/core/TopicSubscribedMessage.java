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

@SuppressWarnings("all")
public class TopicSubscribedMessage extends Message {
   
   public static final int STRUCT_ID = 1498241;
   public static final int CONTRACT_ID = TetrapodContract.CONTRACT_ID;
   public static final int SUB_CONTRACT_ID = TetrapodContract.SUB_CONTRACT_ID;

   public TopicSubscribedMessage() {
      defaults();
   }

   public TopicSubscribedMessage(int publisherId, int topicId, int entityId, int childId, boolean once) {
      this.publisherId = publisherId;
      this.topicId = topicId;
      this.entityId = entityId;
      this.childId = childId;
      this.once = once;
   }   
   
   public int publisherId;
   public int topicId;
   public int entityId;
   public int childId;
   public boolean once;

   public final Structure.Security getSecurity() {
      return Security.INTERNAL;
   }

   public final void defaults() {
      publisherId = 0;
      topicId = 0;
      entityId = 0;
      childId = 0;
      once = false;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.publisherId);
      data.write(2, this.topicId);
      data.write(3, this.entityId);
      data.write(4, this.childId);
      data.write(5, this.once);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.publisherId = data.read_int(tag); break;
            case 2: this.topicId = data.read_int(tag); break;
            case 3: this.entityId = data.read_int(tag); break;
            case 4: this.childId = data.read_int(tag); break;
            case 5: this.once = data.read_boolean(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   public final int getContractId() {
      return TopicSubscribedMessage.CONTRACT_ID;
   }

   public final int getSubContractId() {
      return TopicSubscribedMessage.SUB_CONTRACT_ID;
   }

   public final int getStructId() {
      return TopicSubscribedMessage.STRUCT_ID;
   }
   
   @Override
   public final void dispatch(SubscriptionAPI api, MessageContext ctx) {
      if (api instanceof Handler)
         ((Handler)api).messageTopicSubscribed(this, ctx);
      else
         api.genericMessage(this, ctx);
   }
   
   public static interface Handler extends SubscriptionAPI {
      void messageTopicSubscribed(TopicSubscribedMessage m, MessageContext ctx);
   }
   
   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[5+1];
      result[1] = "publisherId";
      result[2] = "topicId";
      result[3] = "entityId";
      result[4] = "childId";
      result[5] = "once";
      return result;
   }
   
   public final Structure make() {
      return new TopicSubscribedMessage();
   }
   
   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();     
      desc.name = "TopicSubscribedMessage";
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[3] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[4] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[5] = new TypeDescriptor(TypeDescriptor.T_BOOLEAN, 0, 0);
      return desc;
   }
}
