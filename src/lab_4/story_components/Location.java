package lab_4.story_components;

abstract class Location {
    int percentOfIllumination;
    int percentOfDustness;
    int crowdness;
    abstract void meltDust(int degree);
    Location(){};
    Location(int percentOfIllumination, int percentOfDustness, int crowdness){
        this.percentOfIllumination = percentOfIllumination;
        this.percentOfDustness = percentOfDustness;
        this.crowdness = crowdness;
    }
}
