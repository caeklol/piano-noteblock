package me.caek.pnbs;

import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class Scheduler {
    private static ArrayList<Pair<Function<Void, Void>, Integer>> tasks = new ArrayList<>();

    public static void tick() {
        if (tasks.isEmpty()) return;
        Pair<Function<Void, Void>, Integer> task = tasks.getFirst();
        if (task.getRight() == 0) {
            task.getLeft().apply(null);
            tasks.removeFirst();
        } else {
            task.setRight(task.getRight()-1);
        }
    }

    public static void schedule(Function<Void, Void> task) {
        scheduleDelayed(task, 0);
    }

    public static void scheduleDelayed(Function<Void, Void> task, int delay) {
        tasks.add(new Pair<>(task, delay));
    }

}
