package redboxextractor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JScrollableImage extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2923992276504621699L;
	BufferedImage image;
	JImage imagePanel;
	JScrollPane scrollPane;
	JButton plusButton;
	JButton minusButton;
	JButton fillButton;
	JButton normalButton;
	
	public static final int WIDTH = 315;
	public static final int HEIGHT = 446;
	
	public JScrollableImage() {
		imagePanel = new JImage();
		scrollPane = new JScrollPane(imagePanel);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(scrollPane);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
		plusButton = new JButton("+");
		minusButton = new JButton("-");
		fillButton = new JButton("<-->");
		normalButton = new JButton("1:1");
		plusButton.setActionCommand("plus");
		minusButton.setActionCommand("minus");
		fillButton.setActionCommand("fill");
		normalButton.setActionCommand("normal");
		plusButton.addActionListener(this);
		minusButton.addActionListener(this);
		fillButton.addActionListener(this);
		normalButton.addActionListener(this);
		buttonPanel.add(plusButton);
		buttonPanel.add(minusButton);
		buttonPanel.add(fillButton);
		buttonPanel.add(normalButton);
		add(buttonPanel);
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addComponent(buttonPanel)
		);
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane)
						.addComponent(buttonPanel)
				)
		);
	}

	public void setImage(BufferedImage bi) {
		image = bi;
		imagePanel = new JImage(bi);
		scrollPane.setViewportView(imagePanel);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("plus")) {
			int w = (int) Math.round(1.5 * imagePanel.getImageWidth());
			int h = (int) Math.round(1.5 * imagePanel.getImageHeight());
			imagePanel = new JImage(image, w, h);
		}
		if (ae.getActionCommand().equals("minus")) {
			int w = (int) Math.round(imagePanel.getImageWidth()/1.5);
			int h = (int) Math.round(imagePanel.getImageHeight()/1.5);
			imagePanel = new JImage(image, w, h);			
		}
		if (ae.getActionCommand().equals("fill")) {
			imagePanel = new JImage(image);
		}
		if (ae.getActionCommand().equals("normal")) {
			imagePanel = new JImage(image, image.getWidth(), image.getHeight());
		}
		scrollPane.setViewportView(imagePanel);
	}

}
