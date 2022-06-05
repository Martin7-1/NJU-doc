package com.zyinnju.window;

import com.zyinnju.handler.GlobalStateHandler;
import com.zyinnju.utils.ResourcesPathUtil;
import com.zyinnju.utils.StyleUtil;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;

/**
 * 颜色类
 *
 * @author Zyi
 */
public class ColorPanel extends JPanel {

	/**
	 * 调色板
	 * 颜色数组，用来设置按钮的背景颜色
	 */
	private final Color[] colorList = {new Color(255, 255, 255), new Color(0, 0, 0), new Color(127, 127, 127),
		new Color(195, 195, 195), new Color(136, 0, 21), new Color(185, 122, 87), new Color(237, 28, 36),
		new Color(255, 174, 201), new Color(255, 127, 39), new Color(255, 242, 0), new Color(239, 228, 176),
		new Color(34, 117, 76), new Color(181, 230, 29), new Color(0, 162, 232), new Color(153, 217, 234),
		new Color(63, 72, 204), new Color(112, 146, 190), new Color(163, 73, 164), new Color(200, 191, 231),
		new Color(89, 173, 154), new Color(8, 193, 194), new Color(9, 253, 76), new Color(153, 217, 234),
		new Color(199, 73, 4)};
	/**
	 * 选择后景颜色的按钮
	 */
	private JButton backButton;
	/**
	 * 选择前景颜色的按钮
	 */
	private JButton frontButton;
	/**
	 * 选择颜色的面板
	 */
	private JPanel childPanel;

	private ColorPanel() {
		initColorPanel();
	}

	public static ColorPanel getInstance() {
		return InnerClass.INSTANCE;
	}

	private void initColorPanel() {
		// 初始化颜色面板
		setPreferredSize(new Dimension(60, 60));
		setLayout(null);
		setBackground(StyleUtil.BACKGROUND_COLOR);

		initChildPanel();
		JPanel upPanel = initUpPanel();
		JPanel downPanel = initDownPanel();

		childPanel.add(upPanel);
		childPanel.add(downPanel);
	}

	public void chooseColor() {
		Color color = JColorChooser.showDialog(null, "请选择颜色", GlobalStateHandler.getCurColor());
		backButton.setBackground(color);
		GlobalStateHandler.setCurColor(color);
	}

	private void initChildPanel() {
		childPanel = new JPanel();
		childPanel.setBackground(Color.cyan);
		childPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		childPanel.setBounds(10, 10, 40, 280);
		childPanel.setToolTipText("颜色");
		add(childPanel);
	}

	private JPanel initUpPanel() {
		JPanel upPanel = new JPanel();
		// 按钮特效,简单的双线斜面边框
		BevelBorder bevelBorderOne = new BevelBorder(0, Color.gray, Color.white);
		BevelBorder bevelBorderTwo = new BevelBorder(1, Color.gray, Color.white);
		upPanel.setBackground(Color.white);
		upPanel.setLayout(null);
		upPanel.setBorder(bevelBorderOne);
		upPanel.setPreferredSize(new Dimension(40, 40));

		// 上面面板中的两个颜色按钮
		backButton = createButton(5, 5, 20, 20, bevelBorderOne, Color.BLACK);
		frontButton = createButton(15, 15, 20, 20, bevelBorderTwo, Color.WHITE);
		upPanel.add(backButton);
		upPanel.add(frontButton);
		addListener();

		return upPanel;
	}

	private JButton createButton(int x, int y, int width, int height, Border border, Color color) {
		JButton button = new JButton();
		button.setBounds(x, y, width, height);
		button.setBorder(border);
		button.setBackground(color);

		return button;
	}

	private void addListener() {
		backButton.addActionListener(e -> {
			// 拿到被选中按钮的对象
			JButton button = (JButton) e.getSource();
			// 拿到被选中按钮的背景颜色
			GlobalStateHandler.setCurColor(button.getBackground());

		});
		frontButton.addActionListener(e -> {
			// 拿到被选中按钮的对象
			JButton button = (JButton) e.getSource();
			// 拿到被选中按钮的背景颜色
			GlobalStateHandler.setCurColor(button.getBackground());
		});
	}

	private void addChooseColorPanel(JPanel downPanel) {
		JButton btnColor = new JButton();
		ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.COLOR_CHOOSER)));
		btnColor.setIcon(imageIcon);
		btnColor.setPreferredSize(new Dimension(40, 40));
		btnColor.setToolTipText("更多颜色");
		downPanel.add(btnColor);
		btnColor.addActionListener(e -> chooseColor());
	}

	private void addMultiColorPanel(JPanel downPanel) {
		// 循环添加24个颜色按钮
		BevelBorder bevelBorderOne = new BevelBorder(0, Color.gray, Color.white);
		for (Color color : colorList) {
			JButton button = new JButton();
			button.setOpaque(true);
			button.setBackground(color);
			button.setPreferredSize(new Dimension(20, 20));
			button.setBorder(bevelBorderOne);
			button.addActionListener(e -> {
				// 拿到被选中按钮的对象
				JButton jbt = (JButton) e.getSource();
				// 拿到被选中按钮的背景颜色
				GlobalStateHandler.setCurColor(jbt.getBackground());
				// 把左面板中的按钮颜色设置成选中按钮的背景颜色
				backButton.setBackground(GlobalStateHandler.getCurColor());

			});
			downPanel.add(button);
		}
	}

	private JPanel initDownPanel() {
		// 右面板
		JPanel downPanel = new JPanel();
		downPanel.setBackground(StyleUtil.BACKGROUND_COLOR);
		downPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		downPanel.setPreferredSize(new Dimension(60, 240));

		// 添加颜色窗口
		addChooseColorPanel(downPanel);
		addMultiColorPanel(downPanel);
		return downPanel;
	}

	private static class InnerClass {
		private static final ColorPanel INSTANCE = new ColorPanel();
	}
}
