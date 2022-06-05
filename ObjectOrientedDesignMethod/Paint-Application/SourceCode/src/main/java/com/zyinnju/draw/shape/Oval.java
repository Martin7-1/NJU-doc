package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 椭圆类 与圆的差别是分为长径和短径
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Oval extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawOval(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		// 判断点是否在椭圆内部的方法: b^2x^2 + a^2y^2 <= a^2b^2
		Point ovalHeart = new Point(Math.abs(startPoint.getX() - endPoint.getX()) / 2, Math.abs(startPoint.getY() - endPoint.getY()) / 2);
		Point newPoint = new Point(point.getX() - ovalHeart.getX(), point.getY() - ovalHeart.getY());
		int a = Math.abs(startPoint.getX() - endPoint.getX());
		int b = Math.abs(startPoint.getY() - endPoint.getY());
		return Math.pow(b, 2) * Math.pow(newPoint.getX(), 2) + Math.pow(a, 2) * Math.pow(newPoint.getY(), 2) <= Math.pow(a, 2) * Math.pow(b, 2);
	}

	private void drawOval(Graphics2D g) {
		// 椭圆的长宽不一样
		int width = Math.abs(startPoint.getX() - endPoint.getX());
		int height = Math.abs(startPoint.getY() - endPoint.getY());
		g.drawOval(PointUtil.getMinPointX(startPoint, endPoint),
			PointUtil.getMinPointY(startPoint, endPoint), width, height);
	}
}
