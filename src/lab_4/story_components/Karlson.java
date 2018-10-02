package lab_4.story_components;

import lab_4.PropellerCollection;

import java.io.Serializable;
import java.util.*;


public class Karlson extends Hero {

    Propeller propeller;

    Karlson(String name, double w, PropellerCollection p) {
        super(name, w);

        propeller = p.getPropellerMap().get(p.getPropellerMap().keySet().toArray()[0]);

    }

    public void pressButton(double w, int illum) {
        try {
            if (propeller.startEngine(illum)) propeller.fly(w);
        } catch (WeightException e) {
            System.out.print(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Karlson hero = (Karlson) obj;
        return getName().equals(hero.getName()) &&
                getState().equals(hero.getState()) &&
                getWeight() == hero.getWeight();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    public static class Propeller implements PrepareToFly, TryToFly, Comparable<Propeller>, Serializable {

        /*
    характеристику, определяющую местоположение объекта на плоскости/в пространстве;
    */
        // parameters
        private String model; // имя, название или аналогичный текстовый идентификатор (unique)
        private int speed, //
                maxWeight, // грузоподъёмность
                size, // размер или аналогичный числовой параметр
                year; // время/дату рождения/создания объекта
        private ArrayList<String> fans;
        private String color;

        public Propeller() {
        }

        public Propeller(String model, int speed, int maxWeight, int size, int year, ArrayList<String> fans) {
            this.fans = fans;
            this.model = model;
            this.size = size;
            this.maxWeight = maxWeight;
            this.year = year;
            this.speed = speed;
        }

        public int getMaxWeight() {
            return maxWeight;
        }

        public String getModel() {
            return model;
        }

        public int getSpeed() {
            return speed;
        }

        public int getSize() {
            return size;
        }

        public int getYear() {
            return year;
        }

        public ArrayList<String> getFans() {
            return fans;
        }

        public String getColor() {
            return color;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public void setMaxWeight(int maxWeight) {
            this.maxWeight = maxWeight;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void setFans(ArrayList<String> fans) {
            this.fans = fans;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public boolean startEngine(int illum) {
            if (illum > 30) {
                System.out.print(" РїСЂРѕРїРµР»Р»РµСЂ Р·Р°РІС‘Р»СЃСЏ.");
                return true;
            }
            System.out.print(" СѓРїСЃ: РїСЂРѕРїРµР»Р»РµСЂ РЅРµ Р·Р°РІС‘Р»СЃСЏ, СЂР°СЃС…РѕРґРёРјСЃСЏ.");
            return false;
        }

        @Override
        public void fly(double w) throws WeightException {
            if (maxWeight < w)
                throw new WeightException(" РћРґРЅР°РєРѕ, РѕРЅРё СЃСЉРµР»Рё СЃР»РёС€РєРѕРј РјРЅРѕРіРѕ С‚РµС„С‚РµР»РµР№, Рё С‚РµРїРµСЂСЊ РЅРµ РјРѕРіСѓС‚ РІР·Р»РµС‚РµС‚СЊ.");
            System.out.print(" Р’РїРµСЂС‘Рґ!");
        }

        @Override
        public int compareTo(Propeller p) {
            return size - p.getSize();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || this.getClass() != o.getClass())
                return false;
            if (this == o)
                return true;
            Propeller p = (Propeller) o;
            return model.equals(p.getModel())
                    && year == p.getYear()
                    && size == p.getSize()
                    && speed == p.getSpeed()
                    && maxWeight == p.getMaxWeight()
                    && fans == p.getFans()
                    && color.equals(p.getColor());
        }

        @Override
        public int hashCode() {
            int res = 17;
            res = res * 31 + (model == null ? 0 : model.hashCode());
            res = res * 31 + size;
            res = res * 31 + year;
            res = res * 31 + (color == null ? 0 : color.hashCode());
            long l = Double.doubleToLongBits(speed);
            res = res * 31 + (int) (l ^ (l >>> 32));
            l = Double.doubleToLongBits(maxWeight);
            res = res * 31 + (int) (l ^ (l >>> 32));
            res = res * 31 + Objects.hash(new Object[]{fans});
            return res;
        }


        @Override
        public String toString() {
            return model + " " + year;
        }

    }
}