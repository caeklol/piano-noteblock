package me.caek.pnbs;

import me.caek.pnbs.music.NoteManager;
import me.caek.pnbs.music.Scales;
import me.caek.pnbs.piano.MidiManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("pnbs")
                    .then(ClientCommandManager.literal("scan")
                            .executes(ctx -> {
                                MinecraftClient mc = MinecraftClient.getInstance();

                                if (mc.player == null) return 1; // what

                                try {
                                    int found = MidiManager.scanForDevices();
                                    mc.player.sendMessage(Text.of(found + " device(s) found"));
                                } catch (MidiUnavailableException e) {
                                    mc.player.sendMessage(Text.of(e.getLocalizedMessage()));
                                }
                                return 0;
                            }))
                    .then(ClientCommandManager.literal("close")
                            .executes(ctx -> {
                                MidiManager.closeAllDevices();
                                return 0;
                            }))
                    .then(ClientCommandManager.literal("tune")
                            .executes(ctx -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                NoteManager nm = Mod.getNoteManager();

                                ArrayList<Integer> requiredNotes = Scales.generate(List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 2);

                                nm.findNoteBlocks(mc, requiredNotes, 5);
                                nm.tuneNoteBlocks(mc);
                                return 0;
                            }))
                    .then(ClientCommandManager.literal("test")
                            .executes(ctx -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                NoteManager nm = Mod.getNoteManager();

                                ArrayList<Integer> requiredNotes = Scales.generate(List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 4);

                                for (Integer note : requiredNotes) {
                                    nm.playNote(mc, note);
                                }
                                return 0;
                            }))
            );
        });
    }
}