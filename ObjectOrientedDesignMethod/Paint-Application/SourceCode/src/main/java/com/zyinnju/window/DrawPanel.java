package com.zyinnju.window;

import com.zyinnju.draw.AbstractContent;
import com.zyinnju.draw.Point;
import com.zyinnju.draw.shape.AbstractShape;
import com.zyinnju.draw.shape.CompositeShape;
import com.zyinnju.draw.tool.AbstractPaintTool;
import com.zyinnju.enums.ContentType;
import com.zyinnju.factory.ContentFactory;
import com.zyinnju.factory.ImageFactory;
import com.zyinnju.handler.GlobalStateHandler;
import com.zyinnju.memento.CareTaker;
import com.zyinnju.memento.Originator;
import com.zyinnju.utils.ResourcesPathUtil;
import com.zyinnju.utils.StyleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 绘画的画布
 *
 * @author Zyi
 */
public class DrawPanel extends JPanel {

	/**
	 * 撤销功能负责人
	 */
	private final CareTaker careTaker;
	/**
	 * 弹出选择窗
	 */
	private final JPopupMenu copyMenu;
	/**
	 * 绘制内容的列表
	 */
	private List<AbstractContent> contentList;
	/**
	 * 复制的图形
	 */
	private AbstractShape copyShape;
	/**
	 * 复制时点击的位置
	 */
	private Point copyPoint;
	/**
	 * 组合图形的开始的点
	 */
	private Point compositeStartPoint;
	/**
	 * 组合图形结束的点
	 */
	private Point compositeEndPoint;
	/**
	 * 是否开始组合
	 */
	private boolean isBeginComposite;
	/**
	 * 组合的图形 fixme: 目前只支持组合一个
	 */
	private CompositeShape compositeShape;

	private DrawPanel() {
		contentList = new ArrayList<>();
		careTaker = CareTaker.getInstance();
		copyMenu = new JPopupMenu();
		setCursor(Cursor.getDefaultCursor());
		setBackground(StyleUtil.DRAW_PANEL_COLOR);
		addMouseListener(new MouseListener());
		addMouseMotionListener(new MouseMotionListener());

		initPopupMenu();
		add(copyMenu);
	}

	public static DrawPanel getInstance() {
		return InnerClass.INSTANCE;
	}

	public AbstractContent getCurContent() {
		return contentList.get(contentList.size() - 1);
	}

	public AbstractContent getContent(int index) {
		return contentList.get(index);
	}

	public int getContentSize() {
		return contentList.size();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 定义画板
		Graphics2D g2d = (Graphics2D) g;
		for (AbstractContent content : contentList) {
			draw(g2d, content);
		}

		if (GlobalStateHandler.getCurContentType().equals(ContentType.CHOOSE) && isBeginComposite) {
			// 绘制一个虚线框
			drawDashedBox(g2d);
		}
	}

	public void createNewGraphics() {
		ContentType curContentType = GlobalStateHandler.getCurContentType();
		// 设置光标
		if (curContentType.equals(ContentType.ERASER)) {
			// 如果是橡皮的话
			try {
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource(ResourcesPathUtil.CURSOR))).getImage();
				Cursor cursor = tk.createCustomCursor(image, new java.awt.Point(10, 10), "norm");
				setCursor(cursor);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "自定义光标异常");
			}
		} else if (curContentType.equals(ContentType.TEXT)) {
			setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		} else {
			setCursor(Cursor.getDefaultCursor());
		}

		AbstractContent content = ContentFactory.createContent(curContentType);
		System.out.println("create content: " + content);
		doDrawContent(content, curContentType);
	}

	public void createNewGraphics(BufferedImage image) {
		// 绘制图像
		ContentType curContentType = GlobalStateHandler.getCurContentType();
		if (curContentType.equals(ContentType.IMAGE)) {
			// 如果当前是图片的话
			ImageFactory factory = new ImageFactory(image, this);
			AbstractContent content = factory.createImage();
			doDrawContent(content, curContentType);
		}
	}

	public void undo() {
		// 撤销到上一步
		Originator originator = new Originator();
		originator.restoreMemento(careTaker.removeMemento(careTaker.getListSize() - 1));
		// 同时移除该类存内容的列表
		if (getContentSize() > 0) {
			contentList.remove(getContentSize() - 1);
		}
		repaint();
	}

	/**
	 * 清除所有内容
	 */
	public void clearAllContent() {
		this.contentList = new ArrayList<>();
		careTaker.removeAllMemento();
		repaint();
	}

	private void doDrawContent(AbstractContent content, ContentType curContentType) {
		if (content != null && isSaveType(curContentType)) {
			contentList.add(content);
			// 同时加入到备忘录中
			Originator originator = new Originator(content);
			careTaker.addMemento(originator.createMemento());
			content.setColor(GlobalStateHandler.getCurColor());
			content.setThickness(GlobalStateHandler.getThickness());
		}
	}

	private void draw(Graphics2D g2d, AbstractContent abstractShape) {
		// 将画笔传入到各个子类中，用来完成各自的绘图
		abstractShape.draw(g2d);
	}

	private void drawDashedBox(Graphics2D g2d) {
		// todo: 根据组合的起始和结束位置绘制一个虚线框
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{3.0f, 3.0f}, 3.0f));
		Color curColor = g2d.getColor();
		g2d.setColor(Color.BLACK);
		int width = Math.abs(compositeStartPoint.getX() - compositeEndPoint.getX());
		int height = Math.abs(compositeStartPoint.getY() - compositeEndPoint.getY());
		g2d.drawRect(compositeStartPoint.getX(), compositeStartPoint.getY(), width, height);

		// 恢复原本的颜色
		g2d.setColor(curColor);
	}

	private void initPopupMenu() {
		JMenuItem copyItem = new JMenuItem("复制");
		JMenuItem pasteItem = new JMenuItem("粘贴");
		JMenuItem compositeItem = new JMenuItem("组合");
		JMenuItem compositePasteItem = new JMenuItem("组合粘贴");
		copyItem.setBackground(StyleUtil.BACKGROUND_COLOR);
		pasteItem.setBackground(StyleUtil.BACKGROUND_COLOR);
		compositeItem.setBackground(StyleUtil.BACKGROUND_COLOR);
		compositePasteItem.setBackground(StyleUtil.BACKGROUND_COLOR);
		addCopyListener(copyItem);
		addPasteListener(pasteItem);
		addCompositeListener(compositeItem);
		addCompositePasteListener(compositePasteItem);
		copyMenu.add(copyItem);
		copyMenu.add(pasteItem);
		copyMenu.add(compositeItem);
		copyMenu.add(compositePasteItem);
	}

	private void addCopyListener(JMenuItem item) {
		// 点击的话就复制当前的图形
		item.addActionListener(e -> {
			Point clickPoint = new Point(copyPoint.getX(), copyPoint.getY());
			copyShape = getClickContent(clickPoint);
			isBeginComposite = false;
			repaint();
			System.out.println("copy shape! " + copyShape);
		});
	}

	private void addPasteListener(JMenuItem item) {
		item.addActionListener(e -> {
			// 绘制图形
			if (copyShape != null) {
				// 往列表中添加一个新的图形即可
				// 需要克隆
				AbstractShape shape = copyShape.clone();
				// 设置新的坐标
				shape.setStartPointAndEndPoint(copyPoint);
				contentList.add(shape);
				// 同时加入到备忘录中
				Originator originator = new Originator(shape);
				careTaker.addMemento(originator.createMemento());
				repaint();
			}
			isBeginComposite = false;
		});
	}

	private void addCompositeListener(JMenuItem item) {
		item.addActionListener(e -> {
			assert compositeStartPoint != null && compositeEndPoint != null;
			CompositeShape compositeShape = new CompositeShape();
			// 判断图形的 startPoint 和 endPoint 是否在里面
			for (AbstractContent content : contentList) {
				if (content instanceof AbstractShape) {
					// 如果是图形的话
					AbstractShape shape = (AbstractShape) content;
					if (isInnerCompositeSpace(shape)) {
						compositeShape.add(shape);
					}
				}
			}
			this.compositeShape = compositeShape;
			System.out.println("composite finish! composite shape size: " + compositeShape.getShapeListSize());
			isBeginComposite = false;
			repaint();
		});
	}

	private void addCompositePasteListener(JMenuItem item) {
		item.addActionListener(e -> {
			if (compositeShape != null) {
				CompositeShape cloneShape = compositeShape.cloneSelf();
				Point centerPoint = new Point((compositeStartPoint.getX() + compositeEndPoint.getX()) / 2, (compositeStartPoint.getY() + compositeEndPoint.getY()) / 2);
				int moveX = copyPoint.getX() - centerPoint.getX();
				int moveY = copyPoint.getY() - centerPoint.getY();
				// 设置复合图形的起始和结束点
				cloneShape.setStartPoint(new Point(compositeStartPoint.getX() + moveX, compositeStartPoint.getY() + moveY));
				cloneShape.setEndPoint(new Point(compositeEndPoint.getX() + moveX, compositeEndPoint.getY() + moveY));
				System.out.println(cloneShape.getStartPoint());
				System.out.println(cloneShape.getEndPoint());
				for (int i = 0; i < cloneShape.getShapeListSize(); i++) {
					AbstractShape shape = cloneShape.getChild(i);
					Point rawCenterPoint = shape.getCenterPoint();
					Point newCenterPoint = new Point(rawCenterPoint.getX() + moveX, rawCenterPoint.getY() + moveY);
					shape.setStartPointAndEndPoint(newCenterPoint);
				}

				// 加入
				contentList.add(cloneShape);
				Originator originator = new Originator(cloneShape);
				careTaker.addMemento(originator.createMemento());
				repaint();
			}
		});
	}

	private boolean isInnerCompositeSpace(AbstractShape shape) {
		return shape.isInner(compositeStartPoint, compositeEndPoint);
	}

	private boolean isSaveType(ContentType contentType) {
		return !contentType.equals(ContentType.CHOOSE);
	}

	private AbstractShape getClickContent(Point point) {
		// 点击点在图案范围内的作为选择的标准
		// fixme: 图案重叠如何选择?

		for (AbstractContent content : contentList) {
			if (content instanceof AbstractShape) {
				AbstractShape shape = (AbstractShape) content;
				if (shape.hasPoint(point)) {
					System.out.println("click shape!");
					return shape;
				}
			}
		}

		return null;
	}

	private static class InnerClass {
		private static final DrawPanel INSTANCE = new DrawPanel();
	}

	private class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (GlobalStateHandler.getCurContentType().equals(ContentType.CHOOSE)) {
				super.mouseClicked(e);
				int input = e.getButton();
				if (input == MouseEvent.BUTTON3) {
					// 如果按的是右键
					// 需要跳出一个选择菜单来复制
					// 获得当前选择的图标
					copyMenu.show(DrawPanel.this, e.getX(), e.getY());
					copyPoint = new Point(e.getX(), e.getY());
					System.out.println("copy point: " + copyPoint.getX() + ", " + copyPoint.getY());
				}
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!GlobalStateHandler.getCurContentType().equals(ContentType.CHOOSE)) {
				createNewGraphics();
				JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
				mouseStatusBar.setText("point: [" + e.getX() + ", " + e.getY() + "]");
				AbstractContent content = getCurContent();
				content.setStartPoint(new Point(e.getX(), e.getY()));
				content.setEndPoint(new Point(e.getX(), e.getY()));

				// 如果当前选择的图形是画笔或者橡皮檫，则进行下面的操作
				ContentType curContentType = GlobalStateHandler.getCurContentType();

				if (curContentType.equals(ContentType.ERASER) || curContentType.equals(ContentType.PENCIL) || curContentType.equals(ContentType.BRUSH)) {
					((AbstractPaintTool) content).setLength(0);
					content.setStartPoint(new Point(e.getX(), e.getY()));
					content.setEndPoint(new Point(e.getX(), e.getY()));
					((AbstractPaintTool) content).addLength();
					createNewGraphics();
				}
				repaint();
			} else {
				int input = e.getButton();
				if (input == MouseEvent.BUTTON1) {
					// 如果是选择的模式下
					if (!isBeginComposite) {
						isBeginComposite = true;
					}

					compositeStartPoint = new Point(e.getX(), e.getY());
					compositeEndPoint = new Point(e.getX(), e.getY());
					repaint();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!GlobalStateHandler.getCurContentType().equals(ContentType.CHOOSE)) {
				JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
				mouseStatusBar.setText("point: [" + e.getX() + ", " + e.getY() + "]");
				ContentType curContentType = GlobalStateHandler.getCurContentType();
				AbstractContent content = getCurContent();
				if (curContentType.equals(ContentType.ERASER) || curContentType.equals(ContentType.PENCIL) || curContentType.equals(ContentType.BRUSH)) {
					content.setStartPoint(new Point(e.getX(), e.getY()));
					((AbstractPaintTool) content).addLength();
					createNewGraphics();
				}

				content.setEndPoint(new Point(e.getX(), e.getY()));
				repaint();
			} else {
				int input = e.getButton();
				if (input == MouseEvent.BUTTON1) {
					compositeEndPoint = new Point(e.getX(), e.getY());
					repaint();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
			mouseStatusBar.setText("point: [" + e.getX() + ", " + e.getY() + "]");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
			mouseStatusBar.setText("point：");
		}
	}

	private class MouseMotionListener extends MouseAdapter {

		/**
		 * 该事件是用于 铅笔 笔刷 橡皮擦使用
		 *
		 * @param e Mouse Event
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!GlobalStateHandler.getCurContentType().equals(ContentType.CHOOSE)) {
				JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
				mouseStatusBar.setText("point :[" + e.getX() + ", " + e.getY() + "]");
				AbstractContent content = getCurContent();
				AbstractContent lastContent = getContent(getContentSize() - 1);
				ContentType curContentType = GlobalStateHandler.getCurContentType();

				if (curContentType.equals(ContentType.ERASER) || curContentType.equals(ContentType.PENCIL) || curContentType.equals(ContentType.BRUSH)) {
					lastContent.setStartPoint(new Point(e.getX(), e.getY()));
					content.setStartPoint(new Point(e.getX(), e.getY()));
					content.setEndPoint(new Point(e.getX(), e.getY()));
					((AbstractPaintTool) content).addLength();
					createNewGraphics();
				} else {
					// 除了上述的三种像素级的，其他只需要更新当前左边即可
					content.setEndPoint(new Point(e.getX(), e.getY()));
				}
				repaint();
			} else {
				compositeEndPoint = new Point(e.getX(), e.getY());
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			JLabel mouseStatusBar = MainFrame.getInstance().getMouseStatusBar();
			mouseStatusBar.setText("point :[" + e.getX() + ", " + e.getY() + "]");
		}
	}
}
