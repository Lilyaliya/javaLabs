package laba6.example;

import java.util.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
public class Test {
	static int count;
	static Balls balls;

	public static void main(String[] args){
		count = 0;
		balls = new Balls();
	}

	static class Balls extends Frame implements Observer, ActionListener, ItemListener {
		private final LinkedList<Ball> ballsList = new LinkedList<>();
		private Color color;
		private Frame controlFrame;
		private Button button;
		private Choice choiceColorsTable;
		private TextField text;

		Balls() {
			initializeMainFrame();
			initializeControlFrame();
			initializeButtonOfFrame();
			initializeColorTableOfBall();
		}

		private void initializeMainFrame() {
			this.addWindowListener(new WindowAdapter2()); // кнопка закрытия окна
			this.setSize(600, 300);
			this.setResizable(true);
			this.setVisible(true);
			this.setLocation(100, 150);
		}

		private void initializeButtonOfFrame() {
			button = new Button("Пуск");
			button.setSize(new Dimension(10, 40));
			button.setActionCommand("OK");
			button.addActionListener(this);
			controlFrame.add(button, new Point(20, 20));
		}

		private void initializeControlFrame() {
			controlFrame = new Frame(); // создаем 2 окно с кнопками
			controlFrame.setSize(new Dimension(300, 100));
			controlFrame.setTitle("Контроль");
			controlFrame.setLayout(new GridLayout());
			controlFrame.addWindowListener(new WindowAdapter2());
			text = new TextField();
			controlFrame.add(text);
			controlFrame.setVisible(true);
		}

		private void initializeColorTableOfBall() {
			choiceColorsTable = new Choice();
			choiceColorsTable.addItem("Синий");
			choiceColorsTable.addItem("Зелёный");
			choiceColorsTable.addItem("Красный");
			choiceColorsTable.addItem("Чёрный");
			choiceColorsTable.addItem("Жёлтый");
			choiceColorsTable.addItemListener(this);
			controlFrame.add(choiceColorsTable, new Point(600, 20));
		}

		@Override // Метод от Observable
		public void update(Observable o, Object arg) {
			Ball ball = (Ball) arg;
			System.out.println("x= " + ball.thr.getName() + ball.x);
			repaint();
		}

		public void paint(Graphics g) {
			if (!ballsList.isEmpty()) {
				for (Ball ball : ballsList) {
					g.setColor(ball.col);
					g.drawOval(ball.x, ball.y, 50, 50);
				}
			}
		}

		public void actionPerformed(ActionEvent aE) { // обработка нажатия на кнопку "Пуск"
			String str = aE.getActionCommand();
			if (str.equals("OK")) {
				switch (choiceColorsTable.getSelectedIndex()) {
					case 0:
						color = Color.blue;
						break;
					case 1:
						color = Color.green;
						break;
					case 2:
						color = Color.red;
						break;
					case 3:
						color = Color.black;
						break;
					case 4:
						color = Color.yellow;
						break;
				}
				Ball ball = new Ball(color, this.text.getText()); // добавить размеры в класс
				ballsList.add(ball);
				ball.addObserver(this); // добавялем шар к слушателям и он тоже будет обновляться
			}
			repaint();
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
		}

	}

	static class Ball extends Observable implements Runnable {
		Thread thr;
		private boolean xplus;
		private boolean yplus;

		int x;
		int y;
		Color col;

		public Ball(Color col, String text) {
			xplus = true;
			yplus = true;
			x = 0;
			y = 30;
			this.col = col;
			Test.count++;
			thr = new Thread(this, Test.count + ":" + text + ":");
			thr.start(); // врубаем новый поток для шара и переходим в run
		}

		public void run() {
			while (true) {
				Dimension mainFrameSize = balls.getSize();
				if (x == mainFrameSize.width)
					xplus = false;
				if (x == -1)
					xplus = true;
				if (y == mainFrameSize.height)
					yplus = false;
				if (y == 29)
					yplus = true;
				if (xplus)
					x++;
				else
					x--;
				if (yplus)
					y++;
				else
					y--;
				setChanged();
				notifyObservers(this);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	static class WindowAdapter2 extends WindowAdapter {
		public void windowClosing (WindowEvent wE) {
			System.exit (0);
		}
	}

}





