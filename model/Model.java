package model;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import view.View;
import view.FWAndCW.ChangeFWAndCWMenu;
import view.FWAndCW.FWAndCWDesigner;
import view.FWAndCW.FWAndCWMenu;
import view.FWAndCW.IdentifyFWandCWPanel;
import view.FWAndCW.WordList.FWWordListMenu;
import view.FWAndCW.WordList.FWWordListPanel;
import view.FWAndCW.WordList.WordListMenu;
import view.FWAndCW.WordList.WordListPanel;
import view.IU.IUContinueMenu;
import view.IU.IUMenu;
import view.MS.CheckingMenu;
import view.MS.MSDesigner;
import view.MS.MSTailMenu;
import view.MU.MUContinueMenu;
import view.MU.MUMenu;
import view.MU.MUTwoPartMenu;
import view.SG.SGCreatingMenu;
import view.Superclasses.Designer;
import view.Superclasses.Menu;
import additionalPrograms.MUSG_Viewer.MUSG_Viewer;
import controller.Controller;
import controller.FWAndCW.ChangeFWAndCWController;
import controller.FWAndCW.IdentifyFWandCWController;
import controller.FWAndCW.WordList.FWWordListController;
import controller.FWAndCW.WordList.WordListController;
import controller.IU.IUContinueController;
import controller.IU.IUController;
import controller.MS.CheckingController;
import controller.MS.MSTailController;
import controller.MU.MUContinueController;
import controller.MU.MUController;
import controller.MU.MUTwoPartController;
import controller.SG.SGCreatingController;
import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Book;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Chapter;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.dialogs.chapterloader.ChapterLoader;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.pathselector.NumerusPathSelector;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.pathselector.PathSelector;

/*
 * Created on 26.01.2005
 */

/**
 * Hier wird die Verbindung zu Datenbank und den einzelnen Klassen hergestellt
 * 
 * @author shanthy
 */
public class Model extends JFrame implements WindowConstants {

	/**
	 * zufaellig erstellte ID
	 */
	private static final long serialVersionUID = -1196491136331036279L;

	/**
	 * die URL zur DB als String
	 */
	private String url;

	// aus der Datenbank
	/**
	 * die Datenbank-Connection
	 */
	private static DBC dbc;

	/**
	 * das geladene Buch
	 */
	private Book book;

	/**
	 * das geladene Kapitel
	 */
	private static Chapter chapter;

	/**
	 * Tool, um einen Pfad auszuwaehlen
	 */
	private static PathSelector pathSelector;

	/**
	 * Tool, um einen Numerus-Pfad auszuwaehlen
	 */
	private static NumerusPathSelector numerusPathSelector;

	/**
	 * Wurzeln der Aeusserungseinheiten
	 */
	private static IllocutionUnitRoots illocutionUnitRoots;

	// Menues und Controller

	/**
	 * Panel, um FWs und CWs zu bestimmen
	 */
	private IdentifyFWandCWPanel identifyFWandCWPanel;

	/**
	 * Controller, um FWs und CWs zu bestimmen
	 */
	private IdentifyFWandCWController identifyFWandCWController;

	/**
	 * Controller, um die cw Wortlisten zu bestimmen
	 */
	private WordListController wordListController;

	/**
	 * Controller, um die fw Wortlisten zu bestimmen
	 */
	private FWWordListController fwWordListController;

	/**
	 * Panel, um die cw - Wortlisten zu bestimmen
	 */
	private WordListPanel wordListPanel;

	/**
	 * Panel, um die fw - Wortlisten zu bestimmen
	 */
	private FWWordListPanel fwWordListPanel;

	/**
	 * Menue, um Sememgruppen zu erstellen
	 */
	private SGCreatingMenu sgCreatingMenu;

	/**
	 * Menue, um Makrosaetze zu erstellen
	 */
	private MSTailMenu makroSentenceTailMenu;

	// Hilfsvariablen:
	/**
	 * zu jeder Sprache werden die Constitutiven Woerter gespeichert
	 */
	private Hashtable constitutiveWords = new Hashtable();

	/**
	 * zu jeder Sprache werden die Funktionswoerter gespeichert
	 */
	private Hashtable functionWords = new Hashtable();

	/**
	 * wird fuer die Bestimmung der Meaning Unit benoetigt
	 */
	private ConstitutiveWord constitutiveWord;

	/**
	 * alle Sprachen, von denen es Woerter in der DB gibt
	 */
	private Vector languages;

	/**
	 * wird fuer die Bestimmung des Makrosatzes benoetigt
	 */
	private MacroSentence makroSentenceHead;

	// Hilfsprogramme:
	/**
	 * Programm, um die Verbindungen zwischen Meaning Units und Sememgruppen
	 * anzuzeigen
	 */
	private static MUSG_Viewer viewer;

	/**
	 * Staebler-Programm zum Bestimmen von Pfaden auf CW-Ebene
	 */
//	private PraWo praWo;

	// Layout-Componenten:
	/**
	 * Layout, damit die einzelnen Menueteile ausgetauscht werden koennen
	 */
	private CardLayout layout;

	/**
	 * Panel, in dem die unterschiedlichen Menues angezeigt werden
	 */
	private JPanel menu;

	/**
	 * ScrollPane fuer den Text
	 */
	private JScrollPane textSP;

	/**
	 * die einzelnen Menueeintraege
	 */
	private JMenuItem loadItem, saveItem, quitItem, pragWoItem;

	/**
	 * Buttons die fuer jedes Menue gleich sind
	 */
	private JButton saveButton, loadButton;

	/**
	 * der rechte Teil des Fenster: hier werden unterschiedliche Menues angezeigt
	 */
	private JPanel rightPanel;
	
	/**
	 * die ScrollPane fuer den rechten Teil des Fensters
	 */
	private JScrollPane scrollPane;

	/**
	 * die Standardhoehe des Programms
	 */
	private int height = 800;

	/**
	 * die Standardbreite des Programms
	 */
	private int width = 1024;

	/**
	 * der Controller, der aktuell aktiv ist
	 */
	private Controller currentController;

	/**
	 * die Textanzeige
	 */
	private View view;

	/**
	 * true, sobald man Änderungen vorgenommen hat. Wird zur Speichern-Abfrage
	 * am Ende benoetigt
	 */
	private boolean modelChanged = false;

	/**
	 * zur Verwaltung der Controller
	 */
	private Hashtable controllers = new Hashtable();

	/**
	 * normale Schrift
	 */
	public static final Font PLAINFONT = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * Icon fuer den Speichernknopf
	 */
	public ImageIcon saveIcon;

	/**
	 * Icon fuer den Oeffnenknopf
	 */
	public ImageIcon loadIcon;

	/**
	 * das helle blau fuer den markierten Text
	 */
	public static final Color MARKEDTEXT = new Color(204, 204, 255);

	/**
	 * schwarz :)
	 */
	public static final Color BLACK = Color.black;

	/**
	 * wei� :)
	 */
	public static final Color WHITE = Color.white;

	/**
	 * fuer constitutiveWords
	 */
	public static final Color RED1 = new Color(255, 0, 0);

	/**
	 * fuer constitutiveWords
	 */
	public static final Color RED2 = new Color(204, 9, 9);

	/**
	 * fuer functionWords
	 */
	public static final Color GREEN1 = new Color(0, 200, 0);

	/**
	 * fuer functionWords
	 */
	public static final Color GREEN2 = new Color(10, 140, 10);
	
	/**
	 * erstellt das Grundlayout fuer das Programm
	 */
	public Model() {
		super("Bottom Up Analysis");
		// LookAndFeel
	/*	try {
			UIManager
					.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}*/
		// Controller
		currentController = new Controller(this, new Designer());
		addWindowListener(currentController);
		// JScrollPane fuer den Text
		getContentPane().setLayout(new BorderLayout());
		// Hauptpanel fuer Text und JTree
		JPanel mainPanel = new JPanel(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		layout = new CardLayout();
		// ScrollPane fuer Text
		textSP = new JScrollPane(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(textSP, BorderLayout.CENTER);
		
		rightPanel = new JPanel();
		rightPanel.setSize(new Dimension((int) (width * 0.3), height));
		mainPanel.add(rightPanel, BorderLayout.EAST);

		// Bilder laden: wird wegen dem jar benoetigt
		loadImages();

		// die Elemente der MenuBar einfuegen
		addMenuBar();

		Dimension size = getToolkit().getScreenSize();
		width = (int) size.getWidth();
		height = (int) size.getHeight();

		// die verschiedenen Menues hinzufuegen
		addMenus();
		// Vollbild
		//setExtendedState(MAXIMIZED_BOTH);
		setSize(width, height);
		setVisible(true);
		// laden
		load();
	}

	/**
	 * Bilder laden wird benoetigt, wenn man das Programm als jar startet
	 * 
	 */
	private void loadImages() {
		ClassLoader cl = getClass().getClassLoader();
		saveIcon = new ImageIcon(getToolkit().getImage(
				cl.getResource("icons/Save24.gif")));
		loadIcon = new ImageIcon(getToolkit().getImage(
				cl.getResource("icons/Open24.gif")));

	}

	/**
	 * JMenuBar: Datei, Bearbeiten, Icons: Laden, Speichern
	 */
	private void addMenuBar() {
		JPanel menuPanel = new JPanel(new BorderLayout());
		// JToolBar fuer die standardIcons: Laden, Speichern
		JToolBar standardToolBar = new JToolBar();

		getContentPane().add(menuPanel, BorderLayout.PAGE_START);
		menuPanel.add(standardToolBar, BorderLayout.WEST);

		// JPanel fuer die MenuIcons oben
		menu = new JPanel(layout);

		// MenuButtons oben
		menuPanel.add(menu, BorderLayout.CENTER);

		loadButton = new JButton(loadIcon);
		loadButton.setActionCommand("LOAD");
		loadButton.addActionListener(currentController);
		saveButton = new JButton(saveIcon);
		saveButton.setActionCommand("SAVE");
		saveButton.addActionListener(currentController);
		loadButton.setToolTipText("Load");
		saveButton.setToolTipText("Save");
		loadButton.setPreferredSize(new Dimension(25, 25));
		standardToolBar.add(loadButton);
		saveButton.setPreferredSize(new Dimension(25, 25));
		standardToolBar.add(saveButton);

		// JMenuBar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// JMenu Datei
		JMenu file = new JMenu("File");
		menuBar.add(file);
		loadItem = new JMenuItem("Load");
		loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				InputEvent.CTRL_MASK));
		loadItem.addActionListener(currentController);
		file.add(loadItem);
		loadItem.setActionCommand("LOAD");
		saveItem = new JMenuItem("Save");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		saveItem.addActionListener(currentController);
		file.add(saveItem);
		saveItem.setActionCommand("SAVE");
		file.addSeparator();
		quitItem = new JMenuItem("Quit");
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		quitItem.addActionListener(currentController);
		file.add(quitItem);
		quitItem.setActionCommand("QUIT");
		standardToolBar.addSeparator(new Dimension(25, 25));
		// Programme
		JMenu programs = new JMenu("Programs");
		menuBar.add(programs);
		pragWoItem = new JMenuItem("PragWo");
		pragWoItem.addActionListener(currentController);
		programs.add(pragWoItem);
		pragWoItem.setActionCommand("PRAG_WO");
	}

	/**
	 * Menue zum Bestimmen von FWs und CWs
	 */
	public void createIdentifyPanel() {
		if (identifyFWandCWPanel != null) {
			rightPanel.remove(identifyFWandCWPanel);
			identifyFWandCWPanel = null;
			identifyFWandCWPanel = new IdentifyFWandCWPanel(
					identifyFWandCWController);
			rightPanel.add(identifyFWandCWPanel);
		} else if (identifyFWandCWPanel == null) {
			if (wordListPanel != null) {
				rightPanel.remove(wordListPanel);
			}
			if (fwWordListPanel != null) {
				rightPanel.remove(fwWordListPanel);
			}
			if(viewer != null)
				rightPanel.remove(scrollPane);
			identifyFWandCWPanel = new IdentifyFWandCWPanel(
					identifyFWandCWController);
			rightPanel.add(identifyFWandCWPanel);
		}
	}

	/**
	 * Menue zum Bestimmen der cw - Wortlisten
	 * 
	 */
	public void createWordListPanel() {
		if (wordListPanel != null) {
			rightPanel.remove(wordListPanel);
			wordListPanel = null;
			wordListPanel = new WordListPanel(wordListController);
			rightPanel.add(wordListPanel);
		} else if (wordListPanel == null) {
			if (identifyFWandCWPanel != null) {
				rightPanel.remove(identifyFWandCWPanel);
			}
			if (fwWordListPanel != null) {
				rightPanel.remove(fwWordListPanel);
			}
			if(viewer != null)
				rightPanel.remove(scrollPane);
			wordListPanel = new WordListPanel(wordListController);
			rightPanel.add(wordListPanel);
		}
	}
	
	/**
	 * Menue zum bestimmen der fw - Wortlisten
	 *
	 */
	public void createFWWordListPanel() {
		if (fwWordListPanel != null) {
			rightPanel.remove(fwWordListPanel);
			wordListPanel = null;
			fwWordListPanel = new FWWordListPanel(fwWordListController);
			rightPanel.add(fwWordListPanel);
		} else if (fwWordListPanel == null) {
			if (identifyFWandCWPanel != null) {
				rightPanel.remove(identifyFWandCWPanel);
			}
			if (wordListPanel != null) {
				rightPanel.remove(wordListPanel);
			}
			if(viewer != null)
				rightPanel.remove(scrollPane);
			fwWordListPanel = new FWWordListPanel(fwWordListController);
			rightPanel.add(fwWordListPanel);
		}
	}

	/**
	 * erstellt den Graph, bzw. loescht den Alten beim Laden eines Neuen
	 * Kapitels
	 */
	public void createGraph() {
		// loescht den Alten und erstellt einen Neuen
		if (viewer != null) {
			rightPanel.remove(scrollPane);
			viewer = null;
			viewer = new MUSG_Viewer();
			scrollPane = new JScrollPane(viewer,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setPreferredSize(new Dimension((int) (width * 0.3),
					height));
			rightPanel.add(scrollPane);
			viewer.setRoot(illocutionUnitRoots.getRoot(0));
		}
		if (viewer == null) {
			if (identifyFWandCWPanel != null)
				rightPanel.remove(identifyFWandCWPanel);
			viewer = new MUSG_Viewer();
			// ScrollPane fuer den Graph
			scrollPane = new JScrollPane(viewer,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setPreferredSize(new Dimension((int) (width * 0.3),
					height));
			rightPanel.add(scrollPane);
			viewer.setRoot(illocutionUnitRoots.getRoot(0));
		}
	}

	/**
	 * Verbindung zur Datenbank herstellen und das gewuenschte Kapitel aus der
	 * Datenbank laden
	 */
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			ProgressMonitor pm = new ProgressMonitor(this, "Load...", "", 0, 30);
			url = JOptionPane.showInputDialog(this,
					"Please feed in the URL of the database: ", "localhost");
			ChapterLoader cl = new ChapterLoader(url);
			cl.showDialog(this);
			dbc = new DBC(url);
			book = dbc.loadBook(cl.getBookID());
			chapter = dbc.loadChapter(cl.getChapterID());
			pm.setProgress(5);
			illocutionUnitRoots = dbc.loadIllocutionUnitRoots(chapter);
			pm.setProgress(10);
			languages = dbc.getLanguages();
			pm.setProgress(15);
			for (int i = 0; i < languages.size(); i++) {
				String language = languages.get(i).toString();
				constitutiveWords.put(language, dbc
						.getAllConstitutiveWords(language));
				functionWords.put(language, dbc.getAllFunctionWords(language));

			}
			pm.setProgress(20);
			dbc.close();
			setTitle(book.getTitle() + ": " + chapter.getTitle());
			pm.setProgress(25);

			pathSelector = new PathSelector(url);
			numerusPathSelector = new NumerusPathSelector(url);
			view = new View(currentController);
			pm.setProgress(30);
			textSP.setViewportView(view.getEditor());

			pm.setProgress(30);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Fehler beim Laden.");
			System.exit(1);
		}
		doWork();
	}

	/**
	 * Aenderungen in der DB speichern
	 */
	public void save() {
		try {
			ProgressMonitor pm = new ProgressMonitor(this, null, "Fortschritt",
					0, 30);
			pm.setProgress(5);
			pm.setProgress(10);
			dbc.open();
			dbc.saveIllocutionUnitRoots(illocutionUnitRoots);
			dbc.close();
			pm.setProgress(25);
			pm.setProgress(pm.getMaximum());
			JOptionPane.showMessageDialog(this, "Erfolgreich gespeichert.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Aenderungen in der DB speichern
	 */
	public void saveWithoutMessage() {
		try {
			dbc.open();
			dbc.saveIllocutionUnitRoots(illocutionUnitRoots);
			dbc.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern.");
			e.printStackTrace();
		}
	}

	/**
	 * den Graphen erstellen und das erste Menue anzeigen
	 */
	private void doWork() {
//		ConstitutiveWord cw = firstNotAssignedCW();
		showMenu("fwAndCW");
/*		if (!identifyFWandCWController.allWordsSet()) {
			System.out.println("1");
			showMenu("fwAndCW");
		} else if (cw != null) {
			System.out.println("2");
			showMenu("wordList");
			wordListPanel.setCW(cw);
		} else if (Model.getIllocutionUnitRoots().getMeaningUnits().size() == 0) {
			System.out.println("3");
			showMenu("fwAndCW");
		} else if (Model.getIllocutionUnitRoots().getSememeGroups().size() == 0) {
			System.out.println("4");
			createGraph();
//			startPragWo();
		} else if (Model.getIllocutionUnitRoots().getMacroSentences().size() == 0) {
			System.out.println("5");
			createGraph();
			showMenu("sg");
		} else {
			System.out.println("6");
			createGraph();
			showMenu("iu");
		}*/
	}

	/**
	 * 
	 * @return ConstittutiveWord
	 */
	public ConstitutiveWord firstNotAssignedCW() {
		Vector words = chapter.getWords();
		for (int i = 0; i < words.size(); i++) {
			Word w = (Word) words.get(i);
			ConstitutiveWord cw = illocutionUnitRoots.getConstitutiveWordAtPosition(w.getStartPosition());
			int startPos = w.getStartPosition();
			
			do 
			{
				// falls ein Wort aus 2 oder mehr cw besteht, muss jedes cw einzeln betrachtet werden.
				cw = illocutionUnitRoots.getConstitutiveWordAtPosition(startPos);
				if (cw != null) {
					startPos = cw.getEndPosition() + 1;
					TR_Assignation assig = cw.getAssignation();
					if (assig == null || (assig != null && 
							assig.getGenera().length == 0 && assig.getNumeri().length == 0 && 
							assig.getDeterminations().length == 0 && assig.getCases().length == 0 && 
							assig.getPersons().length == 0 && assig.getConjugations().length == 0 &&
							assig.getTempora().length == 0 && assig.getDiatheses().length == 0 && 
							assig.getWordclasses().length == 0 && assig.getWordsubclassesConnector().length ==0 &&
							assig.getWordsubclassesVerb().length == 0 && assig.getWordsubclassesPreposition().length == 0 &&
							assig.getWordsubclassesSign().length == 0 ))
							/*&& assig.getTypes().length == 0		types gibt nur an ob cw oder fw*/
						    return cw;
				}
			} while(cw != null && cw.getEndPosition() != w.getEndPosition());
		}
		return null;
	}
	
	/**
	 * 
	 * @return FunctionWord
	 */
	public FunctionWord firstNotAssignedFW() {
		Vector words = chapter.getWords();
		for (int i = 0; i < words.size(); i++) {
			Word w = (Word) words.get(i);
			FunctionWord fw = illocutionUnitRoots.getFunctionWordAtPosition(w.getStartPosition());
			int startPos = w.getStartPosition();
			
			do 
			{
				// falls ein Wort aus 2 oder mehr cw besteht, muss jedes cw einzeln betrachtet werden.
				fw = illocutionUnitRoots.getFunctionWordAtPosition(startPos);
				if (fw != null) {
					startPos = fw.getEndPosition() + 1;
					TR_Assignation assig = fw.getAssignation();
					if (assig == null || (assig != null && 
							assig.getGenera().length == 0 && assig.getNumeri().length == 0 && 
							assig.getCases().length == 0 && assig.getWortarten1().length == 0 &&
							assig.getWortarten2().length == 0 && assig.getWortarten3().length == 0 &&
							assig.getWortarten4().length == 0))
						    return fw;
				}
			} while(fw != null && fw.getEndPosition() != w.getEndPosition());
		}
		return null;
	}

	/**
	 * die verschiedenen Menues mit den entsprechenden Controllern und Designern
	 * hinzufuegen
	 */
	private void addMenus() {
		// FWAnadCWDesigner
		FWAndCWDesigner designer = new FWAndCWDesigner();

		// FWAndCWMenu
		identifyFWandCWController = new IdentifyFWandCWController(this, designer);
		add("fwAndCW", identifyFWandCWController, new FWAndCWMenu(identifyFWandCWController));

		wordListController = new WordListController(this);
		add("wordList", wordListController,	new WordListMenu(wordListController));

		fwWordListController = new FWWordListController(this);
		add("fwWordList", fwWordListController,	new FWWordListMenu(fwWordListController));
		
		// ChangeFWAndCWMenu
		ChangeFWAndCWController changeFWAndCWController = new ChangeFWAndCWController(
				this, designer);
		add("fwAndCWChange", changeFWAndCWController, new ChangeFWAndCWMenu(
				changeFWAndCWController));

		// MUMenu
		MUController muController = new MUController(this, designer);
		add("mu", muController, new MUMenu(muController));

		// MUContinueMenu
		MUContinueController muContinueController = new MUContinueController(
				this, designer);
		add("muContinue", muContinueController, new MUContinueMenu(
				muContinueController));

		// MUTwoPartMenu
		MUTwoPartController muTwoPartController = new MUTwoPartController(this,
				designer);
		add("muTwoPart", muTwoPartController, new MUTwoPartMenu(
				muTwoPartController));

		// SGCreatingMenu
		SGCreatingController sgCreatingController = new SGCreatingController(
				this, designer);
		sgCreatingMenu = new SGCreatingMenu(sgCreatingController);
		add("sgCreate", sgCreatingController, sgCreatingMenu);

		// IUMenu
		IUController iuController = new IUController(this, designer);
		add("iu", iuController, new IUMenu(iuController));

		// IUContinueMenu
		IUContinueController iuContinueController = new IUContinueController(
				this, designer);
		add("iuContinue", iuContinueController, new IUContinueMenu(
				iuContinueController));
		
		// MSDesigner
		MSDesigner msDesigner = new MSDesigner();

		// MacroSentenceHeaderMenu
		CheckingController msHeadController = new CheckingController(this,
				msDesigner);
		add("msHead", msHeadController, new CheckingMenu(msHeadController));
		// MacroSentenceTailMenu
		MSTailController msTailController = new MSTailController(this,
				msDesigner);
		makroSentenceTailMenu = new MSTailMenu(msTailController);
		add("msTail", msTailController, makroSentenceTailMenu);
	}

	/**
	 * die einzelnen Menues hinzufuegen
	 * 
	 * @param name
	 *            String
	 * @param controller
	 *            Controller
	 * @param m
	 *            Menu
	 */
	@SuppressWarnings("unchecked")
	private void add(String name, Controller controller, Menu m) {
		controller.setTextPart(view);
		menu.add(name, m);
		controllers.put(name, controller);
	}

	/**
	 * sucht das entsprechende Menue aus der Hashtable heraus und passt Menue,
	 * Controller und Designer an
	 * 
	 * @param name
	 *            String
	 */
	public void showMenu(String name) {
		if (identifyFWandCWPanel != null) {
			rightPanel.remove(identifyFWandCWPanel);
			identifyFWandCWPanel = null;
		}
		if (wordListPanel != null) {
			rightPanel.remove(wordListPanel);
			wordListPanel = null;
		}
		if (fwWordListPanel != null) {
			rightPanel.remove(fwWordListPanel);
			fwWordListPanel = null;
		}
		if (viewer != null) {
			rightPanel.remove(viewer);
		}
		if (name.equals("fwAndCW")) {
			rightPanel.removeAll();
			createIdentifyPanel();
			rightPanel.setBorder(BorderFactory.createTitledBorder("Classification of function words and consecutive words"));
		} else if (name.equals("wordList")) {
			createWordListPanel();
			rightPanel.setBorder(BorderFactory.createTitledBorder(name));
		} else if (name.equals("fwWordList")) {
			createFWWordListPanel();
			rightPanel.setBorder(BorderFactory.createTitledBorder(name));
		} else if (name.equals("mu") || name.equals("sg")) {
			if(name.equals("mu"))
				rightPanel.setBorder(BorderFactory.createTitledBorder("Meaning Units"));
			else
				rightPanel.setBorder(BorderFactory.createTitledBorder("Sememe Groups"));
			createGraph();
		} else if(name.equals("sgCreate"))
			rightPanel.setBorder(BorderFactory.createTitledBorder("Create Sememe Group"));
		else if (name.equals("iu"))
			rightPanel.setBorder(BorderFactory.createTitledBorder("Illocution Units"));
		else 
			rightPanel.setBorder(BorderFactory.createTitledBorder(name));
		
		if (view != null) {
			view.getEditor().setCaretPosition(view.getEditor().getSelectionEnd());
		}
		layout.show(menu, name);
//		rightPanel.setBorder(BorderFactory.createTitledBorder(name));
		if (controllers.containsKey(name)) {
			currentController = (Controller) controllers.get(name);
			view.setController(currentController);
			view.setDesigner(currentController.getDesigner());
		}
	}

	/**
	 * wird aufgerufen, wenn PragWo beendet wurde
	 * 
	 */
	public void continueBU() {
//		praWo.getFrame().setVisible(false);
		this.setVisible(true);
		showMenu("mu");
	}

	/**
	 * gibt das aktuelle Buch zurueck
	 * 
	 * @return book Book
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * gibt das aktuelle Kapitel zurueck
	 * 
	 * @return chapter Chapter
	 */
	public static Chapter getChapter() {
		return chapter;
	}

	/**
	 * gibt die View zurueck
	 * 
	 * @return view View
	 */
	public View getView() {
		return view;
	}

	/**
	 * wenn sich etwas geaendert hat, auf true setzen, damit vor dem Schliessen
	 * an das Speichern erinnert wird
	 * 
	 * @param mc
	 *            boolean
	 */
	public void modelChanged(boolean mc) {
		modelChanged = mc;
	}

	/**
	 * falls sich seit dem letzen Speichern was geaendert hat, vor dem Beenden
	 * fragen
	 * 
	 * @return modelChanged boolean
	 */
	public boolean hasModelChanged() {
		return modelChanged;
	}

	/**
	 * @return illocutionUnitRoots IllocutionUnitRoots
	 */
	public static IllocutionUnitRoots getIllocutionUnitRoots() {
		return illocutionUnitRoots;
	}

	/**
	 * @return constitutiveWord ConstitutiveWord
	 */
	public ConstitutiveWord getConstitutiveWord() {
		return constitutiveWord;
	}

	/**
	 * speichert das ConstitutiveWord, das mit einem FunctionWord zu einer
	 * MeaningUnit (in einem anderen Menue) zusammengefasst wird
	 * 
	 * @param constitutiveWord
	 *            ConstitutiveWord
	 */
	public void setConstitutiveWord(ConstitutiveWord constitutiveWord) {
		this.constitutiveWord = constitutiveWord;
	}

	/**
	 * gibt den Graph zurueck
	 * 
	 * @return viewer MUSG_Viewer
	 */
	public static MUSG_Viewer getViewer() {
		return viewer;
	}

	/**
	 * @return relCreatinMenu SGCreatingMenu
	 */
	public SGCreatingMenu getSGCreatingMenu() {
		return sgCreatingMenu;
	}

	/**
	 * gibt die Datenbank-Connection zurueck
	 * 
	 * @return dbc DBC
	 */
	public static DBC getDBC() {
		return dbc;
	}

	/**
	 * gibt den selektierten Pfad als int zurueck
	 * 
	 * @param o
	 *            Object
	 * @return path int
	 */
	public int choosePath(Object o) {
		if (o instanceof MeaningUnit) {
			MeaningUnit mu = (MeaningUnit) o;
			if (mu.getPath() != 0) {
				pathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
						* height, mu.getPath());
			} else
				pathSelector.showDialog(this);
			int id = pathSelector.getSelectedPathID();
			return id;
		} else if (o instanceof SememeGroup) {
			SememeGroup sg = (SememeGroup) o;
			if (sg.getPath() != 0) {
				pathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
						* height, sg.getPath());
			} else
				pathSelector.showDialog(this);
			int id = pathSelector.getSelectedPathID();
			return id;
		} else if (o instanceof IllocutionUnit) {
			IllocutionUnit iu = (IllocutionUnit) o;
			if (illocutionUnitRoots.getRoot(iu).getPath() != 0) {
				pathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
						* height, illocutionUnitRoots.getRoot(iu).getPath());
			} else
				pathSelector.showDialog(this);
			int id = pathSelector.getSelectedPathID();
			return id;
		}
		return 0;
	}
	
	/**
	 * gibt den selektierten Numerus-Pfad als int zurueck
	 * 
	 * @param o Object
	 * @return path int
	 */
	public int chooseNumerusPath(Object o) {
		if (o instanceof MeaningUnit) {
			MeaningUnit mu = (MeaningUnit) o;
			if (mu.getNumerusPath() != 0) {
				numerusPathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
					* height, mu.getNumerusPath());
			} else
				numerusPathSelector.showDialog(this);
			//--------------------------------------------
			//id richtig?
			int id = numerusPathSelector.getSelectedPathID();
			return id;
		}
		else if (o instanceof SememeGroup) {
			SememeGroup sg = (SememeGroup) o;
			if (sg.getNumerusPath() != 0) {
				numerusPathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
						* height, sg.getNumerusPath());
			} else
				numerusPathSelector.showDialog(this);
			int id = numerusPathSelector.getSelectedPathID();
			return id;
		} else if (o instanceof IllocutionUnit) {
			IllocutionUnit iu = (IllocutionUnit) o;
			if (illocutionUnitRoots.getRoot(iu).getNumerusPath() != 0) {
				numerusPathSelector.showDialog(this, (int) 0.7 * width, (int) 0.5
						* height, illocutionUnitRoots.getRoot(iu).getNumerusPath());
			} else
				numerusPathSelector.showDialog(this);
			int id = numerusPathSelector.getSelectedPathID();
			return id;
		}
		return 0;
	}

	/**
	 * Tool Pragmatische Wortarten erstellen
	 */
	public void startPragWo() {
		this.setVisible(false);
//		praWo = new PraWo(this);
//		praWo.DBConnect(dbc, chapter);
	}

	/**
	 * @return Returns the rightPanel.
	 */
	public JPanel getRightPanel() {
		return rightPanel;
	}

	/**
	 * @return Returns the makroSentenceHead.
	 */
	public MacroSentence getMakroSentenceHead() {
		return makroSentenceHead;
	}

	/**
	 * @param makroSentenceHead
	 *            The makroSentenceHead to set.
	 */
	public void setMakroSentenceHead(MacroSentence makroSentenceHead) {
		this.makroSentenceHead = makroSentenceHead;
	}

	/**
	 * @return Returns the makroSentenceTailMenu.
	 */
	public MSTailMenu getMakroSentenceTailMenu() {
		return makroSentenceTailMenu;
	}

	/**
	 * @param language
	 * @return Returns the constitutiveWords for one language
	 */
	@SuppressWarnings("unchecked")
	public Vector getConstitutiveWordsForLanguage(String language) {
		Vector cws = illocutionUnitRoots.getConstitutiveWords();
		if (cws.size() > ((Vector) constitutiveWords.get(language)).size()) {
			for (int i = 0; i < cws.size(); i++) {
				String cw = cws.get(i).toString();
				try {
					Vector cwsForLanguage = (Vector) constitutiveWords
							.get(language);
					if (!cwsForLanguage.contains(cw)) {
						cwsForLanguage.add(cw);
						constitutiveWords.put(language, cwsForLanguage);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// dbc.close();
				}
			}
		}
		return (Vector) constitutiveWords.get(language);
	}

	/**
	 * @param language
	 * @return Returns the functionWord for one language
	 */
	@SuppressWarnings("unchecked")
	public Vector getFunctionWordsForLanguage(String language) {
		Vector fws = illocutionUnitRoots.getFunctionWords();
		if (fws.size() > ((Vector) functionWords.get(language)).size()) {
			for (int i = 0; i < fws.size(); i++) {
				String fw = fws.get(i).toString();
				try {
					Vector fwsForLanguage = (Vector) functionWords
							.get(language);
					if (!fwsForLanguage.contains(fw)) {
						fwsForLanguage.add(fw);
						functionWords.put(language, fwsForLanguage);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return (Vector) functionWords.get(language);
	}

	/**
	 * @return IdentifyFWandCWPanel
	 */
	public IdentifyFWandCWPanel getIdentifyFWandCWPanel() {
		return identifyFWandCWPanel;
	}

	/**
	 * @return IdentifyFWandCWController
	 */
	public IdentifyFWandCWController getIdentifyFWandCWController() {
		return identifyFWandCWController;
	}

	/**
	 * @return Returns the languages.
	 */
	public Vector getLanguages() {
		return languages;
	}

	/**
	 * @return Returns the wordListController.
	 */
	public WordListController getWordListController() {
		return wordListController;
	}
	
	/**
	 * @return Returns the wordListController.
	 */
	public FWWordListController getFWWordListController() {
		return fwWordListController;
	}

	/**
	 * @return Returns the wordListPanel.
	 */
	public WordListPanel getWordListPanel() {
		if (wordListPanel == null)
			createWordListPanel();
		return wordListPanel;
	}

	/**
	 * @return Returns the wordListPanel.
	 */
	public FWWordListPanel getFWWordListPanel() {
		if (fwWordListPanel == null)
			createFWWordListPanel();
		return fwWordListPanel;
	}
	
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
}