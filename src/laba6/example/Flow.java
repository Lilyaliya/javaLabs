package laba6.example;

/* FlowLayout*/
import java.awt.*;
class Flow extends Frame {
	public Flow() {
		this.setSize(200, 100);
		String items[]= {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		Button but[]= new Button[10];
		setLayout (new FlowLayout (FlowLayout.LEFT));
		for (int i= 0; i < items.length; i++) {
			but[i]= new Button (items[i]);
			add(but[i]);
		}
		this.setVisible(true);
	}
	public static void main (String[] args) {
		Flow f = new Flow();
	}
}