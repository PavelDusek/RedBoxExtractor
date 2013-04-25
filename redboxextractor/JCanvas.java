package redboxextractor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JCanvas extends JImage {

	//TODO dragging
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389213373495196685L;
	private boolean doAddPoint = false;
	private BufferedImage image;
	List<Rectangle> rectangles = new ArrayList<Rectangle>();
	Point lastPoint;
	
	public JCanvas() {
		super();
	}

	public JCanvas(BufferedImage bi) {
		super(bi, bi.getWidth(), bi.getHeight());
		image = bi;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouse(e.getX(), e.getY());				
			}
		});
	}

	public JCanvas(BufferedImage bi, int width, int height) {
		super(bi, width, height);
		image = bi;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouse(e.getX(), e.getY());				
			}
		});
	}

	public void setDoAddPoint(boolean b) {
		doAddPoint = b;
	}
	public boolean doAddPoint() {
		return doAddPoint;
	}
	private void mouse(int x, int y) {
		if (doAddPoint) {
			if (lastPoint == null) {
				lastPoint.x = x;
				lastPoint.y = y;
			} else {
				rectangles.add(new Rectangle(lastPoint.x, lastPoint.y, x, y));
				lastPoint = null;
			}
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
			//g.drawImage(image, 0, 0, null);
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		Color color = g.getColor();
		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext(); ) {
			Rectangle rectangle = it.next();
			g.setColor(Color.cyan);
			g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		}
		if (lastPoint != null) {
			g.drawRect(lastPoint.x, lastPoint.y, 1, 1);
		}
		g.setColor(color);
	}
	
}

class Rectangle {
	int x, y, width, height;
	Rectangle(int x1, int y1, int x2, int y2) {
		x = x1;
		y = y1;
		width = x2 - x1;
		height = y2 -y1;
	}
}
class Point {
	public int x, y;
}
