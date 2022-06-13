package com.zyinnju.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zyi
 */
public class CareTaker {

	/**
	 * 存储绘制的图形列表
	 */
	private List<Memento> contentList;

	private CareTaker() {
		contentList = new ArrayList<>();
	}

	public static CareTaker getInstance() {
		return InnerClass.INSTANCE;
	}

	public Memento getMemento(int index) {
		return contentList.get(index);
	}

	public void removeAllMemento() {
		contentList = new ArrayList<>();
	}

	public int getListSize() {
		return contentList.size();
	}

	public void addMemento(Memento memento) {
		contentList.add(memento);
	}

	public Memento removeMemento(int index) {
		if (index >= 0 && index < getListSize()) {
			return contentList.remove(index);
		} else {
			return null;
		}
	}

	private static class InnerClass {
		private static final CareTaker INSTANCE = new CareTaker();
	}
}
