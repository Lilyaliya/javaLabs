Всего открывается 3 консоли
В отдельной консоли пишем это(но вроде работает и без этой фигни)
>> rmiregistry


Эта команда чтобы запустить сервер из src
Здесь используются абсолютные пути до server.policy и до сервера,
    пробовал с относительными, но ничего толкового не вышло
    Можно просто написать команду "java" если у вас версия Java 11 и выше,
    в противном случае придется сначала писать javac
>> java -Djava.rmi.server.codebase=file:./laba5/server/ -Djava.security.policy="/Users/a19810234/Desktop/Для учебы/Java/server.policy" laba5.server.RemoteComputeServer
Если все прошло успешно появится вот это - RemoteComputeServer bound

>> javac ./laba5/client/*.java
>> java -Djava.rmi.server.codebase=file:./laba5/client/ -Djava.security.policy="/Users/a19810234/Desktop/Для учебы/Java/client.policy" laba5.client.ClientMain

