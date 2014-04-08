package io.tetrapod.protocol.service;

// This is a code generated file.  All edits will be lost the next time code gen is run.

import io.*;
import io.tetrapod.core.rpc.*;
import io.tetrapod.core.serialize.*;
import io.tetrapod.protocol.core.TypeDescriptor;
import io.tetrapod.protocol.core.StructDescription;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * allows an empty request to be called from admin app's service menu
 */

@SuppressWarnings("unused")
public class ServiceCommand extends Structure {
   
   public static final int STRUCT_ID = 5461687;
   public static final int CONTRACT_ID = BaseServiceContract.CONTRACT_ID;
    
   public ServiceCommand() {
      defaults();
   }

   public ServiceCommand(String name, String icon, int contractId, int structId) {
      this.name = name;
      this.icon = icon;
      this.contractId = contractId;
      this.structId = structId;
   }   
   
   public String name;
   public String icon;
   public int contractId;
   public int structId;

   public final Structure.Security getSecurity() {
      return Security.INTERNAL;
   }

   public final void defaults() {
      name = null;
      icon = null;
      contractId = 0;
      structId = 0;
   }
   
   @Override
   public final void write(DataSource data) throws IOException {
      data.write(1, this.name);
      data.write(2, this.icon);
      data.write(3, this.contractId);
      data.write(4, this.structId);
      data.writeEndTag();
   }
   
   @Override
   public final void read(DataSource data) throws IOException {
      defaults();
      while (true) {
         int tag = data.readTag();
         switch (tag) {
            case 1: this.name = data.read_string(tag); break;
            case 2: this.icon = data.read_string(tag); break;
            case 3: this.contractId = data.read_int(tag); break;
            case 4: this.structId = data.read_int(tag); break;
            case Codec.END_TAG:
               return;
            default:
               data.skip(tag);
               break;
         }
      }
   }
   
   public final int getContractId() {
      return ServiceCommand.CONTRACT_ID;
   }

   public final int getStructId() {
      return ServiceCommand.STRUCT_ID;
   }

   public final String[] tagWebNames() {
      // Note do not use this tags in long term serializations (to disk or databases) as 
      // implementors are free to rename them however they wish.  A null means the field
      // is not to participate in web serialization (remaining at default)
      String[] result = new String[4+1];
      result[1] = "name";
      result[2] = "icon";
      result[3] = "contractId";
      result[4] = "structId";
      return result;
   }

   public final Structure make() {
      return new ServiceCommand();
   }

   public final StructDescription makeDescription() {
      StructDescription desc = new StructDescription();
      desc.tagWebNames = tagWebNames();
      desc.types = new TypeDescriptor[desc.tagWebNames.length];
      desc.types[0] = new TypeDescriptor(TypeDescriptor.T_STRUCT, getContractId(), getStructId());
      desc.types[1] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      desc.types[2] = new TypeDescriptor(TypeDescriptor.T_STRING, 0, 0);
      desc.types[3] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      desc.types[4] = new TypeDescriptor(TypeDescriptor.T_INT, 0, 0);
      return desc;
   }
}
