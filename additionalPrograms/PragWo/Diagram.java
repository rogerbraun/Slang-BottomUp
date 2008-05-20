package additionalPrograms.PragWo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 03.11.2005<br>
 *         <br>
 *         Diese Klasse ist zuständig für die Erzeugung der drei
 *         Grafiken. Sie erweitert die Klasse JComponent und implementiert die
 *         Funktion paintComponent.
 */
public class Diagram extends JComponent {

	// benötigt von JComponent
	private static final long serialVersionUID = 1L;

	// benötigte Klassenzeiger
	private Analyser analyser;

	private PraWo praWo;

	// Speicherung des Grafiktyps
	private int mode = 0;

	// die verschiedenen Grafiktypen
	/**
	 * Grafiktyp: Kuchendiagramm
	 */
	public final static int PIECHART = 1;

	/**
	 * Grafiktyp: Balkendiagramm
	 */
	public final static int BARCHART = 2;

	/**
	 * Grafiktyp: Dichtediagramm
	 */
	public final static int DENSITYCHART = 3;

	// Variablen die die Grafiken definieren
	private int[] angels;

	private Color[] colors;

	private int width = 0;

	private int height = 0;

	int border = 0;

	int bar = 0;

	int cell = 0;

	int partcell = 0;

	int column = 0;

	private int length;

	private int y;

	// die analysierten Worte
	private Hashtable analysedWords;

	/**
	 * Der Konstruktor bekommt die Zeiger auf die PraWo- und die
	 * Analyser-Instanz und den vom Benutzer gewählten Grafiktyp
	 * übergeben. Entsprechend des Grafiktyps werden die gewünschte
	 * Größe und weitere Parameter festgelegt.
	 * 
	 * @param analyser
	 *            der Zeiger auf Analyser-Instanz
	 * @param praWo
	 *            der Zeiger auf PraWo-Instanz
	 * @param mode
	 *            der Grafiktyp
	 */
	public Diagram(Analyser analyser, PraWo praWo, int mode) {
		this.analyser = analyser;
		this.praWo = praWo;
		this.mode = mode;
		switch (mode) {
		case PIECHART:
			// Größe
			width = 300;
			height = 300;
			break;
		case BARCHART:
			// Abstand zum Rand
			border = 50;
			// Breite eines Balkens
			bar = 3;
			// Höhe einer Zeile (Level 1, Level 2, Level 3)
			cell = 100;
			// die Verlängerung der Balken nach unten
			partcell = 10;
			// column gibt an wieviele Balken zwischen zwei Strichen des
			// Hintergrundrasters sind
			column = 10;
			// Breite der Grafik
			Vector allWords = praWo.getChapter().getWords();
			width = (int) (allWords.size() * bar * 1.25);
			// Höhe der Grafik
			height = 600;
			break;
		case DENSITYCHART:
			// Abstand zum Rand
			border = 50;
			// column gibt an wieviele Balken zwischen zwei Strichen des
			// Hintergrundrasters sind
			column = 10;
			// Breite eines Balkens
			bar = 3;
			// die Breite der Grafik
			analysedWords = analyser.getAll();
			width = (analysedWords.size() * bar) + (2 * border);
			// die Höhe der Grafik
			height = 600;
			break;
		}
	}

	/**
	 * &Uuml;berschriebene Funktion von JComponent. <br>
	 * Erzeugt die drei Grafiktypen aus den Analyseergebnissen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// paint background
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		switch (mode) {
		case PIECHART:
			// die Winkel berechnen
			getAngels();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			for (int i = 0; i < colors.length; i++) {
				// für jede Farbe das entsprechende Kreisstück
				// zeichnen
				g.setColor(colors[i]);
				g.fillArc(0, 0, width, height, angels[i], angels[i + 1]
						- angels[i]);
			}
			break;
		case BARCHART:
			// die analysierten Worte holen
			@SuppressWarnings("hiding") Hashtable analysedWords = analyser.getAll();
			if (analysedWords != null) {
				Enumeration num = analysedWords.keys();
				int[] keys = new int[analysedWords.size()];
				int counter = 0;
				while (num.hasMoreElements()) {
					// von allen analysierten Worten die Keys in ints wandeln
					keys[counter] = Integer.valueOf((String) num.nextElement())
							.intValue();
					counter++;
				}
				// alle Wörter aus dem Kapitel holen
				Vector allWords = praWo.getChapter().getWords();
				int[] words = new int[allWords.size()];
				for (int k = 0; k < allWords.size(); k++) {
					// und für jedes Wort die Startposition speichern
					int start_position = ((Word) allWords.get(k))
							.getStartPosition();
					words[k] = start_position;
				}
				Arrays.sort(words);
				Arrays.sort(keys);

				PragmatischesWort pw = null;
				int level = 0;
				Color color = null;
				int x = border + bar;
				int word_counter = 0;
				int key_counter = 0;
				int bar_counter = 0;

				// die Grundgrafik zeichnen
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, (allWords.size() * bar * 2) + 150, 700);
				g.setColor(Color.BLACK);
				g.drawString("Level of Abstraction", border, 30);
				g.drawString("Level 1", 5, border + (3 * cell));
				g.drawString("Level 2", 5, border + (2 * cell));
				g.drawString("Level 3", 5, border + (1 * cell));
				g.drawString("All Words", border, (height - border) + 35);

				// solange noch Wörter in der Liste der analysierten
				// Wörter sind
				while (key_counter < keys.length) {
					if (words[word_counter] < keys[key_counter]) {
						// ist die Position des aktuellen Wortes im Text
						// kleiner als die Position
						// des aktuellen analysierten Wortes (aktuelles Wort
						// ist Funktionswort),
						// so wird ein kurzer Balken in der Farbe für
						// Funtkionswörter gezeichnet.
						length = cell / 2;
						y = border + (3 * cell) + (cell / 2);
						g.setColor(praWo.getFunctionColor());
						g.fillRect(x, y, bar, length);
						x = x + bar;
						word_counter++;
					} else if (words[word_counter] > keys[key_counter]) {
						// ist die Position des aktuellen Wortes im Text
						// größer als die Position
						// des aktuellen analysierten Wortes (aktuelles
						// Wort ist konstitutives Teilwort und analysiert),
						// so wird ein Balken in der Farbe der Abstraktheit
						// und der Länge der Abstrakheit gezeichnet.
						pw = (PragmatischesWort) analysedWords.get(Integer
								.toString(keys[key_counter]));
						level = pw.getLevel();
						color = analyser.getLevelColor(level);
						getBarLength(level, key_counter);
						g.setColor(color);
						g.fillRect(x, y, bar, length);
						x = x + bar;
						key_counter++;
					} else {
						// ist die Position des aktuellen Wortes im Text
						// größer gleich der Position
						// des aktuellen analysierten Wortes (aktuelles
						// Wort ist konstitutives Wort und analysiert),
						// so wird ein Balken in der Farbe der Abstraktheit
						// und der Länge der Abstrakheit gezeichnet.
						pw = (PragmatischesWort) analysedWords.get(Integer
								.toString(keys[key_counter]));
						level = pw.getLevel();
						color = analyser.getLevelColor(level);
						getBarLength(level, key_counter);
						g.setColor(color);
						g.fillRect(x, y, bar, length);
						x = x + bar;
						key_counter++;
						word_counter++;
					}
					bar_counter++;
					// das Hintergrundraster Zeichnen
					if ((bar_counter % column) == 0) {
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(x, border, x, height - border);
					}
					if ((bar_counter % (2 * column)) == 0) {
						g.setColor(Color.BLACK);
						// g.drawLine(x, border, x, height-border);
						g.drawString(Integer.toString(bar_counter), x,
								(height - border) + 20);
					}
				}

				// Umrandung zeichnen
				g.setColor(Color.BLACK);
				// links
				g.drawLine(border, border, border, height - border);
				// unten
				g.drawLine(border, height - border, x + bar, height - border);
				// oben
				g.drawLine(border, border, x + bar, border);
				// rechts
				g.drawLine(x + bar, border, x + bar, height - border);

			}
			break;
		case DENSITYCHART:
			// Grundgrafik zeichnen
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width + 100, height + 100);
			g.setColor(Color.WHITE);
			g.drawString("Density Chart", border, 30);
			g.drawString("1", 30, (height - border) - (1 * 50));
			g.drawString("2", 30, (height - border) - (2 * 50));
			g.drawString("3", 30, (height - border) - (3 * 50));
			g.drawString("4", 30, (height - border) - (4 * 50));
			g.drawString("5", 30, (height - border) - (5 * 50));
			g.drawString("6", 30, (height - border) - (6 * 50));
			g.drawString("7", 30, (height - border) - (7 * 50));
			g.drawString("8", 30, (height - border) - (8 * 50));
			g.drawString("9", 30, (height - border) - (9 * 50));
			g.drawString("Constitutive Words", border, (height - border) + 35);
			g.setColor(Color.RED);
			g.drawString("Level 1", border, (height - border) + 60);
			g.setColor(Color.YELLOW);
			g.drawString("Level 2", border + 60, (height - border) + 60);
			g.setColor(Color.BLUE);
			g.drawString("Level 3", border + 120, (height - border) + 60);
			g.setColor(Color.MAGENTA);
			g.drawString("no level", border + 180, (height - border) + 60);
			// analysierte Worte holen
			analysedWords = analyser.getAll();
			if (analysedWords != null) {
				// Hintergrundraster zeichnen
				g.setColor(Color.LIGHT_GRAY);
				for (int i = 0; i <= analysedWords.size(); i++) {
					if ((i % column) == 0) {
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(border + (i * bar), border, border
								+ (i * bar), height - border);
					}
					if ((i % (2 * column)) == 0) {
						g.setColor(Color.WHITE);
						g.drawString(Integer.toString(i), border + (i * bar),
								(height - border) + 20);
					}
				}
				// die analysierten Worte nach Position im Text sortieren
				Enumeration num = analysedWords.keys();
				int[] keys = new int[analysedWords.size()];
				int counter = 0;
				while (num.hasMoreElements()) {
					keys[counter] = Integer.valueOf((String) num.nextElement())
							.intValue();
					counter++;
				}
				Arrays.sort(keys);
				// die Koordinaten der Startposition für die drei
				// Abstraktheitslevel initialisieren
				int x1_1 = border;
				int y1_1 = height - border;
				int x2_1 = 0;
				int y2_1 = 0;
				int x1_2 = border;
				int y1_2 = height - border;
				int x2_2 = 0;
				int y2_2 = 0;
				int x1_3 = border;
				int y1_3 = height - border;
				int x2_3 = 0;
				int y2_3 = 0;
				int x1_4 = border;
				int y1_4 = height - border;
				int x2_4 = 0;
				int y2_4 = 0;
				for (int i = 0; i < keys.length; i++) {
					// für jedes analysierte Wort, die
					// Abstraktheitslevel der fünf constitutiven
					// Wörter
					// davor und der vier danach zählen
					int level = 0;
					int[] levels = new int[4];
					levels[0] = 0;
					levels[1] = 0;
					levels[2] = 0;
					levels[3] = 0;
					for (int k = 5; k > 0; k--) {
						if (i - k >= 0) {
							level = ((PragmatischesWort) analysedWords
									.get(Integer.toString(keys[i - k])))
									.getLevel();
							if (level == ConstitutiveWord.LEXPRAG_E1
									|| level == ConstitutiveWord.LEXPRAG_P1
									|| level == ConstitutiveWord.LEXPRAG_A1) {
								levels[0]++;
							} else if (level == ConstitutiveWord.LEXPRAG_E2
									|| level == ConstitutiveWord.LEXPRAG_P2
									|| level == ConstitutiveWord.LEXPRAG_A2) {
								levels[1]++;
							} else if (level == ConstitutiveWord.LEXPRAG_E3
									|| level == ConstitutiveWord.LEXPRAG_P3
									|| level == ConstitutiveWord.LEXPRAG_A3) {
								levels[2]++;
							} else {
								levels[3]++;
							}
						}
					}
					for (int k = 0; k < 5; k++) {
						if (i + k < keys.length) {
							level = ((PragmatischesWort) analysedWords
									.get(Integer.toString(keys[i + k])))
									.getLevel();
							if (level == ConstitutiveWord.LEXPRAG_E1
									|| level == ConstitutiveWord.LEXPRAG_P1
									|| level == ConstitutiveWord.LEXPRAG_A1) {
								levels[0]++;
							} else if (level == ConstitutiveWord.LEXPRAG_E2
									|| level == ConstitutiveWord.LEXPRAG_P2
									|| level == ConstitutiveWord.LEXPRAG_A2) {
								levels[1]++;
							} else if (level == ConstitutiveWord.LEXPRAG_E3
									|| level == ConstitutiveWord.LEXPRAG_P3
									|| level == ConstitutiveWord.LEXPRAG_A3) {
								levels[2]++;
							} else {
								levels[3]++;
							}
						}
					}
					// die Kurven in die Grafik zeichnen

					// Level 1
					// vertikale linie
					x2_1 = x1_1;
					y2_1 = (height - border) - (levels[0] * 50);
					g.setColor(Color.RED);
					g.drawLine(x1_1, y1_1, x2_1, y2_1);

					// horizontale linie
					x1_1 = x2_1 + bar;
					y1_1 = y2_1;
					g.setColor(Color.RED);
					g.drawLine(x1_1, y1_1, x2_1, y2_1);

					// Level 2
					// vertikale linie
					x2_2 = x1_2;
					y2_2 = (height - border) - (levels[1] * 50);
					g.setColor(Color.YELLOW);
					g.drawLine(x1_2, y1_2, x2_2, y2_2);

					// horizontale linie
					x1_2 = x2_2 + bar;
					y1_2 = y2_2;
					g.setColor(Color.YELLOW);
					g.drawLine(x1_2, y1_2, x2_2, y2_2);

					// Level 3
					// vertikale linie
					x2_3 = x1_3;
					y2_3 = (height - border) - (levels[2] * 50);
					g.setColor(Color.BLUE);
					g.drawLine(x1_3, y1_3, x2_3, y2_3);

					// horizontale linie
					x1_3 = x2_3 + bar;
					y1_3 = y2_3;
					g.setColor(Color.BLUE);
					g.drawLine(x1_3, y1_3, x2_3, y2_3);

					// konstiutive Wörter, die weder Nomen noch Verb
					// noch Adjektiv sind
					// vertikale linie
					x2_4 = x1_4;
					y2_4 = (height - border) - (levels[3] * 50);
					g.setColor(Color.MAGENTA);
					g.drawLine(x1_4, y1_4, x2_4, y2_4);

					// horizontale linie
					x1_4 = x2_4 + bar;
					y1_4 = y2_4;
					g.setColor(Color.MAGENTA);
					g.drawLine(x1_4, y1_4, x2_4, y2_4);
				}
			}

			// Die Umrandung zeichnen.
			g.setColor(Color.WHITE);
			// links
			g.drawLine(border, border, border, height - border);
			// unten
			g
					.drawLine(border, height - border, width - border, height
							- border);
			// oben
			g.drawLine(border, border, width - border, border);
			// rechts
			g.drawLine(width - border, border, width - border, height - border);
			break;
		}
	}

	/**
	 * Diese Funktion berechnet auf Grund der Anzahl der Wörter im Kapitel
	 * und der Anzahl der Wörter für die verschiedenen Wortarten und
	 * Abstraktheitslevel die zugehörigen Kreissegmente und speichert die
	 * passende Farbe.<br>
	 * <br>
	 * winkel = vorherigerWinkel+((wortzahl * 360)/gesamtwortzahl)
	 */
	private void getAngels() {
		angels = new int[12];
		colors = new Color[11];
		int words = praWo.getChapter().getWords().size();
		angels[0] = 0;
		colors[0] = praWo.getE1Color();
		angels[1] = (int) (angels[0] + Math
				.floor((analyser.getE1().size() * 360) / words));
		colors[1] = praWo.getE2Color();
		angels[2] = (int) (angels[1] + Math
				.floor((analyser.getE2().size() * 360) / words));
		colors[2] = praWo.getE3Color();
		angels[3] = (int) (angels[2] + Math
				.floor((analyser.getE3().size() * 360) / words));
		colors[3] = praWo.getP1Color();
		angels[4] = (int) (angels[3] + Math
				.floor((analyser.getP1().size() * 360) / words));
		colors[4] = praWo.getP2Color();
		angels[5] = (int) (angels[4] + Math
				.floor((analyser.getP2().size() * 360) / words));
		colors[5] = praWo.getP3Color();
		angels[6] = (int) (angels[5] + Math
				.floor((analyser.getP3().size() * 360) / words));
		colors[6] = praWo.getA1Color();
		angels[7] = (int) (angels[6] + Math
				.floor((analyser.getA1().size() * 360) / words));
		colors[7] = praWo.getA2Color();
		angels[8] = (int) (angels[7] + Math
				.floor((analyser.getA2().size() * 360) / words));
		colors[8] = praWo.getA3Color();
		angels[9] = (int) (angels[8] + Math
				.floor((analyser.getA3().size() * 360) / words));
		colors[9] = praWo.getOtherColor();
		angels[10] = (int) (angels[8] + Math.floor((analyser.getOtherWords()
				.size() * 360)
				/ words));
		colors[10] = Color.BLACK;
		angels[11] = 360;
	}

	/**
	 * Die Funktion berechnet die Länge eines Balkens abhängig von der
	 * Abstraktheit und der Anzahl der bisherigen Balken.<br>
	 * <br>
	 * &Uuml;ber der Nulllinie gibt es drei Längen (Level 1, Level 2, Level
	 * 3) unter der Nulllinie gibt es 10 Längen die durch Modulorechnung
	 * mit counter berechnet wird.
	 * 
	 * @param level
	 *            die Abstraktheit (LEXPRAG_??)
	 * @param counter
	 *            der wievielte Balken dies ist
	 */
	private void getBarLength(int level, int counter) {
		length = 0;
		y = 0;
		int offset = (counter % 10);
		if (level == ConstitutiveWord.LEXPRAG_E1
				|| level == ConstitutiveWord.LEXPRAG_P1
				|| level == ConstitutiveWord.LEXPRAG_A1) {
			length = (1 * cell) + (offset * partcell);
			y = border + (3 * cell);
		} else if (level == ConstitutiveWord.LEXPRAG_E2
				|| level == ConstitutiveWord.LEXPRAG_P2
				|| level == ConstitutiveWord.LEXPRAG_A2) {
			length = (2 * cell) + (offset * partcell);
			y = border + (2 * cell);
		} else if (level == ConstitutiveWord.LEXPRAG_E3
				|| level == ConstitutiveWord.LEXPRAG_P3
				|| level == ConstitutiveWord.LEXPRAG_A3) {
			length = (3 * cell) + (offset * partcell);
			y = border + (1 * cell);
		} else {
			length = (cell / 2);
			y = border + (3 * cell) + length;
		}
	}

	/**
	 * &Uuml;berschriebene Funktion von JComponent. Gibt die gewünschte
	 * Größe zurück.
	 * 
	 * @return die gewünschte Größe
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	/**
	 * &Uuml;berschriebene Funktion von JComponent. Gibt die
	 * Mindestgröße zurück.
	 * 
	 * @return die Mindestgröße
	 */
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}
}
