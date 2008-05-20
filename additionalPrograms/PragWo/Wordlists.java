package additionalPrograms.PragWo;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse generiert die kleinen Dialoge zur Darstellung der
 *         Wortlisten.
 */
public class Wordlists {

	private PraWo praWo;

	/**
	 * Der Konstruktor bekommt die Zeiger auf die PraWo- und die
	 * Analyser-Instanz.
	 * 
	 * @param praWo
	 *            der Zeiger auf PraWo-Instanz
	 */
	public Wordlists(PraWo praWo) {
		this.praWo = praWo;
	}

	/**
	 * Die Funktion bekommt den Titel des Dialogs und die anzuzeigenenden
	 * Wörter übergeben und generiert daraus den Dialog.
	 * 
	 * @param title
	 *            der Titel des Dialogs
	 * @param words
	 *            die Wörter die angezeigt werden sollen
	 */
	@SuppressWarnings("unchecked")
	public void wordlistDialog(String title, Hashtable words) {
		JDialog wordlistDialog = new JDialog(praWo.getFrame(), title);
		Vector editedWords = new Vector();
		Enumeration wordsKeys = words.keys();
		while (wordsKeys.hasMoreElements()) {
			// für jedes Wort werden die Informationen ausgelesen
			// und daraus Listeneinträge erstellt
			PragmatischesWort word = (PragmatischesWort) words.get(wordsKeys
					.nextElement());
			String kind = null;
			String level = null;
			switch (word.getKind()) {
			case TR_Assignation.WORDCLASS_NOUN:
				kind = "noun";
				switch (word.getLevel()) {
				case ConstitutiveWord.LEXPRAG_E1:
					level = "E1";
					break;
				case ConstitutiveWord.LEXPRAG_E2:
					level = "E2";
					break;
				case ConstitutiveWord.LEXPRAG_E3:
					level = "E3";
					break;
				}
				break;
			case TR_Assignation.WORDCLASS_VERB:
				kind = "verb";
				switch (word.getLevel()) {
				case ConstitutiveWord.LEXPRAG_P1:
					level = "P1";
					break;
				case ConstitutiveWord.LEXPRAG_P2:
					level = "P2";
					break;
				case ConstitutiveWord.LEXPRAG_P3:
					level = "P3";
					break;
				}
				break;
			case TR_Assignation.WORDCLASS_ADJECTIVE:
				kind = "adjective";
				switch (word.getLevel()) {
				case ConstitutiveWord.LEXPRAG_A1:
					level = "A1";
					break;
				case ConstitutiveWord.LEXPRAG_A2:
					level = "A2";
					break;
				case ConstitutiveWord.LEXPRAG_A3:
					level = "A3";
					break;
				}
				break;
			}
			String wordInfo = word.getContent() + " " + kind + " " + level
					+ " " + word.getPath() + " (" + word.getStart() + "-"
					+ word.getEnd() + ")";
			editedWords.add(wordInfo);
		}
		if (editedWords.size() == 0) {
			String noWords = "There are no words in this category.";
			editedWords.add(noWords);
		}
		JList wordlist = new JList(editedWords);
		JScrollPane panel = new JScrollPane(wordlist);
		panel.setPreferredSize(new Dimension(300, 500));

		wordlistDialog.getContentPane().add(panel);
		wordlistDialog.pack();
		wordlistDialog.setVisible(true);
	}

}
