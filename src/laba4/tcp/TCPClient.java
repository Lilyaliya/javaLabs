package laba4.tcp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;

public class TCPClient {

	static Socket socket;
	static int PORT;
	static String HOST;
	static BufferedReader userConsoleReader;
	static BufferedWriter journalFileWriter;

	static String fileSettingsPath = "resources/fileSettings";

	public static void main(String[] args) throws Exception {
		try {
			// в аргументы передается хост и порт, запускать java TCPClient.java localhost 8000 clientJournal
			getClientFromFileSettings(fileSettingsPath);
			socket = new Socket(HOST, PORT);
			userConsoleReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Подключаемся к серверу на порту " + PORT);
			String serverResponse;
			String requestToServer = "start";
			while (true) {
				sendToServer(requestToServer);
				if (requestToServer.equals("finish"))
					break;
				serverResponse = getFromServer();
				System.out.println(serverResponse);
				journalFileWriter.write(serverResponse + "\n");
				System.out.print("Введите команду\n>> ");
				requestToServer = userConsoleReader.readLine();
			}
			journalFileWriter.close();
			userConsoleReader.close();
			socket.close();
			System.out.println("Connection Closed");
		}
		catch (IOException | InterruptedException e){
			e.printStackTrace();
			System.out.println("Socket read Error");
		}
	}

//	private static boolean getClientJournal(String[] args) throws Exception {
//		if (args.length < 3){
//			System.out.println("Недостаточно аргументов");
//			return false;
//		}
//		journalFileWriter = new BufferedWriter(new FileWriter(args[2]));
//		return true;
//	}

	private static void getClientFromFileSettings(String fileSettingsPath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileSettingsPath));
		String fileLine = reader.readLine();
		if (fileLine.isEmpty())
			throw new FileSystemException("Пустой файл");
		String[] wordsInLine = fileLine.split(" ");
		if (wordsInLine.length < 2)
			throw new FileSystemException("Недостаточно аргументов");
		String journalLine = reader.readLine();
		if (journalLine.isEmpty())
			throw new FileSystemException("Не найден файл журнала");
		HOST = wordsInLine[0];
		PORT = Integer.parseInt(wordsInLine[1]);
		journalFileWriter = new BufferedWriter(new FileWriter(journalLine));
	}

	private static String getFromServer() throws IOException, InterruptedException {
		InputStream input = socket.getInputStream();
		// не получается облегчить чтение с сервера, тк приходят многострочные  данные
		byte[] data = new byte[1000];
		input.read(data);
		return new String(data, StandardCharsets.UTF_8).trim();
	}

	private static void sendToServer(String message) throws IOException {
		// облегчаем чтение сокета с помощью буфера(чтобы не читать побайтно)
		PrintWriter writer = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())), true);
		writer.println(message);
	}

//	private static boolean getServerHostAndPort(String[] args) throws NumberFormatException {
//		if (args.length < 2){
//			System.out.println("Недостаточно аргументов");
//			return false;
//		}
//		if (!validIP(args[1])){
//			System.out.println("IP is invalid!");
//			return false;
//		}
//		HOST = args[0];
//		PORT = Integer.parseInt(args[1]);
//		return true;
//	}

//	public static boolean validIP (String ip) {
//		try {
//			if ( ip == null || ip.isEmpty() ) {
//				return false;
//			}
//			String[] parts = ip.split( "\\." );
//			if ( parts.length != 4 ) {
//				return false;
//			}
//			for ( String s : parts ) {
//				int i = Integer.parseInt( s );
//				if ( (i < 0) || (i > 255) ) {
//					return false;
//				}
//			}
//			return !ip.endsWith(".");
//		} catch (NumberFormatException nfe) {
//			return false;
//		}
//	}
}