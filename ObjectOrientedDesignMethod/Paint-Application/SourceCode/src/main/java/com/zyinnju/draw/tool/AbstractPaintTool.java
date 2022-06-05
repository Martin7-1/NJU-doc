package com.zyinnju.draw.tool;

import com.zyinnju.draw.AbstractContent;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 抽象的画图工具类 - 包括画笔和刷子和橡皮擦
 *
 * @author Zyi
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractPaintTool extends AbstractContent {

	/**
	 * 铅笔或橡皮擦的笔迹长度
	 */
	protected int length = 0;

	public void addLength() {
		length++;
	}
}
