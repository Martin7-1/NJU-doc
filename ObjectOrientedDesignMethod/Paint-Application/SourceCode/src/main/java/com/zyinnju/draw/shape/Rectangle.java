package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 矩形
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Rectangle extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawRectangle(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return point.getX() <= PointUtil.getMaxPointX(startPoint, endPoint) && point.getX() >= PointUtil.getMinPointX(startPoint, endPoint)
			&& point.getY() >= PointUtil.getMinPointY(startPoint, endPoint) && point.getY() <= PointUtil.getMaxPointY(startPoint, endPoint);
	}

	private void drawRectangle(Graphics2D g) {
		int width = Math.abs(startPoint.getX() - endPoint.getX());
		int height = Math.abs(startPoint.getY() - endPoint.getY());
		g.drawRect(PointUtil.getMinPointX(startPoint, endPoint),
			PointUtil.getMinPointY(startPoint, endPoint), width, height);
	}
}
