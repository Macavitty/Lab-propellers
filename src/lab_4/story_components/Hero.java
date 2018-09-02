package lab_4.story_components;
import java.util.*;

abstract class Hero {
    private String name;
    private State state;
    private double weight;

    Hero(String name, double w){
        this.name = name;
        this.weight = w;
    }

    public void setState(State s){
        this.state = s;
    }
    public String getName(){return name;}
    public State getState(){return state;}

    public void addWeight(int n, int k){
        CurrentDay t = new CurrentDay();
        this.weight = this.weight + t.currentDay().getWeightOfMeatball() * n * k / 100;
    }

    public double getWeight(){return this.weight;}

    @Override
    public String toString(){return this.name;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Hero hero = (Hero) obj;
        return name.equals(hero.getName()) &&
                state.equals(hero.getState()) &&
                weight == hero.getWeight();
    }
    @Override
    public int hashCode() {
        return Objects.hash(weight, name, state);
    }

    static enum State{
        HAPPY, NORMAL, FRUSTRATED;
    }
}
