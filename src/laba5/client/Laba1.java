package laba5.client;

import laba5.server.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Laba1 implements Task<ArrayList<Integer>>, Serializable {
	String[] numbersArray;

	public  Laba1(String[] args){
		numbersArray = args;
	}

	@Override
	public ArrayList<Integer> execute() {
		ArrayList<Integer> listInt = new ArrayList<>();
		for ( String elem: numbersArray) {
			listInt.add(Integer.parseInt(elem));
		}
		listInt.sort(Collections.reverseOrder());
		return listInt;
	}
}
