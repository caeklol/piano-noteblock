package me.caek.pnbs;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class EventListener {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(EventListener::startClientTick);
        ClientTickEvents.END_CLIENT_TICK.register(EventListener::endClientTick);
    }

    private static void startClientTick(MinecraftClient minecraftClient) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Scheduler.tick();
    }

    private static void endClientTick(MinecraftClient minecraftClient) {
    }
}
