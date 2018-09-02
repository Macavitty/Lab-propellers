package lab_4.story_components;

import lab_4.PropellerCollection;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Story {

    public void tellStory(PropellerCollection p, ObjectOutputStream oos){
        StringBuilder theStory = new StringBuilder();
        final double MARK =  Math.random();
        Boy malysh = new Boy("Малыш", 45);
        Karlson karlson = new Karlson("Карлсон", 50, p);

        class Room extends Location{
            Room(){super();}

            Room(int pi, int pd, int cr){super(pi, pd, cr);}
            void meltDust(int sneezeDegree){
                try{
                    while (percentOfDustness > 0) {
                        percentOfDustness /= sneezeDegree;
                        theStory.append(" и чихал");
                    }
                    theStory.append(" до тех пор, пока не “расчихал” пыль по всей комнате.");
                } catch (ArithmeticException e ){percentOfDustness = 0; }
            }
            @Override
            public String toString(){ return " в комнате";}
        }

        Room room = new Room((int)(Math.random()*100), (int)(Math.random()*50) + 1, 2);

        theStory.append(karlson + " стоял" + room);
        room.meltDust(10);
        theStory.append("\n");
        theStory.append("A " + malysh);
        theStory.append(malysh.evaluateMark(MARK));
        theStory.append(" от своей марки.");
        malysh.branch(" Он её уже наклеил и сейчас любовался ею - до чего хороша! ",
                " Он наклеил её в альбом и сейчас любовался ею. ",
                " Он не наклеил её в альбом. ");
        theStory.append(malysh + " отложил альбом. Лететь с Карлсоном на крышу - ");
        malysh.branch(" об этом можно было только мечтать.",
                "звучало довольно заманчиво.",
                "не было настроения.");
        CurrentDay day = new CurrentDay();
        if (day.currentDay().getIs()){
            int numberOfMeatballs = Rand.rand(-10, 40);
            theStory.append(" Но сегодня мама приготовила " + numberOfMeatballs + " тефтели(ей/ю),");
            malysh.branch("поэтому сперва","так что", "и");
            malysh.addWeight(numberOfMeatballs, 30);
            karlson.addWeight(numberOfMeatballs, 70);
            theStory.append(" друзья решили подкрепиться.");
            malysh.branch("", "", " Настроение у Малыша улучшилось.");
            if (malysh.getState() == Hero.State.FRUSTRATED){malysh.setState(Hero.State.NORMAL);}
            theStory.append("\n");
        }
        theStory.append("Лишь однажды довелось ему побывать у Карлсона в его маленьком домике на крыше.");
        if (!malysh.getDecision()){theStory.append(" Но в тот раз мама ужасно испугалась и вызвала пожарников.");}
        else{
            theStory.append("И Малыш согласился и в этот раз. " + karlson + " нажал на кнопку. Иии ... ");
            karlson.pressButton(malysh.getWeight() + karlson.getWeight(), room.percentOfIllumination);
        }
        try{
            theStory.append("\nThe End.");
            oos.writeObject(theStory.toString());
        }catch (IOException e){e.printStackTrace();}

    }
}
