package me.caek.pnbs.piano;

import me.caek.pnbs.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiInputReceiver implements Receiver {

    public MidiInputReceiver() {}

    @Override
    public void send(MidiMessage message, long timeStamp) {
        ShortMessage m = (ShortMessage) message;

        int pitch = m.getData1();
        int vel = m.getData2();
        int command = m.getCommand();
        boolean released = vel == 0;

        if (command == 144) {
            int pitchNormalized = pitch - 21; // A0 -> NoteManager 0

            if (!released) {
                Mod.getNoteManager().playNote(MinecraftClient.getInstance(), pitchNormalized);
            }
        }
    }

    @Override
    public void close() {

    }
}
