package exercise2;

import java.util.ArrayList;
import java.util.Random;

public class ReplaceElements {
    public static void main(String[] args) {
        String[] str = new String[5];
        str[0] = "xzczxc";
        str[1] = "asdasda";
        str[2] = "dadsada";
        str[3] = "dads22ada";
        str[4] = "Трофозоит";
        for (String s : str) {
            System.out.println(s);
        }
        System.out.println('\n');
        replaceElements(str, 1, 3);
        for (String s : str) {
            System.out.println(s);
        }
        System.out.println('\n');
        Integer[] integers = new Integer[5];
        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            integers[i] = random.nextInt(154);
            System.out.println(integers[i]);
        }
        System.out.println('\n');
        replaceElements(integers, 2, 0);
        for (Integer integer : integers) {
            System.out.println(integer);
        }

    }

    public static <T> void replaceElements(T[] arr, int index1, int index2) {
        T tmp;
        if (index1 < 0 || index1 >= arr.length || index2 < 0 || index2 >= arr.length) {
            throw new IllegalArgumentException("Индекс вне границ массива!!");
        }
        tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }
}
