package io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.core.rpc.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class EntityRegisteredMessage extends Message {
   
   public static final int STRUCT_ID = 1454035;
    
   public EntityRegisteredMessage() {
      defaults();
   }

   public EntityRegisteredMessage(Entity entity, FlatTopic[] topics) {
      this.entity = entity;
      this.topics = topics;
   }   
   
   public Entity entity;
   public FlatTopic[] topics;

   public final Structure.Security getSecurity() {
      return Security.INTERNAL;
   }

   public final void defaults() {
      entity = null;
      topics = null;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      if (this.entity != null) data.write(1, this.entity);
      if (this.topics != null) data.write(2, this.topics);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.entity = data.read_struct(tag, Entity.class); break;
            case 2: this.topics = data.read_struct_array(tag, FlatTopic.class); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   @Override
   public final int getStructId() {
      return EntityRegisteredMessage.STRUCT_ID;
   }
   
   @Override
   public final void dispatch(SubscriptionAPI api) {
      if (api instanceof Handler)
         ((Handler)api).messageEntityRegistered(this);
      else
         api.genericMessage(this);
   }
   
   public static interface Handler extends SubscriptionAPI {
      void messageEntityRegistered(EntityRegisteredMessage m);
   }
   
   public static Callable<Structure> getInstanceFactory() {
      return new Callable<Structure>() {
         public Structure call() { return new EntityRegisteredMessage(); }
      };
   }
   
   public final int getContractId() {
      return TetrapodContract.CONTRACT_ID;
   }
}