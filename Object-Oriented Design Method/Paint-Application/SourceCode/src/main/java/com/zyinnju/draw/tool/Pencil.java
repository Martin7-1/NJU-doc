package com.zyinnju.draw.tool;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * 铅笔类
 *
 * @author Zyi
 */
@NoArgsConstructor
@SuperBuilder
public class Pencil extends AbstractPaintTool {

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (startPoint != null && endPoint != null) {
			g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		}
	}
}
