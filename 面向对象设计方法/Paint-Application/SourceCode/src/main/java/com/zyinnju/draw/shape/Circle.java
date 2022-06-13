package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Circle extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawOval(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		// 半径
		int radius = getRadius() / 2;
		Point circlePoint = new Point((startPoint.getX() + endPoint.getX()) / 2, (startPoint.getY() + endPoint.getY()) / 2);
		// 判断距离
		return point.distanceOf(circlePoint) <= radius;
	}

	private void drawOval(Graphics2D g) {
		int radius = getRadius();
		g.drawOval(PointUtil.getMinPointX(startPoint, endPoint),
			PointUtil.getMinPointY(startPoint, endPoint), radius, radius);
	}

	private int getRadius() {
		return Math.max(Math.abs(startPoint.getX() - endPoint.getX()), Math.abs(startPoint.getY() - endPoint.getY()));
	}
}
