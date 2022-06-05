package com.zyinnju.draw.shape;

import com.zyinnju.draw.AbstractContent;
import com.zyinnju.draw.Point;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.awt.*;
import java.io.Serializable;

/**
 * 抽象的图形类
 *
 * @author Zyi
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractShape extends AbstractContent implements Serializable, Cloneable {

	@Override
	public void draw(Graphics2D g) {
		// 设置颜色
		g.setPaint(color);
		// 设置线宽
		g.setStroke(new BasicStroke(thickness));
		// 设置渲染算法
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * 通过中心点来设置图形的起始点和终止点
	 *
	 * @param centerPoint 中心点
	 */
	public void setStartPointAndEndPoint(Point centerPoint) {
		int width = Math.abs(startPoint.getX() - endPoint.getX());
		int height = Math.abs(startPoint.getY() - endPoint.getY());
		Point newStartPoint = new Point(centerPoint.getX() - width / 2, centerPoint.getY() - height / 2);
		Point newEndPoint = new Point(centerPoint.getX() + width / 2, centerPoint.getY() + height / 2);
		startPoint = newStartPoint;
		endPoint = newEndPoint;
	}

	public boolean isInner(Point startPoint, Point endPoint) {
		return this.startPoint.between(startPoint, endPoint) && this.endPoint.between(startPoint, endPoint);
	}

	public Point getCenterPoint() {
		return new Point((startPoint.getX() + endPoint.getX()) / 2, (startPoint.getY() + endPoint.getY()) / 2);
	}

	/**
	 * 判断某个点是否在图形的面积内
	 *
	 * @param point 点
	 * @return true if the point is inner the shape, false otherwise
	 */
	public abstract boolean hasPoint(Point point);

	@Override
	public AbstractShape clone() {
		try {
			return (AbstractShape) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
