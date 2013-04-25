package redboxextractor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JImage extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2273695351264187969L;
	BufferedImage image;
	public static final int WIDTH = 315;
	public static final int HEIGHT = 446;
	int imageWidth;
	int imageHeight;

	public JImage() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		imageWidth = WIDTH;
		imageHeight = HEIGHT;
	}
	public JImage(BufferedImage bi) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		image = bi;
		double ratio;
		if (bi.getHeight() > bi.getWidth()) {
			ratio = ((double) HEIGHT) / ((double) bi.getHeight());
		} else {
			ratio = ((double) WIDTH) / ((double) bi.getWidth());
		}
		imageWidth = (int) Math.round(ratio * bi.getWidth());
		imageHeight = (int) Math.round(ratio * bi.getHeight());
	}
	public JImage(BufferedImage bi, int width, int height) {
		image = bi;
		setPreferredSize(new Dimension(width, height));
		imageWidth = width;
		imageHeight = height;
	}
	
	public void setImage(String path) {
		try {
			image = ImageIO.read(new File(path));
			repaint();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void setImage(File file) {
		try {
			image = ImageIO.read(file);
			repaint();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void setImage(BufferedImage inputImage) {
		image = inputImage;
		repaint();
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
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
}
