package me.caek.pnbs;

import me.caek.pnbs.music.NoteManager;
import me.caek.pnbs.music.Scales;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("pnbs")
                    .then(ClientCommandManager.literal("scan")
                            .executes(ctx -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                NoteManager noteManager = Mod.getNoteManager();
                                ArrayList<Integer> notes = Scales.generateMajor(2);

                                for (int i = 0; i < notes.size(); i++) {
                                    notes.set(i, notes.get(i));
                                }

                                //ArrayList<Integer> notes = new ArrayList<>();
                                //notes.add(1);
                                //notes.add(2);
                                noteManager.findNoteBlocks(mc, notes, 5);
                                noteManager.tuneNoteBlocks(mc);

                                for (Integer note : notes) {
                                    noteManager.playNote(mc, note);
                                }
                                return 0;
                            }))
            );
        });
    }
}