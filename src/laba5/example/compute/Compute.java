package laba5.example.compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Интерфейс вычислителя, Compute, позволяет работам быть посланными на вычислитель,
public interface Compute extends Remote {
	<T> T executeTask(Task<T> t) throws RemoteException;
}
