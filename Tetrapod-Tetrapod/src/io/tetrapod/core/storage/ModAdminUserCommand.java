package io.tetrapod.core.storage;

import io.tetrapod.core.serialize.datasources.IOStreamDataSource;
import io.tetrapod.protocol.core.*;
import io.tetrapod.raft.Command;

import java.io.*;

public class ModAdminUserCommand implements Command<TetrapodStateMachine> {
   public static final int COMMAND_ID = TetrapodStateMachine.MOD_ADMIN_COMMAND_ID;

   private Admin           admin;

   public ModAdminUserCommand() {}

   public ModAdminUserCommand(Admin admin) {
      this.admin = admin;
   }

   @Override
   public void applyTo(TetrapodStateMachine state) {
      state.modifyAdminUser(admin);
   }

   @Override
   public void write(DataOutputStream out) throws IOException {
      admin.write(IOStreamDataSource.forWriting(out));
   }

   @Override
   public void read(DataInputStream in, int fileVersion) throws IOException {
      admin = new Admin();
      admin.read(IOStreamDataSource.forReading(in));
   }

   @Override
   public int getCommandType() {
      return COMMAND_ID;
   }

   public Admin getAdminUser() {
      return admin;
   }

   public static void register(TetrapodStateMachine state) {
      state.registerCommand(COMMAND_ID, () -> new ModAdminUserCommand());
   }
}
