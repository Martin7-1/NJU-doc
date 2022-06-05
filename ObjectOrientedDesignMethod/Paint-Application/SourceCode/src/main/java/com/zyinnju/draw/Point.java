package com.zyinnju.draw;

import com.zyinnju.utils.PointUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 坐标点
 *
 * @author Zyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Point implements Serializable {

	private Integer x;

	private Integer y;

	public double distanceOf(Point point) {
		return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2));
	}

	public boolean between(Point pointOne, Point pointTwo) {
		// 首先需要判断哪个比较大
		int minX = PointUtil.getMinPointX(pointOne, pointTwo);
		int maxX = PointUtil.getMaxPointX(pointOne, pointTwo);
		int minY = PointUtil.getMinPointY(pointOne, pointTwo);
		int maxY = PointUtil.getMaxPointY(pointOne, pointTwo);

		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}
}
