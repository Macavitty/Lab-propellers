package lab_4.story_components;

import java.util.*;

class Rand{
    public static int rand(int min, int max){
        return (new Random().nextInt(max) - min);
    }
}
