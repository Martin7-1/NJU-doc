package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 三角形类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Triangle extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawTriangle(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return false;
	}

	private void drawTriangle(Graphics2D g) {
		int[] xPoints = getPointsX();
		int[] yPoints = getPointsY();
		g.drawPolygon(xPoints, yPoints, 3);
	}

	private int[] getPointsX() {
		int xGap = Math.abs(startPoint.getX() - endPoint.getX());
		return new int[]{PointUtil.getMinPointX(startPoint, endPoint) + xGap / 2,
			PointUtil.getMaxPointX(startPoint, endPoint), PointUtil.getMinPointX(startPoint, endPoint)};
	}

	private int[] getPointsY() {
		int yGap = Math.abs(startPoint.getY() - endPoint.getY());
		int maxPoint = PointUtil.getMaxPointY(startPoint, endPoint);
		return new int[]{PointUtil.getMinPointY(startPoint, endPoint), maxPoint, maxPoint};
	}
}
