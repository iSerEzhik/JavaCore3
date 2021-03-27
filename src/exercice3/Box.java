package exercice3;

import exercice3.Fruits.Fruit;

import java.util.ArrayList;


public class Box<T extends Fruit>{
    private ArrayList<T> fruits = new ArrayList<>();


    public  double getFullWeight() {
        if (fruits.size() == 0){
            return 0;
        }
        return fruits.size()*fruits.get(0).getWeight();
    }
    public void addFruit(T fruit){
        fruits.add(fruit);
    }
    public boolean compareTo(Box<T> compareWith){
        return getFullWeight() == compareWith.getFullWeight();
    }
    public T getFruit(){
        T fruit = fruits.get(fruits.size()-1);
        fruits.remove(fruits.size()-1);
        return fruit;
    }

}
