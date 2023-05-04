package laba4.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystemException;
import java.util.Arrays;


/**
 * 1) TSP SERVER 19 вариант = 023213
 * 2) Реализовать в клиенте указание адреса и порта сервера, так: 2 – из командной строки(аргументы main)
 * 3) Реализовать указание порта для сервера, так: 3 – из файла настроек.
 * 4) Сообщения, получаемые клиентом с сервера должны записываться в файл
 * «Журнала клиента» путь к которому определяется: 2 – из командной строки(аргументы main)
 * 5) Сообщения, получаемые сервером от клиента должны записываться в файл
 * «Журнала сервера» путь к которому определяется: 1 – с консоли ввода приложения
 * 3) На сервере есть три двумерных массива данных: целочисленных, вещественных и
 * строковых.
 */
public class TCPServer {

	static int PORT;
	static String clientRequest = "";
	static String fileSettingsPath = "fileSettingsServer";
	static BufferedWriter journalFileWriter;

	public static void main(String args[]) throws Exception {
		try {
			PORT = getServerPortFromArgs(args);
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Сервер запущен на порту " + PORT);
			getServerJournal(fileSettingsPath);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Подключение клиента");
				// запускаем новый поток для каждого нового клиента
				new Listener(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getServerJournal(String fileSettingsPath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileSettingsPath));
		String fileLine = reader.readLine();
		if (fileLine.isEmpty())
			throw new FileSystemException("Пустой файл");
		journalFileWriter = new BufferedWriter(new FileWriter(fileLine));
	}

//	private static int getServerPortFromFileSettings(String fileSettingsPath) throws IOException {
//
//		BufferedReader reader = new BufferedReader(new FileReader(fileSettingsPath));
//		String fileLine = reader.readLine();
//		if (fileLine.isEmpty())
//			throw new FileSystemException("Пустой файл");
//		String[] wordsInLine = fileLine.split("=");
//		if (wordsInLine.length != 2 || !wordsInLine[0].equals("serverPort"))
//			throw new FileSystemException("Неверные данные в файле");
//		return Integer.parseInt(wordsInLine[1]);
//	}
	private static int getServerPortFromArgs(String[] args) throws Exception{
		if (args.length < 1){
			System.out.println("Недостаточно аргументов");
		}
		return Integer.parseInt(args[0]);
	}
	static class Listener extends Thread {
		Socket socket;

		static int[][]      intArr = new int[5][5];
		static String[][]   strArr = new String[5][5];
		static float[][]    floatArr = new float[5][5];
		static final int    defaultIntValue = 1000;
		static final String defaultStrValue = "default string";
		static final float  defaultFloatValue = 123.345f;
		static int[][] reservedIndexes  = new int[][]{{0, 3, 4}, {1, 2},{4}};

		public Listener(Socket clientSocket) {
			this.socket = clientSocket;
		}

		/** метод start в main переходит в этот */
		public void run() {
			try {
				while (true) {
					clientRequest = getFromClient();
					journalFileWriter.write(clientRequest + "\n");
					System.out.println("Сервер принял от клиента " + clientRequest);
					String messageToClient = selectActionByRequest(clientRequest);
					if (messageToClient == null)
						break;
					sendToClient(messageToClient);
				}
				journalFileWriter.close();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		private String getFromClient() throws IOException {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return in.readLine();
		}

		private void sendToClient(String message) throws IOException, InterruptedException {
			byte[] data = message.getBytes();
			OutputStream output = socket.getOutputStream();
			output.write(data);
		}

		private String selectActionByRequest(String clientRequest){
			if (clientRequest.equals("start")){
				return startClient();
			}
//			else if (clientRequest.startsWith("size")){
//				return getArraySize();
//			}
			else if (clientRequest.equals("finish")){
				return null;
			}
			else if (clientRequest.matches("[-+]?\\d+.*")){
				return workWithArrs(clientRequest);
			}
			return  "Неверная команда!";
		}

		private static String workWithArrs(String answer) {
			String[] array = answer.split(" ");
			String data = null;
			if (array.length == 3){
				data = checkArraysElem(array);
			}
			else if (array.length > 3){
					data = editArraysElement(array);
			}
			if (data == null)
				return "Неверная команда!";
			return data;
		}

//		private static String editArraysElementsToDefaultValue(String command) {
//			String[] array = command.split(",");// разделили по ..., ..., ..., ...
//			for (String elem : array) {
//				elem = elem.trim();
//				String[] elemStr = elem.split(" ");
//				if (elemStr.length != 3)
//					return null;
//				int[] numbers = new int[3];
//				for (int i = 0; i < 3; i++) {
//					if (elemStr[i].matches("\\d+")){
//						numbers[i] = Integer.parseInt(elemStr[i]);
//					}
//					else {
//						return null;
//					}
//				}
//				if (!putDefaultValue(numbers)){
//					return null;
//				}
//			}
//			return "1: массив целых чисел " + Arrays.deepToString(intArr) +
//					"\n2: массив строк " + Arrays.deepToString(strArr) +
//					"\n3: массив вещественных чисел " + Arrays.deepToString(floatArr);
//		}

//		private static boolean putDefaultValue(int[] numbers) {
//			if (numbers[0] > 3 || numbers[0] < 1)
//				return false;
//			if (numbers[1] > 24 || numbers[1] < 0 || numbers[2] > 24 || numbers[2] < 0)
//				return false;
//			if (numbers[0] == 1){
//				intArr[numbers[1]][numbers[2]] = defaultIntValue;
//			}
//			else if (numbers[0] == 2){
//				strArr[numbers[1]][numbers[2]] = defaultStrValue;
//			}
//			else {
//				floatArr[numbers[1]][numbers[2]] = defaultFloatValue;
//			}
//			return true;
//		}


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
			System.out.println(array);
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

//		private static String getArraySize() {
//			return "Размер массива - 5 на 5 ячеек\n";
//		}


		private static String startClient() {
			return ("1: массив целых чисел " + Arrays.deepToString(intArr) +
					"\n2: массив строк " + Arrays.deepToString(strArr) +
					"\n3: массив вещественных чисел " + Arrays.deepToString(floatArr) +
					"\nФормат ввода: первая цифра от 1 до 3(тип массива),\n" +
					" далее номер столбца(от 0, до 4) и номер строки(от 0, до 4)." +
					"Позволит узнать значение в этой ячейке.\n" +
					"Чтобы изменить значение, добавьте еще один аргумент.\n");
		}
	}
}
