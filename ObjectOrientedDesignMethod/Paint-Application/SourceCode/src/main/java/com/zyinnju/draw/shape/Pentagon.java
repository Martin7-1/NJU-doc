package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 五边形类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Pentagon extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawPentagon(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return false;
	}

	private void drawPentagon(Graphics2D g) {
		int[] xPoints = getPointsX();
		int[] yPoints = getPointsY();
		g.drawPolygon(xPoints, yPoints, 5);
	}

	private int[] getPointsX() {
		int xGap = Math.abs(startPoint.getX() - endPoint.getX());
		// 中点
		int pointOne = PointUtil.getMinPointX(startPoint, endPoint) + xGap / 2;
		double tan = Math.tan((54 * 2 * Math.PI) / 360);
		double width = xGap / (2 * tan);
		int pointTwo = PointUtil.getMaxPointX(startPoint, endPoint);
		int pointThree = PointUtil.getMaxPointX(startPoint, endPoint) - (int) ((xGap - width) / 2);
		int pointFour = PointUtil.getMinPointX(startPoint, endPoint) + (int) ((xGap - width) / 2);
		int pointFive = PointUtil.getMinPointX(startPoint, endPoint);

		return new int[]{pointOne, pointTwo, pointThree, pointFour, pointFive};
	}

	private int[] getPointsY() {
		int yGap = Math.abs(startPoint.getY() - endPoint.getY());
		int xGap = Math.abs(startPoint.getX() - endPoint.getX());
		// 中点
		int pointOne = PointUtil.getMinPointY(startPoint, endPoint);
		double tan = Math.tan((54 * 2 * Math.PI) / 360);
		double width = xGap / (2 * tan);
		double cos = Math.cos((54 * 2 * Math.PI) / 360);
		double offset = width * cos;
		int pointTwo = PointUtil.getMinPointY(startPoint, endPoint) + (int) offset;
		int pointThree = PointUtil.getMaxPointY(startPoint, endPoint);

		return new int[]{pointOne, pointTwo, pointThree, pointThree, pointTwo};
	}
}
