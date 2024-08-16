package me.caek.pnbs.music;

import me.caek.pnbs.Scheduler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;


public class NoteManager {

    public class Key {
        public int noteOffset;
        public int note;

        public Key(int noteOffset, int note) {
            this.note = note;
            this.noteOffset = noteOffset;
        }
    }

    private ArrayList<Pair<Key, BlockPos>> noteBlockMap = new ArrayList<>();

    private static int getNote(NoteBlockInstrument instrument, Integer noteOffset) {
        int instrumentOffset;

        if (instrument.equals(NoteBlockInstrument.BELL)) {
            instrumentOffset = 0;
        } else if (instrument.equals(NoteBlockInstrument.HARP)) {
            instrumentOffset = 0;
        } else if (instrument.equals(NoteBlockInstrument.DIDGERIDOO)) {
            instrumentOffset = 0;
        } else {
            throw new Error("What?!");
        }

        return instrumentOffset + noteOffset;
    }

    public void findNoteBlocks(MinecraftClient mc, ArrayList<Integer> requiredNotes, int radius) {
        noteBlockMap = new ArrayList<>();

        // find note blocks (with instrument)
        // decide which ones need to be tuned to a specific note
        BlockPos playerPos = mc.player.getBlockPos();
        ClientWorld world = mc.world;

        HashMap<NoteBlockInstrument, ArrayList<Integer>> requiredNotesPerInstrument = new HashMap<>();
        requiredNotesPerInstrument.put(NoteBlockInstrument.HARP, new ArrayList<>());
        requiredNotesPerInstrument.put(NoteBlockInstrument.BELL, new ArrayList<>());
        requiredNotesPerInstrument.put(NoteBlockInstrument.DIDGERIDOO, new ArrayList<>());

        for (Integer note : requiredNotes) {
            int octave = (int)Math.floor((double)note/12.0);

            System.out.println("oct: " + octave + ", note: " + note);
            if (octave >= 4) {
                requiredNotesPerInstrument.get(NoteBlockInstrument.BELL).add(note);
            } else if (octave >= 2) {
                requiredNotesPerInstrument.get(NoteBlockInstrument.HARP).add(note);
            } else {
                requiredNotesPerInstrument.get(NoteBlockInstrument.DIDGERIDOO).add(note);
            }
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos blockPos = playerPos.add(x, y, z);
                    BlockState blockState = world.getBlockState(blockPos);
                    Block block = blockState.getBlock();

                    if (!(block instanceof NoteBlock)) continue;

                    Property<?> instrumentProp = block.getStateManager().getProperty("instrument");
                    NoteBlockInstrument instrument = (NoteBlockInstrument) blockState.get(instrumentProp);

                    if (!requiredNotesPerInstrument.containsKey(instrument)) continue;

                    ArrayList<Integer> instrumentQueue = requiredNotesPerInstrument.get(instrument);

                    int requiredRemaining = instrumentQueue.size();
                    if (requiredRemaining > 0) {
                        int noteOffset = instrumentQueue.removeFirst();
                        int noteAdjusted = getNote(instrument, noteOffset);

                        noteBlockMap.add(new Pair<>(new Key(noteAdjusted%24, noteOffset), blockPos));
                    }
                }
            }
        }
    }

    public void tuneNoteBlocks(MinecraftClient mc) {
        for (Pair<Key, BlockPos> pair : noteBlockMap) {
            Key key = pair.getLeft();
            BlockPos pos = pair.getRight();

            ClientWorld world = mc.world;
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();

            if (!(block instanceof NoteBlock)) continue; // error handling

            Property<?> noteProp = block.getStateManager().getProperty("note");
            Integer note = (Integer) blockState.get(noteProp);
            int targetNote = key.noteOffset;

            int noteOffset;

            if (targetNote >= note) {
                noteOffset = targetNote - note;
            } else {
                noteOffset = (25-note)+targetNote;
            }

            System.out.println("kn" + key.note + ", n:" + note + ", tn:" + targetNote + ", nf: " + noteOffset);

            for (int i = 0; i < noteOffset; i++) {
                Scheduler.schedule((unused) -> {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, false));
                    return null;
                });
            }
        }
    }

    public void playNote(MinecraftClient mc, int note) {
        for (Pair<Key, BlockPos> pair : noteBlockMap) {
            Key key = pair.getLeft();
            BlockPos pos = pair.getRight();
            if (key.note == note) {
                Scheduler.scheduleDelayed((unused) -> {
                    mc.interactionManager.attackBlock(pos, Direction.UP);
                    return null;
                }, 5);
            }
        }
    }
}

