package me.caek.pnbs;

import me.caek.pnbs.music.NoteManager;
import net.fabricmc.api.ClientModInitializer;

public class Mod implements ClientModInitializer {
    private static NoteManager noteManager;
    @Override
    public void onInitializeClient() {
        CommandManager.register();
        EventListener.register();
        noteManager = new NoteManager();
    }

    public static NoteManager getNoteManager() {
        return noteManager;
    }
}
