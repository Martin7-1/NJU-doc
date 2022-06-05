package com.zyinnju.utils;

import com.zyinnju.draw.Point;

/**
 * @author Zyi
 */
public class PointUtil {

	public static Integer getMinPointX(Point pointOne, Point pointTwo) {
		return Math.min(pointOne.getX(), pointTwo.getX());
	}

	public static Integer getMaxPointX(Point pointOne, Point pointTwo) {
		return Math.max(pointOne.getX(), pointTwo.getX());
	}

	public static Integer getMinPointY(Point pointOne, Point pointTwo) {
		return Math.min(pointOne.getY(), pointTwo.getY());
	}

	public static Integer getMaxPointY(Point pointOne, Point pointTwo) {
		return Math.max(pointOne.getY(), pointTwo.getY());
	}
}
