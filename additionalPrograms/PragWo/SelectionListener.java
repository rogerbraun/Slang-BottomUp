package additionalPrograms.PragWo;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import connection.DBC;
import connection.DBC_ConnectionException;
import data.ConstitutiveWord;
import data.Token;
import data.Word;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse überwacht das Textfeld, falls der Benutzer den
 *         Modus SELECTEDMODE gewählt hat und bereitet den Dialog dann
 *         für das selektierte Wort vor.
 */
public class SelectionListener implements CaretListener {

	private PraWo praWo;
	
	private DBC dbc;

	private Analyser analyser;

	/**
	 * Der Konstruktor bekommt die Zeiger auf die PraWo- und die
	 * Analyser-Instanz.
	 * 
	 * @param praWo
	 *            der Zeiger auf PraWo-Instanz
	 * @param analyser
	 *            der Zeiger auf Analyser-Instanz
	 */
	public SelectionListener(PraWo praWo, Analyser analyser) {
		this.praWo = praWo;
		this.analyser = analyser;
		dbc = praWo.getDbc();
	}

	/**
	 * Implementierte Funktion von CaretListener. <br>
	 * Diese Funktion überwacht, ob der Benutzer im Textfeld etwas
	 * selektiert hat.
	 */
	public void caretUpdate(CaretEvent e) {
		int begin = e.getMark();
		int end = e.getDot();
		// ist SELECTEDMODE gewählt worden
		if (analyser.getMode() == analyser.SELECTEDMODE) {
			// dann wird das erste selektierte (konstitutive) Wort gesucht und
			// dem Dialog übergeben
			if (begin < end) {
				getSelection(begin, end);
			} else if (end < begin) {
				getSelection(end, begin);
			}
		}
	}

	/**
	 * Diese Funktion sucht das passende (konstitutive) Wort und übergibt
	 * es dem Dialog.
	 * 
	 * @param begin
	 *            der Anfang der Selektion
	 * @param end
	 *            das Ende der Selektion
	 */
	private void getSelection(int begin, int end) {
		ConstitutiveWord cw = null;
		String content_now;
		int startPosition_now;
		int endPosition_now;
		String language_now;
		int chapterID_now;
		Token isotopie_token;
		// zunächst wird nach einem konstitutiven Wort innerhalb der
		// Selektion gesucht
		try {
			dbc.open();
			cw = dbc.loadIllocutionUnitRoots(praWo.getChapter())
					.getConstitutiveWordAtPosition(begin);
			dbc.close();
		} catch (DBC_ConnectionException e1) {
			e1.printStackTrace();
			dbc.close();
		} catch (Exception e2) {
			e2.printStackTrace();
			dbc.close();
		}
		if (cw == null) {
			// gibt es kein konstitutives Wort innerhalb der Selektion,
			// wird das erste Wort genommen
			int[] index = praWo.getChapter().getIndexSequence(begin, end);
			try {
				isotopie_token = praWo.getChapter().getTokenAtIndex(index[0]);
				if (isotopie_token != null) {
					// alle benötigten Daten werden ausgelesen und an den
					// Dialog übergeben
					Word token = (Word) isotopie_token;
					content_now = token.getContent();
					startPosition_now = token.getStartPosition();
					endPosition_now = token.getEndPosition();
					language_now = token.getLanguage();
					chapterID_now = token.getChapter().getDB_ID();
					if (end < endPosition_now) {
						String short_content = content_now.substring(0, end
								- begin);
						analyser.runSelected(short_content, startPosition_now,
								end - 1, language_now, chapterID_now,
								isotopie_token);
					} else {
						analyser.runSelected(content_now, startPosition_now,
								endPosition_now, language_now, chapterID_now,
								isotopie_token);
					}
				}
			} catch (ClassCastException e1) {
				System.out.println("Catched Class Exception.");
			}
		} else {
			// es gibt ein konstitutives Wort
			// also werden alle benötigten Daten ausgelesen und an den
			// Dialog übergeben
			content_now = cw.getContent();
			startPosition_now = cw.getStartPosition();
			endPosition_now = cw.getEndPosition();
			language_now = cw.getWord().getLanguage();
			chapterID_now = cw.getWord().getChapter().getDB_ID();
			isotopie_token = cw.getWord();
			analyser.runSelected(content_now, startPosition_now,
					endPosition_now, language_now, chapterID_now,
					isotopie_token);
		}
	}

}
