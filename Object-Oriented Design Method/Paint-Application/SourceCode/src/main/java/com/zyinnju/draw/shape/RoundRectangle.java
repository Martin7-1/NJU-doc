package com.zyinnju.draw.shape;

import com.zyinnju.draw.Point;
import com.zyinnju.utils.PointUtil;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 圆角矩形类
 *
 * @author Zyi
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoundRectangle extends AbstractShape {

	/**
	 * 圆角的水平直径
	 */
	private Integer arcWidth;
	/**
	 * 圆角的竖直直径
	 */
	private Integer arcHeight;

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		drawRoundRectangle(g);
	}

	@Override
	public boolean hasPoint(Point point) {
		return point.getX() <= PointUtil.getMaxPointX(startPoint, endPoint) && point.getX() >= PointUtil.getMinPointX(startPoint, endPoint)
			&& point.getY() >= PointUtil.getMinPointY(startPoint, endPoint) && point.getY() <= PointUtil.getMaxPointY(startPoint, endPoint);
	}

	private void drawRoundRectangle(Graphics2D g) {
		int width = Math.abs(startPoint.getX() - endPoint.getY());
		int height = Math.abs(startPoint.getY() - endPoint.getY());
		g.drawRoundRect(PointUtil.getMinPointX(startPoint, endPoint),
			PointUtil.getMinPointY(startPoint, endPoint), width, height, arcWidth, arcHeight);
	}
}
