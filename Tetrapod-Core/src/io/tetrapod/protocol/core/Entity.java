package io.tetrapod.protocol.core;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.core.serialize.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class Entity extends Structure {
   
   public static final byte TYPE_TETRAPOD = 1; 
   public static final byte TYPE_SERVICE = 2; 
   public static final byte TYPE_ADMIN = 3; 
   public static final byte TYPE_CLIENT = 4; 
   public static final byte TYPE_ANONYMOUS = 5; 
   public static final int STATUS_INIT = 1; 
   public static final int STATUS_PAUSED = 2; 
   public static final int STATUS_GONE = 4; 
   public static final int STATUS_BUSY = 8; 
   public static final int STATUS_OVERLOADED = 16; 
   
   public static final int STRUCT_ID = 10171140;
    
   public Entity() {
      defaults();
   }

   public Entity(int entityId, int parentId, long reclaimToken, String host, int status, byte type, String name, int build, int version) {
      this.entityId = entityId;
      this.parentId = parentId;
      this.reclaimToken = reclaimToken;
      this.host = host;
      this.status = status;
      this.type = type;
      this.name = name;
      this.build = build;
      this.version = version;
   }   
   
   public int entityId;
   public int parentId;
   public long reclaimToken;
   public String host;
   public int status;
   public byte type;
   public String name;
   public int build;
   public int version;

   public final Structure.Security getSecurity() {
      return Security.PUBLIC;
   }

  public final void defaults() {
      entityId = 0;
      parentId = 0;
      reclaimToken = 0;
      host = null;
      status = 0;
      type = 0;
      name = null;
      build = 0;
      version = 0;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.entityId);
      data.write(2, this.parentId);
      data.write(3, this.reclaimToken);
      data.write(4, this.host);
      data.write(5, this.status);
      data.write(6, this.type);
      data.write(7, this.name);
      data.write(8, this.build);
      data.write(9, this.version);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.entityId = data.read_int(tag); break;
            case 2: this.parentId = data.read_int(tag); break;
            case 3: this.reclaimToken = data.read_long(tag); break;
            case 4: this.host = data.read_string(tag); break;
            case 5: this.status = data.read_int(tag); break;
            case 6: this.type = data.read_byte(tag); break;
            case 7: this.name = data.read_string(tag); break;
            case 8: this.build = data.read_int(tag); break;
            case 9: this.version = data.read_int(tag); break;
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
      return Entity.STRUCT_ID;
   }
   
   public static Callable<Structure> getInstanceFactory() {
      return new Callable<Structure>() {
         public Structure call() { return new Entity(); }
      };
   }
}