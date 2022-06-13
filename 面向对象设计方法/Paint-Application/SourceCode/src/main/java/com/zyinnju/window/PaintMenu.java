package com.zyinnju.window;

import com.zyinnju.enums.ContentType;
import com.zyinnju.handler.GlobalStateHandler;
import com.zyinnju.utils.ResourcesPathUtil;
import com.zyinnju.utils.filter.BMPFilter;
import com.zyinnju.utils.filter.GIFFilter;
import com.zyinnju.utils.filter.JPGFilter;
import com.zyinnju.utils.filter.PNGFilter;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 菜单类
 *
 * @author Zyi
 */
public class PaintMenu {

	/**
	 * 文件路径 todo: 替换为File.Separator
	 */
	private static final String AUTHOR_INFO = "src/main/resources/msg/AuthorInfo.txt";
	private static final String USER_HELP_INFO = "src/main/resources/msg/HelpInfo.txt";
	/**
	 * 菜单栏
	 */
	@Getter
	private JMenuBar menuBar;
	/**
	 * 画笔粗细的具体选项
	 */
	private JMenuItem[] strokeItemList;
	/**
	 * 菜单栏的分类按钮
	 */
	private JMenu[] menuList;

	private PaintMenu() {
		initMenu();
	}

	public static PaintMenu getInstance() {
		return InnerClass.INSTANCE;
	}

	private void initMenu() {
		// 菜单条
		menuBar = new JMenuBar();
		strokeItemList = new JMenuItem[GlobalStateHandler.getStrokeSize()];
		menuList = new JMenu[]{
			new JMenu("文件"),
			new JMenu("设置"),
			new JMenu("帮助"),
			new JMenu("粗细")
		};

		initFileMenu(menuList[0]);
		initSettingMenu(menuList[1]);
		initHelpMenu(menuList[2]);

		for (int i = 0; i < menuList.length - 1; i++) {
			menuBar.add(menuList[i]);
		}
	}

	public void saveFile() {
		// swing自带的文件选择器
		JFileChooser fileChooser = getFileChooser();
		int result = fileChooser.showSaveDialog(MainFrame.getInstance());
		if (result == JFileChooser.CANCEL_OPTION) {
			// 如果取消了
			return;
		}

		File file = fileChooser.getSelectedFile();
		if (!file.getName().endsWith(fileChooser.getFileFilter().getDescription())) {
			// 判断是否后缀和选择的类型相同
			String fileName = file.getPath() + fileChooser.getFileFilter().getDescription();
			file = new File(fileName);
		}
		if (!file.canWrite()) {
			JOptionPane.showMessageDialog(fileChooser, "该文件不可写", "该文件不可写", JOptionPane.ERROR_MESSAGE);
		}
		if ("".equals(file.getName())) {
			JOptionPane.showMessageDialog(fileChooser, "无效的文件名", "无效的文件名", JOptionPane.ERROR_MESSAGE);
		}

		BufferedImage image = createImage(DrawPanel.getInstance());
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createNewFile() {
		GlobalStateHandler.setCurContentType(ContentType.IMAGE);
		GlobalStateHandler.setCurColor(Color.BLACK);
		GlobalStateHandler.setThickness("thinnest");
		DrawPanel.getInstance().createNewGraphics();
		DrawPanel.getInstance().repaint();
	}

	public void openFile() {
		JFileChooser fileChooser = getFileChooser();
		// 弹出一个 "Open File" 文件选择器对话框
		int result = fileChooser.showOpenDialog(MainFrame.getInstance());
		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}
		// 得到选择文件的名字
		File file = fileChooser.getSelectedFile();
		if (!file.getName().endsWith(fileChooser.getFileFilter().getDescription())) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "文件格式错误！");
			return;
		}
		if (!file.canRead()) {
			JOptionPane.showMessageDialog(fileChooser, "该文件不可读", "该文件不可读", JOptionPane.ERROR_MESSAGE);
		}

		if ("".equals(file.getName())) {
			JOptionPane.showMessageDialog(fileChooser, "无效的文件名", "无效的文件名", JOptionPane.ERROR_MESSAGE);
		}

		BufferedImage image;

		try {
			GlobalStateHandler.setCurContentType(ContentType.IMAGE);
			image = ImageIO.read(file);
			DrawPanel.getInstance().createNewGraphics(image);
			DrawPanel.getInstance().repaint();
			GlobalStateHandler.setCurContentType(ContentType.PENCIL);
			DrawPanel.getInstance().createNewGraphics();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		// 设置文件显示类型为仅显示文件
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// 文件过滤器
		JPGFilter jpgFilter = new JPGFilter();
		BMPFilter bmpFilter = new BMPFilter();
		PNGFilter pngFilter = new PNGFilter();
		GIFFilter gifFilter = new GIFFilter();
		// 向用户可选择的文件过滤器列表添加一个过滤器。
		fileChooser.addChoosableFileFilter(jpgFilter);
		fileChooser.addChoosableFileFilter(bmpFilter);
		fileChooser.addChoosableFileFilter(pngFilter);
		fileChooser.addChoosableFileFilter(gifFilter);
		// 返回当前的文本过滤器，并设置成当前的选择
		fileChooser.setFileFilter(fileChooser.getFileFilter());
		return fileChooser;
	}

	private BufferedImage createImage(DrawPanel drawPanel) {
		int width = MainFrame.getInstance().getWidth();
		int height = MainFrame.getInstance().getHeight();
		BufferedImage panelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = panelImage.createGraphics();

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		graphics.translate(0, 0);
		drawPanel.paint(graphics);
		graphics.dispose();
		return panelImage;
	}

	private String getMessage(String path) {
		// 读取文件
		StringBuilder content = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String res = null;
			while ((res = reader.readLine()) != null) {
				content.append(res);
				content.append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content.toString();
	}

	private void initFileMenu(JMenu menu) {
		JMenuItem fileItemNew = new JMenuItem("新建", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.NEW))));
		JMenuItem fileItemOpen = new JMenuItem("打开", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.OPEN))));
		JMenuItem fileItemSave = new JMenuItem("保存", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.SAVE))));
		JMenuItem fileItemExit = new JMenuItem("退出", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.EXIT))));

		// 设置快捷键
		fileItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		fileItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		addNewListener(fileItemNew);
		addSaveListener(fileItemSave);
		addOpenListener(fileItemOpen);
		addExitListener(fileItemExit);

		menu.add(fileItemNew);
		menu.add(fileItemOpen);
		menu.add(fileItemSave);
		menu.add(fileItemExit);
	}

	private void initSettingMenu(JMenu menu) {
		JMenuItem setItemColor = new JMenuItem("颜色", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.MENU_COLOR_CHOOSER))));
		JMenuItem setItemUndo = new JMenuItem("撤销", new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.UNDO))));
		for (int i = 0; i < strokeItemList.length; i++) {
			strokeItemList[i] = new JMenuItem("", new ImageIcon(Objects.requireNonNull(getClass().getResource(GlobalStateHandler.getStrokeSource(i)))));
			menuList[3].add(strokeItemList[i]);
		}
		setItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));

		addColorListener(setItemColor);
		addUndoListener(setItemUndo);
		addStrokeListener(menuList[3]);

		// 添加菜单项到菜单
		menu.add(setItemColor);
		menu.add(setItemUndo);
		menu.add(menuList[3]);
	}

	private void initHelpMenu(JMenu menu) {
		JMenuItem helpItemUse = new JMenuItem("使用手册");
		JMenuItem authorInfo = new JMenuItem("关于画图");
		authorInfo.addActionListener(e -> JOptionPane.showMessageDialog(null, getMessage(AUTHOR_INFO), "关于画图", JOptionPane.PLAIN_MESSAGE));
		helpItemUse.addActionListener(e -> JOptionPane.showMessageDialog(null, getMessage(USER_HELP_INFO), "使用说明", JOptionPane.PLAIN_MESSAGE));
		menu.add(helpItemUse);
		menu.add(authorInfo);
	}

	private void addNewListener(JMenuItem fileItemNew) {
		fileItemNew.addActionListener(e -> createNewFile());
	}

	private void addSaveListener(JMenuItem fileItemSave) {
		fileItemSave.addActionListener(e -> {
			// 保存文件，并将标志符saved设置为1
			saveFile();
			GlobalStateHandler.setIsSaved(true);
		});
	}

	private void addOpenListener(JMenuItem fileItemOpen) {
		fileItemOpen.addActionListener(e -> {
			// 打开文件，并将标志符saved设置为0
			openFile();
			GlobalStateHandler.setIsSaved(false);
		});

	}

	private void addExitListener(JMenuItem fileItemExit) {
		fileItemExit.addActionListener(e -> {
			// 如果文件已经保存就直接退出，若果文件没有保存，提示用户选择是否退出
			if (GlobalStateHandler.isSaved()) {
				System.exit(0);
			} else {
				int exitMark = JOptionPane.showConfirmDialog(null, "您还没保存，确定要退出？", "提示", JOptionPane.OK_CANCEL_OPTION);
				if (exitMark == 0) {
					System.exit(0);
				}
			}
		});
	}

	private void addColorListener(JMenuItem setItemColor) {
		setItemColor.addActionListener(e -> {
			// 设置粗细
			ColorPanel.getInstance().chooseColor();
		});
	}

	private void addUndoListener(JMenuItem setItemUndo) {
		setItemUndo.addActionListener(e -> DrawPanel.getInstance().undo());
	}

	private void addStrokeListener(JMenu strokeMenu) {
		Map<Integer, String> map = initStrokeMap();
		for (int i = 0; i < strokeItemList.length; i++) {
			int tempI = i;
			strokeItemList[i].addActionListener(e -> GlobalStateHandler.setThickness(map.get(tempI)));
		}
	}

	private Map<Integer, String> initStrokeMap() {
		Map<Integer, String> map = new HashMap<>(4);
		map.put(0, "thinnest");
		map.put(1, "thinner");
		map.put(2, "thicker");
		map.put(3, "thickest");
		return map;
	}

	private static class InnerClass {
		private static final PaintMenu INSTANCE = new PaintMenu();
	}
}
