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
public class BuildCommand extends Structure {
   
   public static final int BUILD = 1; 
   public static final int DEPLOY = 2; 
   public static final int LAUNCH = 3; 
   public static final int DEPLOY_LATEST = -1; 
   public static final int LAUNCH_DEPLOYED = -1; 
   
   public static final int STRUCT_ID = 4239258;
   public static final int CONTRACT_ID = TetrapodContract.CONTRACT_ID;
    
   public BuildCommand() {
      defaults();
   }

   public BuildCommand(String serviceName, int build, int command) {
      this.serviceName = serviceName;
      this.build = build;
      this.command = command;
   }   
   
   public String serviceName;
   public int build;
   public int command;

   public final Structure.Security getSecurity() {
      return Security.ADMIN;
   }

   public final void defaults() {
      serviceName = null;
      build = 0;
      command = 0;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.serviceName);
      data.write(2, this.build);
      data.write(3, this.command);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.serviceName = data.read_string(tag); break;
            case 2: this.build = data.read_int(tag); break;
            case 3: this.command = data.read_int(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   public final int getContractId() {
      return BuildCommand.CONTRACT_ID;
   }

   public final int getStructId() {
      return BuildCommand.STRUCT_ID;
   }

   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[3+1];
      result[1] = "serviceName";
      result[2] = "build";
      result[3] = "command";
      return result;
   }

   public final Structure make() {
      return new BuildCommand();
   }

   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[3] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      return desc;
   }
}