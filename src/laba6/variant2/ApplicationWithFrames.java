package laba6.variant2;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorChooserUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

// 1100100011
// 1110010110 Наш 2 вариант
// 1100011101
// 5. В УО должна быть предусмотрена возможность выбора уже запущенного ФиО и
//		изменения его параметров (например, цвет, скорость).
// 6. Номер, который присваивается ФиО должен отображаться рядом с ФИО, когда
//		ФИО перемещается в ДО.
// 7. Все ФиО должны быть пронумерованы, при чём, если предусмотрено изменение
//		номера ФиО, то необходимо обеспечить уникальность номера каждого ФиО.
// 1. Число ФиО 2 – задано в коде УО                DONE
// 2. ФиО 2 - объекты                               DONE
// 3. Задание цвета текста и заливки ФиО 2 – стандартный элемент выбора цвета                   !!!
// 4. Выбор запускаемого ФиО 1 – выпад. список назв.;                                           DONE
// 5. Задание начальной скорости ФиО 1 – указанием в текст. поле;                               DONE
// 6. Способ выбора запущенного ФиО 2 – из текст. поля, где вводится номер ФиО;                 DONE
// 7. Присвоение номера ФиО 1 – авто;                                                           DONE
// 8. Возможность смены номера ФиО из УО 2 – да                                                 DONE
// 9. Регулировка скорости перемещения выбран-го ФиО 2 – из выпадающего списка (пять скоростей);DONE
// 10. Изменения размера окна отображения ФиО 1 – нет; (т.е. отражение ФиО в новых границах)    DONE

public class ApplicationWithFrames {
	static int count;
	static MainFrame mainFrame;

	public static void main(String[] args) throws IOException {
		count = 0;
		mainFrame = new MainFrame();
	}

	static class MainFrame extends Frame implements Observer, ActionListener, ItemListener {
		private final LinkedList<Figure> figuresList = new LinkedList<>();
		private Color   color;
		private Frame   controlFrame;
		private Button  startFigureButton;
		private Button  changeFigureButton;
		private JLabel  alertFigureCountLabel;
		//private Choice  choiceColorsTable;
		private Choice  choiceObjectType;
		private Choice  choiceSpeedX;
		private Choice  choiceSpeedY;
		private TextField   figureNumberText;
		private TextField   text;
		private TextField   speedTextX;
		private TextField   speedTextY;
		private TextField   checkAlreadyAddedFigureText;
		JColorChooser choiceColorsTable;

		BufferedImage image;

		MainFrame() throws IOException {

			initializeMainFrame();
			initializeControlFrame();
			initializeColorTableOfFigure();
			initializeStartButtonOfFrame();
			initializeChangeFigureSpeeds();
			initializeChangeFigureNumbers();
			initializeChangeFigureButton();
			initializeTableOfObjectType();

			controlFrame.setVisible(true);
			File file = new File("src/laba6/instagram.jpg");
			image = ImageIO.read(file);
		}

		private void initializeChangeFigureNumbers() {
			figureNumberText = new TextField("Укажите новый Номер фигуры");
			controlFrame.add(figureNumberText);

			checkAlreadyAddedFigureText = new TextField("Укажите Номер созданной фигуры");
			controlFrame.add(checkAlreadyAddedFigureText);
		}

		private void initializeChangeFigureSpeeds() {
			choiceSpeedX = new Choice();
			choiceSpeedX.addItem("1");
			choiceSpeedX.addItem("2");
			choiceSpeedX.addItem("4");
			choiceSpeedX.addItem("6");
			choiceSpeedX.addItem("8");
			choiceSpeedX.addItemListener(this);
			controlFrame.add(choiceSpeedX);

			choiceSpeedY = new Choice();
			choiceSpeedY.addItem("1");
			choiceSpeedY.addItem("2");
			choiceSpeedY.addItem("4");
			choiceSpeedY.addItem("6");
			choiceSpeedY.addItem("8");
			choiceSpeedY.addItemListener(this);
			controlFrame.add(choiceSpeedY);
		}

		private void initializeChangeFigureButton() {
			changeFigureButton = new Button("Изменить");
			changeFigureButton.setActionCommand("CHANGE");
			changeFigureButton.addActionListener(this);
			controlFrame.add(changeFigureButton);
		}


		private void initializeMainFrame() {
			this.addWindowListener(new WindowCloser()); // кнопка закрытия окна
			this.setSize(300, 300);
			this.setResizable(false);
			this.setVisible(true);
			this.setLocation(700, 300);
		}

		private void initializeStartButtonOfFrame() {
			startFigureButton = new Button("Пуск");
			//startFigureButton.setSize(new Dimension(10, 40));
			startFigureButton.setActionCommand("OK");
			startFigureButton.addActionListener(this);
			controlFrame.add(startFigureButton, new Point(20, 20));
		}

		private void initializeControlFrame() {
			controlFrame = new Frame(); // создаем 2 окно с кнопками
			controlFrame.setResizable(true);
			controlFrame.setSize(700, 900);

			controlFrame.setResizable(true);
			controlFrame.setTitle("Контроль");
			controlFrame.setLayout(new GridLayout(4,3,5,5));
			controlFrame.addWindowListener(new WindowCloser());
			text = new TextField("Текст");
			text.setName("Введите текст");
			controlFrame.add(text);
			speedTextX = new TextField("Введите скорость по X");
			controlFrame.add(speedTextX);
			speedTextY = new TextField("Введите скорость по Y");
			controlFrame.add(speedTextY);



			alertFigureCountLabel = new JLabel("Вы можете создать только 10 объектов!");
			alertFigureCountLabel.setLocation(1000, 300);
			controlFrame.add(alertFigureCountLabel);

		}

		private void initializeColorTableOfFigure() {
//			choiceColorsTable = new Choice();
//			choiceColorsTable.addItem("Синий");
//			choiceColorsTable.addItem("Зелёный");
//			choiceColorsTable.addItem("Красный");
//			choiceColorsTable.addItem("Чёрный");
//			choiceColorsTable.addItem("Жёлтый");
//			choiceColorsTable.addItemListener(this);
			choiceColorsTable = new JColorChooser(Color.black);
			controlFrame.add(choiceColorsTable, new Point(600, 20));
		}

		private void initializeTableOfObjectType() {
			choiceObjectType = new Choice();
			choiceObjectType.addItem(ObjectType.IMAGE.getType());
			choiceObjectType.addItem(ObjectType.TEXT.getType());
			controlFrame.add(choiceObjectType, 1);
		}

		@Override // Метод от Observable
		public void update(Observable o, Object arg) {
			Figure figure = (Figure) arg;
			System.out.println("x= " + figure.thr.getName() + figure.x);
			repaint();
		}

		public void paint(Graphics g) {
			if (!figuresList.isEmpty()) {
				for (Figure figure : figuresList) {
					g.setColor(figure.color);
					g.drawString(Integer.toString(figure.number), figure.x, figure.y);
					if (figure.objectType == ObjectType.IMAGE) {
						//g.drawOval(figure.x, figure.y, 50, 50);
						g.drawImage(image, figure.x, figure.y, 50, 50, null);
					}
					else {
						Font font = new Font("Serif", Font.BOLD, 25);
						g.setFont(font);
						g.drawString(figure.getText(), figure.x, figure.y + 25);
						g.setFont(new Font("Dialog", Font.ITALIC, 15));
					}
				}
			}
		}

		public void actionPerformed(ActionEvent aE) { // обработка нажатий 2-х кнопок
			String str = aE.getActionCommand();
			if (str.equals("OK")) {// обработка нажатия на кнопку "Пуск"
				if (count == 10) {
					return;
				}
				buttonOKIsPressed();
			}
			else if (str.equals("CHANGE")) {// обработка нажатия на кнопку "Изменить"
				try {
					if (figuresList.isEmpty())
						return;
					int changeableNumber = Integer.parseInt(checkAlreadyAddedFigureText.getText());
					for (int i = 0; i < figuresList.size(); i++) {
						if (figuresList.get(i).number == changeableNumber){
							color = getColorFromTable();
							try {
								changeFigureWithNewNumberByIndex(i);
							}
							catch (Exception ignored) {
								changeFigureByIndex(i);
							}
						}
					}
				}
				catch (Exception ignored) {
					return;
				}
			}
			repaint();
		}

		private void changeFigureByIndex(int figureIndex) {
			Figure figure = figuresList.get(figureIndex);
			figure.setSpeedX(getChoiceSpeedXFromTable());
			figure.setSpeedY(getChoiceSpeedYFromTable());
			figure.setColor(color);
		}

		private void changeFigureWithNewNumberByIndex(int figureIndex) {
			Figure figure = figuresList.get(figureIndex);
			int newNumber = Integer.parseInt(figureNumberText.getText());
			figure.number = getUniqueNumberForFigure(newNumber);
			figure.setSpeedX(getChoiceSpeedXFromTable());
			figure.setSpeedY(getChoiceSpeedYFromTable());
			figure.setColor(color);
		}

		private int getUniqueNumberForFigure(int newFigureNumber) {
			if (newFigureNumber == 0)
				newFigureNumber++;
			//int attemptsToAssignNumber = 1;
			for (int i = 0; i < figuresList.size(); i++){
				if (figuresList.get(i).number == newFigureNumber){
//					newFigureNumber = attemptsToAssignNumber;
//					attemptsToAssignNumber++;
					newFigureNumber++;
					i = -1;
				}
			}
			return newFigureNumber;
		}

		private int getChoiceSpeedXFromTable() {
			switch (choiceSpeedX.getSelectedIndex()) {
				case 0:
					return 1;
				case 1:
					return 2;
				case 2:
					return 4;
				case 3:
					return 6;
				case 4:
					return 8;
			}
			return 1;
		}

		private int getChoiceSpeedYFromTable() {
			switch (choiceSpeedY.getSelectedIndex()) {
				case 0:
					return 1;
				case 1:
					return 2;
				case 2:
					return 4;
				case 3:
					return 6;
				case 4:
					return 8;
			}
			return 1;
		}

		private void buttonOKIsPressed() {
			Figure figure = null;
			int newFigureNumber = 0;
			color = getColorFromTable();
			ObjectType objectType = getObjectType();
			try {
				//newFigureNumber = Integer.parseInt(figureNumberText.getText());
				newFigureNumber = getUniqueNumberForFigure(newFigureNumber);
				figure = getFigureByFigureBuilder(objectType, newFigureNumber);
			}
			catch (Exception ignored) {
			}
			finally {
				if (newFigureNumber == 0){
					newFigureNumber = getUniqueNumberForFigure(newFigureNumber);
				}
				if (figure == null){
					figure = getFigureByFigureBuilderWithoutSpeeds(objectType, newFigureNumber);
				}
				figuresList.add(figure);
				figure.addObserver(this); // добавялем объект к слушателям и он тоже будет обновляться
			}
		}

		// использовали Builder чтобы не писать длинный конструктор из 5 аргументов
		private Figure getFigureByFigureBuilder(ObjectType objectType, int number) throws NumberFormatException {
			return Figure.FigureBuilder.aFigure()
					.withColor(color)
					.withNumber(number)
					.withObjectType(objectType)
					.withText(this.text.getText())
					.withSpeedX( Integer.parseInt(speedTextX.getText())) // здесь может быть исключение,
					.withSpeedY(Integer.parseInt(speedTextY.getText())) // но это не беда, тк мы можем просто исключить эти методы
					.build();
		}

		private Figure getFigureByFigureBuilderWithoutSpeeds(ObjectType objectType, int number) {
			return Figure.FigureBuilder.aFigure()
					.withColor(color)
					.withNumber(number)
					.withObjectType(objectType)
					.withText(this.text.getText())
					.build();
		}

		private ObjectType getObjectType() {
			switch (choiceObjectType.getSelectedIndex()) {
				case 0:
					return ObjectType.IMAGE;
				case 1:
					return ObjectType.TEXT;
			}
			return ObjectType.TEXT;
		}

		private Color getColorFromTable() {
//			switch (choiceColorsTable.getSelectedIndex()) {
//				case 0:
//					return Color.blue;
//				case 1:
//					return Color.green;
//				case 2:
//					return Color.red;
//				case 3:
//					return Color.black;
//				case 4:
//					return Color.yellow;
//			}
//			return Color.blue;
			return choiceColorsTable.getColor();
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
		}

	}

	static class Figure extends Observable implements Runnable {
		Thread thr;
		private boolean xplus = true;
		private boolean yplus = true;
		public ObjectType objectType;
		private int speedX = 1;
		private int speedY = 1;

		final static int WIDTH = 50;
		final static int HEIGHT = 50;
		int x = 0;
		int y = 30;
		int number;
		private final String text;
		private Color color;

		// Создали конструктор который принимает наш Builder, он позволит нам без проблем получить все атрибуты
		public Figure(FigureBuilder builder){
			color = builder.color;
			text = builder.text;
			speedX = builder.speedX;
			speedY = builder.speedY;
			objectType = builder.objectType;
			number = builder.number;
			count++;
			thr = new Thread(this, count + ":" + text + ":");
			thr.start(); // врубаем новый поток для фигуры и переходим в run
		}

		public Figure(Color color, String text, ObjectType type) {
			//xplus = true;
			//yplus = true;
			objectType = type;
			this.text = text;
			//x = 0;
			//y = 30;
			this.color = color;
			count++;
			//number = count;
			thr = new Thread(this, count + ":" + text + ":");
			thr.start(); // врубаем новый поток для шара и переходим в run
		}

		public int getSpeedX() {
			return speedX;
		}

		public void setSpeedX(int speedX) {
			this.speedX = speedX;
		}

		public int getSpeedY() {
			return speedY;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public void setSpeedY(int speedY) {
			this.speedY = speedY;
		}

		public String getText() {
			return text;
		}

		public void run() {
			while (true) {
				Dimension mainFrameSize = mainFrame.getSize();
				if (x > mainFrameSize.width - WIDTH)
					xplus = false;
				if (x < -1)
					xplus = true;
				if (y > mainFrameSize.height - HEIGHT)
					yplus = false;
				if (y < 31)
					yplus = true;
				if (xplus)
					x += speedX;
				else
					x -= speedX;
				if (yplus)
					y += speedY;
				else
					y -= speedY;
				setChanged();
				notifyObservers(this);
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		//  использую паттерн проектирования Builder
		public static class FigureBuilder {
			private ObjectType  objectType = ObjectType.IMAGE;
			private int         speedX = 1;
			private int         speedY = 1;
			private String      text = "default text";
			private Color       color = Color.black;
			private int number;

			private FigureBuilder(){
			}

			public static FigureBuilder aFigure() {
				return new FigureBuilder();
			}

			public FigureBuilder withColor(Color color){
				this.color = color;
				return this;
			}

			public Figure.FigureBuilder withNumber(int number){
				this.number = number;
				return this;
			}

			public FigureBuilder withText(String text) {
				this.text = text;
				return this;
			}

			public FigureBuilder withSpeedX(int speedX) {
				this.speedX = speedX;
				return this;
			}

			public FigureBuilder withSpeedY(int speedY) {
				this.speedY = speedY;
				return this;
			}

			public FigureBuilder withObjectType(ObjectType type) {
				this.objectType = type;
				return this;
			}

			// собираем все наши полученные атрибуты и передаем в
			// нужный класс через передачу нашего Builder
			public Figure build() {
				return new Figure(this);
			}
		}
	}

	static class WindowCloser extends WindowAdapter {
		@Override
		public void windowClosing (WindowEvent wE) {
			System.exit (0);
		}
	}

	static enum ObjectType {
		IMAGE("Картинка"),
		TEXT("Текст");

		private final String type;

		ObjectType(String type){
			this.type = type;
		}

		private String getType(){
			return type;
		}
	}

}
