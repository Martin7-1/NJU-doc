package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 直线类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Line extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}

	@Override
	public boolean hasPoint(Point point) {
		// 判断斜率相等
		return Math.abs(getSlope(point, startPoint) - getSlope(point, endPoint)) <= 10e-5;
	}

	private double getSlope(Point pointOne, Point pointTwo) {
		return (pointOne.getY() - pointTwo.getY()) / (double) (pointOne.getX() - pointTwo.getX());
	}
}
