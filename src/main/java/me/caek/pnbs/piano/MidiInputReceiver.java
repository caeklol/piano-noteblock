package me.caek.pnbs.piano;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiInputReceiver implements Receiver {
    private MidiDevice device;

    public MidiInputReceiver(MidiDevice device) {
        this.device = device;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        ShortMessage m = (ShortMessage) message;

        int channel = m.getChannel();
        int pitch = m.getData1();
        int vel = m.getData2();
        String out = "c: " + channel + ", p: " + pitch + ", v: " + vel;
        MinecraftClient.getInstance().player.sendMessage(Text.of(out));
    }

    @Override
    public void close() {

    }
}
