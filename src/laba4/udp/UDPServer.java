package laba4.udp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 1) UDP клиент/сервер
// 2) в клиенте указание адреса и порта сервера 1 - с консоли
// 3) указание порта для сервера 1 – с консоли
// 4) Сообщения, получаемые клиентом с сервера должны записываться в файл
//        «Журнала клиента» путь к которому определяется 1 – с консоли
// 5) Сообщения, получаемые сервером от клиента должны записываться в файл
//    «Журнала сервера» путь к которому определяется: 2 – из командной строки(аргументы main)

//  3)      На сервере есть три двумерных массива данных: целочисленных, вещественных и
//  строковых. Клиент по указанию номера массива данных и ячейки в нём должен быть
//  способен: считывать и перезаписывать её. Если клиент указывает несколько ячеек
//  (возможно в разных массивах данных), то они должны принимать некоторое
//  предустановленное значение. Также, по запросу клиента сервер должен возвращать
//  размерность указанного массива данных.
//  Изменения в массивах должны дублироваться выводом массивов на консоль.
public class UDPServer {
	public static final int LENGTH_PACKET = 30;
	public static final String HOST = "localhost";
	static int[][] intArr = new int[5][5];
	static String[][] strArr = new String[5][5];
	static float[][] floatArr = new float[5][5];
	private static InetAddress     clientAddr;
	private static int             clientPort;
	private static final int defaultIntValue = 1000;
	private static final String defaultStrValue = "default string";
	private static final float defaultFloatValue = 123.345f;

	public static class Server implements Runnable{
		DatagramSocket  servSocket;
		DatagramPacket  datagram;
		byte[]          data;
		String          journalPath;



		public Server(int port, String journalPath) throws IOException {
			this.journalPath = journalPath;
			servSocket = new DatagramSocket(port);
		}

		@Override
		public void run() {
			try {
				data = new byte[LENGTH_PACKET];
				datagram = new DatagramPacket(data, data.length);
				servSocket.receive(datagram);
				String answer = new String(data, StandardCharsets.UTF_8).trim();
				BufferedWriter  journalFileWriter = new BufferedWriter(new FileWriter(journalPath));
				journalFileWriter.write(answer + "\n");
				journalFileWriter.close();
				System.out.println("Принято от клиента: " + answer);
				if (answer.equals("start")){
					data = startClient(datagram);
				}
				else if (answer.startsWith("size")){
					data = getArraySize();
				}
				else if (answer.matches("[-+]?\\d+.*")){
					data = workWithArrs(answer);
				}
				else {
					data = "Неверная команда!".getBytes();
				}
				datagram = new DatagramPacket(data, data.length, clientAddr, clientPort);
				servSocket.send(datagram);
				servSocket.close();
			}
			catch(SocketException e){
				System.err.println("Не удаётся открыть сокет : " + e);
			}
			catch (IOException e){
				System.out.println(e.getMessage());
			}

		}
	}

	public static void main(String[] args) {
//		try {
//			while (true) {
//				Server server = new Server(getPortFromUser(), "journal");
//				Thread thread = new Thread(server, "thread");
//				thread.start();
//
//			}
//		}
//		catch(SocketException e){
//			System.err.println("Не удаётся открыть сокет : " + e.toString());
//		}
//		catch (IOException e){
//			System.out.println(e.getMessage());
//		}

		DatagramSocket  servSocket = null;
		DatagramPacket  datagram;
		byte[]          data;

		try {
			int serverPort = getPortFromUser();
			BufferedWriter journalFileWriter = new BufferedWriter(new FileWriter(/*args[0]*/"journal"));
			while (true){
				servSocket = new DatagramSocket(serverPort);
				data = new byte[LENGTH_PACKET];
				datagram = new DatagramPacket(data, data.length);
				servSocket.receive(datagram);
				String answer = new String(data, StandardCharsets.UTF_8).trim();
				journalFileWriter.write(answer + "\n");
				clientAddr = datagram.getAddress(); // запоминаем клиента
				clientPort = datagram.getPort();
				System.out.println("Принято от клиента: " + answer);
				if (answer.equals("start")){
					data = startClient(datagram);
				}
				else if (answer.startsWith("size")){
					data = getArraySize();
				}
				else if (answer.equals("finish")){
					break;
				}
				else if (answer.matches("[-+]?\\d+.*")){
					data = workWithArrs(answer);
				}
				else {
					data = "Неверная команда!".getBytes();
				}
				datagram = new DatagramPacket(data, data.length, clientAddr, clientPort);
				servSocket.send(datagram);
				servSocket.close();
			}
			journalFileWriter.close();

		}
		catch(SocketException e){
			System.err.println("Не удаётся открыть сокет : " + e.toString());
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}

	}

	private static byte[] workWithArrs(String answer) {
		String[] array = answer.split(" ");
		String data = null;
		if (array.length == 3){
			data = checkArraysElem(array);
		}
		else if (array.length > 3){
			if (answer.indexOf(',') != -1){
				data = editArraysElementsToDefaultValue(answer);
			}
			else {
				data = editArraysElement(array);
			}
		}
		if (data == null)
			return ("Неверная команда!").getBytes();
		return data.getBytes();
	}

	private static String editArraysElementsToDefaultValue(String command) {
		String[] array = command.split(",");// разделили по ..., ..., ..., ...
		for (String elem : array) {
			elem = elem.trim();
			String[] elemStr = elem.split(" ");
			if (elemStr.length != 3)
				return null;
			int[] numbers = new int[3];
			for (int i = 0; i < 3; i++) {
				if (elemStr[i].matches("\\d+")){
					numbers[i] = Integer.parseInt(elemStr[i]);
				}
				else {
					return null;
				}
			}
			if (!putDefaultValue(numbers)){
				return null;
			}
		}
		return "1: массив целых чисел " + Arrays.deepToString(intArr) +
				"\n2: массив строк " + Arrays.deepToString(strArr) +
				"\n3: массив вещественных чисел " + Arrays.deepToString(floatArr);
	}

	private static boolean putDefaultValue(int[] numbers) {
		if (numbers[0] > 3 || numbers[0] < 1)
			return false;
		if (numbers[1] > 24 || numbers[1] < 0 || numbers[2] > 24 || numbers[2] < 0)
			return false;
		if (numbers[0] == 1){
			intArr[numbers[1]][numbers[2]] = defaultIntValue;
		}
		else if (numbers[0] == 2){
			strArr[numbers[1]][numbers[2]] = defaultStrValue;
		}
		else {
			floatArr[numbers[1]][numbers[2]] = defaultFloatValue;
		}
		return true;
	}


	private static String editArraysElement(String[] array) {
		int[] commandNumbers = new int[3];
		for (int i = 0; i != 3; i++) {
			if (array[i].matches("[-+]?\\d+")){
				commandNumbers[i] = Integer.parseInt(array[i]);
			}
			else {
				return null;
			}
		}
		if (commandNumbers[1] > 4 || commandNumbers[2] > 4 || commandNumbers[0] < 0){
			return null;
		}
		if (commandNumbers[0] == 1){// интовый массив
			if (array[3].matches("[-+]?\\d+")){
				intArr[commandNumbers[1]][commandNumbers[2]] = Integer.parseInt(array[3]);
			}
			else {
				return null;
			}
			return Arrays.deepToString(intArr);
		}
		else if (commandNumbers[0] == 2){// массив строк
			StringBuilder stringArg = new StringBuilder();
			for (int i = 3; i < array.length; i++){
				stringArg.append(array[i]).append(" ");
			}
			stringArg.deleteCharAt(stringArg.length() - 1);
			strArr[commandNumbers[1]][commandNumbers[2]] = stringArg.toString();
			return Arrays.deepToString(strArr);
		}
		else if (commandNumbers[0] == 3){// массив веществ чисел
			if (array[3].matches("[-+]?\\d+\\.?\\d*")){
				floatArr[commandNumbers[1]][commandNumbers[2]] = Float.parseFloat(array[3]);
			}
			else {
				return null;
			}
			return Arrays.deepToString(floatArr);
		}
		return null;
	}

	private static String checkArraysElem(String[] array) {
		int[] comandNumbers = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i].matches("[-+]?\\d+")){
				comandNumbers[i] = Integer.parseInt(array[i]);
			}
			else {
				return null;
			}
		}
		if (comandNumbers[1] > 4 || comandNumbers[2] > 4 || comandNumbers[0] < 0){
			return null;
		}
		else if (comandNumbers[0] == 1){
			System.out.println(comandNumbers);
			return "Значение ячейки числового массива [" + comandNumbers[1] +
					"][" + comandNumbers[2] + "]: "
					+ intArr[comandNumbers[1]][comandNumbers[2]];
		}
		else if (comandNumbers[0] == 2){
			return "Значение ячейки строкового массива [" + comandNumbers[1] +
					"][" + comandNumbers[2] + "]: "
					+ strArr[comandNumbers[1]][comandNumbers[2]];
		}
		else if (comandNumbers[0] == 3){
			return "Значение ячейки вещественного массива [" + comandNumbers[1] +
					"][" + comandNumbers[2] + "]: "
					+ floatArr[comandNumbers[1]][comandNumbers[2]];
		}
		return null;
	}

	private static byte[] getArraySize() {
		return "Размер массива - 5 на 5 ячеек\n".getBytes();
	}

	public static int getPortFromUser() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Введите порт для сервера\n>> ");
		//return Integer.parseInt(reader.readLine());
		return 1234;
	}

	private static byte[] startClient(DatagramPacket  datagram){
//		clientAddr = datagram.getAddress();
//		clientPort = datagram.getPort();
		return ("1: массив целых чисел " + Arrays.deepToString(intArr) +
				"\n2: массив строк " + Arrays.deepToString(strArr) +
				"\n3: массив вещественных чисел " + Arrays.deepToString(floatArr) +
				"\nФормат ввода: первая цифра от 1 до 3(тип массива),\n" +
				" далее номер столбца(от 0, до 4) и номер строки(от 0, до 4)." +
				"Позволит узнать значение в этой ячейке.\n" +
				"Чтобы изменить значение, добавьте еще один аргумент." +
				"Команда \"size\" узнать размер массивов\n" +
				"\"finish\" - выключить сервер.").getBytes();
	}

}