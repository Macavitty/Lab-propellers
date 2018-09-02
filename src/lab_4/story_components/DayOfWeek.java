package lab_4.story_components;

enum DayOfWeek{

    MONDAY(false), TUESDAY(true, 0.09), WEDNESDAY(false), THURSDAY(true, 0.05),
    FRIDAY(true, 0.05), SATURDAY(true, 0.06), SUNDAY(true, 0.075);

    private double weightOfMeatball;
    private boolean is;

    DayOfWeek(boolean is){this.is = is;}

    DayOfWeek(boolean is, double w){
        this.is = is;
        this.weightOfMeatball = w + (double)Rand.rand(1, 2)/100;
    }

    boolean getIs(){return is;}

    double getWeightOfMeatball(){return weightOfMeatball;}
}