package com.zyinnju.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zyi
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ContentType implements ValueEnum, Serializable {

	/**
	 * 鼠标选择
	 */
	CHOOSE("选择"),
	/**
	 * 图片
	 */
	IMAGE("图片"),
	/**
	 * 铅笔
	 */
	PENCIL("铅笔"),
	/**
	 * 笔刷
	 */
	BRUSH("笔刷"),
	/**
	 * 橡皮
	 */
	ERASER("橡皮"),
	/**
	 * 空心圆
	 */
	CIRCLE("空心圆形"),
	/**
	 * 实心圆
	 */
	FILL_CIRCLE("填充圆形"),
	/**
	 * 实心椭圆
	 */
	FILL_OVAL("填充椭圆"),
	/**
	 * 实心矩形
	 */
	FILL_RECT("填充矩形"),
	/**
	 * 实心圆角矩形
	 */
	FILL_ROUND_RECT("填充圆角矩形"),
	/**
	 * 六边形
	 */
	HEXAGON("六边形"),
	/**
	 * 直线
	 */
	LINE("直线"),
	/**
	 * 椭圆
	 */
	OVAL("空心椭圆"),
	/**
	 * 五边形
	 */
	PENTAGON("五边形"),
	/**
	 * 矩形
	 */
	RECTANGLE("空心矩形"),
	/**
	 * 圆角矩形
	 */
	ROUND_RECT("空心圆角矩形"),
	/**
	 * 三角形
	 */
	TRIANGLE("三角形"),
	/**
	 * 文字
	 */
	TEXT("文本");

	private static final Map<String, ContentType> SHAPE_MAP = new HashMap<>();

	static {
		for (ContentType contentType : ContentType.values()) {
			SHAPE_MAP.put(contentType.getValue(), contentType);
		}
	}

	@Setter
	private String value;

	public static ContentType getContentTypeByValue(String value) {
		return SHAPE_MAP.get(value);
	}

	@Override
	public String getValue() {
		return this.value;
	}
}
