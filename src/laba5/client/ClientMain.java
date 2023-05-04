package laba5.client;


import laba5.server.Compute;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class ClientMain {

	public static void main(String[] args) {
		//System.setProperty("java.server.policy", "file:./bin/laba5/client/client.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "Compute";
			Registry registry = LocateRegistry.getRegistry("localhost", 8000);//(args[0]);
			Compute comp = (Compute) registry.lookup(name);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true){
				System.out.print("Введите числа\n>> ");
				String[] arrStr = reader.readLine().split(" ");
				Laba1 task = new Laba1(arrStr);
				ArrayList<Integer> intArray = comp.executeTask(task);
				System.out.println(intArray);
			}
		} catch (Exception e) {
			System.err.println("Laba1 exception:");
			e.printStackTrace();
		}
	}
}
