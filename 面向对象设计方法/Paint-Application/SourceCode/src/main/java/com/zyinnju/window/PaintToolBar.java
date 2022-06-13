package com.zyinnju.window;

import com.zyinnju.handler.GlobalStateHandler;
import com.zyinnju.utils.StyleUtil;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 工具栏类 用于选择所要绘制的图形等
 *
 * @author Zyi
 */
@Getter
public class PaintToolBar extends JToolBar {

	private static final int LENGTH = GlobalStateHandler.getResourceSize();
	/**
	 * 各种图形的选择按钮
	 */
	private JButton[] contentButtonList;
	/**
	 * 字体选择栏
	 */
	private JComboBox<String> fontComboBox;
	/**
	 * 字大小选择栏
	 */
	private JComboBox<String> fontSizeComboBox;
	/**
	 * 图标
	 */
	private ImageIcon[] imageIconList;
	/**
	 * 是否粗体勾选框
	 */
	private Checkbox boldButton;
	/**
	 * 是否斜体勾选框
	 */
	private Checkbox italicButton;

	private PaintToolBar() {
		initToolBar();
	}

	public static PaintToolBar getInstance() {
		return InnerClass.INSTANCE;
	}

	private void initToolBar() {
		// 初始化工具栏
		contentButtonList = new JButton[LENGTH];
		// 流式布局
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(StyleUtil.BACKGROUND_COLOR);
		boldButton = createCheckBox("bold");
		italicButton = createCheckBox("italic");

		initFontComboBox();
		initFontSizeComboBox();
		initImageIcon();
		setFloatable(false);
		add(fontComboBox);
		add(fontSizeComboBox);
		add(italicButton);
		add(boldButton);

		addListener();
	}

	private Checkbox createCheckBox(String title) {
		Checkbox checkbox = new Checkbox(title);
		checkbox.setBackground(StyleUtil.BACKGROUND_COLOR);
		checkbox.setPreferredSize(new Dimension(45, 30));

		return checkbox;
	}

	private void initFontComboBox() {
		fontComboBox = new JComboBox<>(GlobalStateHandler.FONT_LIST);
		fontComboBox.setPreferredSize(new Dimension(100, 30));
	}

	private void initFontSizeComboBox() {
		fontSizeComboBox = new JComboBox<>(GlobalStateHandler.FONT_SIZE_LIST);
		fontSizeComboBox.setPreferredSize(new Dimension(50, 30));
	}

	private void initImageIcon() {
		imageIconList = new ImageIcon[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			contentButtonList[i] = new JButton();
			imageIconList[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(GlobalStateHandler.getResource(i))));
			contentButtonList[i].setIcon(imageIconList[i]);
			contentButtonList[i].setToolTipText(GlobalStateHandler.getToolTip(i));
			// 设置图标大小
			contentButtonList[i].setPreferredSize(new Dimension(28, 28));
			contentButtonList[i].setBackground(Color.WHITE);
			add(contentButtonList[i]);
		}
	}

	private void addListener() {
		// 设置监听事件
		for (int i = 4; i < LENGTH; i++) {
			int finalI = i;
			contentButtonList[i].addActionListener(e -> {
				GlobalStateHandler.setCurContentType(finalI);
				repaint();
			});
		}

		contentButtonList[0].addActionListener(e -> GlobalStateHandler.setCurContentType(0));

		// 保存操作
		contentButtonList[1].addActionListener(e -> {
			PaintMenu.getInstance().saveFile();
			GlobalStateHandler.setIsSaved(true);
		});
		// 创建新文件操作
		contentButtonList[2].addActionListener(e -> DrawPanel.getInstance().clearAllContent());
		// 撤销操作
		contentButtonList[3].addActionListener(e -> DrawPanel.getInstance().undo());

		// 添加监听
		italicButton.addItemListener(e -> GlobalStateHandler.setIsItalicType(true));
		boldButton.addItemListener(e -> GlobalStateHandler.setIsBoldType(true));

		// 设置字体大小
		fontSizeComboBox.addItemListener(e -> GlobalStateHandler.setCurFontSize(fontSizeComboBox.getSelectedIndex()));
		// 设置字体
		fontComboBox.addItemListener(e -> GlobalStateHandler.setCurFont(fontComboBox.getSelectedIndex()));
	}

	private static class InnerClass {
		private static final PaintToolBar INSTANCE = new PaintToolBar();
	}
}
