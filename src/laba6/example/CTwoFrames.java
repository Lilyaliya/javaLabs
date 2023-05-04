package laba6.example;

/* Окно с дочерним окном*/
import java.awt.*;
import java.awt.event.*;

class CTwoFrames extends Frame {
	Frame childFrame ; // Дочерняя форма

	TextField ourTextBox; // Редактор
	Button ourButton; // Кнопка
	Point point; // Начальные координаты строки
	Color [] colors; // Массив цветов флага
	int n; // Индекс массива цветов colors

	public CTwoFrames() {
		setTitle("Russian flag");
		setSize (400, 200);
// Установить заголовок прикладного окна63
		this.setBackground(Color.lightGray);
		ourTextBox= new TextField();
// Создать редактор
		ourTextBox.setSize(new Dimension(150, 20)); // Установить позицию
		ourTextBox.setText ("Russia"); // Установить текст в редакторе
		ourButton = new Button ("OK"); // Создать кнопку
// Подписать обработчик на событие кнопки
		ourButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent aE) {
				n++;
				if (n >= 3)
					n = 0;
			}
		});
// Подписать обработчик на событие MouseDown мыши
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed (MouseEvent mE) {
				point.x = mE.getX();
				point.y = mE.getY();
				repaint ( );
			}
		});
// Подписать обработчик для закрытия окна
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing (WindowEvent wE) {
				System.exit(0);
			}
		});
		n= 0;
		point= new Point (100, 100);
		colors= new Color [3]; // Создать массив цветов
		colors [0]= Color.white; // Белый цвет
		colors [1]= Color.blue; // Синий цвет
		colors [2]= Color.red; // Красный цвет
		childFrame= new Frame(); // Создать дочернюю форму и
		childFrame.setSize (200, 100); // с размером
		childFrame.setLocation (410, 0);
		childFrame.setLayout (new FlowLayout ( ));
		childFrame.add (ourTextBox); // Добавить редактор
		childFrame.add (ourButton); // Добавить кнопку
		childFrame.setVisible(true);
	}


	// Перерисовать область клиента окна
	public void paint (Graphics g) {// Нарисовать строку от носика курсора мыши

		g.setColor(colors[n]);
		g.drawString(ourTextBox.getText(), point.x, point.y);
	}

	public static void main (String[] args) {
		CTwoFrames cT = new CTwoFrames();
		cT.setVisible(true);
	}
}
