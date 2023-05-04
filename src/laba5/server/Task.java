package laba5.server;

// клиентский интерфейс, Task, определяет, как вычислитель выполняет полученную задачу.
public interface Task<T> {
	T execute();
}