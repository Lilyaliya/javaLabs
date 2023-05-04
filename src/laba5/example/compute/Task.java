package laba5.example.compute;

// клиентский интерфейс, Task, определяет, как вычислитель выполняет полученную задачу.
public interface Task<T> {
	T execute();
}
