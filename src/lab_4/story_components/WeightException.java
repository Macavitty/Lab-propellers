package lab_4.story_components;

class WeightException extends Exception {
    String st;
    public WeightException(String s){super(s); st = s;}
    @Override
    public String toString(){return st;}
}
