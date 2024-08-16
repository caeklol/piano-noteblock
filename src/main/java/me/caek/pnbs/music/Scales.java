package me.caek.pnbs.music;

import java.util.ArrayList;
import java.util.List;

public class Scales {
    // takes in
    public static ArrayList<Integer> generate(List<Integer> scale, int octaves) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(0);
        for (int i = 0; i < octaves; i++) {
            for (Integer step : scale) {
               result.add(result.getLast() + step);
            }
        }
        return result;
    }

    public static ArrayList<Integer> generateMajor(int octaves) {
        return generate(List.of(2, 2, 1, 2, 2, 2, 1), octaves);
    }
}
