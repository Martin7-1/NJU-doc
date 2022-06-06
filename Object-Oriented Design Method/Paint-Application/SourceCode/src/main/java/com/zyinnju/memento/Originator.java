package com.zyinnju.memento;

import com.zyinnju.draw.AbstractContent;
import lombok.Data;
import lombok.Getter;

/**
 * @author Zyi
 */
@Data
public class Originator {

	@Getter
	private AbstractContent content;

	public Originator(AbstractContent content) {
		this.content = content;
	}

	public Originator() {

	}

	public Memento createMemento() {
		return new Memento(this);
	}

	public void restoreMemento(Memento memento) {
		if (memento != null) {
			content = memento.getContent();
		}
	}
}
