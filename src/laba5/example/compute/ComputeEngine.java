package laba5.example.compute;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// ComputeEngine реализует удаленный интерфейс Compute
// и также включает метод main для установки вычислителя.
public class ComputeEngine implements Compute {
	public ComputeEngine() {
		super();
	}
	public <T> T executeTask(Task<T> t) {
		return t.execute();
	}
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			// Имя Compute идентифицирует удаленный объект в реестре.
			String name = "Compute";
			Compute engine = new ComputeEngine();
			// UnicastRemoteObject - суперкласс для реализаций удаленного объекта.
			Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
			// связующие API для связывания, регистрации, поиска удаленных объектов в реестре.
			Registry registry = LocateRegistry.getRegistry();
			// Программа затем добавляет имя в реестр RMI, выполняющийся на сервере.
			registry.rebind(name, stub);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}
}