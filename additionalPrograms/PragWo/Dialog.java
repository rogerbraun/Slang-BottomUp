package additionalPrograms.PragWo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

/**
 * @author Natascha Stäbler natascha@mainiero.de Last modified on
 *         03.11.2005 Diese Klasse ist für den Aufbau des Dialogs
 *         zuständig. Au&szlig;erdem stellt sie einen MiniDialog zur
 *         Verfügung, mit dem die Hilfelisten dargestellt werden
 *         können und den Dialog, der zu Beginn der Analyse angezeigt wird.
 */
public class Dialog {

	// benötigte Klassenzeiger
	private PraWo praWo;

	private Analyser analyser;

	// die RadioButtons für die Abstraktheitsgrade
	private JRadioButton e1Radio;

	private JRadioButton e2Radio;

	private JRadioButton e3Radio;

	private JRadioButton eDeselect;

	private JRadioButton p1Radio;

	private JRadioButton p2Radio;

	private JRadioButton p3Radio;

	private JRadioButton pDeselect;

	private JRadioButton a1Radio;

	private JRadioButton a2Radio;

	private JRadioButton a3Radio;

	private JRadioButton aDeselect;

	// der ok Button
	private JButton ok;

	// die RadioButtons für die Wortklassen
	private JRadioButton nomenRadio;

	private JRadioButton verbRadio;

	private JRadioButton adjektivRadio;

	private JRadioButton otherRadio;

	private JRadioButton kindDeselect;

	private JRadioButton fwRadio;

	// das Label, das dem Benutzer Benutzungshinweise gibt
	private JLabel label;

	// die Listen für Vorschläge, Isotopien und Individual Meaning
	private JList possList;

	private JList isoList;

	private JList meanList;

	private JButton pathButton;

	// die Buttons für den MiniDialog
	private JButton wordclassButton;

	private JButton levelButton;

	// CheckBox, für den Transfer der Analyse auf weitere Instanzen
	private JCheckBox transfer;

	// die Textfeld für die Anzeige des kompletten Pfades
	private JTextPane pathPane;

	// der StartDialog
	private JDialog startDialog;

	// ComboBox für den Startdialog
	private JComboBox wordListBox;

	/**
	 * Der Konstruktor speichert nur die beiden Zeiger.
	 * 
	 * @param praWo
	 *            der Zeiger auf die PraWo-Instanz
	 * @param analyser
	 *            der Zeiger auf die Analyser-Instanz
	 */
	public Dialog(PraWo praWo, Analyser analyser) {
		this.praWo = praWo;
		this.analyser = analyser;
	}

	/**
	 * Diese Funktion erstellt den kompletten Eingabedialog und gibt diesen
	 * zurück.
	 * 
	 * @return das Panel mit dem Dialog
	 */
	@SuppressWarnings("unchecked")
	public JPanel runDialog() {
		DialogListener dialogListener = new DialogListener(this, praWo,
				analyser);

		// Upper Panel
		JPanel upperPanel = new JPanel();
		label = new JLabel("Start the analysis.");
		upperPanel.add(label);

		// Center Panel
		// Create a radio button.
		nomenRadio = new JRadioButton("Noun");
		nomenRadio.setActionCommand("Nomen");
		nomenRadio.setEnabled(false);
		nomenRadio.addActionListener(dialogListener);

		// Create a second radio button.
		verbRadio = new JRadioButton("Verb");
		verbRadio.setActionCommand("Verb");
		verbRadio.setEnabled(false);
		verbRadio.addActionListener(dialogListener);

		// Create a second radio button.
		adjektivRadio = new JRadioButton("Adjectiv");
		adjektivRadio.setActionCommand("Adjektiv");
		adjektivRadio.setEnabled(false);
		adjektivRadio.addActionListener(dialogListener);

		// Create a third radio button.
		otherRadio = new JRadioButton("Other");
		otherRadio.setEnabled(false);
		otherRadio.addActionListener(dialogListener);

		// Create a fourth radio button.
		fwRadio = new JRadioButton("FW");
		fwRadio.setActionCommand("FW");
		fwRadio.setEnabled(false);
		fwRadio.addActionListener(dialogListener);

		kindDeselect = new JRadioButton();
		kindDeselect.setVisible(false);

		// Group the radio buttons.
		ButtonGroup kindGroup = new ButtonGroup();
		kindGroup.add(nomenRadio);
		kindGroup.add(verbRadio);
		kindGroup.add(adjektivRadio);
		kindGroup.add(otherRadio);
		kindGroup.add(kindDeselect);
		kindGroup.add(fwRadio);

		wordclassButton = new JButton("Help");
		wordclassButton.setActionCommand("wordclass");
		wordclassButton.setEnabled(false);
		wordclassButton.addActionListener(dialogListener);

		// Lay out the kind buttons.
		JPanel kindPane0 = new JPanel();
		kindPane0.setLayout(new BoxLayout(kindPane0, BoxLayout.PAGE_AXIS));
		kindPane0.add(nomenRadio);
		kindPane0.add(verbRadio);
		kindPane0.add(adjektivRadio);
		kindPane0.add(otherRadio);
		kindPane0.add(fwRadio);
		JPanel kindPane = new JPanel();
		kindPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Word class"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		kindPane.add(kindPane0);
		kindPane.add(wordclassButton);

		// Create a fourth radio button.
		e1Radio = new JRadioButton("E1");
		e1Radio.setActionCommand("E1-INFO");
		e1Radio.setEnabled(false);
		e1Radio.addActionListener(dialogListener);

		// Create a third radio button.
		e2Radio = new JRadioButton("E2");
		e2Radio.setActionCommand("E2-INFO");
		e2Radio.setEnabled(false);
		e2Radio.addActionListener(dialogListener);

		// Create a third radio button.
		e3Radio = new JRadioButton("E3");
		e3Radio.setActionCommand("E3-INFO");
		e3Radio.setEnabled(false);
		e3Radio.addActionListener(dialogListener);

		eDeselect = new JRadioButton();
		eDeselect.setVisible(false);

		// Group the radio buttons.
		ButtonGroup nomenGroup = new ButtonGroup();
		nomenGroup.add(e1Radio);
		nomenGroup.add(e2Radio);
		nomenGroup.add(e3Radio);
		nomenGroup.add(eDeselect);

		// Create a fourth radio button.
		p1Radio = new JRadioButton("P1");
		p1Radio.setActionCommand("P1-INFO");
		p1Radio.setEnabled(false);
		p1Radio.addActionListener(dialogListener);

		// Create a fifth radio button.
		p2Radio = new JRadioButton("P2");
		p2Radio.setActionCommand("P2-INFO");
		p2Radio.setEnabled(false);
		p2Radio.addActionListener(dialogListener);

		// Create a sixth radio button.
		p3Radio = new JRadioButton("P3");
		p3Radio.setActionCommand("P3-INFO");
		p3Radio.setEnabled(false);
		p3Radio.addActionListener(dialogListener);

		pDeselect = new JRadioButton();
		pDeselect.setVisible(false);

		// Group the radio buttons.
		ButtonGroup verbGroup = new ButtonGroup();
		verbGroup.add(p1Radio);
		verbGroup.add(p2Radio);
		verbGroup.add(p3Radio);
		verbGroup.add(pDeselect);

		// Create a fourth radio button.
		a1Radio = new JRadioButton("A1");
		a1Radio.setActionCommand("A1-INFO");
		a1Radio.setEnabled(false);
		a1Radio.addActionListener(dialogListener);

		// Create a fifth radio button.
		a2Radio = new JRadioButton("A2");
		a2Radio.setActionCommand("A2-INFO");
		a2Radio.setEnabled(false);
		a2Radio.addActionListener(dialogListener);

		// Create a sixth radio button.
		a3Radio = new JRadioButton("A3");
		a3Radio.setActionCommand("A3-INFO");
		a3Radio.setEnabled(false);
		a3Radio.addActionListener(dialogListener);

		aDeselect = new JRadioButton();
		aDeselect.setVisible(false);

		// Group the radio buttons.
		ButtonGroup adjektivGroup = new ButtonGroup();
		adjektivGroup.add(a1Radio);
		adjektivGroup.add(a2Radio);
		adjektivGroup.add(a3Radio);
		adjektivGroup.add(aDeselect);

		// Lay out the nomen buttons.
		JPanel nomenPane = new JPanel();
		nomenPane.setLayout(new BoxLayout(nomenPane, BoxLayout.PAGE_AXIS));
		nomenPane.add(e1Radio);
		nomenPane.add(e2Radio);
		nomenPane.add(e3Radio);

		// Lay out the verb buttons.
		JPanel verbPane = new JPanel();
		verbPane.setLayout(new BoxLayout(verbPane, BoxLayout.PAGE_AXIS));
		verbPane.add(p1Radio);
		verbPane.add(p2Radio);
		verbPane.add(p3Radio);

		// Lay out the verb buttons.
		JPanel adjektivPane = new JPanel();
		adjektivPane
				.setLayout(new BoxLayout(adjektivPane, BoxLayout.PAGE_AXIS));
		adjektivPane.add(a1Radio);
		adjektivPane.add(a2Radio);
		adjektivPane.add(a3Radio);

		levelButton = new JButton("Help");
		levelButton.setActionCommand("level");
		levelButton.setEnabled(false);
		levelButton.addActionListener(dialogListener);

		// Lay out the level buttons.
		JPanel levelPane = new JPanel();
		levelPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Level"), BorderFactory.createEmptyBorder(
				5, 5, 5, 5)));
		levelPane.add(nomenPane);
		levelPane.add(verbPane);
		levelPane.add(adjektivPane);
		levelPane.add(levelButton);

		// Create Panel for Path selection
		JPanel pathPanel = new JPanel();
		pathPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Path"), BorderFactory.createEmptyBorder(5,
				5, 5, 5)));
		pathButton = new JButton("Pathselector");
		pathButton.setEnabled(false);
		pathButton.addActionListener(dialogListener);

		pathPane = new JTextPane();
		pathPane.setText("Choose a path.");
		pathPane.setForeground(Color.RED);
		pathPane.setPreferredSize(new Dimension(150, 120));
		pathPane.setEditable(false);

		pathPanel.add(pathButton);
		pathPanel.add(pathPane);

		// Put middle panel together
		JPanel middle = new JPanel(new BorderLayout());
		middle.add(kindPane, BorderLayout.PAGE_START);
		middle.add(levelPane, BorderLayout.CENTER);
		middle.add(pathPanel, BorderLayout.PAGE_END);

		// Create Panel for isotopy
		JPanel isoPanel = new JPanel();
		isoPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Isotopy"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));

		Vector isoVector = new Vector();
		isoVector.add("No isotopies");

		isoList = new JList(isoVector);
		isoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollIso = new JScrollPane(isoList);
		scrollIso.setPreferredSize(new Dimension(250, 150));
		isoPanel.add(scrollIso);

		// Create Panel for meaning
		JPanel meanPanel = new JPanel();
		meanPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Individual Meaning"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));

		Vector meanVector = new Vector();
		meanVector.add("No individual meaning");

		meanList = new JList(meanVector);
		meanList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollMean = new JScrollPane(meanList);
		scrollMean.setPreferredSize(new Dimension(250, 150));
		meanPanel.add(scrollMean);

		// Create ok and cancel buttons
		ok = new JButton("Ok");
		ok.setEnabled(false);
		ok.addActionListener(dialogListener);

		// Create checkbox for transfer
		transfer = new JCheckBox("Transfer to all other instances.");
		transfer.setEnabled(false);
		transfer.setSelected(true);

		// Lay out the ok/cancel buttons
		JPanel buttonsPane = new JPanel();
		buttonsPane.add(ok);
		buttonsPane.add(transfer);

		// Left side panel
		// Create a JList
		Vector possVector = new Vector();
		possVector.add("No proposals");
		possList = new JList(possVector);

		possList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		possList.addListSelectionListener(dialogListener);
		JScrollPane scrollPoss = new JScrollPane(possList);
		scrollPoss.setPreferredSize(new Dimension(250, 150));
		JPanel possPane = new JPanel();
		possPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Proposal"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		possPane.add(scrollPoss);
		JPanel left = new JPanel(new BorderLayout());
		left.add(possPane, BorderLayout.PAGE_START);
		left.add(meanPanel, BorderLayout.CENTER);
		left.add(isoPanel, BorderLayout.PAGE_END);
		left.validate();

		// Put everything together
		JPanel dialog = new JPanel();
		dialog.setLayout(new BorderLayout());

		dialog.add(upperPanel, BorderLayout.PAGE_START);
		dialog.add(middle, BorderLayout.LINE_START);
		dialog.add(left, BorderLayout.CENTER);
		dialog.add(buttonsPane, BorderLayout.PAGE_END);

		return dialog;
	}

	/**
	 * Die Funktion erstellt den MiniDialog mit allen Wörtern einer Wortart
	 * bzw. Abstraktheit.
	 * 
	 * @param wordclass
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param level
	 *            die Abstraktheit (LEXRAG_??)
	 */
	@SuppressWarnings("unchecked")
	public void runMiniDialog(boolean wordclass, boolean level) {
		Hashtable show = null;
		// die benötigten Wörter holen
		if (wordclass) {
			show = analyser.getNeededWordclassVector();
		} else if (level) {
			show = analyser.getNeededLevelVector();
		}
		if (show != null) {
			// den Dialog erstellen
			JDialog miniDialog = new JDialog(praWo.getFrame(), "Info");
			JPanel panel = new JPanel();
			PragmatischesWort pw = null;
			Vector words = new Vector();
			String wordInfo = null;
			Enumeration showKeys = show.keys();
			while (showKeys.hasMoreElements()) {
				// alle Wörter in die Liste einfügen
				pw = (PragmatischesWort) show.get(showKeys.nextElement());
				wordInfo = pw.getContent() + " (" + pw.getStart() + "-"
						+ pw.getEnd() + ")";
				words.add(wordInfo);
			}
			if (words.size() == 0) {
				String noWords = "There are no words in this category.";
				words.add(noWords);
			}
			JList wordlist = new JList(words);
			// die Liste in den Dialog einfügen
			panel.add(wordlist);
			JScrollPane scrollPane = new JScrollPane(panel);
			scrollPane.setPreferredSize(new Dimension(250, 500));
			miniDialog.getContentPane().add(scrollPane);
			// den dialog anzeigen
			miniDialog.pack();
			miniDialog.setVisible(true);
		}
	}

	/**
	 * Der Dialog wird angezeigt, damit der Benuzter entscheiden kann von
	 * welchem (konstitutiven) Wort er die Analyse starten möchte.
	 * 
	 * @param words
	 *            der Vektor, der Wörter, die zur Wahl stehen
	 * @param isCW
	 *            true, wenn Wörter konstitutiv, sonst false
	 */
	public void runStartDialog(Vector words, boolean isCW) {
		DialogListener dialogListener = new DialogListener(this, praWo,
				analyser);

		// den Dialog erstellen
		startDialog = new JDialog(praWo.getFrame(), "Analyse");
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(250, 100));

		// Label erstellen
		@SuppressWarnings("hiding") JLabel label = new JLabel("Please choose the start position.");
		JPanel labelpanel = new JPanel();
		labelpanel.add(label);

		// ComboBox erstellen
		wordListBox = new JComboBox(words);
		wordListBox.setSelectedIndex(0);
		if (isCW) {
			wordListBox.setActionCommand("wordlistCwChanged");
		} else {
			wordListBox.setActionCommand("wordlistAllChanged");
		}
		wordListBox.addActionListener(dialogListener);
		JPanel wordListBoxpanel = new JPanel();
		wordListBoxpanel.add(wordListBox);

		// Ok-Button erstellen
		@SuppressWarnings("hiding") JButton ok = new JButton("Ok");
		ok.setActionCommand("StartOk");
		ok.addActionListener(dialogListener);
		JPanel okpanel = new JPanel();
		okpanel.add(ok);

		// das Label in den Dialog einfügen
		panel.add(labelpanel, BorderLayout.NORTH);
		// die ComboBox in den Dialog einfügen
		panel.add(wordListBoxpanel, BorderLayout.CENTER);
		// den Ok-Button in den Dialog einfügen
		panel.add(okpanel, BorderLayout.SOUTH);

		// den dialog anzeigen
		startDialog.getContentPane().add(panel);
		startDialog.pack();
		startDialog.setLocation(250, 250);
		startDialog.setVisible(true);

	}

	/**
	 * Mit dieser Funktion können alle Knöpfe des Dialogs an und aus
	 * geschaltet werden.
	 * 
	 * @param kind
	 *            sollen die Wortart-Buttons an sein
	 * @param nomen
	 *            sollen die E?-Buttons an sein
	 * @param verb
	 *            sollen die P?-Buttons an sein
	 * @param adjektiv
	 *            sollen die A?-Buttons an sein
	 * @param path
	 *            sollen der Pfad-Button an sein
	 * @param isOk
	 *            sollen der Ok-Button an sein
	 */
	public void enableButtons(boolean kind, boolean nomen, boolean verb,
			boolean adjektiv, boolean path, boolean isOk) {
		nomenRadio.setEnabled(kind);
		verbRadio.setEnabled(kind);
		adjektivRadio.setEnabled(kind);
		otherRadio.setEnabled(kind);
		fwRadio.setEnabled(kind);
		e1Radio.setEnabled(nomen);
		e2Radio.setEnabled(nomen);
		e3Radio.setEnabled(nomen);
		p1Radio.setEnabled(verb);
		p2Radio.setEnabled(verb);
		p3Radio.setEnabled(verb);
		a1Radio.setEnabled(adjektiv);
		a2Radio.setEnabled(adjektiv);
		a3Radio.setEnabled(adjektiv);
		pathButton.setEnabled(path);
		ok.setEnabled(isOk);
		transfer.setEnabled(isOk);
	}

	/**
	 * Mit dieser Funktion können alle RadioButtons des Dialogs
	 * deselektiert werden.
	 */
	public void deselectButtons() {
		kindDeselect.setSelected(true);
		eDeselect.setSelected(true);
		pDeselect.setSelected(true);
		aDeselect.setSelected(true);
		transfer.setSelected(false);
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the e1Radio.
	 */
	public JRadioButton getE1Radio() {
		return e1Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the e2Radio.
	 */
	public JRadioButton getE2Radio() {
		return e2Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the e3Radio.
	 */
	public JRadioButton getE3Radio() {
		return e3Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the p1Radio.
	 */
	public JRadioButton getP1Radio() {
		return p1Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the p2Radio.
	 */
	public JRadioButton getP2Radio() {
		return p2Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the p3Radio.
	 */
	public JRadioButton getP3Radio() {
		return p3Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the ok.
	 */
	public JButton getOk() {
		return ok;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the nomenRadio.
	 */
	public JRadioButton getNomenRadio() {
		return nomenRadio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the otherRadio.
	 */
	public JRadioButton getOtherRadio() {
		return otherRadio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the verbRadio.
	 */
	public JRadioButton getVerbRadio() {
		return verbRadio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the a1Radio.
	 */
	public JRadioButton getA1Radio() {
		return a1Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the a2Radio.
	 */
	public JRadioButton getA2Radio() {
		return a2Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the a3Radio.
	 */
	public JRadioButton getA3Radio() {
		return a3Radio;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the adjektivRadio.
	 */
	public JRadioButton getAdjektivRadio() {
		return adjektivRadio;
	}

	/**
	 * Gibt das Label des Dialogs zurück.
	 * 
	 * @return Returns the label.
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * Gibt die Vorschlagsliste zurück.
	 * 
	 * @return Returns the possList.
	 */
	public JList getPossList() {
		return possList;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the levelButton.
	 */
	public JButton getLevelButton() {
		return levelButton;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the pathButton.
	 */
	public JButton getPathButton() {
		return pathButton;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the wordclassButton.
	 */
	public JButton getWordclassButton() {
		return wordclassButton;
	}

	/**
	 * Gibt die Isotopienliste zurück.
	 * 
	 * @return Returns the isoList.
	 */
	public JList getIsoList() {
		return isoList;
	}

	/**
	 * Gibt die Individual-Meaning-Liste zurück.
	 * 
	 * @return Returns the meanList.
	 */
	public JList getMeanList() {
		return meanList;
	}

	/**
	 * Gibt den entsprechenden Knopf des Dialogs zurück.
	 * 
	 * @return Returns the transfer.
	 */
	public JCheckBox getTransfer() {
		return transfer;
	}

	/**
	 * Gibt das Textfeld für die Pfade zurück.
	 * 
	 * @return Returns the pathPane.
	 */
	public JTextPane getPathPane() {
		return pathPane;
	}

	/**
	 * Gibt den Togglebutton FW zurück.
	 * 
	 * @return Returns the fwRadio.
	 */
	public JRadioButton getFwRadio() {
		return fwRadio;
	}

	/**
	 * Gibt die ComboBox zurück.
	 * 
	 * @return Returns the wordListBox.
	 */
	public JComboBox getWordListBox() {
		return wordListBox;
	}

	/**
	 * Gibt den Startdialog zurück.
	 * 
	 * @return Returns the startDialog.
	 */
	public JDialog getStartDialog() {
		return startDialog;
	}
}
