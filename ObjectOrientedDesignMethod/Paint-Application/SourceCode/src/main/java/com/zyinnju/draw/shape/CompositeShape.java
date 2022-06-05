package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 复合图形类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
@ToString
public class CompositeShape extends AbstractShape {

	private final List<AbstractShape> shapeList = new ArrayList<>();

	public void add(AbstractShape shape) {
		shapeList.add(shape);
	}

	public void remove(AbstractShape shape) {
		shapeList.remove(shape);
	}

	public AbstractShape getChild(int index) {
		return shapeList.get(index);
	}

	public int getShapeListSize() {
		return shapeList.size();
	}

	@Override
	public void draw(Graphics2D g) {
		for (AbstractShape shape : shapeList) {
			shape.draw(g);
		}
	}

	public CompositeShape cloneSelf() {
		CompositeShape cloneCompositeShape = new CompositeShape();
		for (int i = 0; i < getShapeListSize(); i++) {
			AbstractShape shape = getChild(i).clone();
			// 通过平移的距离来获得中心的点
			cloneCompositeShape.add(shape);
		}

		return cloneCompositeShape;
	}

	@Override
	public boolean hasPoint(Point point) {
		return false;
	}
}
