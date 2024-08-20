package me.caek.pnbs.music;

import java.util.ArrayList;
import java.util.List;

public class Scales {
    // takes in
    public static ArrayList<Integer> generate(List<Integer> scale, int octaves) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(0);
        int scaleIndex = 0;
        while (result.getLast() < (octaves * 24)) {
            System.out.println(result.getLast());
            result.add(result.getLast() + scale.get(scaleIndex));
            scaleIndex++;
            if (scaleIndex == scale.size()) scaleIndex = 0;
        }
        return result;
    }

    public static ArrayList<Integer> generateMajor(int octaves) {
        return generate(List.of(2, 2, 1, 2, 2, 2, 1), octaves);
    }
}
