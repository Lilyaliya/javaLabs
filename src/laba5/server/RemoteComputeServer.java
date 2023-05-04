package laba5.server;


import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Policy;

public class RemoteComputeServer implements Compute{
	@Override
	public <T> T executeTask(Task<T> task) throws RemoteException {
		return task.execute();
	}

	public static void main(String[] args)  throws RemoteException, AlreadyBoundException, InterruptedException {
		// я так понял, что нужно писать путь от места где мы запускаем прогу
		//System.setProperty("java.security.policy", "file:./laba5/server/server.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
		// Имя Compute идентифицирует удаленный объект в реестре.
		String name = "Compute";
		RemoteComputeServer server = new RemoteComputeServer();
			// связующие API для связывания, регистрации, поиска удаленных объектов в реестре.
			Registry registry = LocateRegistry.createRegistry(8000);
			// UnicastRemoteObject - суперкласс для реализаций удаленного объекта.
			Remote stub = UnicastRemoteObject.exportObject(server, 0);
			// Программа затем добавляет имя в реестр RMI, выполняющийся на сервере.
			registry.rebind(name, stub);
			System.out.println("RemoteComputeServer bound");
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			System.err.println("RemoteComputeServer exception:");
			e.printStackTrace();
		}

	}
}
