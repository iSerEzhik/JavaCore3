package exercise1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class ToArrayList {
    public static void main(String[] args) {
        String[] str = new String[5];
        str[0] = "xzczxc";
        str[1] = "asdasda";
        str[2] = "dadsada";
        str[3] = "dads22ada";
        str[4] = "Трофозоит";
        ArrayList<String> strList= arrayToArrayList(str);
        strList.forEach(s ->
            System.out.println(s)
        );
        System.out.println('\n');
        Integer[] integers = new Integer[5];
        Random random = new Random();
        for (int i = 0;i<5;++i){
            integers[i] = random.nextInt(154);
        }
        ArrayList<Integer> integerList= arrayToArrayList(integers);
        integerList.forEach(s-> System.out.println(s));

    }
    public static <T> ArrayList<T> arrayToArrayList(T[] arr){
        ArrayList<T> list = new ArrayList<>();
        for (T e: arr) {
            list.add(e);
        }
        return list;
    }
}
