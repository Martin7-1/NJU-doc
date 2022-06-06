package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 实心矩形类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class FillRect extends AbstractShape {

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		fillRectangle(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return point.getX() <= PointUtil.getMaxPointX(startPoint, endPoint) && point.getX() >= PointUtil.getMinPointX(startPoint, endPoint)
			&& point.getY() >= PointUtil.getMinPointY(startPoint, endPoint) && point.getY() <= PointUtil.getMaxPointY(startPoint, endPoint);
	}

	private void fillRectangle(Graphics2D g) {
		int width = Math.abs(startPoint.getX() - endPoint.getX());
		int height = Math.abs(startPoint.getY() - endPoint.getY());
		g.fillRect(PointUtil.getMinPointX(startPoint, endPoint),
			PointUtil.getMinPointY(startPoint, endPoint), width, height);
	}
}
