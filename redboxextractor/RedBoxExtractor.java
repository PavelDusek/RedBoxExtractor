package redboxextractor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RedBoxExtractor extends JFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 932004580719449638L;
	JFileChooser fileChooser;
	JFileChooser directoryChooser;
	JList<String> fileNameList;
	DefaultListModel<String> fileNameListModel;
	List<File> fileList = new Vector<File>();
	JTextField outputDirectory;
	JScrollableImage oldImagePanel;
	JScrollableImage newImagePanel;
	JSpinner redThresholdSpinner;
	JRadioButton automaticRadio;
	JSpinner answerCountSpinner;
	JRadioButton manualRadio;
	JSpinner lineWidthSpinner;
	JTextField outputPrefix;
	BufferedImage outputImage;
	JFrame canvasFrame;
	JCanvas canvas;


	/**
	 * GUI constructor
	 */
	public RedBoxExtractor() {
		super("RedBoxExtractor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GroupLayout layout = new GroupLayout(getContentPane());
		setLayout(layout);
		
		//left panel
		JPanel leftPanel = new JPanel();
		GroupLayout leftLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftLayout);
		
		JPanel addRemoveFilesPanel = new JPanel(new FlowLayout());
		JButton addFilesButton = new JButton("Přidat soubory");
		addFilesButton.setActionCommand("addFiles");
		addFilesButton.addActionListener(this);
		addRemoveFilesPanel.add(addFilesButton);
		JButton removeFilesButton = new JButton("Odebrat soubory");
		removeFilesButton.setActionCommand("removeFiles");
		removeFilesButton.addActionListener(this);
		addRemoveFilesPanel.add(removeFilesButton);
		leftPanel.add(addRemoveFilesPanel);
		
		fileNameListModel = new DefaultListModel<String>();
		fileNameList = new JList<String>(fileNameListModel);
		ListSelectionModel lsm = fileNameList.getSelectionModel();
		lsm.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(fileNameList);
		leftPanel.add(listScroller);
		
		JLabel outputDirectoryLabel = new JLabel("Výstupní adresář");
		leftPanel.add(outputDirectoryLabel);
		
		JPanel outputDirectoryPanel = new JPanel(new FlowLayout());
		outputDirectory = new JTextField(25);
		outputDirectoryPanel.add(outputDirectory);
		JButton setOutputDirectoryButton = new JButton("Procházet…");
		setOutputDirectoryButton.setActionCommand("setOutputDirectory");
		setOutputDirectoryButton.addActionListener(this);
		outputDirectoryPanel.add(setOutputDirectoryButton);
		leftPanel.add(outputDirectoryPanel);
		
		JPanel prefixPanel = new JPanel(new FlowLayout());
		JLabel prefixLabel = new JLabel("Prefix výstupu:");
		outputPrefix = new JTextField(20);
		outputPrefix.setText("odpovedni_arch_");
		prefixPanel.add(prefixLabel);
		prefixPanel.add(outputPrefix);
		leftPanel.add(prefixPanel);
		
		leftLayout.setAutoCreateContainerGaps(true);
		leftLayout.setAutoCreateGaps(true);
		leftLayout.setVerticalGroup(
				leftLayout.createSequentialGroup()
				.addComponent(addRemoveFilesPanel)
				.addComponent(listScroller)
				.addComponent(outputDirectoryLabel)
				.addComponent(outputDirectoryPanel)
				.addComponent(prefixPanel)
		);
		leftLayout.setHorizontalGroup(
			leftLayout.createSequentialGroup()
			.addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(addRemoveFilesPanel)
					.addComponent(listScroller)
					.addComponent(outputDirectoryLabel)
					.addComponent(outputDirectoryPanel)
					.addComponent(prefixPanel)
			)
		);
		add(leftPanel);
		
		//right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		JPanel imagePanel = new JPanel(new GridLayout(1,2));
		oldImagePanel = new JScrollableImage();
		imagePanel.add(oldImagePanel);
		newImagePanel = new JScrollableImage();
		imagePanel.add(newImagePanel);
		rightPanel.add(imagePanel);

		JPanel settingsPanel = new JPanel();
		GroupLayout settingsLayout = new GroupLayout(settingsPanel);
		settingsPanel.setLayout(settingsLayout);
		
		JLabel redThresholdLabel = new JLabel("Hranice sytosti červené:");
		SpinnerModel spinnerModel = new SpinnerNumberModel(-30, -512, 256, 1);
		redThresholdSpinner = new JSpinner(spinnerModel);
		JPanel redThresholdPanel = new JPanel();
		redThresholdPanel.add(redThresholdLabel);
		redThresholdPanel.add(redThresholdSpinner);
		settingsPanel.add(redThresholdPanel);
		
		automaticRadio = new JRadioButton("Najít oblasti automaticky");
		automaticRadio.setSelected(true);
		settingsPanel.add(automaticRadio);
		JLabel answerCountLabel = new JLabel("Počet odpovětí:");
		SpinnerModel answerCountSpinnerModel = new SpinnerNumberModel(5, 0, 50, 1);
		answerCountSpinner = new JSpinner(answerCountSpinnerModel);
		JLabel lineWidthLabel = new JLabel("Přibližná šířka čáry (px):");
		JPanel answerCountPanel = new JPanel();
		answerCountPanel.add(answerCountLabel);
		answerCountPanel.add(answerCountSpinner);
		settingsPanel.add(answerCountPanel);
		
		SpinnerModel lineWidthSpinnerModel = new SpinnerNumberModel(20,1,30,1);
		lineWidthSpinner = new JSpinner(lineWidthSpinnerModel);
		JPanel lineWidthPanel = new JPanel();
		lineWidthPanel.add(lineWidthLabel);
		lineWidthPanel.add(lineWidthSpinner);
		settingsPanel.add(lineWidthPanel);

		manualRadio = new JRadioButton("Zpracovávat oblasti dle ručního nastavení");
		rightPanel.add(manualRadio);
		JButton manualButton = new JButton("Nastavit oblasti ručně");
		manualButton.addActionListener(this);
		manualButton.setActionCommand("setManual");
		settingsPanel.add(manualButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(automaticRadio);
		group.add(manualRadio);
		
		//JLabel redThresholdLabel2 = new JLabel("(doporučené hodnoty mezi cca mezi -50 a 0)");
		//centralPanel.add(redThresholdLabel2);
		
		JButton runButton = new JButton("Spustit");
		runButton.setActionCommand("run");
		runButton.addActionListener(this);
		settingsPanel.add(runButton);
		
		JButton saveButton = new JButton("Uložit");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		settingsPanel.add(saveButton);
		
		JButton runAllButton = new JButton("Spustit a uložit všechny");
		runAllButton.setActionCommand("saveAll");
		runAllButton.addActionListener(this);
		settingsPanel.add(runAllButton);
		
		settingsLayout.setAutoCreateContainerGaps(true);
		settingsLayout.setAutoCreateGaps(true);
		settingsLayout.setHorizontalGroup(
				settingsLayout.createSequentialGroup()
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(automaticRadio)
						.addComponent(manualRadio)
						.addComponent(runButton)
				)
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(answerCountPanel)
						.addComponent(manualButton)
						.addComponent(lineWidthPanel)
						.addComponent(saveButton)
				)
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(redThresholdPanel)
						.addComponent(runAllButton)
				)
		);
		settingsLayout.setVerticalGroup(
				settingsLayout.createSequentialGroup()
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(automaticRadio)
						.addComponent(answerCountPanel)
						.addComponent(redThresholdPanel)
				)
				.addComponent(lineWidthPanel)
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(manualRadio)
						.addComponent(manualButton)
				)
				.addGroup(settingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(runButton)
						.addComponent(saveButton)
						.addComponent(runAllButton)
				)
		);

		rightPanel.add(settingsPanel);
		add(rightPanel);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addComponent(leftPanel)
				.addComponent(rightPanel)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(leftPanel)
						.addComponent(rightPanel)
				)
		);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setMultiSelectionEnabled(true);
		directoryChooser = new JFileChooser();
		directoryChooser.setCurrentDirectory(new File("."));
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		directoryChooser.setAcceptAllFileFilterUsed(false);
	}
	
	private JFrame getCanvasFrame(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.setSize(600,600);
		canvas = new JCanvas(image);
		canvas.setDoAddPoint(true);
		JScrollPane scrollPane = new JScrollPane(canvas);
		frame.add(scrollPane);
		return frame;
	}

	/**
	 * This methods fires function to process the image according to user-selected options
	 * @param file
	 */
	private void process(File file) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			//TODO manual vs. automatic
			//BufferedImage newImage = processImage(file, (int) redThresholdSpinner.getValue());
			outputImage = selectAreasAccordingToAnswerCount(file, (int) answerCountSpinner.getValue(), (int) lineWidthSpinner.getValue(), (int) redThresholdSpinner.getValue()); 
			newImagePanel.setImage(outputImage);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	/**
	 * This method fires a directory chooser to enable the user select output directory for the processed images
	 */
	private void setOutputDirectory() {
		int value = directoryChooser.showOpenDialog(this);
		if (value == JFileChooser.APPROVE_OPTION) {
			File file = directoryChooser.getSelectedFile();
			String path = file.getPath();
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
			outputDirectory.setText(path);
		}
	}
	
	/**
	 * This method checks if the image has been processed (if not, fires the process method),
	 * if so, saves it to a file according to output directory and file prefix mask.
	 */
	private void save() {
		int index = fileNameList.getSelectedIndex();
		File file = fileList.get(index);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			if (outputImage == null) {
				process(file);
			}
			String path = outputDirectory.getText();
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
			path += outputPrefix.getText() + file.getName();
			File outputFile = new File(path); 
			saveImage(outputImage, outputFile);
			outputImage = null;
		} catch (IOException excp) {
			JOptionPane.showMessageDialog(this, excp.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	/**
	 * This method remove selected file from the list of project files
	 */
	private void removeFiles() {
		int index = fileNameList.getSelectedIndex();
		fileNameList.clearSelection();
		fileList.remove(index);
		fileNameListModel.remove(index);
		if (index != 0) {
			fileNameList.setSelectedIndex(index - 1);
		} else {
			fileNameList.setSelectedIndex(0);
		}
		fileNameList.repaint();
	}
	
	/**
	 * This method starts a fileChooser to add new files to the list and adds them.
	 */
	private void addFiles() {
		int value = fileChooser.showOpenDialog(this);
		if (value == JFileChooser.APPROVE_OPTION) {
			for (File file: fileChooser.getSelectedFiles()) {
				fileList.add(file);
				fileNameListModel.addElement(file.getName());
			}
			fileNameList.setSelectedIndex(0); //select the first item
			String path = fileList.get(0).getParentFile().getPath();
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
			outputDirectory.setText(path);
		}		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("addFiles")) {
			addFiles();
		}
		if (ae.getActionCommand().equals("removeFiles")) {
			removeFiles();
		}
		if (ae.getActionCommand().equals("run")) {
			int index = fileNameList.getSelectedIndex();
			File file = fileList.get(index);
			process(file);
		}
		if (ae.getActionCommand().equals("setManual")) {
			manualRadio.setSelected(true);
			BufferedImage oldImage = getOldImage();
			canvasFrame = getCanvasFrame(oldImage);
			canvasFrame.setVisible(true);
		}
		if (ae.getActionCommand().equals("setOutputDirectory")) {
			setOutputDirectory();
		}
		if (ae.getActionCommand().equals("save")) {
			save();
		}
		if (ae.getActionCommand().equals("saveAll")) {
			for (int i = 0; i < fileList.size(); i++) {
				fileNameList.setSelectedIndex(i);
				save();
			}
		}
	}

	/**
	 * This method save image "image" to a file "file"
	 * @param image
	 * @param file
	 * @throws IOException
	 */
	private void saveImage(BufferedImage image, File file) throws IOException {
		String name = file.getName();
		//".png", ".jpg" etc.
		String format = name.substring(name.lastIndexOf(".") + 1);
		OutputStream out = new FileOutputStream(file, false);		
		ImageIO.write(image, format, out);
		out.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			
		} else {
			RedBoxExtractor rbe = new RedBoxExtractor();
			//rbe.setSize(1366, 768);
			rbe.setSize(1299, 707);
			rbe.setVisible(true);
		}
	}
	
	/**
	 * This method selects the areas of image that have borders with highest amount of read
	 * @param file file with the source image
	 * @param answerCount number of ares
	 * @param minLineWidth the width of the border
	 * @return new image made of the areas
	 * @throws IOException
	 */
	private BufferedImage selectAreasAccordingToAnswerCount(File file, int answerCount, int minLineWidth, int redDominanceThreshold) throws IOException {
		BufferedImage image = ImageIO.read(file);
		List<Integer> listOfRedLines = findHorizontalLines(image, redDominanceThreshold);
		int linesCount = 2 * answerCount;
		List<Integer> uniqueLines = new ArrayList<Integer>();
		int i = 0;
		int lineIndex;
		boolean lineIsUnique = true;
		int addedUniqueLine;

		//find the most red lines that are unique (not too close from each other)
		while (uniqueLines.size() < linesCount && i < listOfRedLines.size()) {
			lineIndex = listOfRedLines.get(i);
			lineIsUnique = true;
			//Check if line is unique:
			for (Iterator<Integer> it = uniqueLines.iterator(); it.hasNext(); ) {
				addedUniqueLine = it.next();
				if (Math.abs(lineIndex - addedUniqueLine) < minLineWidth) {
					lineIsUnique = false;
					break;
				}
			}
			if (lineIsUnique) {
				uniqueLines.add(lineIndex);
			}
			i++;
		}
		Collections.sort(uniqueLines);
		int height = 0;
		for (i = 0; i < uniqueLines.size(); i += 2) {
			height += uniqueLines.get(i+1) - uniqueLines.get(i);
		}
		int width = image.getWidth();
		//Create the image according to horizontal lines:
		//BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] imagePortion;
		int startIndex, endIndex;
		int heightOffset = 0;
		for (i = 0; i < uniqueLines.size(); i += 2) {
			startIndex = uniqueLines.get(i);
			endIndex = uniqueLines.get(i+1);
			imagePortion = image.getRGB(0, startIndex, width, endIndex - startIndex, null, 0, width);
			newImage.setRGB(0, heightOffset, width, endIndex - startIndex, imagePortion, 0, width);
			heightOffset += endIndex - startIndex;
		}
		return newImage;
	}
	
	/**
	 * For a image in the file file, this method finds out in which horizontal lines there is
	 * the highest amount of red color.
	 * @param file
	 * @return List of the line numbers ordered according to how many red pixels are there (from highest to lowest) 
	 * @throws IOException
	 */
	private List<Integer> findHorizontalLines(BufferedImage image, int redDominanceThreshold) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		int rgb, redDominance;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		boolean hasalpha = hasAlpha(image);
		List<Integer> redDominancesForALine = new ArrayList<Integer>();
		List<Integer> lineNumbers = new ArrayList<Integer>();
		int index;
		int lineRedness;

		for (int h = 0; h < height; h++) {
			lineRedness = 0;
			for (int w = 0; w < width; w++) {
				rgb = image.getRGB(w, h);
				Color color = new Color(rgb, hasalpha);
				//Red color has to be more intensive than both blue and green together
				redDominance = color.getRed() - color.getBlue() - color.getGreen();
				if (redDominance > redDominanceThreshold) {
					lineRedness++;
				}
			}
			index = getPositionInOrderedList(lineRedness, redDominancesForALine);
			redDominancesForALine.add(index, lineRedness);
			lineNumbers.add(index, h);
		}
		return lineNumbers;
	}
	
	/**
	 * For a List<Integer> list, that is ordered from the biggest to the lowest,
	 * the method finds a position where to put the number, so that the ordered holds.
	 * @param number
	 * @param list
	 * @return position/index where to put the number
	 */
	private int getPositionInOrderedList(int number, List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			if (number >= list.get(i)) {
				return i;
			}
		}
		return list.size();
	}
	
	/**
	 * For the image finds whether it has alpha channel.
	 * @param image
	 * @return true/false according to if the image has alpha channel
	 */
	private boolean hasAlpha(BufferedImage image) {
		switch (image.getType()) {
		case BufferedImage.TYPE_4BYTE_ABGR:
			return true;
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
			return true;
		case BufferedImage.TYPE_INT_ARGB:
			return true;
		case BufferedImage.TYPE_INT_ARGB_PRE:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * This method reads an image from a file according to which is selected in the fileName list
	 * @return
	 */
	private BufferedImage getOldImage() {
		BufferedImage image = null;
		int index = fileNameList.getSelectedIndex();
		try {
			image = ImageIO.read(fileList.get(index));
		} catch (IOException excp) {
			JOptionPane.showMessageDialog(this, excp.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		if (!fileNameList.isSelectionEmpty()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			BufferedImage oldImage = getOldImage();
			oldImagePanel.setImage(oldImage);
			setCursor(Cursor.getDefaultCursor());
		}
	}
}
