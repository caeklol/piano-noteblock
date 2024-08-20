package me.caek.pnbs.piano;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.util.ArrayList;

public class MidiManager {
    private static ArrayList<MidiDevice> devices = new ArrayList<MidiDevice>();

    public static void closeAllDevices() {
        for (MidiDevice device : devices) {
            device.close();
        }
    }

    public static int scanForDevices() throws MidiUnavailableException {
        closeAllDevices();
        devices = new ArrayList<>();

        int recognized = 0;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxTransmitters() > 0 || device.getMaxTransmitters() == -1) { // -1 = unlimited
                devices.add(device);
            }
        }

        if(!devices.isEmpty()) {
            for (MidiDevice device : devices) {
                device.getTransmitter().setReceiver(new MidiInputReceiver(device));
                for (Transmitter transmitter : device.getTransmitters()) {
                    transmitter.setReceiver(new MidiInputReceiver(device));
                }
                device.open();
                recognized++;
            }
        }

        return recognized;
    }
}
