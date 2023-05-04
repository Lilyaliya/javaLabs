package laba1.main;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> listInt = new ArrayList<>();
        try {
            for ( String elem: args) {
                listInt.add(Integer.parseInt(elem));
            }
            listInt.sort(Collections.reverseOrder());
            System.out.println(listInt);
        }
        catch (Exception e){
            System.out.println("Wrong arguments!");
        }
    }
}
