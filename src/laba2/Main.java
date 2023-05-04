package laba2;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static class MyException extends Exception{

        String message;
        public MyException(String message){
            this.message = message;
        }
        public String getMessage(){
            return message;
        }
    }

    public interface ISort1{
        final static int wrongNumber = 51;

        void checkWrongNumber() throws MyException;
    }

    public interface ISort2{
        String  sort();
        void    parseStringArray() throws MyException;
    }

    public static class SortNumber implements ISort1, ISort2{

        String[]    args;
        ArrayList<Integer> listInt = new ArrayList<>();

        public SortNumber(String[] args){
            this.args = args;
        }

        @Override
        public String sort(){
            listInt.sort(Collections.reverseOrder());
            return listInt.toString();
        }

        @Override
        public void parseStringArray() throws MyException {
            if (!args[0].matches("[-+]?\\d+")){
                throw new MyException("Can't parse string to int!");
            }
            int argsNumber = Integer.parseInt(args[0]);
            if (args.length > argsNumber + 1){
                throw new MyException("In the array, the number of elements is greater than the specified");
            }
            int number;
            for ( int i = 1; i < args.length; i++) {
                if (!args[i].matches("[-+]?\\d+")){
                    throw new MyException("Can't parse string to int!");
                }
                number = Integer.parseInt(args[i]);
                listInt.add(number);
            }
            checkWrongNumber();
        }

        @Override
        public void checkWrongNumber() throws MyException {
            for (Integer integer : listInt) {
                if (integer == wrongNumber) {
                    throw new MyException("Equality to " + wrongNumber + " number!");
                }
            }
        }

        public String getSortArray() throws MyException {
            parseStringArray();
            return sort();
        }
    }


    public static void main(String[] args) {
        try {
            SortNumber sortNumber = new SortNumber(args);
            System.out.println(sortNumber.getSortArray());
        }
        catch (MyException e){
            System.out.println(e.getMessage());
        }
    }
}
