/*
 * Created on 30.05.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package additionalPrograms.PragWo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import model.Model;
import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Chapter;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.pathselector.PathNode;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.pathselector.PathSelector;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse implementiert die Main-Funktion, erstellt das
 *         Hauptfenster und implementiert Funktionen zur Kommunikation mit der
 *         Datenbank.
 */
public class PraWo implements WindowListener {
	private DBC dbc;

	// das Hauptfenster
	private JFrame frame;

	// das Textfeld
	private JTextPane textPane;

	// sämtliche Objekte des Hauptfensters
	private Dialog dialog;

	private JMenu show;

	private JMenu run;

	private JPanel dialogPanel;

	private JToggleButton a1Button;

	private JToggleButton a2Button;

	private JToggleButton a3Button;

	private JToggleButton e1Button;

	private JToggleButton e2Button;

	private JToggleButton e3Button;

	private JToggleButton p1Button;

	private JToggleButton p2Button;

	private JToggleButton p3Button;

	private JToggleButton otherButton;

	private JMenuItem dbentries;

	private JMenu results;

	// der Status der Knöpfe in der Werkzeugleiste
	private boolean selectedE1 = true;

	private boolean selectedE2 = true;

	private boolean selectedE3 = true;

	private boolean selectedP1 = true;

	private boolean selectedP2 = true;

	private boolean selectedP3 = true;

	private boolean selectedA1 = true;

	private boolean selectedA2 = true;

	private boolean selectedA3 = true;

	private boolean selectedOther = true;

	// die benötigten Instanzenzeiger
	private MenuListener menuListener = null;

	private Analyser analyser;

	private Wordlists wordlists;

	private SelectionListener selectionListener;

	// Speicherung des Wurzelknotens vom Pfadbaum
	private PathNode root;

	// gibt es ungespeicherte Analyseergebnisse
	private boolean unsaved = false;

	// das gerade bearbeitet Kapitel
	private Chapter chapter = null;

	// die benötigten Farben für die Markierungen
	private final Color e1Color = Color.MAGENTA;

	private final Color e2Color = Color.GREEN;

	private final Color e3Color = Color.YELLOW;

	private final Color p1Color = Color.PINK;

	private final Color p2Color = Color.ORANGE;

	private final Color p3Color = Color.CYAN;

	private final Color a1Color = Color.RED;

	private final Color a2Color = Color.BLUE;

	private final Color a3Color = Color.GRAY;

	private final Color otherColor = Color.LIGHT_GRAY;

	private final Color functionColor = Color.BLACK;

	private final Color selectionColor = new Color(204, 204, 255);

	private Model model;

	/**
	 * Der Konstruktor dieser Klasse erstellt das Hauptfenster des Programms.
	 * @param model 
	 */
	public PraWo(Model model) {
		this.model = model;
		dbc = Model.getDBC();

		initClasses();

		// 1. Optional: Specify who draws the window decorations.
		JFrame.setDefaultLookAndFeelDecorated(false);

		// 2. Create the frame.
		frame = new JFrame("Abstraction analyser");

		// 3. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(this);

		// Create the Menu

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);

		JMenuItem open = new JMenuItem("Open");
		open.setMnemonic(KeyEvent.VK_O);
		open.addActionListener(menuListener);
		file.add(open);

		JMenuItem save = new JMenuItem("Save");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(menuListener);
		file.add(save);

		dbentries = new JMenuItem("Load DB entries");
		dbentries.setMnemonic(KeyEvent.VK_L);
		dbentries.addActionListener(menuListener);
		dbentries.setEnabled(false);
		file.add(dbentries);

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(menuListener);
		file.add(exit);

		show = new JMenu("Show");
		show.setMnemonic(KeyEvent.VK_S);
		show.setEnabled(false);
		menuBar.add(show);

		JMenuItem allCWs = new JMenuItem("...all constitutive words");
		allCWs.setMnemonic(KeyEvent.VK_C);
		allCWs.addActionListener(menuListener);
		show.add(allCWs);

		JMenuItem allLevels = new JMenuItem("...all levels");
		allLevels.setMnemonic(KeyEvent.VK_L);
		allLevels.addActionListener(menuListener);
		show.add(allLevels);

		JMenuItem standard = new JMenuItem("...standard");
		standard.setMnemonic(KeyEvent.VK_S);
		standard.addActionListener(menuListener);
		show.add(standard);

		run = new JMenu("Analyse");
		run.setMnemonic(KeyEvent.VK_A);
		run.setEnabled(false);
		menuBar.add(run);

		ButtonGroup analyse_group = new ButtonGroup();

		JRadioButtonMenuItem runningAll = new JRadioButtonMenuItem(
				"...all words");
		runningAll.setMnemonic(KeyEvent.VK_A);
		runningAll.addActionListener(menuListener);
		analyse_group.add(runningAll);
		run.add(runningAll);

		JRadioButtonMenuItem runningCW = new JRadioButtonMenuItem(
				"...constitutive words");
		runningCW.setMnemonic(KeyEvent.VK_C);
		runningCW.addActionListener(menuListener);
		analyse_group.add(runningCW);
		run.add(runningCW);

		JRadioButtonMenuItem runningSelected = new JRadioButtonMenuItem(
				"...selected words");
		runningSelected.setMnemonic(KeyEvent.VK_S);
		runningSelected.addActionListener(menuListener);
		analyse_group.add(runningSelected);
		run.add(runningSelected);

		results = new JMenu("Results");
		results.setMnemonic(KeyEvent.VK_R);
		results.setEnabled(false);
		menuBar.add(results);

		JMenu diagram = new JMenu("View results as diagram");
		diagram.setMnemonic(KeyEvent.VK_D);
		results.add(diagram);

		JMenuItem piechart = new JMenuItem("Pie Chart");
		piechart.setMnemonic(KeyEvent.VK_P);
		piechart.addActionListener(menuListener);
		diagram.add(piechart);

		JMenuItem barchart = new JMenuItem("Bar chart");
		barchart.setMnemonic(KeyEvent.VK_B);
		barchart.addActionListener(menuListener);
		diagram.add(barchart);

		JMenuItem density = new JMenuItem("Density chart");
		density.setMnemonic(KeyEvent.VK_D);
		density.addActionListener(menuListener);
		diagram.add(density);

		JMenu lists = new JMenu("View results as list");
		lists.setMnemonic(KeyEvent.VK_L);
		results.add(lists);

		JMenu nounResults = new JMenu("nouns");
		nounResults.setMnemonic(KeyEvent.VK_N);
		lists.add(nounResults);

		JMenuItem e1Results = new JMenuItem("E1");
		e1Results.setActionCommand("noun1");
		e1Results.setMnemonic(KeyEvent.VK_1);
		e1Results.addActionListener(menuListener);
		nounResults.add(e1Results);

		JMenuItem e2Results = new JMenuItem("E2");
		e2Results.setActionCommand("noun2");
		e2Results.setMnemonic(KeyEvent.VK_2);
		e2Results.addActionListener(menuListener);
		nounResults.add(e2Results);

		JMenuItem e3Results = new JMenuItem("E3");
		e3Results.setActionCommand("noun3");
		e3Results.setMnemonic(KeyEvent.VK_3);
		e3Results.addActionListener(menuListener);
		nounResults.add(e3Results);

		JMenuItem eAllResults = new JMenuItem("all");
		eAllResults.setActionCommand("all_nouns");
		eAllResults.setMnemonic(KeyEvent.VK_A);
		eAllResults.addActionListener(menuListener);
		nounResults.add(eAllResults);

		JMenu verbResults = new JMenu("verbs");
		verbResults.setMnemonic(KeyEvent.VK_V);
		lists.add(verbResults);

		JMenuItem p1Results = new JMenuItem("P1");
		p1Results.setActionCommand("verb1");
		p1Results.setMnemonic(KeyEvent.VK_1);
		p1Results.addActionListener(menuListener);
		verbResults.add(p1Results);

		JMenuItem p2Results = new JMenuItem("P2");
		p2Results.setActionCommand("verb2");
		p2Results.setMnemonic(KeyEvent.VK_2);
		p2Results.addActionListener(menuListener);
		verbResults.add(p2Results);

		JMenuItem p3Results = new JMenuItem("P3");
		p3Results.setActionCommand("verb3");
		p3Results.setMnemonic(KeyEvent.VK_3);
		p3Results.addActionListener(menuListener);
		verbResults.add(p3Results);

		JMenuItem pAllResults = new JMenuItem("all");
		pAllResults.setActionCommand("all_verbs");
		pAllResults.setMnemonic(KeyEvent.VK_A);
		pAllResults.addActionListener(menuListener);
		verbResults.add(pAllResults);

		JMenu adjectiveResults = new JMenu("adjectives");
		adjectiveResults.setMnemonic(KeyEvent.VK_A);
		lists.add(adjectiveResults);

		JMenuItem a1Results = new JMenuItem("A1");
		a1Results.setActionCommand("adjective1");
		a1Results.setMnemonic(KeyEvent.VK_1);
		a1Results.addActionListener(menuListener);
		adjectiveResults.add(a1Results);

		JMenuItem a2Results = new JMenuItem("A2");
		a2Results.setActionCommand("adjective2");
		a2Results.setMnemonic(KeyEvent.VK_2);
		a2Results.addActionListener(menuListener);
		adjectiveResults.add(a2Results);

		JMenuItem a3Results = new JMenuItem("A3");
		a3Results.setActionCommand("adjective3");
		a3Results.setMnemonic(KeyEvent.VK_3);
		a3Results.addActionListener(menuListener);
		adjectiveResults.add(a3Results);

		JMenuItem aAllResults = new JMenuItem("all");
		aAllResults.setActionCommand("all_adjectives");
		aAllResults.setMnemonic(KeyEvent.VK_A);
		aAllResults.addActionListener(menuListener);
		adjectiveResults.add(aAllResults);

		JMenuItem allResults = new JMenuItem("all");
		allResults.setActionCommand("all_words");
		allResults.setMnemonic(KeyEvent.VK_L);
		allResults.addActionListener(menuListener);
		lists.add(allResults);

		// Create the Content
		JPanel p = new JPanel(new BorderLayout());
		p.setSize(200, 200);

		// Create the ToolBar

		JToolBar toolBar = new JToolBar();

		e1Button = new JToggleButton("E1", selectedE1);
		e1Button.setForeground(e1Color);
		e1Button.addActionListener(menuListener);
		toolBar.add(e1Button);

		e2Button = new JToggleButton("E2", selectedE2);
		e2Button.setForeground(e2Color);
		e2Button.addActionListener(menuListener);
		toolBar.add(e2Button);

		e3Button = new JToggleButton("E3", selectedE3);
		e3Button.setForeground(e3Color);
		e3Button.addActionListener(menuListener);
		toolBar.add(e3Button);

		p1Button = new JToggleButton("P1", selectedP1);
		p1Button.setForeground(p1Color);
		p1Button.addActionListener(menuListener);
		toolBar.add(p1Button);

		p2Button = new JToggleButton("P2", selectedP2);
		p2Button.setForeground(p2Color);
		p2Button.addActionListener(menuListener);
		toolBar.add(p2Button);

		p3Button = new JToggleButton("P3", selectedP3);
		p3Button.setForeground(p3Color);
		p3Button.addActionListener(menuListener);
		toolBar.add(p3Button);

		a1Button = new JToggleButton("A1", selectedA1);
		a1Button.setForeground(a1Color);
		a1Button.addActionListener(menuListener);
		toolBar.add(a1Button);

		a2Button = new JToggleButton("A2", selectedA2);
		a2Button.setForeground(a2Color);
		a2Button.addActionListener(menuListener);
		toolBar.add(a2Button);

		a3Button = new JToggleButton("A3", selectedA3);
		a3Button.setForeground(a3Color);
		a3Button.addActionListener(menuListener);
		toolBar.add(a3Button);

		otherButton = new JToggleButton("other", selectedOther);
		otherButton.setForeground(otherColor);
		otherButton.addActionListener(menuListener);
		toolBar.add(otherButton);

		p.add(toolBar, BorderLayout.NORTH);

		// Create the Editor
		textPane = new JTextPane();
		textPane.setPreferredSize(new Dimension(500, 500));
		textPane.setEditable(false);

		textPane.addCaretListener(selectionListener);

		JScrollPane scrollPane = new JScrollPane(textPane);
		StyledDocument doc = textPane.getStyledDocument();
		addStylesToDocument(doc);
		p.add(scrollPane, BorderLayout.CENTER);

		dialog = new Dialog(this, analyser);
		dialogPanel = dialog.runDialog();
		JScrollPane scrollDialog = new JScrollPane(dialogPanel);

		p.setMinimumSize(new Dimension(500, 500));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p,
				scrollDialog);

		// 4. Create components and put them in the frame.
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		// 5. Size the frame.
		frame.pack();

		// 6. Show it.
		frame.setVisible(true);
	}

	/**
	 * Diese Funktion instanziiert alle benötigten Klassen.
	 */
	private void initClasses() {
		analyser = new Analyser(this);
		menuListener = new MenuListener(this, analyser);
		selectionListener = new SelectionListener(this, analyser);
		wordlists = new Wordlists(this);
	}

	/**
	 * Diese Funktion aktiviert die Menüs.
	 */
	private void enableMenu() {
		show.setEnabled(true);
		run.setEnabled(true);
		results.setEnabled(true);
		dbentries.setEnabled(true);
	}

	/**
	 * Diese Funktion fügt die verschiedenen benötigten Stile zum
	 * Dokumentobjekt des Textfeldes.
	 * 
	 * @param doc
	 *            das Dokumentobjekt des Textfelds
	 */
	private void addStylesToDocument(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);

		Style s = doc.addStyle("selection", regular);
		StyleConstants.setBackground(s, selectionColor);

		s = doc.addStyle("e1", regular);
		StyleConstants.setBackground(s, e1Color);

		s = doc.addStyle("e1-db", regular);
		StyleConstants.setForeground(s, e1Color);

		s = doc.addStyle("e2", regular);
		StyleConstants.setBackground(s, e2Color);

		s = doc.addStyle("e2-db", regular);
		StyleConstants.setForeground(s, e2Color);

		s = doc.addStyle("e3", regular);
		StyleConstants.setBackground(s, e3Color);

		s = doc.addStyle("e3-db", regular);
		StyleConstants.setForeground(s, e3Color);

		s = doc.addStyle("p1", regular);
		StyleConstants.setBackground(s, p1Color);

		s = doc.addStyle("p1-db", regular);
		StyleConstants.setForeground(s, p1Color);

		s = doc.addStyle("p2", regular);
		StyleConstants.setBackground(s, p2Color);

		s = doc.addStyle("p2-db", regular);
		StyleConstants.setForeground(s, p2Color);

		s = doc.addStyle("p3", regular);
		StyleConstants.setBackground(s, p3Color);

		s = doc.addStyle("p3-db", regular);
		StyleConstants.setForeground(s, p3Color);

		s = doc.addStyle("a1", regular);
		StyleConstants.setBackground(s, a1Color);

		s = doc.addStyle("a1-db", regular);
		StyleConstants.setForeground(s, a1Color);

		s = doc.addStyle("a2", regular);
		StyleConstants.setBackground(s, a2Color);

		s = doc.addStyle("a2-db", regular);
		StyleConstants.setForeground(s, a2Color);

		s = doc.addStyle("a3", regular);
		StyleConstants.setBackground(s, a3Color);

		s = doc.addStyle("a3-db", regular);
		StyleConstants.setForeground(s, a3Color);

		s = doc.addStyle("cws", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("other", regular);
		StyleConstants.setBackground(s, otherColor);

	}

	/**
	 * Diese Funktion erhält die URL des der Datenbank und lädt das
	 * vom Benutzer ausgewählte Kapitel. Gibt es ungespeicherte Daten
	 * werden diese noch gespeichert, wenn der Benutzer das möchte.
	 * 
	 * @param dbc
	 * @param chapter
	 */
	@SuppressWarnings("unchecked")
	public void DBConnect(@SuppressWarnings("hiding") DBC dbc, @SuppressWarnings("hiding") Chapter chapter) {
		this.dbc = dbc;
		try {
			if (chapter != null) {
				if (unsaved) {
					int value = JOptionPane
							.showOptionDialog(
									frame,
									"Do you want to save current results to the database?",
									"Save", JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null,
									null, null);
					if (value == 0) {
						analyser.saveCurrentResults();
					}
				}
				dialog.getLabel().setText("Start the analysis.");
				dialog.getPathPane().setForeground(Color.RED);
				dialog.getPathPane().setText("Choose a path.");
				Vector isoVector = new Vector();
				isoVector.add("No isotopies");
				dialog.getIsoList().setListData(isoVector);
				Vector possVector = new Vector();
				possVector.add("No proposals");
				dialog.getPossList().setListData(possVector);
				dialog.enableButtons(false, false, false, false, false, false);
				try{
				textPane.getStyledDocument().remove(0,
						chapter.getContent().length());}
				catch(BadLocationException ignore){
					//nichts tun
				}
				analyser.cleanAnalyser();
			}
			this.chapter = chapter;
			dbc.open();
			root = dbc.getPaths();
			dbc.close();
			if (chapter != null) {
				textPane.getStyledDocument().insertString(0,
						chapter.getContent(),
						textPane.getStyledDocument().getStyle("regular"));
				enableMenu();
				// wenn ja, dann Eintr&auml;ge laden
				// und markieren
				analyser.getDBEntries();
				analyser.showLevels();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, e.getMessage(), "DBC Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Diese Funktion startet den Datenbankdialog zur Auswahl eines Pfades und
	 * gibt gegebenenfalls den Pfad zurück.
	 * 
	 * @return den gewählten Pfad oder null
	 */
	public PathNode getPathSelector() {
		try {
			PathSelector pathSelector = new PathSelector(model.getUrl());
			pathSelector.showDialog(frame);
			@SuppressWarnings("unused") int id = pathSelector.getSelectedPathID();
			PathNode path = pathSelector.getSelectedPath();
			return path;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "DBC Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	// /**
	// * Diese Funktion wird aufgerufen, um die Verbindung zur Datenbank
	// * herzustellen.
	// */
	// public void connecting() {
	// String input_server = (String) JOptionPane.showInputDialog(frame,
	// "Do you want to connect to the database?",
	// "Connecting",
	// JOptionPane.PLAIN_MESSAGE,
	// null,
	// null,
	// server);
	// if (input_server != null) {
	// DBConnect(input_server);
	// }
	// }

	/**
	 * Diese Funktion kann alle Knöpfe der Werkzeugleiste auf einmal
	 * selektieren oder deselektieren.
	 * 
	 * @param state
	 *            true für selektiert und false für deselektiert
	 */
	public void setButtons(boolean state) {
		e1Button.setSelected(state);
		selectedE1 = state;
		e2Button.setSelected(state);
		selectedE2 = state;
		e3Button.setSelected(state);
		selectedE3 = state;
		p1Button.setSelected(state);
		selectedP1 = state;
		p2Button.setSelected(state);
		selectedP2 = state;
		p3Button.setSelected(state);
		selectedP3 = state;
		a1Button.setSelected(state);
		selectedA1 = state;
		a2Button.setSelected(state);
		selectedA2 = state;
		a3Button.setSelected(state);
		selectedA3 = state;
		otherButton.setSelected(state);
		selectedOther = state;
	}

	// /**
	// * Die Mainfunktion.
	// *
	// * @param args die Kommandozeilenargumente, aber die werden nicht
	// benötigt.
	// */
	// public static void main(String[] args) {
	// PraWo praWo = new PraWo();
	// praWo.connecting();
	// }

	/**
	 * Gibt das Hauptfenster zurück.
	 * 
	 * @return Returns the frame.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Gibt das aktuelle Kapitel zurück.
	 * 
	 * @return Returns the chapter.
	 */
	public Chapter getChapter() {
		return chapter;
	}

	/**
	 * Gibt das Textfeld zurück.
	 * 
	 * @return Returns the textPane.
	 */
	public JTextPane getTextPane() {
		return textPane;
	}

	/**
	 * Gibt den Status von E1 zurück.
	 * 
	 * @return Returns the selectedE1.
	 */
	public boolean isSelectedE1() {
		return selectedE1;
	}

	/**
	 * Setzt den Status von E1.
	 * 
	 * @param selectedE1
	 *            The selectedE1 to set.
	 */
	public void setSelectedE1(boolean selectedE1) {
		this.selectedE1 = selectedE1;
	}

	/**
	 * Gibt den Status von E2 zurück.
	 * 
	 * @return Returns the selectedE2.
	 */
	public boolean isSelectedE2() {
		return selectedE2;
	}

	/**
	 * Setzt den Status von E2.
	 * 
	 * @param selectedE2
	 *            The selectedE2 to set.
	 */
	public void setSelectedE2(boolean selectedE2) {
		this.selectedE2 = selectedE2;
	}

	/**
	 * Gibt den Status von E3 zurück.
	 * 
	 * @return Returns the selectedE3.
	 */
	public boolean isSelectedE3() {
		return selectedE3;
	}

	/**
	 * Setzt den Status von E3.
	 * 
	 * @param selectedE3
	 *            The selectedE3 to set.
	 */
	public void setSelectedE3(boolean selectedE3) {
		this.selectedE3 = selectedE3;
	}

	/**
	 * Gibt den Status von P1 zurück.
	 * 
	 * @return Returns the selectedP1.
	 */
	public boolean isSelectedP1() {
		return selectedP1;
	}

	/**
	 * Setzt den Status von P1.
	 * 
	 * @param selectedP1
	 *            The selectedP1 to set.
	 */
	public void setSelectedP1(boolean selectedP1) {
		this.selectedP1 = selectedP1;
	}

	/**
	 * Gibt den Status von P2 zurück.
	 * 
	 * @return Returns the selectedP2.
	 */
	public boolean isSelectedP2() {
		return selectedP2;
	}

	/**
	 * Setzt den Status von P2.
	 * 
	 * @param selectedP2
	 *            The selectedP2 to set.
	 */
	public void setSelectedP2(boolean selectedP2) {
		this.selectedP2 = selectedP2;
	}

	/**
	 * Gibt den Status von P3 zurück.
	 * 
	 * @return Returns the selectedP3.
	 */
	public boolean isSelectedP3() {
		return selectedP3;
	}

	/**
	 * Setzt den Status von P3.
	 * 
	 * @param selectedP3
	 *            The selectedP3 to set.
	 */
	public void setSelectedP3(boolean selectedP3) {
		this.selectedP3 = selectedP3;
	}

	/**
	 * Gibt den Status von A1 zurück.
	 * 
	 * @return Returns the selectedA1.
	 */
	public boolean isSelectedA1() {
		return selectedA1;
	}

	/**
	 * Setzt den Status von A1.
	 * 
	 * @param selectedA1
	 *            The selectedA1 to set.
	 */
	public void setSelectedA1(boolean selectedA1) {
		this.selectedA1 = selectedA1;
	}

	/**
	 * Gibt den Status von A2 zurück.
	 * 
	 * @return Returns the selectedA2.
	 */
	public boolean isSelectedA2() {
		return selectedA2;
	}

	/**
	 * Setzt den Status von A2.
	 * 
	 * @param selectedA2
	 *            The selectedA2 to set.
	 */
	public void setSelectedA2(boolean selectedA2) {
		this.selectedA2 = selectedA2;
	}

	/**
	 * Gibt den Status von A3 zurück.
	 * 
	 * @return Returns the selectedA3.
	 */
	public boolean isSelectedA3() {
		return selectedA3;
	}

	/**
	 * Setzt den Status von A3.
	 * 
	 * @param selectedA3
	 *            The selectedA3 to set.
	 */
	public void setSelectedA3(boolean selectedA3) {
		this.selectedA3 = selectedA3;
	}

	/**
	 * Gibt den Status von Other zurück.
	 * 
	 * @return Returns the selectedOther.
	 */
	public boolean isSelectedOther() {
		return selectedOther;
	}

	/**
	 * Setzt den Status von Other.
	 * 
	 * @param selectedOther
	 *            The selectedOther to set.
	 */
	public void setSelectedOther(boolean selectedOther) {
		this.selectedOther = selectedOther;
	}

	/**
	 * Gibt den Server zurück.
	 * 
	 * @return Returns the server.
	 */
	public String getServer() {
		return model.getUrl();
	}

	/**
	 * Gibt den Dialog zurück.
	 * 
	 * @return Returns the dialog.
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * Gibt die Farbe für A1 zurück.
	 * 
	 * @return Returns the a1Color.
	 */
	public Color getA1Color() {
		return a1Color;
	}

	/**
	 * Gibt die Farbe für A2 zurück.
	 * 
	 * @return Returns the a2Color.
	 */
	public Color getA2Color() {
		return a2Color;
	}

	/**
	 * Gibt die Farbe für A3 zurück.
	 * 
	 * @return Returns the a3Color.
	 */
	public Color getA3Color() {
		return a3Color;
	}

	/**
	 * Gibt die Farbe für E1 zurück.
	 * 
	 * @return Returns the e1Color.
	 */
	public Color getE1Color() {
		return e1Color;
	}

	/**
	 * Gibt die Farbe für E2 zurück.
	 * 
	 * @return Returns the e2Color.
	 */
	public Color getE2Color() {
		return e2Color;
	}

	/**
	 * Gibt die Farbe für E3 zurück.
	 * 
	 * @return Returns the e3Color.
	 */
	public Color getE3Color() {
		return e3Color;
	}

	/**
	 * Gibt die Farbe für P1 zurück.
	 * 
	 * @return Returns the p1Color.
	 */
	public Color getP1Color() {
		return p1Color;
	}

	/**
	 * Gibt die Farbe für P2 zurück.
	 * 
	 * @return Returns the p2Color.
	 */
	public Color getP2Color() {
		return p2Color;
	}

	/**
	 * Gibt die Farbe für P3 zurück.
	 * 
	 * @return Returns the p3Color.
	 */
	public Color getP3Color() {
		return p3Color;
	}

	/**
	 * Gibt die Farbe für Funktionswörter zurück.
	 * 
	 * @return Returns the functionColor.
	 */
	public Color getFunctionColor() {
		return functionColor;
	}

	/**
	 * Gibt die Farbe für andere Wörter zurück.
	 * 
	 * @return Returns the otherColor.
	 */
	public Color getOtherColor() {
		return otherColor;
	}

	/**
	 * Gibt den Zeiger auf die Wordlists-Instanz zurück.
	 * 
	 * @return Returns the wordlists.
	 */
	public Wordlists getWordlists() {
		return wordlists;
	}

	/**
	 * Gibt den Wurzelknoten des Pfadbaums zurück.
	 * 
	 * @return Returns the root.
	 */
	public PathNode getRoot() {
		return root;
	}

	/**
	 * Gibt zurück, ob ungespeicherte Ergebnisse vorliegen.
	 * 
	 * @return Returns the unsaved.
	 */
	public boolean isUnsaved() {
		return unsaved;
	}

	/**
	 * &Auml;ndert den Status auf ungespeicherte Ergebnisse.
	 * @param unsaved 
	 */
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowActivated(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowClosed(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Fragt den Benutzer vor dem Schlie&szlig;en, ob er ungespeicherte
	 * Ergebnisse speichern möchte.
	 */
	public void windowClosing(@SuppressWarnings("unused") WindowEvent e) {
		if (unsaved) {
			int value = JOptionPane.showOptionDialog(frame,
					"Do you want to save current results to the database?",
					"Save", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (value == 0) {
				analyser.saveCurrentResults();
			}
		}

		model.continueBU();
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowDeactivated(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowDeiconified(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowIconified(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * Implementierte Funktion von WindowListener. <br>
	 * Nicht ausprogrammiert.
	 */
	public void windowOpened(@SuppressWarnings("unused") WindowEvent e) {
		//nothing to do
	}

	/**
	 * @return Returns the dbc.
	 */
	public DBC getDbc() {
		return dbc;
	}

	/**
	 * @return Returns the model.
	 */
	protected Model getModel() {
		return model;
	}
}