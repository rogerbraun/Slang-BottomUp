package additionalPrograms.PragWo;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Chapter;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Checking;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.DB_Tupel;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Isotopes;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.DBC_ConnectionException;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 03.11.2005<br>
 *         <br>
 *         Dies ist die wichtigste Klasse für das Abstraktheitsanalysetool.
 *         Hier sind die Funktionen für die drei Analysearten, das holen
 *         der Daten aus der Datenbank und das Speichern der Daten in die
 *         Datenbank implementiert. Au&szlig;erdem stellt die Klasse die
 *         Funktionen zur Markierung von konstitutiven Bedeutungen und
 *         Abstreikheitsgraden zur Verfügung.
 */
public class Analyser {

	// benötigte Klassenzeiger
	private PraWo praWo;

	private DBC dbc;

	// interne Variable für nicht gesetzte Felder
	/**
	 * Nicht gesetzt.
	 */
	public final int NA = 99;

	// interne Variable zur Speicherung des aktuellen Analysemodus
	private int mode = 0;

	// die verschiedenen Analysemodi
	/**
	 * Analysemodus: alle Wörter (Words) werden nacheinander analysiert.
	 */
	public final int ALLMODE = 0;

	/**
	 * Analysemodus: alle konstitutiven Bedeutungen (ConstitutiveWords) werden
	 * nacheinander analysiert.
	 */
	public final int CWMODE = 1;

	/**
	 * Analysemodus: der Benutzer selektiert, das Wort, das analysiert werden
	 * soll.
	 */
	public final int SELECTEDMODE = 2;

	// interne Speicherung der Analyseergebnisse
	private Hashtable nomina;

	private Hashtable verba;

	private Hashtable adjektiva;

	private Hashtable otherWords;

	private Hashtable e1;

	private Hashtable e2;

	private Hashtable e3;

	private Hashtable p1;

	private Hashtable p2;

	private Hashtable p3;

	private Hashtable a1;

	private Hashtable a2;

	private Hashtable a3;

	private Hashtable all;

	private Hashtable allWords;

	private Hashtable transferWords;

	// die Variablen, die immer die Werte des gerade analysierten Wortes
	// enthalten
	String content_now = null;

	int startPosition_now = 0;

	int endPosition_now = 0;

	String language_now = null;

	int chapterID_now = 0;

	private int validPath = -1;

	private Token isotopie_token = null;

	// der Vektor, der die zu analysierenden Worte bzw. konstitutiven
	// Bedeutungen enthält
	private Vector v;

	// der Zähler für die zu analysierenden Worte bzw.
	// konstitutiven Bedeutungen
	private int index;

	// der Vektor, in dem alle Vorschläge gespeichert werden
	private Vector possibilities;

	// in dieser Hashtable werden die Instanzen vom Typ PragmatischesWort
	// auf die dem Benutzer angezeigten Vorschläge gemapped
	private Hashtable namePW;

	// die in der Datenbank gefundenen Isotopien und Informationen zu
	// Individual Meaning
	private Vector isotopies;

	private Vector meaning;

	/**
	 * Der Konstruktor initialisiert alle benötigten Hashtables, in die die
	 * Ergebnisse intern gespeichert werden
	 * 
	 * @param praWo
	 */
	public Analyser(PraWo praWo) {
		this.praWo = praWo;
		dbc = praWo.getDbc();
		allWords = new Hashtable();
		transferWords = new Hashtable();
		// analysedWords = new Hashtable();
		all = new Hashtable();
		nomina = new Hashtable();
		verba = new Hashtable();
		adjektiva = new Hashtable();
		otherWords = new Hashtable();
		e1 = new Hashtable();
		e2 = new Hashtable();
		e3 = new Hashtable();
		p1 = new Hashtable();
		p2 = new Hashtable();
		p3 = new Hashtable();
		a1 = new Hashtable();
		a2 = new Hashtable();
		a3 = new Hashtable();
	}

	/**
	 * Wird aufgerufen, wenn eine neue Analyse gestartet werden soll. Setzt alle
	 * Werte auf den Ausgangswert zurück.
	 */
	public void cleanAnalyser() {
		allWords = new Hashtable();
		transferWords = new Hashtable();
		all = new Hashtable();
		nomina = new Hashtable();
		verba = new Hashtable();
		adjektiva = new Hashtable();
		otherWords = new Hashtable();
		e1 = new Hashtable();
		e2 = new Hashtable();
		e3 = new Hashtable();
		p1 = new Hashtable();
		p2 = new Hashtable();
		p3 = new Hashtable();
		a1 = new Hashtable();
		a2 = new Hashtable();
		a3 = new Hashtable();
		praWo.setUnsaved(false);
	}

	/**
	 * Die Funktion erzeugt ein neues PragmatischesWort anhand der Angaben, die
	 * der Benutzer im Dialog gemacht hat.
	 */
	@SuppressWarnings("unchecked")
	public void newEntry() {
		int level = NA;
		TR_Assignation.Wordclass kind;
		int path = NA;
		String pathString = null;
		// Herausfinden welche RadioButtons selektiert sind und
		// entsprechend Wortart (kind) und Abstraktheit (level) setzen.
		if (praWo.getDialog().getNomenRadio().isSelected()) {
			kind = TR_Assignation.Wordclass.NOUN;
			if (praWo.getDialog().getE1Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_E1;
			} else if (praWo.getDialog().getE2Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_E2;
			} else if (praWo.getDialog().getE3Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_E3;
			}
		} else if (praWo.getDialog().getVerbRadio().isSelected()) {
			kind = TR_Assignation.Wordclass.VERB;
			if (praWo.getDialog().getP1Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_P1;
			} else if (praWo.getDialog().getP2Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_P2;
			} else if (praWo.getDialog().getP3Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_P3;
			}
		} else if (praWo.getDialog().getAdjektivRadio().isSelected()) {
			kind = TR_Assignation.Wordclass.ADJECTIVE;
			if (praWo.getDialog().getA1Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_A1;
			} else if (praWo.getDialog().getA2Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_A2;
			} else if (praWo.getDialog().getA3Radio().isSelected()) {
				level = ConstitutiveWord.LEXPRAG_A3;
			}
//		} else if (praWo.getDialog().getOtherRadio().isSelected()) {
//			kind = TR_Assignation.Wordclass.UNKNOWN;
//		}

		// der gültige Pfad steht in validPath
		path = validPath;
		// der zugehörige komplette Pathstring muss aus dem Dialog
		// ausgelesen werden
		pathString = praWo.getDialog().getPathPane().getText();

		// neue Instanz von PragmatischesWort erzeugen
		PragmatischesWort pw = new PragmatischesWort(content_now,
				chapterID_now, startPosition_now, endPosition_now, kind, level,
				path, pathString);

		// PragmatischesWort in die passenden Listen einfügen
		insertPwInLists(level, kind, pw);

		// will der Benutzer das aktuelle Analyseergebnis auf alle anderen
		// Instanzen im Text
		// übertragen, wird das jetzt gemacht und das Wort wird in die
		// Transferliste eingefügt
		if (praWo.getDialog().getTransfer().isSelected()) {
			transferToOtherInstances(kind, level, path, pathString);
			transferWords.put(content_now, pw);
		} else {
			// ansonsten wird das Wort aus der Transferliste entfernt, falls
			// es drin ist
			PragmatischesWort testPW = (PragmatischesWort) transferWords
					.get(content_now);
			if (testPW != null) {
				transferWords.remove(content_now);
			}
		}

		// wurde zum ersten Mal eine Analyse gemacht, so wird der Status
		// jetzt auf
		// unsaved gesetzt
		if (!praWo.isUnsaved()) {
			praWo.setUnsaved(true);
		}
	}

	/**
	 * Dies Funktion überträgt das Analyseergebnis, der aktuellen
	 * Wortes auf alle anderen Instanzen dieses Wortes im Kapitel. Auch
	 * rückwirkend!
	 * 
	 * @param kind
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param level
	 *            die Abstrakthiet (LEXPRAG_??)
	 * @param path
	 *            die ID eines Knotens im Pfadbaum
	 * @param pathString
	 *            der zugehörige komplette Pfad
	 */
	private void transferToOtherInstances(TR_Assignation.Wordclass kind, int level, int path,
			String pathString) {
		Vector instances = praWo.getChapter().getWords(content_now);
		if (instances != null) {
			for (int i = 0; i < instances.size(); i++) {
				Word instance = (Word) instances.get(i);
				startPosition_now = instance.getStartPosition();
				endPosition_now = instance.getEndPosition();
				PragmatischesWort pw = new PragmatischesWort(content_now,
						chapterID_now, startPosition_now, endPosition_now,
						kind, level, path, pathString);
				insertPwInLists(level, kind, pw);
			}
		}
	}

	/**
	 * Diese Funktion fügt ein PragmatischesWort in alle erforderlichen
	 * Listen ein.
	 * 
	 * @param level
	 *            die Abstraktheit (LEXPRAG_??)
	 * @param kind
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param pw
	 *            PragmatischesWort
	 */
	@SuppressWarnings("unchecked")
	private void insertPwInLists(int level, TR_Assignation.Wordclass kind, PragmatischesWort pw) {
		// um die Suche nach schon gemachten Analysen zu vereinfachen,
		// werden alle analysierten Worte mit dem gleichen Inhalt nach
		// Startposition
		// in eine Hashtable gepackt und diese dann wiederum in eine
		// Hashtable mit dem Inhalt als Key
		Hashtable list = (Hashtable) allWords.get(content_now.toLowerCase());
		if (list != null) {
			list.put(Integer.toString(startPosition_now), pw);
		} else {
			Hashtable newList = new Hashtable();
			newList.put(Integer.toString(startPosition_now), pw);
			allWords.put(content_now.toLowerCase(), newList);
		}
		// in all werden alle analysierten Worte nach Position abgespeichert
		all.put(Integer.toString(startPosition_now), pw);
		// je nach Wortart und Abstraktheit werden alle Worte noch
		// zusälich
		// in die passende Wortart- und Abstraktheit-Hashtable gepackt.
		// Der Key ist jeweils die Position
		if(kind == TR_Assignation.Wordclass.NOUN) {
			nomina.put(Integer.toString(startPosition_now), pw);
			switch (level) {
			case ConstitutiveWord.LEXPRAG_E1:
				e1.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_E2:
				e2.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_E3:
				e3.put(Integer.toString(startPosition_now), pw);
				break;
			}
			break;
		} else if(kind == TR_Assignation.Wordclass.VERB) {
			verba.put(Integer.toString(startPosition_now), pw);
			switch (level) {
			case ConstitutiveWord.LEXPRAG_P1:
				p1.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_P2:
				p2.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_P3:
				p3.put(Integer.toString(startPosition_now), pw);
				break;
			}
			break;
		} else if(kind == TR_Assignation.Wordclass.ADJECTIVE) {
			adjektiva.put(Integer.toString(startPosition_now), pw);
			switch (level) {
			case ConstitutiveWord.LEXPRAG_A1:
				a1.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_A2:
				a2.put(Integer.toString(startPosition_now), pw);
				break;
			case ConstitutiveWord.LEXPRAG_A3:
				a3.put(Integer.toString(startPosition_now), pw);
				break;
			}
			break;
//		case TR_Assignation.Wordclass.UNKNOWN:
//			otherWords.put(Integer.toString(startPosition_now), pw);
//			break;
//		}
		// das Wort wird markiert im Textfeld
		switchColoring(kind, level, true);
	}

	/**
	 * Diese Funktion wird ausgeführt, wenn der Benutzer alle Wörter
	 * analysieren will. Sie bereitet den Dialog vor.
	 */
	public void runAll() {
		// der Analysemodus wird gesetzt
		mode = ALLMODE;
		// das aktuelle Kapitel holen
		Chapter chapter = praWo.getChapter();
		// die Wörter des Kapitels auslesen
		v = chapter.getWords();
		index = 0;
		// den Benutzer fragen, bei welchem Wort er beginnen möchte
		praWo.getDialog().runStartDialog(v, false);
		praWo.getTextPane().requestFocus();
	}

	/**
	 * Diese Funktion wird ausgeführt, wenn der Benutzer alle konstitutiven
	 * Wörter analysieren will. Sie bereitet den Dialog vor.
	 */
	public void runCW() {
		// Analysemodus setzen
		mode = CWMODE;
		// alle konstitutiven Wörter des Kapitels holen
		v = getCWs();
		if (v != null) {
			index = 0;
			// den Benutzer fragen, bei welchem konstitutiven Wort er starten
			// will
			praWo.getDialog().runStartDialog(v, true);
			praWo.getTextPane().requestFocus();
		}
	}

	/**
	 * Diese Funktion wird ausgeführt, wenn der Benutzer slektierte
	 * Wörter analysieren will.
	 */
	@SuppressWarnings("unchecked")
	public void runSelected() {
		// den Analysemodus setzen
		mode = SELECTEDMODE;
		// den Dialog vorbereiten
		praWo.getDialog().getLabel().setText("Select a word.");
		possibilities = new Vector();
		possibilities.add("No proposals");
		praWo.getDialog().getPossList().setListData(possibilities);
		isotopies = new Vector();
		isotopies.add("No isotopies");
		praWo.getDialog().getIsoList().setListData(isotopies);
		meaning = new Vector();
		meaning.add("No individual meaning");
		praWo.getDialog().getMeanList().setListData(meaning);
		praWo.getDialog().enableButtons(false, false, false, false, false,
				false);
		praWo.getDialog().getWordclassButton().setEnabled(true);
		praWo.getDialog().getLevelButton().setEnabled(true);
		praWo.getDialog().getPathPane().setForeground(Color.RED);
		praWo.getDialog().getPathPane().setText("Choose a path");
	}

	/**
	 * Diese Funktion wird ausgeführt, wenn der Benutzer ein Wort
	 * selektiert hat und der Analysemodus SELECTEDMODE ist
	 * 
	 * @param content__input
	 * @param startPosition_input
	 * @param endPosition_input
	 * @param language_input
	 * @param chapterID_input
	 * @param token
	 */
	@SuppressWarnings("unchecked")
	public void runSelected(String content__input, int startPosition_input,
			int endPosition_input, String language_input, int chapterID_input,
			Token token) {
		content_now = content__input;
		startPosition_now = startPosition_input;
		endPosition_now = endPosition_input;
		language_now = language_input;
		chapterID_now = chapterID_input;
		isotopie_token = token;
		if (mode == SELECTEDMODE) {
			// nach Vorschlägen, Isotopien und Individual Meaning suchen
			LookUp();
			// den Dialog für das selektierte Wort vorbereiten
			praWo.getDialog().getLabel().setText(
					"Make your decisions for \"" + content_now + "\"");
			if (possibilities.size() != 0) {
				praWo.getDialog().getPossList().setListData(possibilities);
			} else {
				possibilities.add("No proposals");
				praWo.getDialog().getPossList().setListData(possibilities);
			}
			if (isotopies != null && isotopies.size() != 0) {
				praWo.getDialog().getIsoList().setListData(isotopies);
			} else {
				isotopies = new Vector();
				isotopies.add("No isotopies");
				praWo.getDialog().getIsoList().setListData(isotopies);
			}
			if (meaning.size() != 0) {
				praWo.getDialog().getMeanList().setListData(meaning);
			} else {
				meaning = new Vector();
				meaning.add("No individual meaning");
				praWo.getDialog().getMeanList().setListData(meaning);
			}
			praWo.getDialog().enableButtons(true, false, false, false, false,
					false);
			praWo.getDialog().getWordclassButton().setEnabled(true);
			praWo.getDialog().getLevelButton().setEnabled(true);
		}
	}

	/**
	 * Diese Funktion bereitet den Dialog für das gerade aktuelle
	 * (konstitutive) Wort vor.
	 */
	@SuppressWarnings("unchecked")
	public void runDialog() {
		boolean lastWord = false;
		switch (mode) {
		case ALLMODE:
			if (index <= v.indexOf(v.lastElement())) {
				// Sind noch nicht alle Worte bearbeitet,
				// dann alle Informationen zu dem nächsten Wort
				// auslesen
				Word word = (Word) v.get(index);
				isotopie_token = word;
				content_now = word.getContent();
				startPosition_now = word.getStartPosition();
				endPosition_now = word.getEndPosition();
				language_now = word.getLanguage();
				chapterID_now = word.getChapter().getDB_ID();
			} else {
				lastWord = true;
			}
			break;
		case CWMODE:
			if (index <= v.indexOf(v.lastElement())) {
				// Sind noch nicht alle konstitutiven Worte bearbeitet,
				// dann alle Informationen zu dem nächsten Wort
				// auslesen
				ConstitutiveWord constitutiveWord = (ConstitutiveWord) v
						.get(index);
				isotopie_token = constitutiveWord.getWord();
				content_now = constitutiveWord.getContent();
				startPosition_now = constitutiveWord.getStartPosition();
				endPosition_now = constitutiveWord.getEndPosition();
				language_now = constitutiveWord.getWord().getLanguage();
				chapterID_now = constitutiveWord.getWord().getChapter()
						.getDB_ID();
			} else {
				lastWord = true;
			}
			break;
		}
		if (mode != SELECTEDMODE) {
			if (lastWord) {
				// alle Wörter wurden analysiert
				praWo.getDialog().getLabel()
						.setText("No more words to analyse");

				praWo.getDialog().enableButtons(false, false, false, false,
						false, false);
				praWo.getDialog().getWordclassButton().setEnabled(false);
				praWo.getDialog().getLevelButton().setEnabled(false);

				possibilities = new Vector();
				possibilities.add("No proposals");
				praWo.getDialog().getPossList().setListData(possibilities);

				isotopies = new Vector();
				isotopies.add("No isotopies");
				praWo.getDialog().getIsoList().setListData(isotopies);

				meaning = new Vector();
				meaning.add("No individual meaning");
				praWo.getDialog().getMeanList().setListData(meaning);

				praWo.getTextPane().requestFocusInWindow();
			} else {
				index++;
				PragmatischesWort testPW = (PragmatischesWort) transferWords
						.get(content_now);
				if (testPW != null) {
					// ist das aktuelle (konstitutive) Wort in der
					// Transferliste,
					// so wird das Wort übergangen (es wurde dann ja schon
					// analysiert)
					runDialog();
				} else {
					// ansonsten wird nach Vorschlägen, Isotopien und
					// Individual Meaning gesucht
					LookUp();
					// und der Dialog für das (konstitutive) Wort
					// vorbereitet
					praWo.getDialog().getLabel().setText(
							"Make your decisions for \"" + content_now + "\"");
					if (possibilities.size() != 0) {
						praWo.getDialog().getPossList().setListData(
								possibilities);
					} else {
						possibilities.add("No proposals");
						praWo.getDialog().getPossList().setListData(
								possibilities);
					}
					if (isotopies != null && isotopies.size() != 0) {
						praWo.getDialog().getIsoList().setListData(isotopies);
					} else {
						isotopies = new Vector();
						isotopies.add("No isotopies");
						praWo.getDialog().getIsoList().setListData(isotopies);
					}
					if (meaning.size() != 0) {
						praWo.getDialog().getMeanList().setListData(meaning);
					} else {
						meaning = new Vector();
						meaning.add("No individual meaning");
						praWo.getDialog().getMeanList().setListData(meaning);
					}
					praWo.getTextPane().requestFocusInWindow();
					// das (Teil-)Wort wird selektiert
					praWo.getTextPane().select(startPosition_now,
							endPosition_now + 1);
				}
			}
		}
	}

	/**
	 * Diese Funktion startet das Nachschauen in der Datenbank und in der
	 * aktuellen Analyse. Dabei wird nach Vorschlägen, Isotopien und
	 * Individual Meaning gesucht.
	 */
	private void LookUp() {
		possibilities = new Vector();
		namePW = new Hashtable();
		// ist eine Instanz dieses Wortes schon einmal analysiert worden
		// (Speicher)
		isInAnalyse();
		// ist eine Instanz dieses Wortes schon einmal analysiert worden (DB)
		isInDB();
		// sind Informationen zu Isotopien in der Datenbank?
		isotopies = new Vector();
		try {
			dbc.open();
			Isotopes isotopes = dbc.loadIsotopes(praWo.getChapter());
			isotopies = isotopes.getIsotopes(isotopie_token);
			dbc.close();
		} catch (DBC_ConnectionException e) {
			e.printStackTrace();
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			dbc.close();
		}
		// sind Informationen zu Individual Meaning in der Datenbank?
		/*
		 * TODO es gibt bisher 3.11.2005 noch keine Informationen zu Individual
		 * Meaning in der Datenbank, deshalb erzeuge ich hier nur einen leeren
		 * Vektor
		 */
		meaning = new Vector();
	}

	/**
	 * Diese Funktion schaut in der Datenbank nach Vorschlägen zu dem
	 * aktuellen Wort nach.
	 */
	@SuppressWarnings("unchecked")
	private void isInDB() {
		// alle konstitutiven Wörter mit dem gleichen Inhalt
		// aus der Datenbank holen
		Vector<DB_Tupel> cws = DBCgetConstitutiveWords(content_now, language_now);
		DB_Tupel tupel = null;
		if (cws != null) {
			for (DB_Tupel tupel : cws) {
				// für jedes konstitutive Wort die benötigten
				// Information auslesen
//				tupel = (DB_Tupel) cws.get(i);
				String content = null;
				int chapter = NA;
				int start = NA;
				int wStart = NA;
				int end = NA;
				TR_Assignation.Wordclass kind;
				int level = NA;
				int path = NA;
				String pathString = null;
				String name = null;
				PragmatischesWort newPW = null;
				content = tupel.getString("content");
				chapter = tupel.getInt("chapter");
				start = tupel.getInt("position");
				wStart = tupel.getInt("start");
				start = start + wStart;
				if (content != null) {
					end = (start + content.length()) - 1;
				}
				kind = tupel.getByte("tr_wordclass");
				level = tupel.getInt("lexprag_level");
				// der Pfad eines Wortes ist abhängig von der Wortart
				// an unterschiedlichen Stellen gespeichert.
				if (kind == TR_Assignation.Wordclass.NOUN) {
					path = tupel.getInt("lexprag_path");
					break;
				} else if (kind ==TR_Assignation.Wordclass.NOUN) { 
					path = tupel.getInt("text_gr_path");
					if (praWo.getChapter().getDB_ID() == chapter
							&& startPosition_now == start
							&& endPosition_now == end) {
						try {
							dbc.open();
							ConstitutiveWord cw = dbc.loadIllocutionUnitRoots(
									praWo.getChapter())
									.getConstitutiveWordAtPosition(
											startPosition_now);
							// ist der Wort ein Verb und handelt es sich um
							// genau die selbe
							// Instanz des Wortes, das gerade analysiert wird
							// muss auch in der Textgrammatik nach einem Pfad
							// geschaut werden
							getTextGrPath(cw, kind, level);
							dbc.close();
						} catch (DBC_ConnectionException e) {
							e.printStackTrace();
							dbc.close();
						} catch (Exception e) {
							e.printStackTrace();
							dbc.close();
						}
					}
					break;
				case TR_Assignation.WORDCLASS_ADJECTIVE:
					path = tupel.getInt("sem_path");
					if (praWo.getChapter().getDB_ID() == chapter
							&& startPosition_now == start
							&& endPosition_now == end) {
						try {
							// ist der Wort ein Adjektiv und handelt es sich um
							// genau die selbe
							// Instanz des Wortes, das gerade analysiert wird
							// muss auch in der Semantik nach einem Pfad
							// geschaut werden
							dbc.open();
							ConstitutiveWord cw = dbc.loadIllocutionUnitRoots(
									praWo.getChapter())
									.getConstitutiveWordAtPosition(
											startPosition_now);
							getSemantikPath(cw, kind, level);
							dbc.close();
						} catch (DBC_ConnectionException e) {
							e.printStackTrace();
							dbc.close();
						} catch (Exception e) {
							e.printStackTrace();
							dbc.close();
						}
					}
					break;
				default:
					if (praWo.getChapter().getDB_ID() == chapter
							&& startPosition_now == start
							&& endPosition_now == end) {
						try {
							// ist die Wortart unbekann und handelt es sich um
							// genau die selbe
							// Instanz des Wortes, das gerade analysiert wird
							// muss auch in der Textgrammatik und der Semantik
							// nach einem Pfad geschaut werden
							dbc.open();
							ConstitutiveWord cw = dbc.loadIllocutionUnitRoots(
									praWo.getChapter())
									.getConstitutiveWordAtPosition(
											startPosition_now);
							getTextGrPath(cw, kind, level);
							getSemantikPath(cw, kind, level);
							dbc.close();
						} catch (DBC_ConnectionException e) {
							e.printStackTrace();
							dbc.close();
						} catch (Exception e) {
							e.printStackTrace();
							dbc.close();
						}
					}
					break;
				}
				pathString = getFullPathString(path);
				// neues PragmatischesWort mit den ausgelesenen Daten erzeugen
				newPW = new PragmatischesWort(content, chapter, start, end,
						kind, level, path, pathString);
				if (praWo.getChapter().getDB_ID() == chapter
						&& startPosition_now == start && endPosition_now == end) {
					// handelt es sich um genau die selbe Instanz wird THIS in
					// der Proposal-Liste angezeigt
					name = "[THIS] " + newPW.getContent() + " [Pos.: "
							+ +newPW.getStart() + "]";
				} else if (praWo.getChapter().getDB_ID() == chapter) {
					// ist das Wort im selben Kapitel wird CHAP in der
					// Proposal-Liste angezeigt
					name = "[CHAP] " + newPW.getContent() + " [Pos.: "
							+ +newPW.getStart() + "]";
				} else {
					// ansonsten wird DB in der Proposal-Liste angezeigt
					name = "[DB] " + newPW.getContent() + " [Pos.: "
							+ newPW.getStart() + "]";
				}
				// PragmatischesWort und angezeigten Listeneintrag merken
				possibilities.add(name);
				namePW.put(name, newPW);
			}
		}
	}

	/**
	 * Die zugehörige Checking-Instanz zu dem konstitutiven Wort suchen,
	 * den Pfad auslesen und einen Eintrag in die Vorschlagsliste machen.
	 * 
	 * @param cw
	 *            das konstitutive Wort
	 * @param kind
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param level
	 *            die Abstraktheit (LEXPRAG_??)
	 */
	@SuppressWarnings("unchecked")
	private void getTextGrPath(ConstitutiveWord cw, byte kind, int level) {
		MeaningUnit mu = cw.getMeaningUnit();
		IllocutionUnitRoot uir = cw.getRoot();
		Checking checking = uir.getChecking(mu);
		int text_gr_Path = 0;
		if (checking != null) {
			text_gr_Path = checking.getPath();
			if (text_gr_Path != 0) {
				String text_gr_PathString = getFullPathString(text_gr_Path);
				// PragmatischesWort erzeugen
				PragmatischesWort newPW = new PragmatischesWort(
						cw.getContent(), praWo.getChapter().getDB_ID(), cw
								.getStartPosition(), cw.getEndPosition(), kind,
						level, text_gr_Path, text_gr_PathString);
				// es wird TEGR in der Proposal-Liste angezeigt
				String name = "[TEGR] " + newPW.getContent() + " ["
						+ newPW.getStart() + "]";
				// PragmatischesWort und angezeigten Listeneintrag merken
				possibilities.add(name);
				namePW.put(name, newPW);
			}
		}
	}

	/**
	 * Die zugehörigen Sememe-Group-Instanzen zu dem konstitutiven Wort
	 * suchen, den Pfad auslesen und einen Eintrag in die Vorschlagsliste
	 * machen.
	 * 
	 * @param cw
	 * @param kind
	 * @param level
	 */
	@SuppressWarnings("unchecked")
	private void getSemantikPath(ConstitutiveWord cw, byte kind, int level) {
		MeaningUnit mu = cw.getMeaningUnit();
		Vector sememeGroups = null;
		if (mu != null) {
			sememeGroups = mu.getSememeGroups();
			if (sememeGroups != null) {
				for (int k = 0; k < sememeGroups.size(); k++) {
					int semPath = ((SememeGroup) sememeGroups.get(k)).getPath();
					String semPathString = getFullPathString(semPath);
					if (semPath != 0) {
						// PragmatischesWort erzeugen
						PragmatischesWort newPW = new PragmatischesWort(cw
								.getContent(), praWo.getChapter().getDB_ID(),
								cw.getStartPosition(), cw.getEndPosition(),
								kind, level, semPath, semPathString);
						// es wird SEM in der Proposal-Liste angezeigt
						String name = "[SEM] " + newPW.getContent() + " ["
								+ newPW.getStart() + "]";
						// PragmatischesWort und angezeigten Listeneintrag
						// merken
						possibilities.add(name);
						namePW.put(name, newPW);
					}
				}
			}
		}
	}

	/**
	 * Zur angegebenen ID wird der zugehörige komplette Pfad
	 * zurückgegeben. Der Pfad wird zur besseren Lesbarkeit mit eckigen
	 * Klammern versehen.
	 * 
	 * @param id
	 *            die ID eines Knotens aus dem Pfadbaum
	 * @return der komplette Pfad
	 */
	public String getFullPathString(int id) {
		String fullPathString = "[Path]";
		int startID = id;
		while (id != 0) {
			// solange die ID nicht 0 ist, wird für jeden Knoten,
			// über den der Pfad
			// läuft [Knotenname] zu dem Pfadstring hinzugefügt
			if (id == startID) {
				fullPathString = "[" + praWo.getRoot().getNode(id).getName()
						+ "]";
			} else {
				fullPathString = "[" + praWo.getRoot().getNode(id).getName()
						+ "] " + fullPathString;
			}
			id = praWo.getRoot().getNode(id).getRoot().getId();
		}
		return fullPathString;
	}

	/**
	 * Die Funktion gibt für einen gegebenen Abstraktheitsgrad
	 * ConstitutiveWord.LEXPRAG_?? die passende Farbe zurück.
	 * 
	 * @param level
	 *            ein Abstraktheitsgrad ConstitutiveWord.LEXPRAG_??
	 * @return die entsprechende Farbe
	 */
	public Color getLevelColor(int level) {
		Color color = null;
		switch (level) {
		case ConstitutiveWord.LEXPRAG_E1:
			color = praWo.getE1Color();
			break;
		case ConstitutiveWord.LEXPRAG_E2:
			color = praWo.getE2Color();
			break;
		case ConstitutiveWord.LEXPRAG_E3:
			color = praWo.getE3Color();
			break;
		case ConstitutiveWord.LEXPRAG_P1:
			color = praWo.getP1Color();
			break;
		case ConstitutiveWord.LEXPRAG_P2:
			color = praWo.getP2Color();
			break;
		case ConstitutiveWord.LEXPRAG_P3:
			color = praWo.getP3Color();
			break;
		case ConstitutiveWord.LEXPRAG_A1:
			color = praWo.getA1Color();
			break;
		case ConstitutiveWord.LEXPRAG_A2:
			color = praWo.getA2Color();
			break;
		case ConstitutiveWord.LEXPRAG_A3:
			color = praWo.getA3Color();
			break;
		default:
			color = praWo.getOtherColor();
			break;
		}
		return color;
	}

	/**
	 * In den Analyseergebnissen schauen, ob ein Wort mit dem selben Inhalt
	 * schon einmal analysiert wurde und in die Vorschlägeliste
	 * einfügen.
	 */
	@SuppressWarnings("unchecked")
	private void isInAnalyse() {
		// alle Wörter mit dem selben Inhalt holen
		Hashtable list = (Hashtable) allWords.get(content_now);
		if (list != null) {
			Enumeration keys = list.keys();
			// für jedes Wort einen Eintrag in die Liste machen
			while (keys.hasMoreElements()) {
				PragmatischesWort pw = (PragmatischesWort) list.get(keys
						.nextElement());
				// es wird ANA in der Proposal-Liste angezeigt
				String name = "[ANA] " + pw.getContent() + " [Pos.: "
						+ pw.getStart() + "]";
				// PragmatischesWort und angezeigten Listeneintrag merken
				possibilities.add(name);
				namePW.put(name, pw);
			}
		}
	}

	/**
	 * Sucht in der Datenbank nach konstitutiven Wörtern mit dem Inhalt
	 * content und der Sprache language.
	 * 
	 * @param content
	 *            ein Wort
	 * @param language
	 *            eine Sprache
	 * @return Vector
	 */
	private Vector DBCgetConstitutiveWords(String content, String language) {
		Vector cws = null;
		try {
			dbc.open();
			cws = dbc.getConstitutiveWords(content, language);
			dbc.close();
		} catch (DBC_ConnectionException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(praWo.getFrame(), e.getMessage(),
					"DBC Error", JOptionPane.ERROR_MESSAGE);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(praWo.getFrame(), e.getMessage(),
					"DBC Error", JOptionPane.ERROR_MESSAGE);
			dbc.close();
		}
		return cws;
	}

	/**
	 * Wird aufgerufen, wenn einer der ToggleButtons gedrückt bzw. wenn ein
	 * neues PragmatischesWort erzeugt wurde, damit die Markierung im Textfeld
	 * vorgenommen wird.
	 * 
	 * @param kind
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param level
	 *            die Abstraktheit (LEXPRAG_??)
	 * @param oneword
	 *            TRUE,nur aktuelles Wort, FALSE alle Wörter
	 */
	public void switchColoring(TR_Assignation.Wordclass kind, int level, boolean oneword) {
		StyledDocument doc = praWo.getTextPane().getStyledDocument();
		Hashtable neededVector = null;
		String neededStyle = null;
		boolean selected = true;
		// zu Wortart und Abstraktheit, den passenden Stil und
		// den Vektor mit den passenden Wörtern wählen und
		// herausfinden, ob
		// der entsprechende ToggleButten selektiert ist
		switch (kind) {
		if(kind == TR_Assignation.Wordclass.NOUN) {
			switch (level) {
			case ConstitutiveWord.LEXPRAG_E1:
				neededVector = e1;
				neededStyle = "e1";
				selected = praWo.isSelectedE1();
				break;
			case ConstitutiveWord.LEXPRAG_E2:
				neededVector = e2;
				neededStyle = "e2";
				selected = praWo.isSelectedE2();
				break;
			case ConstitutiveWord.LEXPRAG_E3:
				neededVector = e3;
				neededStyle = "e3";
				selected = praWo.isSelectedE3();
				break;
			}
			break;
		} else if(kind == TR_Assignation.Wordclass.VERB) {
			switch (level) {
			case ConstitutiveWord.LEXPRAG_P1:
				neededVector = p1;
				neededStyle = "p1";
				selected = praWo.isSelectedP1();
				break;
			case ConstitutiveWord.LEXPRAG_P2:
				neededVector = p2;
				neededStyle = "p2";
				selected = praWo.isSelectedP2();
				break;
			case ConstitutiveWord.LEXPRAG_P3:
				neededVector = p3;
				neededStyle = "p3";
				selected = praWo.isSelectedP3();
				break;
			}
			break;
		} else if(kind == TR_Assignation.Wordclass.ADJECTIVE) {
			switch (level) {
			case ConstitutiveWord.LEXPRAG_A1:
				neededVector = a1;
				neededStyle = "a1";
				selected = praWo.isSelectedA1();
				break;
			case ConstitutiveWord.LEXPRAG_A2:
				neededVector = a2;
				neededStyle = "a2";
				selected = praWo.isSelectedA2();
				break;
			case ConstitutiveWord.LEXPRAG_A3:
				neededVector = a3;
				neededStyle = "a3";
				selected = praWo.isSelectedA3();
				break;
			}
//			break;
//		case TR_Assignation.WORDCLASS_UNKNOWN:
//			neededVector = otherWords;
//			neededStyle = "other";
//			selected = praWo.isSelectedOther();
//			break;
//		default:
//			neededStyle = "regular";
		}
		if (!selected) {
			// ist der ToggleButton nicht selektiert, dann ist der Stil
			// regular
			neededStyle = "regular";
		}
		if (!oneword) {
			// sollen alle Wörter des Vekors überprüft werden
			Enumeration neededVectorKeys = neededVector.keys();
			while (neededVectorKeys.hasMoreElements()) {
				// wird für jedes Wort der Stil entsprechend gesetzt
				PragmatischesWort pw = (PragmatischesWort) neededVector
						.get(neededVectorKeys.nextElement());
				try {
					String currentText = doc.getText(pw.getStart(), pw
							.getContent().length());
					doc.remove(pw.getStart(), pw.getContent().length());
					doc.insertString(pw.getStart(), currentText, doc
							.getStyle(neededStyle));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			// soll nur ein Wort genommen werden
			try {
				// so wird der Stil des aktuellen Wortes entsprechend
				// geändert
				String currentText = doc.getText(startPosition_now, content_now
						.length());
				doc.remove(startPosition_now, content_now.length());
				doc.insertString(startPosition_now, currentText, doc
						.getStyle(neededStyle));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Die Funktion erhält ein PragmatischesWort, das sie ausliest und im
	 * Dialog die entsprechenden Buttons selektiert, bzw. die Listen
	 * ausfüllt. Dabei wird überprüft, ob der Pfad gültig
	 * ist oder nicht und dieser dann entsprechend schwarz oder rot markiert.
	 * 
	 * @param pw
	 *            PragmatischesWort, das vom Benutzer selektiert wurde
	 */
	public void preselection(PragmatischesWort pw) {
		switch (pw.getKind()) {
		case TR_Assignation.WORDCLASS_NOUN:
			praWo.getDialog().getNomenRadio().setSelected(true);
			switch (pw.getLevel()) {
			case ConstitutiveWord.LEXPRAG_E1:
				praWo.getDialog().getE1Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_E2:
				praWo.getDialog().getE2Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_E3:
				praWo.getDialog().getE3Radio().setSelected(true);
				break;
			}
			if (isValidPath(TR_Assignation.WORDCLASS_NOUN, pw.getPath())) {
				praWo.getDialog().getPathPane().setForeground(Color.BLACK);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, true, false, false, true,
						true);
			} else {
				praWo.getDialog().getPathPane().setForeground(Color.RED);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, true, false, false, true,
						false);
			}
			break;
		case TR_Assignation.WORDCLASS_VERB:
			praWo.getDialog().getVerbRadio().setSelected(true);
			switch (pw.getLevel()) {
			case ConstitutiveWord.LEXPRAG_P1:
				praWo.getDialog().getP1Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_P2:
				praWo.getDialog().getP2Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_P3:
				praWo.getDialog().getP3Radio().setSelected(true);
				break;
			}
			if (isValidPath(TR_Assignation.WORDCLASS_VERB, pw.getPath())) {
				praWo.getDialog().getPathPane().setForeground(Color.BLACK);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, false, true, false, true,
						true);
			} else {
				praWo.getDialog().getPathPane().setForeground(Color.RED);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, false, true, false, true,
						false);
			}
			break;
		case TR_Assignation.WORDCLASS_ADJECTIVE:
			praWo.getDialog().getAdjektivRadio().setSelected(true);
			switch (pw.getLevel()) {
			case ConstitutiveWord.LEXPRAG_A1:
				praWo.getDialog().getA1Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_A2:
				praWo.getDialog().getA2Radio().setSelected(true);
				break;
			case ConstitutiveWord.LEXPRAG_A3:
				praWo.getDialog().getA3Radio().setSelected(true);
				break;
			}
			if (isValidPath(TR_Assignation.WORDCLASS_ADJECTIVE, pw.getPath())) {
				praWo.getDialog().getPathPane().setForeground(Color.BLACK);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, false, false, true, true,
						true);
			} else {
				praWo.getDialog().getPathPane().setForeground(Color.RED);
				praWo.getDialog().getPathPane().setText(pw.getPathString());
				praWo.getDialog().enableButtons(true, false, false, true, true,
						false);
			}
			break;
		case TR_Assignation.WORDCLASS_UNKNOWN:
			praWo.getDialog().getOtherRadio().setSelected(true);
			praWo.getDialog().getPathPane().setForeground(Color.BLACK);
			praWo.getDialog().getPathPane().setText("Valid Path.");
			praWo.getDialog().enableButtons(true, false, false, false, false,
					true);
			break;
		default:
			praWo.getDialog().getOtherRadio().setSelected(true);
			praWo.getDialog().getPathPane().setForeground(Color.BLACK);
			praWo.getDialog().getPathPane().setText("Valid Path.");
			praWo.getDialog().enableButtons(true, false, false, false, false,
					true);
			break;
		}
	}

	/**
	 * Fragt den Dialog welcher Wordclass-Radiobutton selektiert ist und gibt
	 * den entsprechenden Vektor mit den Wörtern des gewählten Typs
	 * zurück.
	 * 
	 * @return der Vektor mit Wörtern eines Typs
	 */
	public Hashtable getNeededWordclassVector() {
		Hashtable show = null;
		if (praWo.getDialog().getNomenRadio().isSelected()) {
			show = nomina;
		} else if (praWo.getDialog().getVerbRadio().isSelected()) {
			show = verba;
		} else if (praWo.getDialog().getAdjektivRadio().isSelected()) {
			show = adjektiva;
		} else if (praWo.getDialog().getOtherRadio().isSelected()) {
			show = otherWords;
		}
		return show;
	}

	/**
	 * Fragt den Dialog welcher Level-Radiobutton selektiert ist und gibt den
	 * entsprechenden Vektor mit den Wörtern des gewählten Typs
	 * zurück.
	 * 
	 * @return der Vektor mit Wörtern eines Typs
	 */
	public Hashtable getNeededLevelVector() {
		Hashtable show = null;
		if (praWo.getDialog().getNomenRadio().isSelected()) {
			if (praWo.getDialog().getE1Radio().isSelected()) {
				show = e1;
			} else if (praWo.getDialog().getE2Radio().isSelected()) {
				show = e2;
			} else if (praWo.getDialog().getE3Radio().isSelected()) {
				show = e3;
			}
		} else if (praWo.getDialog().getVerbRadio().isSelected()) {
			if (praWo.getDialog().getP1Radio().isSelected()) {
				show = p1;
			} else if (praWo.getDialog().getP2Radio().isSelected()) {
				show = p2;
			} else if (praWo.getDialog().getP3Radio().isSelected()) {
				show = p3;
			}
		} else if (praWo.getDialog().getAdjektivRadio().isSelected()) {
			if (praWo.getDialog().getA1Radio().isSelected()) {
				show = a1;
			} else if (praWo.getDialog().getA2Radio().isSelected()) {
				show = a2;
			} else if (praWo.getDialog().getA3Radio().isSelected()) {
				show = a3;
			}
		} else if (praWo.getDialog().getOtherRadio().isSelected()) {
			show = otherWords;
		}
		return show;
	}

	/**
	 * Holt aus der Datenbank alle konstitutiven Wörter des aktuellen
	 * Kapitels.
	 * 
	 * @return die konstitutiven Wörter des Kapitels oder null
	 */
	private Vector getCWs() {
		Chapter chapter = praWo.getChapter();
		Vector cws = null;
		if (chapter != null) {
			try {
				dbc.open();
				IllocutionUnitRoots uir = dbc.loadIllocutionUnitRoots(chapter);
				cws = uir.getConstitutiveWords();
				dbc.close();
			} catch (DBC_ConnectionException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(praWo.getFrame(), e.getMessage(),
						"DBC Error", JOptionPane.ERROR_MESSAGE);
				dbc.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(praWo.getFrame(), e.getMessage(),
						"DBC Error", JOptionPane.ERROR_MESSAGE);
				dbc.close();
			}
		}
		return cws;
	}

	/**
	 * Alle konstitutiven Wörter des Kapitels fett markieren.
	 */
	public void showCWs() {
		// die konstitutiven Wörter holen
		Vector cws = getCWs();
		if (cws != null) {
			// die ToggelButtons deselektieren
			praWo.setButtons(false);
			ConstitutiveWord cw = null;
			StyledDocument doc = praWo.getTextPane().getStyledDocument();
			// für jedes konstitutive Wort
			for (int i = 0; i < cws.size(); i++) {
				cw = (ConstitutiveWord) cws.get(i);
				try {
					// den Stil auf cws setzen (die Schrift wird fett angezeigt)
					String currentText = doc.getText(cw.getStartPosition(), cw
							.getContent().length());
					doc.remove(cw.getStartPosition(), cw.getContent().length());
					doc.insertString(cw.getStartPosition(), currentText, doc
							.getStyle("cws"));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Hebt die Abstraktheitsgrade farbig hervor.
	 */
	public void showLevels() {
		Hashtable cws = all;
		Enumeration cwsKey = all.keys();
		praWo.setButtons(true);
		if (cws != null) {
			String style = "regular";
			PragmatischesWort cw = null;
			int level = 0;
			StyledDocument doc = praWo.getTextPane().getStyledDocument();
			while (cwsKey.hasMoreElements()) {
				// für alle konstitutiven Wörter in der aktuellen
				// Analyse
				// wird der zugehörige Stil festgestellt
				cw = (PragmatischesWort) cws.get(cwsKey.nextElement());
				level = cw.getLevel();
				switch (level) {
				case ConstitutiveWord.LEXPRAG_E1:
					style = "e1";
					break;
				case ConstitutiveWord.LEXPRAG_E2:
					style = "e2";
					break;
				case ConstitutiveWord.LEXPRAG_E3:
					style = "e3";
					break;
				case ConstitutiveWord.LEXPRAG_P1:
					style = "p1";
					break;
				case ConstitutiveWord.LEXPRAG_P2:
					style = "p2";
					break;
				case ConstitutiveWord.LEXPRAG_P3:
					style = "p3";
					break;
				case ConstitutiveWord.LEXPRAG_A1:
					style = "a1";
					break;
				case ConstitutiveWord.LEXPRAG_A2:
					style = "a2";
					break;
				case ConstitutiveWord.LEXPRAG_A3:
					style = "a3";
					break;
				default:
					style = "other";
				}
				if (style.compareToIgnoreCase("regular") != 0) {
					try {
						// und, falls der Stil nicht regular ist
						// das Wort entsprechend markiert
						String currentText = doc.getText(cw.getStart(), cw
								.getContent().length());
						doc.remove(cw.getStart(), cw.getContent().length());
						doc.insertString(cw.getStart(), currentText, doc
								.getStyle(style));
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Alle Stile, die auf das Dokument im Textfeld angewendet wurden, werden
	 * entfernt.
	 */
	public void removeStyles() {
		// das aktuelle Kapitel holen
		Chapter chapter = praWo.getChapter();
		// das angezeigte Dokument holen
		StyledDocument doc = praWo.getTextPane().getStyledDocument();
		// die ToggleButtons deselektieren
		praWo.setButtons(false);
		try {
			// den Stil des kompletten Dokuments auf regular (Standard) setzen
			doc.remove(0, chapter.getContent().length());
			doc.insertString(0, chapter.getContent(), doc.getStyle("regular"));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Funktion überprüft, ob der gegebene Pfad für die
	 * gegebene Wortart gültig ist.
	 * 
	 * @param wordclass
	 *            die Wortart (TR_WORDCLASS_??)
	 * @param path
	 *            die ID eines Knotens aus dem Pfadbaum
	 * @return TRUE, fals der Pfad für die Wortart gültig ist,
	 *         andernfalls FALSE
	 */
	public boolean isValidPath(int wordclass, int path) {
		switch (wordclass) {
		case TR_Assignation.WORDCLASS_NOUN:
			if (path > 559 && path < 793) {
				return true;
			}
			return false;
		case TR_Assignation.WORDCLASS_VERB:
			if ((path > 467 && path < 560) || path > 792) {
				return true;
			}
			return false;
		case TR_Assignation.WORDCLASS_ADJECTIVE:
			if (path > 0 && path < 468) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}

	/**
	 * Lädt aus der Datenbank alle bisherigen Analyseergebnisse (mit
	 * Wortart, Abstraktheit und Pfad), wenn welche vorhanden sind, in die
	 * internen Ergebnislisten.<br>
	 * <br>
	 * Achtung: Diese Funktion überschreibt nicht gespeicherte
	 * Analyseergebnisse!
	 */
	public void getDBEntries() {
		// Die konstitutiven Bedeutungen des aktuellen Kapitels holen.
		Vector cws = getCWs();
		for (int i = 0; i < cws.size(); i++) {
			// Für jedes konstitutive Wort
			ConstitutiveWord cw = (ConstitutiveWord) cws.get(i);
			// Wortart, Abstraktheit und Pfad auslesen
			byte kind = (byte) cw.getAssignation().getWordclassesBinary();
			int level = cw.getLexpragLevel();
			int path = 0;
			// der Pfad ist abhängig von der Wortart zu suchen
			if (kind == TR_Assignation.WORDCLASS_NOUN) {
				path = cw.getLexpragPath();
			} else if (kind == TR_Assignation.WORDCLASS_VERB) {
				path = cw.getTextGrPath();
			} else if (kind == TR_Assignation.WORDCLASS_ADJECTIVE) {
				path = cw.getSemPath();
			}
			if (kind != 0 && level != 0) {
				// Wenn Wortart und Abstraktheit nicht leer sind,
				// werden alle weiteren Daten geladen
				content_now = cw.getContent();
				startPosition_now = cw.getStartPosition();
				endPosition_now = cw.getEndPosition();
				language_now = cw.getWord().getLanguage();
				chapterID_now = cw.getWord().getChapter().getDB_ID();
				String pathString = getFullPathString(path);
				// und eine neue PragmatischesWort-Instanz erzeugt,
				PragmatischesWort pw = new PragmatischesWort(content_now,
						chapterID_now, startPosition_now, endPosition_now,
						kind, level, path, pathString);
				// die in die entsprechenden Listen eingefügt werden muss.
				insertPwInLists(level, kind, pw);
			}
		}
	}

	/**
	 * Diese Funktion speichert alle Analyseergebnisse in die Datenbank.
	 */
	public void saveCurrentResults() {
		Enumeration allKeys = all.keys();
		ConstitutiveWord existingCW = null;
		Object key = null;
		String keyString = null;
		PragmatischesWort pw = null;
		try {
			// Verbindung zur Datenbank herstellen
			dbc.open();
			// und die IllocutionUnitRoots des Kapitels laden
			IllocutionUnitRoots iur = dbc.loadIllocutionUnitRoots(praWo
					.getChapter());
			while (allKeys.hasMoreElements()) {
				// solange noch ungespeicherte Daten da sind
				key = allKeys.nextElement();
				pw = (PragmatischesWort) all.get(key);
				keyString = (String) key;
				// nach existierendem ConstitutiveWord suchen
				existingCW = iur.getConstitutiveWordAtPosition(Integer
						.parseInt(keyString));
				if (existingCW != null) {
					if (existingCW.getContent().compareToIgnoreCase(
							pw.getContent()) == 0) {
						// existiert ein solches und stimmt der Inhalt
						// überein,
						// alle benötigten Daten aus PragmatischesWort
						// auslesen
						// und in ConstitutiveWord schreiben
						switch (pw.getKind()) {
						case TR_Assignation.WORDCLASS_NOUN:
							existingCW.setLexpragPath(pw.getPath());
							existingCW.getAssignation().setWordclasssBinary(pw.getKind());
							existingCW.setLexpragLevel(pw.getLevel());
							break;
						case TR_Assignation.WORDCLASS_VERB:
							existingCW.setTextGrPath(pw.getPath());
							existingCW.getAssignation().setWordclasssBinary(pw.getKind());
							existingCW.setLexpragLevel(pw.getLevel());
							break;
						case TR_Assignation.WORDCLASS_ADJECTIVE:
							existingCW.setSemPath(pw.getPath());
							existingCW.getAssignation().setWordclasssBinary(pw.getKind());
							existingCW.setLexpragLevel(pw.getLevel());
							break;
						case TR_Assignation.WORDCLASS_UNKNOWN:
							break;
						}

					} else {
						// stimmt der Inhalt nicht überein, den Benutzer
						// informieren
						JOptionPane.showMessageDialog(praWo.getFrame(),
								"Different content in DB "
										+ existingCW.getContent()
										+ " and in results " + pw.getContent(),
								"Could not save entry.",
								JOptionPane.ERROR_MESSAGE, null);
					}
				} else {
					// existiert kein ConstitutiveWord, so wird ein neues
					// erzeugt
					// und mit den vorhandenen Daten gefüllt
					IllocutionUnitRoot root = iur.getRoot(praWo.getChapter()
							.getTokenAtPosition(pw.getStart()));
					ConstitutiveWord newCW = new ConstitutiveWord(root, pw
							.getStart(), pw.getEnd());
					switch (pw.getKind()) {
					case TR_Assignation.WORDCLASS_NOUN:
						newCW.setLexpragPath(pw.getPath());
						newCW.setLexpragLevel(pw.getLevel());
						newCW.getAssignation().setWordclasssBinary(pw.getKind());
						break;
					case TR_Assignation.WORDCLASS_VERB:
						newCW.setTextGrPath(pw.getPath());
						newCW.setLexpragLevel(pw.getLevel());
						newCW.getAssignation().setWordclasssBinary(pw.getKind());
						break;
					case TR_Assignation.WORDCLASS_ADJECTIVE:
						newCW.setSemPath(pw.getPath());
						newCW.setLexpragLevel(pw.getLevel());
						newCW.getAssignation().setWordclasssBinary(pw.getKind());
						break;
					case TR_Assignation.WORDCLASS_UNKNOWN:
						newCW.getAssignation().setWordclasssBinary(pw.getKind());
						break;
					}
				}
			}
			// die gemachten &Auml;nderungen in der Datenbank abspeichern
			dbc.saveIllocutionUnitRoots(iur);
			praWo.setUnsaved(false);
			dbc.close();
		} catch (DBC_ConnectionException e) {
			e.printStackTrace();
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			dbc.close();
		}
	}

	/**
	 * Gibt die Hashtable zurück, die die Vorschläge
	 * (PragmatischesWort) auf die dem Benutzer angezeigten Vorschläge der
	 * Proposals-Liste mapped.
	 * 
	 * @return das Mapping zwischen PragmatischesWort und Listeneintrag
	 */
	public Hashtable getNamePW() {
		return namePW;
	}

	/**
	 * Gibt den aktuellen Analysemodus zurück.
	 * 
	 * @return der aktuelle Analysemodus
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Setzt den Analysemodus.
	 * 
	 * @param mode
	 *            der Analysemodus
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Gibt den Pfad zu der aktuellen Analyse zurück.
	 * 
	 * @return der aktuell gältige Pfad
	 */
	public int getValidPath() {
		return validPath;
	}

	/**
	 * Setzt den gültigen Pfad.
	 * 
	 * @param validPath
	 *            der gültige Pfad
	 */
	public void setValidPath(int validPath) {
		this.validPath = validPath;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad A1 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ A1
	 */
	public Hashtable getA1() {
		return a1;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad A2 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ A2
	 */
	public Hashtable getA2() {
		return a2;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad A3 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ A3
	 */
	public Hashtable getA3() {
		return a3;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad E1 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ E1
	 */
	public Hashtable getE1() {
		return e1;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad E2 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ E2
	 */
	public Hashtable getE2() {
		return e2;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad E3 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ E3
	 */
	public Hashtable getE3() {
		return e3;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad P1 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ P1
	 */
	public Hashtable getP1() {
		return p1;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad P2 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ P2
	 */
	public Hashtable getP2() {
		return p2;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse mit
	 * Abstraktheitsgrad P3 zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter vom Typ P3
	 */
	public Hashtable getP3() {
		return p3;
	}

	/**
	 * Gibt Hashtable mit allen Adjektiven der aktuellen Analyse zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die Adjektive
	 */
	public Hashtable getAdjektiva() {
		return adjektiva;
	}

	/**
	 * Gibt Hashtable mit allen Nomien der aktuellen Analyse zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die Nomen
	 */
	public Hashtable getNomina() {
		return nomina;
	}

	/**
	 * Gibt Hashtable mit allen Verben der aktuellen Analyse zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die Verben
	 */
	public Hashtable getVerba() {
		return verba;
	}

	/**
	 * Gibt Hashtable mit allen Wörter der aktuellen Analyse zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die (Teil-)Wörter
	 */
	public Hashtable getAll() {
		return all;
	}

	/**
	 * Gibt Hashtable mit allen Wörtern der aktuellen Analyse, die als
	 * Other eingestuft wurden, zurück.<br>
	 * <br>
	 * Key: String der Startposition des (Teil-)Wortes<br>
	 * <br>
	 * Value: PragmatischesWort des (Teil-)Wortes
	 * 
	 * @return die Wörter, die als Other eingestuft wurden
	 */
	public Hashtable getOtherWords() {
		return otherWords;
	}

	/**
	 * Gibt den Vektor mit den aktuell zu analysierenden Worten zurück.
	 * 
	 * @return die Wörter, die gerade analysiert werden sollen
	 */
	public Vector getV() {
		return v;
	}

	/**
	 * Setzt den Index des aktuell zu analysierenden Wortes.
	 * 
	 * @param index
	 *            der Index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}