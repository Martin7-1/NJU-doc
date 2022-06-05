package com.zyinnju.handler;

import com.zyinnju.enums.ContentType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.zyinnju.utils.ResourcesPathUtil.*;

/**
 * 用来记录画图工具中一些公共的状态
 * 有点类似观察者模式
 * 只是Handler没有办法去通知观察者状态的改变
 * 而是观察者每次需要状态需要自己来请求Handler
 *
 * @author Zyi
 */
public class GlobalStateHandler {

	/**
	 * 选择的图标
	 */
	public static final String[] RESOURCE_LIST = {MOUSE, SAVE, REFRESH, UNDO, PENCIL, LINE, RECTANGLE, FILL_RECT, OVAL, FILL_OVAL
		, CIRCLE, FILL_CIRCLE, ROUND_RECT, FILL_ROUND_RECT, TRIANGLE, PENTAGON, HEXAGON, ERASER, BRUSH, FONT};
	/**
	 * 图标提示
	 */
	public static final String[] TOOL_TIP_LIST = {"选择", "保存", "清空", "撤销", "铅笔", "直线", "空心矩形", "填充矩形", "空心椭圆", "填充椭圆", "空心圆形", "填充圆形",
		"空心圆角矩形", "填充圆角矩形", "三角形", "五边形", "六边形", "橡皮擦", "笔刷", "文本", "粗细"};
	/**
	 * 字体的选择
	 */
	public static final String[] FONT_LIST = {"宋体", "隶书", "华文彩云", "仿宋_GB2312", "华文行楷", "方正舒体"};
	public static final String[] FONT_SIZE_LIST = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36",
		"48", "72"};
	/**
	 * 笔粗细的提示
	 */
	private static final String[] STROKE_LIST = new String[]{STROKE_THINNEST, STROKE_THINNER, STROKE_THICKER, STROKE_THICKEST};
	/**
	 * 画笔粗细map
	 */
	private static final Map<String, Integer> STROKE_MAP = new HashMap<>();
	/**
	 * 是否保存
	 */
	private static boolean isSaved = false;
	/**
	 * 画笔粗细
	 */
	private static int thickness = 1;
	/**
	 * 是否是粗体
	 */
	private static boolean isBoldType = false;
	/**
	 * 是否是斜体
	 */
	private static boolean isItalicType = false;
	/**
	 * 当前选择的内容索引
	 */
	private static ContentType curContentType = ContentType.CHOOSE;
	/**
	 * 当前字体大小
	 */
	private static int curFontSize = 8;
	/**
	 * 当前字体
	 */
	private static String curFont = "宋体";
	/**
	 * 当前颜色
	 */
	private static Color curColor = Color.BLACK;

	static {
		STROKE_MAP.put("thinnest", 1);
		STROKE_MAP.put("thinner", 5);
		STROKE_MAP.put("thicker", 15);
		STROKE_MAP.put("thickest", 25);
	}

	public static boolean isSaved() {
		return isSaved;
	}

	public static void setIsSaved(boolean isSaved) {
		GlobalStateHandler.isSaved = isSaved;
	}

	public static int getThickness() {
		return thickness;
	}

	public static void setThickness(String thickness) {
		GlobalStateHandler.thickness = STROKE_MAP.get(thickness);
	}

	public static boolean isBoldType() {
		return isBoldType;
	}

	public static void setIsBoldType(boolean isBoldType) {
		GlobalStateHandler.isBoldType = isBoldType;
	}

	public static boolean isItalicType() {
		return isItalicType;
	}

	public static void setIsItalicType(boolean isItalicType) {
		GlobalStateHandler.isItalicType = isItalicType;
	}

	public static ContentType getCurContentType() {
		return curContentType;
	}

	public static void setCurContentType(ContentType curContentType) {
		GlobalStateHandler.curContentType = curContentType;
	}

	public static void setCurContentType(int index) {
		String name = TOOL_TIP_LIST[index];
		// 撤销保存不更改内容
		if (!"撤销".equals(name) && !"保存".equals(name) && !"清空".equals(name)) {
			System.out.println("you set content type: " + name);
			curContentType = ContentType.getContentTypeByValue(name);
		}
	}

	public static int getResourceSize() {
		return RESOURCE_LIST.length;
	}

	public static String getResource(int index) {
		return RESOURCE_LIST[index];
	}

	public static String getToolTip(int index) {
		return TOOL_TIP_LIST[index];
	}

	public static String getFontSize(int index) {
		return FONT_SIZE_LIST[index];
	}

	public static int getCurFontSize() {
		return curFontSize;
	}

	public static void setCurFontSize(int index) {
		GlobalStateHandler.curFontSize = Integer.parseInt(FONT_SIZE_LIST[index]);
	}

	public static String getCurFont() {
		return curFont;
	}

	public static void setCurFont(int index) {
		GlobalStateHandler.curFont = FONT_LIST[index];
	}

	public static String getFont(int index) {
		return FONT_LIST[index];
	}

	public static int getStrokeSize() {
		return STROKE_LIST.length;
	}

	public static String getStrokeSource(int index) {
		return STROKE_LIST[index];
	}

	public static int getDrawWidth(String width) {
		return STROKE_MAP.get(width);
	}

	public static Color getCurColor() {
		return curColor == null ? Color.BLACK : curColor;
	}

	public static void setCurColor(Color curColor) {
		GlobalStateHandler.curColor = curColor;
	}
}
