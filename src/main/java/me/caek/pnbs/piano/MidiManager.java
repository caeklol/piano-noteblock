package me.caek.pnbs.piano;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            if (device.getMaxTransmitters() > 0 || device.getMaxTransmitters() == -1) { // -1 = unlimited
                devices.add(device);
            }
        }

        if(!devices.isEmpty()) {
            for (MidiDevice device : devices) {
                device.getTransmitter().setReceiver(new MidiInputReceiver());
                for (Transmitter transmitter : device.getTransmitters()) {
                    transmitter.setReceiver(new MidiInputReceiver());
                }
                device.open();
                recognized++;
            }
        }

        return recognized;
    }

    public static List<String> getDeviceNames() {
        return devices.stream().map((d) -> d.getDeviceInfo().getName()).toList();
    }
}
