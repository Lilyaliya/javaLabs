package laba6.example;

import java.awt.*;
import java.awt.event.*;
class CTextBox_Button extends Frame {// Класс прикладного окна

	TextField ourTextBox; // Ссылка на редактор текста
	Button ourButton; // Ссылка на кнопку
	Point point; // Начальные координаты строки
	Color[] colors; // Ссылка на массив цветов флага
	int n; // Индекс массива цветов colors

	public CTextBox_Button() { // Конструктор
		setSize (new Dimension (400, 200));
		this.setBackground (Color.lightGray);

		setLayout (new FlowLayout ( ));
		ourTextBox = new TextField ( ); // Создать редактор
		ourTextBox.setSize (150, 20); // Установить размер
		ourTextBox.setText ("Russia"); // Установить текст в редакторе
		add (ourTextBox); // Добавить редактор в форму
		ourButton= new Button ("OK"); // Создать кнопку
		add (ourButton); // Добавить кнопку к форме
// Подписать обработчик на событие кнопки
		ourButton.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent aE) {
				n++;
				if (n >= 3)
					n= 0;
			}
		});
// Подписать обработчик на событие мыши
		this.addMouseListener (new MouseAdapter() {
			public void mousePressed (MouseEvent mE) {
				System.out.println ("Mouse: x = " + mE.getX() + " y= " + mE.getY());
				point.x = mE.getX();
				point.y = mE.getY();
				repaint(); // видимо эта функция снова вызывает paint
			}
		});
// Подписать обработчик для закрытия окна
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing (WindowEvent wE) {
				System.exit (0);
			}
		});
		n= 0;
		point= new Point (100, 100);
		colors = new Color[3]; // Создать массив цветов
		colors[0]= Color.white; // Серый цвет
		colors[1]= Color.blue; // Синий цвет
		colors[2]= Color.red; // Красный цвет
	}

	// Перерисовать область клиента окна
	public void paint (Graphics g) {// Нарисовать строку от носика курсора мыши
		g.setColor (colors[n]);
		g.drawString (ourTextBox.getText(), point.x, point.y);
	}

	public static void main (String[] args) {
		CTextBox_Button cT= new CTextBox_Button();
		cT.setVisible(true);
	}
}
