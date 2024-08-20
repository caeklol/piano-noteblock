package me.caek.pnbs;

import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class Scheduler {
    private static ArrayList<Pair<Runnable, Integer>> tasks = new ArrayList<>();

    public static void tick() {
        if (tasks.isEmpty()) return;
        Pair<Runnable, Integer> task = tasks.getFirst();
        if (task.getRight() == 0) {
            task.getLeft().run();
            tasks.removeFirst();
        } else {
            task.setRight(task.getRight()-1);
        }
    }

    public static void schedule(Runnable task) {
        scheduleDelayed(task, 0);
    }

    public static void scheduleDelayed(Runnable task, int delay) {
        tasks.add(new Pair<>(task, delay));
    }

}
