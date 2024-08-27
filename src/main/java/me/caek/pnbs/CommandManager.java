package me.caek.pnbs;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
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
                                if (mc.player == null) return 1;

                                try {
                                    int found = MidiManager.scanForDevices();
                                    mc.player.sendMessage(Text.of(found + " device(s) found"));
                                    List<String> deviceNames = MidiManager.getDeviceNames();
                                    for (String device : deviceNames) {
                                        mc.player.sendMessage(Text.of("Device: " + device));
                                    }
                                } catch (MidiUnavailableException e) {
                                    mc.player.sendMessage(Text.of("An error occured:"));
                                    mc.player.sendMessage(Text.of(e.getLocalizedMessage()));
                                }
                                return 0;
                            }))
                    .then(ClientCommandManager.literal("close")
                            .executes(ctx -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                if (mc.player == null) return 1;

                                MidiManager.closeAllDevices();
                                mc.player.sendMessage(Text.of("All devices closed!"));
                                return 0;
                            }))
                    .then(ClientCommandManager.literal("offset")
                            .then(ClientCommandManager.argument("offset", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int offset = ctx.getArgument("offset", Integer.class);
                                    MinecraftClient mc = MinecraftClient.getInstance();

                                    if (mc.player == null) return 1;
                                    Mod.getNoteManager().setNoteOffset(offset);
                                    mc.player.sendMessage(Text.of("Set note offset!"));
                                    return 0;
                                })))
                    .then(ClientCommandManager.literal("tune")
                            .then(ClientCommandManager.argument("octaves", IntegerArgumentType.integer(1, 6))
                                .executes(ctx -> {
                                    int octaves = ctx.getArgument("octaves", Integer.class);
                                    MinecraftClient mc = MinecraftClient.getInstance();

                                    if (mc.player == null) return 1;
                                    mc.player.sendMessage(Text.of("Tuning..."));

                                    NoteManager nm = Mod.getNoteManager();

                                    ArrayList<Integer> requiredNotes = Scales.generate(List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), octaves);

                                    nm.findNoteBlocks(mc, requiredNotes, 5);
                                    nm.tuneNoteBlocks(mc);
                                    mc.player.sendMessage(Text.of("Tuning scheduled -- please do not move!"));
                                    return 0;
                                })))
            );
        });
    }
}