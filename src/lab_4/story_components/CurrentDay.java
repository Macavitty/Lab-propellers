package lab_4.story_components;

import java.text.*;
import java.util.*;

class CurrentDay{
    SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    @Override
    public String toString(){return dayOfWeek.format(new Date());}
    DayOfWeek currentDay(){return DayOfWeek.valueOf(toString().toUpperCase());}
}
