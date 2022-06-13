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
public class Hexagon extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawHexagon(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return false;
	}

	private void drawHexagon(Graphics2D g) {
		// 获得六边形的六个顶点
		int[] xPoints = getPointsX();
		int[] yPoints = getPointsY();
		g.drawPolygon(xPoints, yPoints, 6);
	}

	private int[] getPointsX() {
		int xGap = Math.abs(startPoint.getX() - endPoint.getX());
		// x坐标分别是0、1/4gap、3/4gap、1
		int pointOne = PointUtil.getMinPointX(startPoint, endPoint) + xGap / 4;
		int pointTwo = PointUtil.getMinPointX(startPoint, endPoint);
		int pointThree = PointUtil.getMaxPointX(startPoint, endPoint) - xGap / 4;
		int pointFour = PointUtil.getMaxPointX(startPoint, endPoint);

		// 从左上开始顺时针
		return new int[]{pointOne, pointThree, pointFour, pointThree, pointOne, pointTwo};
	}

	private int[] getPointsY() {
		int yGap = Math.abs(startPoint.getY() - endPoint.getY());
		// x坐标分别是0、1/4gap、3/4gap、1
		int pointOne = PointUtil.getMinPointY(startPoint, endPoint) + yGap / 2;
		int pointTwo = PointUtil.getMinPointY(startPoint, endPoint);
		int pointThree = PointUtil.getMaxPointY(startPoint, endPoint);

		// 从左上开始顺时针
		return new int[]{pointTwo, pointTwo, pointOne, pointThree, pointThree, pointOne};
	}
}
