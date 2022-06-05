package com.zyinnju.factory;

import com.zyinnju.draw.AbstractContent;
import com.zyinnju.draw.Images;
import com.zyinnju.draw.Text;
import com.zyinnju.draw.shape.*;
import com.zyinnju.draw.tool.Brush;
import com.zyinnju.draw.tool.Eraser;
import com.zyinnju.draw.tool.Pencil;
import com.zyinnju.enums.ContentType;
import com.zyinnju.handler.GlobalStateHandler;

import javax.swing.*;

/**
 * 绘图内容工厂类
 * 简单工程设计模式
 *
 * @author Zyi
 */
public class ContentFactory {

	/**
	 * 工厂方法
	 *
	 * @param contentType 当前的内容类型
	 * @return 创建出来的绘图内容
	 */
	public static AbstractContent createContent(ContentType contentType) {
		AbstractContent content = null;
		switch (contentType) {
			case PENCIL:
				content = new Pencil();
				break;
			case IMAGE:
				content = new Images();
				break;
			case LINE:
				content = new Line();
				break;
			case RECTANGLE:
				content = new Rectangle();
				break;
			case FILL_RECT:
				content = new FillRect();
				break;
			case OVAL:
				content = new Oval();
				break;
			case FILL_OVAL:
				content = new FillOval();
				break;
			case CIRCLE:
				content = new Circle();
				break;
			case FILL_CIRCLE:
				content = new FillCircle();
				break;
			case ROUND_RECT:
				content = new RoundRectangle();
				break;
			case FILL_ROUND_RECT:
				content = new FillRoundRect();
				break;
			case TRIANGLE:
				content = new Triangle();
				break;
			case PENTAGON:
				content = new Pentagon();
				break;
			case HEXAGON:
				content = new Hexagon();
				break;
			case ERASER:
				content = new Eraser();
				break;
			case BRUSH:
				content = new Brush();
				break;
			case TEXT:
				content = new Text();
				Text text = (Text) content;
				text.setText(JOptionPane.showInputDialog("请输入文字"));
				System.out.println("text: " + text.getText());
				text.setFontSize(GlobalStateHandler.getCurFontSize());
				text.setFontName(GlobalStateHandler.getCurFont());
				text.setIsItalicType(GlobalStateHandler.isItalicType());
				text.setIsBoldType(GlobalStateHandler.isBoldType());
				break;
			default:
		}

		return content;
	}
}
