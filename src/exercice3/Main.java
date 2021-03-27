package exercice3;

import exercice3.Box;
import exercice3.Fruits.Apple;
import exercice3.Fruits.Fruit;
import exercice3.Fruits.Orange;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Box<Apple> box1 = new Box<>();
        for (int i = 0; i < 5; ++i) {
            box1.addFruit(new Apple());
        }
        Box<Apple> box2 = new Box<>();
        for (int i = 0; i < 5; ++i) {
            box2.addFruit(new Apple());
        }
        Box<Orange> orangeBox= new Box<>();
        for (int i = 0; i < 5; i++) {
            orangeBox.addFruit(new Orange());
        }

        System.out.println(box1.compareTo(box2));
        System.out.println(box1.getFullWeight());
        System.out.println(box2.getFullWeight());
        intersperseFrom1To2(box1,box2);
        System.out.println(box1.compareTo(box2));
        System.out.println(box2.getFullWeight());
    }
    public static  <T extends Fruit> void intersperseFrom1To2(Box<T> box1, Box<T> box2){
        while (box1.getFullWeight()!=0){
            box2.addFruit(box1.getFruit());
        }
    }
}
