package com.zyinnju.draw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;
import java.io.Serializable;

/**
 * 抽象的绘制内容类
 *
 * @author Zyi
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractContent implements Serializable {

	/**
	 * 绘制内容的起始位置 -- 鼠标一开始点的位置
	 */
	protected Point startPoint = new Point(0, 0);
	/**
	 * 绘制内容的结束位置 -- 鼠标结束点的位置
	 */
	protected Point endPoint = new Point(0, 0);
	/**
	 * 绘制内容的颜色
	 */
	protected Color color;
	/**
	 * 绘制该图形的粗细
	 */
	protected int thickness;

	/**
	 * 绘制内容方法
	 *
	 * @param g 画笔
	 */
	public abstract void draw(Graphics2D g);
}
