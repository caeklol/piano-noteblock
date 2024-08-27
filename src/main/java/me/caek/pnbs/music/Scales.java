package me.caek.pnbs.music;

import java.util.ArrayList;
import java.util.List;

public class Scales {
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
}
