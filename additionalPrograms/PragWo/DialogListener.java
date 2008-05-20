package additionalPrograms.PragWo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.pathselector.PathNode;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse verarbeitet alle Benutzereingaben, die über den
 *         Dialog gemacht werden.
 */
public class DialogListener implements ActionListener, ListSelectionListener {

	private PraWo praWo;

	private Analyser analyser;

	private Dialog dialog;

	/**
	 * Der Konstruktor bekommt die Zeiger auf die Dialog-, PraWo- und die
	 * Analyser-Instanz.
	 * 
	 * @param dialog
	 *            der Zeiger auf Analyser-Instanz
	 * @param praWo
	 *            der Zeiger auf PraWo-Instanz
	 * @param analyser
	 *            der Zeiger auf Analyser-Instanz
	 */
	public DialogListener(Dialog dialog, PraWo praWo, Analyser analyser) {
		this.dialog = dialog;
		this.praWo = praWo;
		this.analyser = analyser;
	}

	/**
	 * Implementierte Funktion von ActionListener. <br>
	 * Diese Funktion überwacht alle Knöpfe des Dialogs und reagiert
	 * auf die Benutzereingaben.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareToIgnoreCase("Nomen") == 0) {
			// der Togglebutton Nomen wurde ausgewählt
			if (dialog.getE1Radio().isSelected()
					|| dialog.getE2Radio().isSelected()
					|| dialog.getE3Radio().isSelected()) {
				// einer der drei Togglebuttons E1, E2, E3 ist selektiert
				// also, kann der Path-Button aktiviert werden
				dialog.enableButtons(true, true, false, false, true, false);
			} else {
				// keiner der drei Togglebuttons E1, E2, E3 ist selektiert
				// also, kann der Path-Button nicht aktiviert werden
				dialog.enableButtons(true, true, false, false, false, false);
			}
			// es ist noch kein Pfad ausgewählt worden
			analyser.setValidPath(-1);
			praWo.getDialog().getPathPane().setForeground(Color.RED);
			dialog.getPathPane().setText(
					"Path must begin with [Lexempragmatik]");
		} else if (e.getActionCommand().compareToIgnoreCase("Verb") == 0) {
			// der Togglebutton Verb wurde ausgewählt
			if (dialog.getP1Radio().isSelected()
					|| dialog.getP2Radio().isSelected()
					|| dialog.getP3Radio().isSelected()) {
				// einer der drei Togglebuttons P1, P2, P3 ist selektiert
				// also, kann der Path-Button aktiviert werden
				dialog.enableButtons(true, false, true, false, true, false);
			} else {
				// keiner der drei Togglebuttons P1, P2, P3 ist selektiert
				// also, kann der Path-Button nicht aktiviert werden
				dialog.enableButtons(true, false, true, false, false, false);
			}
			// es ist noch kein Pfad ausgewählt worden
			analyser.setValidPath(-1);
			praWo.getDialog().getPathPane().setForeground(Color.RED);
			dialog
					.getPathPane()
					.setText(
							"Path must not begin with [Lexempragmatik] or [Adjunktion]");
		} else if (e.getActionCommand().compareToIgnoreCase("Adjektiv") == 0) {
			// der Togglebutton Adjektiv wurde ausgewählt
			if (dialog.getA1Radio().isSelected()
					|| dialog.getA2Radio().isSelected()
					|| dialog.getA3Radio().isSelected()) {
				// einer der drei Togglebuttons A1, A2, A3 ist selektiert
				// also, kann der Path-Button aktiviert werden
				dialog.enableButtons(true, false, false, true, true, false);
			} else {
				// keiner der drei Togglebuttons A1, A2, A3 ist
				// selektiert
				// also, kann der Path-Button nicht aktiviert werden
				dialog.enableButtons(true, false, false, true, false, false);
			}
			// es ist noch kein Pfad ausgewählt worden
			analyser.setValidPath(-1);
			praWo.getDialog().getPathPane().setForeground(Color.RED);
			dialog.getPathPane().setText("Path must begin with [Adjunktion]");
		} else if (e.getActionCommand().compareToIgnoreCase("Other") == 0) {
			// der Togglebutton Other wurde ausgewählt
			// also muss kein Level gewählt werden, der Pfad ist
			// gültig
			// und der Ok- und Transfer-Button können aktiviert
			// werden
			dialog.enableButtons(true, false, false, false, false, true);
			analyser.setValidPath(-1);
			praWo.getDialog().getPathPane().setForeground(Color.BLACK);
			dialog.getPathPane().setText("Valid Path.");
		} else if (e.getActionCommand().compareToIgnoreCase("FW") == 0) {
			// der Togglebutton FW wurde ausgewählt
			// also muss kein Level gewählt werden, der Pfad
			// ist gültig
			// und der Ok- und Transfer-Button können
			// aktiviert werden
			dialog.enableButtons(true, false, false, false, false, true);
			analyser.setValidPath(-1);
			praWo.getDialog().getPathPane().setForeground(Color.BLACK);
			dialog.getPathPane().setText("Valid Path.");
		} else if (e.getActionCommand().compareToIgnoreCase("E1-INFO") == 0) {
			// der E1-Toggelbutton ist selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also können
				// der Ok- und Transfer-Button aktiviert werden
				dialog.enableButtons(true, true, false, false, true, true);
			} else {
				// der Pfad ist ungültig, also können
				// der Ok- und Transfer-Button nicht aktiviert
				// werden
				dialog.enableButtons(true, true, false, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("E2-INFO") == 0) {
			// der E2-Toggelbutton ist selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also können
				// der Ok- und Transfer-Button aktiviert
				// werden
				dialog.enableButtons(true, true, false, false, true, true);
			} else {
				// der Pfad ist ungültig, also
				// können der Ok- und Transfer-Button
				// nicht aktiviert werden
				dialog.enableButtons(true, true, false, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("E3-INFO") == 0) {
			// der E3-Toggelbutton ist selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also
				// können der Ok- und Transfer-Button
				// aktiviert werden
				dialog.enableButtons(true, true, false, false, true, true);
			} else {
				// der Pfad ist ungültig, also
				// können der Ok- und Transfer-Button
				// nicht aktiviert werden
				dialog.enableButtons(true, true, false, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("P1-INFO") == 0) {
			// der P1-Toggelbutton ist selektiert
			// worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also
				// können der Ok- und
				// Transfer-Button aktiviert werden
				dialog.enableButtons(true, false, true, false, true, true);
			} else {
				// der Pfad ist ungültig, also
				// können der Ok- und
				// Transfer-Button nicht aktiviert
				// werden
				dialog.enableButtons(true, false, true, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("P2-INFO") == 0) {
			// der P2-Toggelbutton ist selektiert
			// worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also
				// können der Ok- und
				// Transfer-Button aktiviert werden
				dialog.enableButtons(true, false, true, false, true, true);
			} else {
				// der Pfad ist ungültig, also
				// künnen der Ok- und
				// Transfer-Button nicht aktiviert
				// werden
				dialog.enableButtons(true, false, true, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("P3-INFO") == 0) {
			// der P2-Toggelbutton ist
			// selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig, also
				// künnen der Ok- und
				// Transfer-Button aktiviert
				// werden
				dialog.enableButtons(true, false, true, false, true, true);
			} else {
				// der Pfad ist ungültig,
				// also können der Ok- und
				// Transfer-Button nicht
				// aktiviert werden
				dialog.enableButtons(true, false, true, false, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("A1-INFO") == 0) {
			// der A1-Toggelbutton ist
			// selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist gültig,
				// also können der Ok-
				// und Transfer-Button
				// aktiviert werden
				dialog.enableButtons(true, false, false, true, true, true);
			} else {
				// der Pfad ist ungültig,
				// also können der Ok-
				// und Transfer-Button nicht
				// aktiviert werden
				dialog.enableButtons(true, false, false, true, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("A2-INFO") == 0) {
			// der A2-Toggelbutton ist
			// selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist
				// gültig, also
				// können der Ok- und
				// Transfer-Button
				// aktiviert werden
				dialog.enableButtons(true, false, false, true, true, true);
			} else {
				// der Pfad ist
				// ungültig, also
				// können der Ok- und
				// Transfer-Button nicht
				// aktiviert werden
				dialog.enableButtons(true, false, false, true, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("A3-INFO") == 0) {
			// der A3-Toggelbutton ist
			// selektiert worden
			if (analyser.getValidPath() != -1) {
				// der Pfad ist
				// gültig, also
				// können der Ok-
				// und Transfer-Button
				// aktiviert werden
				dialog.enableButtons(true, false, false, true, true, true);
			} else {
				// der Pfad ist
				// ungültig, also
				// können der Ok-
				// und Transfer-Button
				// nicht aktiviert
				// werden
				dialog.enableButtons(true, false, false, true, true, false);
			}
		} else if (e.getActionCommand().compareToIgnoreCase("Ok") == 0) {
			// der Ok-Button wurde
			// gedrückt
			if (!dialog.getFwRadio().isSelected()) {
				// ein neuer Eintrag
				// muss gemacht
				// werden, wenn es
				// kein Funktionswort
				// ist
				analyser.newEntry();
			}
			// und der Dialog
			// für das
			// nächste Wort
			// vorbereitet werden
			praWo.getDialog().deselectButtons();
			praWo.getDialog().enableButtons(true, false, false, false, false,
					false);
			if (analyser.getMode() != analyser.SELECTEDMODE) {
				// wenn es nicht
				// SELECTEDMODE ist,
				// dann wird der
				// Dialog mit dem
				// nächsten Wort
				// gestartet
				analyser.runDialog();
			} else {
				// ansonsten wartet
				// der Dialog auf die
				// neue
				// Benutzereingabe
				analyser.runSelected();
			}
		} else if (e.getActionCommand().compareToIgnoreCase("Pathselector") == 0) {
			// der Paht-Button
			// wurde
			// gedrückt
			// also muss der
			// PathSelector-Dialog
			// der Datenbank
			// gestartet werden
			PathNode path = praWo.getPathSelector();
			if (path != null) {
				// wenn der Path
				// nicht null ist,
				// muss
				// abhängig
				// von der
				// gewählten
				// Wortklasse
				// auf
				// Gültigkeit
				// überprüft
				// werden
				boolean isValidPath = false;
				String wrongPath = "";
				if (dialog.getNomenRadio().isSelected()) {
					isValidPath = analyser.isValidPath(
							TR_Assignation.WORDCLASS_NOUN, path.getId());
					wrongPath = "Path must begin with [Lexempragmatik]";
				} else if (dialog.getVerbRadio().isSelected()) {
					isValidPath = analyser.isValidPath(
							TR_Assignation.WORDCLASS_VERB, path.getId());
					wrongPath = "Path must not begin with [Lexempragmatik] or [Adjunktion]";
				} else if (dialog.getAdjektivRadio().isSelected()) {
					isValidPath = analyser.isValidPath(
							TR_Assignation.WORDCLASS_ADJECTIVE, path.getId());
					wrongPath = "Path must begin with [Adjunktion]";
				}
				if (!isValidPath) {
					// der Pfad ist
					// ungülig,
					// also muss
					// der
					// komplette
					// Pfad
					// in rot
					// angezeigt
					// werden und
					// der Ok- und
					// Transfer-Butten
					// deaktiviert
					// werden
					dialog.getPathPane().setForeground(Color.RED);
					dialog.getPathPane().setText(
							"Wrong path "
									+ analyser.getFullPathString(path.getId())
									+ ". " + wrongPath);
					analyser.setValidPath(-1);
					dialog.getOk().setEnabled(false);
					dialog.getTransfer().setEnabled(false);
				} else {
					// der Pfad ist
					// gülig,
					// also kann
					// der
					// komplette
					// Pfad
					// in schwarr
					// angezeigt
					// werden und
					// der Ok- und
					// Transfer-Butten
					// aktiviert
					// werden
					dialog.getPathPane().setForeground(Color.BLACK);
					dialog.getPathPane().setText(
							analyser.getFullPathString(path.getId()));
					analyser.setValidPath(path.getId());
					dialog.getOk().setEnabled(true);
					dialog.getTransfer().setEnabled(true);
				}
			}
		} else if (e.getActionCommand().compareToIgnoreCase("wordclass") == 0) {
			// der Help-Button
			// neben den
			// Wortarten ist
			// gedrückt
			// worden
			// also muss der
			// Minidialog mit
			// der
			// selektierten
			// Wortart
			// angezeigt
			// werden
			dialog.runMiniDialog(true, false);
		} else if (e.getActionCommand().compareToIgnoreCase("level") == 0) {
			// der
			// Help-Button
			// neben den
			// Anstraktheiten
			// ist
			// gedrückt
			// worden
			// also muss
			// der
			// Minidialog
			// mit der
			// selektierten
			// Abstraktheit
			// angezeigt
			// werden
			dialog.runMiniDialog(false, true);
		} else if (e.getActionCommand().compareToIgnoreCase(
				"wordlistAllChanged") == 0) {
			// die
			// DropDown
			// Liste im
			// dem
			// Dialog,
			// der nach
			// dem Start
			// von all
			// words
			// erscheint,
			// hat sich
			// geändert
			// selektiere
			// den
			// gewählten
			// index
			praWo.getTextPane().requestFocus();
			int index = ((JComboBox) e.getSource()).getSelectedIndex();
			Word word = (Word) analyser.getV().get(index);
			praWo.getTextPane().select(word.getStartPosition(),
					word.getEndPosition() + 1);
		} else if (e.getActionCommand()
				.compareToIgnoreCase("wordlistCwChanged") == 0) {
			// die
			// DropDown
			// Liste
			// im dem
			// Dialog,
			// der
			// nach
			// dem
			// Start
			// von
			// constitutive
			// words
			// erscheint,
			// hat
			// sich
			// geändert
			// selektiere
			// den
			// gewählten
			// index
			praWo.getTextPane().requestFocus();
			int index = ((JComboBox) e.getSource()).getSelectedIndex();
			ConstitutiveWord cw = (ConstitutiveWord) analyser.getV().get(index);
			praWo.getTextPane().select(cw.getStartPosition(),
					cw.getEndPosition() + 1);
		} else if (e.getActionCommand().compareToIgnoreCase("startOk") == 0) {
			// die
			// DropDown
			// Liste
			// im
			// dem
			// Dialog,
			// der
			// nach
			// dem
			// Start
			// von
			// constitutive
			// words
			// erscheint,
			// hat
			// sich
			// geändert
			// den
			// gewählten
			// Index
			// dem
			// Analyser
			// mitteilen
			analyser.setIndex(dialog.getWordListBox().getSelectedIndex());
			// den
			// Startdialog
			// deaktivieren
			dialog.getStartDialog().dispose();
			// den
			// Dialog
			// vorbereiten
			praWo.getDialog().enableButtons(true, false, false, false, false,
					false);
			praWo.getDialog().getWordclassButton().setEnabled(true);
			praWo.getDialog().getLevelButton().setEnabled(true);
			praWo.getDialog().getPathPane().setForeground(Color.RED);
			praWo.getDialog().getPathPane().setText("Choose a path");
			// den
			// Dialog
			// ausführen
			analyser.runDialog();
		} else {
			// wenn
			// hier
			// was
			// kommt,
			// ist
			// was
			// schief
			// gelaufen
			// :-)
		}
	}

	/**
	 * Implementierte Funktion von ListSelectionListener. <br>
	 * Diese Funktion überwacht die Vorschlagsliste, damit falls der
	 * Benutzer einen der Vorschläge auswählt, die zugehörigen
	 * Radiobuttons und Textfelder entsprechend selektiert und ausgefüllt
	 * werden können.
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		if (list.getSelectedValue() != null) {
			PragmatischesWort pw = (PragmatischesWort) analyser.getNamePW()
					.get(list.getSelectedValue());
			analyser.preselection(pw);
		}
	}

}