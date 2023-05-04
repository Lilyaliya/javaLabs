package laba3;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class Main {

    public static class ConsoleAccessor extends Observable {// при обращении к консоли
        void notifyObs() {
            setChanged();
            notifyObservers("Обращение к потоку ввода с консоли");
        }
    }

    public static class NumberEqualizer extends Observable {// уравнитель чисел
        void notifyObs() {
            setChanged();
            notifyObservers("В массиве число элементов равно указанному");
        }
    }

    public static class FileAccessor extends Observable {// при обращении к консоли
        void notifyObs() {
            setChanged();
            notifyObservers("Обращение к потоку ввода из указанного файла");
        }
    }

    public static class Watcher implements Observer {//Класс обозревателя
        public void update(Observable obs, Object arg) {
            System.out.println ("Обозреватель получил сообщение \"" + arg + "\"");
            try {
                journalFileWriter.write("Обозреватель получил сообщение \"" + arg + "\"\n");
            } catch (IOException e) {
                System.out.println("Проблемы с файлом журнала!");
            }
        }
    }

    static BufferedWriter journalFileWriter;

    public static void main(String[] args) throws IOException {
        Watcher watcher = new Watcher();//Создать объект приемника
        ConsoleAccessor consoleAccessor = new ConsoleAccessor();
        FileAccessor fileAccessor = new FileAccessor();
        NumberEqualizer equalizer = new NumberEqualizer();
        consoleAccessor.addObserver(watcher); // добавялем последователя
        fileAccessor.addObserver(watcher);
        equalizer.addObserver(watcher);

        ArrayList<Integer> listInt = new ArrayList<>();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите путь к файлу \"Журналу\"\n>> ");
        try {
            String journalFilePath = consoleReader.readLine();
            journalFileWriter = new BufferedWriter(new FileWriter(journalFilePath));
            consoleAccessor.notifyObs(); //              уведомляем

            System.out.print("Введите путь к файлу, где находится число элементов\n>> ");
            String filePath = consoleReader.readLine();
            consoleAccessor.notifyObs(); //               уведомляем
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

            System.out.print("Введите элементы \n>> ");
            String argsLine = consoleReader.readLine();
            consoleAccessor.notifyObs(); //               уведомляем
            String[] arrArgs = argsLine.split(" ");

            String line = fileReader.readLine();
            fileAccessor.notifyObs(); //                  уведомляем
            int argsNumber = Integer.parseInt(line);
            if (argsNumber == arrArgs.length)
                equalizer.notifyObs(); //                 уведомляем

            for ( String elem: arrArgs) {
                listInt.add(Integer.parseInt(elem));
            }
            listInt.sort(Collections.reverseOrder());
            System.out.println(listInt);
            journalFileWriter.write(listInt + "\n");
        }
        catch (FileNotFoundException e){
            System.out.println("Файл не найден!");
            journalFileWriter.write("Файл не найден!\n");
        }
        catch(NumberFormatException e){
            System.out.println("Не получилось перевести в число!");
            journalFileWriter.write("Не получилось перевести в число!\n");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            journalFileWriter.write(e.getMessage() + "\n");
        }
        finally {
            journalFileWriter.close();
        }
    }

}