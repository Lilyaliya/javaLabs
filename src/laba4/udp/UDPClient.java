package laba4.udp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPClient {
	public static final int LENGTH_PACKET = 1000;
	public static String host;
	public static int port;
	static String journalFilePath;
	public static void main(String[] args) {
		try{
			getPortAndAddressFromUser();
			String message = "";
			BufferedWriter journalFileWriter = new BufferedWriter(new FileWriter(journalFilePath));
			byte[] data = ("start").getBytes();
			System.out.println("Подключаемся к серверу...");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			InetAddress addr = InetAddress.getByName(host);
			DatagramSocket socket = new DatagramSocket();
			while (true){
				DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
				socket.send(packet);
				if (message.equals("finish"))
					break;
//-----------------------------------------------------------------
//приём сообщения с сервера
//-----------------------------------------------------------------
				byte[] data2 = new byte[LENGTH_PACKET];
				packet = new DatagramPacket(data2, data2.length);
				socket.receive(packet);
				String servResponse = (new String(packet.getData())).trim();
				System.out.println(servResponse);
				journalFileWriter.write(servResponse + "\n");
				System.out.print("Введите команду\n>> ");
				message = reader.readLine();
				data = message.getBytes();
				if (message.isEmpty())
					break;
			}
//-----------------------------------------------------------------
//закрытие сокета
//-----------------------------------------------------------------
			journalFileWriter.close();
			socket.close();
		} catch(IOException e){e.printStackTrace();
		}
	}

	public static void getPortAndAddressFromUser() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Введите порт для клиента\n>> ");
		//port = Integer.parseInt(reader.readLine());
		System.out.print("Введите хост для клиента\n>> ");
		//host = reader.readLine();
		System.out.print("Введите путь для журнала клиента\n>> ");
//		journalFilePath = reader.readLine();
		journalFilePath = "clientJournal";
		port = 1234;
		host = "192.168.6.41";
	}
}