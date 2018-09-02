package lab_4.story_components;

class Boy extends Hero implements Decide {

    private int respons = Rand.rand(0, 2);

    Boy(String name, double w){super(name, w);}

    boolean getRespons(){return respons == 1 ? true : false;}
    //Оценивает красоту марки
    String evaluateMark(double m){
        if (m >= 0.8){
            this.setState(State.HAPPY);
            return " не мог оторваться";
        }
        else if (m < 0.5){
            this.setState(State.FRUSTRATED);
            return " быстро оторвался";
        }
        else{
            this.setState(State.NORMAL);
            return " оторвался";
        }
    }
    void branch(String h, String n, String f){
        switch(this.getState()){
            case HAPPY:{System.out.print(h);}
            break;
            case NORMAL:{System.out.print(n);}
            break;
            case FRUSTRATED:{System.out.print(f);}
            break;
        }
    }
    @Override
    public boolean getDecision(){
        if ((this).getRespons() && this.getState() == State.NORMAL) return false;
        return true;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Boy hero = (Boy) obj;
        return getName().equals(hero.getName()) &&
                getState().equals(hero.getState()) &&
                getWeight() == hero.getWeight();
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

