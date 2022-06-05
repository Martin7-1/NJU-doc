package com.zyinnju.draw.tool;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 橡皮擦类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Eraser extends AbstractPaintTool {

	@Override
	public void draw(Graphics2D g) {
		// 把颜色变为背景色
		g.setPaint(Color.white);
		g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}
}
