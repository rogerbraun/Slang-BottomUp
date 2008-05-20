/*
 * Created on 30.05.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package additionalPrograms.PragWo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;

/**
 * @author Natascha Stäbler natascha@mainiero.de<br>
 *         <br>
 *         Last modified on 04.11.2005<br>
 *         <br>
 *         Diese Klasse verarbeitet alle Benutzereingaben, die über das
 *         Menü und die Werkzeugleiste gemacht werden.
 */
public class MenuListener implements ActionListener {

	private PraWo praWo;

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
	public MenuListener(PraWo praWo, Analyser analyser) {
		this.praWo = praWo;
		this.analyser = analyser;
	}

	/**
	 * Implementierte Funktion von ActionListener. <br>
	 * Diese Funktion überwacht das Menü und die Werkzeugleiste und
	 * reagiert auf die Benutzereingaben.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareToIgnoreCase("Open") == 0) {
			// ein Text soll aus der Datenbank geladen werden
			praWo.DBConnect(praWo.getDbc(), praWo.getChapter());
		} else if (e.getActionCommand().compareToIgnoreCase("Save") == 0) {
			// die Analyseergebnisse sollen in der Datenbank gespeichert
			// werden
			analyser.saveCurrentResults();
			JOptionPane.showMessageDialog(null, "Successfully saved");
		} else if (e.getActionCommand().compareToIgnoreCase("Exit") == 0) {
			// das Programm soll beendet werden
			// falls es ungespeicherte Analyseergebnisse gibt,
			if (praWo.isUnsaved()) {
				// muss der Benutzer gefragt werden, ob diese gespeichert
				// werden sollen
				int value = JOptionPane.showOptionDialog(praWo.getFrame(),
						"Do you want to save current results to the database?",
						"Save", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, null, null);
				// wenn ja
				if (value == 0) {
					// die Ergebnisse speichern
					analyser.saveCurrentResults();
				}
			}
			// das Programm beenden
			praWo.getFrame().dispose();
			praWo.getModel().continueBU();
		} else if (e.getActionCommand().compareToIgnoreCase("...all words") == 0) {
			// alle Wörter sollen nacheinander analysiert werden
			analyser.runAll();
		} else if (e.getActionCommand().compareToIgnoreCase(
				"...constitutive words") == 0) {
			// alle konstitutiven Wörter sollen anaylsiert
			// werden
			analyser.runCW();
		} else if (e.getActionCommand()
				.compareToIgnoreCase("...selected words") == 0) {
			// selektierte Wörter sollen analysiert werden
			analyser.runSelected();
		} else if (e.getActionCommand().compareToIgnoreCase(
				"...all constitutive words") == 0) {
			// alle konstitutiven Wörter des Textes
			// sollen markiert werden
			analyser.showCWs();
		} else if (e.getActionCommand().compareToIgnoreCase("...all levels") == 0) {
			// alle Abstraktheiten sollen markiert werden
			analyser.showLevels();
		} else if (e.getActionCommand().compareToIgnoreCase("...standard") == 0) {
			// alle Markierungen sollen entfernt
			// werden
			analyser.removeStyles();
		} else if (e.getActionCommand().compareToIgnoreCase("Load DB entries") == 0) {
			// die Analyseergebnisse der Datenbank
			// sollen geladen werden
			// dazu muss der Benutzer gefragt
			// werden, ob er das wirklich will, da
			// bisherige Analysen
			// überschrieben werden
			// könnten
			int value = JOptionPane
					.showOptionDialog(
							praWo.getFrame(),
							"Current results could be overwritten. Do you want to continue.",
							"DB Entries", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (value == 0) {
				// wenn ja, dann Einträge laden
				// und markieren
				analyser.getDBEntries();
				analyser.showLevels();
			}
		} else if (e.getActionCommand().compareToIgnoreCase("E1") == 0) {
			// in der Werkzeugleiste wurde E1
			// gedrückt,
			// also muss die Markierung für
			// alle Worte dieses Typs
			// geändert werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedE1(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_NOUN,
					ConstitutiveWord.LEXPRAG_E1, false);
		} else if (e.getActionCommand().compareToIgnoreCase("E2") == 0) {
			// in der Werkzeugleiste wurde E2
			// gedrückt,
			// also muss die Markierung
			// für alle Worte dieses
			// Typs geändert werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedE2(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_NOUN,
					ConstitutiveWord.LEXPRAG_E2, false);
		} else if (e.getActionCommand().compareToIgnoreCase("E3") == 0) {
			// in der Werkzeugleiste wurde
			// E3 gedrückt,
			// also muss die Markierung
			// für alle Worte dieses
			// Typs geändert werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedE3(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_NOUN,
					ConstitutiveWord.LEXPRAG_E3, false);
		} else if (e.getActionCommand().compareToIgnoreCase("P1") == 0) {
			// in der Werkzeugleiste
			// wurde P1 gedrückt,
			// also muss die Markierung
			// für alle Worte
			// dieses Typs
			// geändert werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedP1(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_VERB,
					ConstitutiveWord.LEXPRAG_P1, false);
		} else if (e.getActionCommand().compareToIgnoreCase("P2") == 0) {
			// in der Werkzeugleiste
			// wurde P2
			// gedrückt,
			// also muss die
			// Markierung für
			// alle Worte dieses
			// Typs geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedP2(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_VERB,
					ConstitutiveWord.LEXPRAG_P2, false);
		} else if (e.getActionCommand().compareToIgnoreCase("P3") == 0) {
			// in der
			// Werkzeugleiste
			// wurde P3
			// gedrückt,
			// also muss die
			// Markierung
			// für alle
			// Worte dieses Typs
			// geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedP3(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_VERB,
					ConstitutiveWord.LEXPRAG_P3, false);
		} else if (e.getActionCommand().compareToIgnoreCase("A1") == 0) {
			// in der
			// Werkzeugleiste
			// wurde A1
			// gedrückt,
			// also muss die
			// Markierung
			// für alle
			// Worte dieses
			// Typs
			// geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedA1(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_ADJECTIVE,
					ConstitutiveWord.LEXPRAG_A1, false);
		} else if (e.getActionCommand().compareToIgnoreCase("A2") == 0) {
			// in der
			// Werkzeugleiste
			// wurde A2
			// gedrückt,
			// also muss
			// die
			// Markierung
			// für
			// alle Worte
			// dieses Typs
			// geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedA2(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_ADJECTIVE,
					ConstitutiveWord.LEXPRAG_A2, false);
		} else if (e.getActionCommand().compareToIgnoreCase("A3") == 0) {
			// in der
			// Werkzeugleiste
			// wurde A3
			// gedrückt,
			// also muss
			// die
			// Markierung
			// für
			// alle
			// Worte
			// dieses
			// Typs
			// geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedA3(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_ADJECTIVE,
					ConstitutiveWord.LEXPRAG_A3, false);
		} else if (e.getActionCommand().compareToIgnoreCase("other") == 0) {
			// in der
			// Werkzeugleiste
			// wurde
			// other
			// gedrückt,
			// also
			// muss
			// die
			// Markierung
			// für
			// alle
			// Worte
			// dieses
			// Typs
			// geändert
			// werden
			JToggleButton button = (JToggleButton) e.getSource();
			praWo.setSelectedOther(button.isSelected());
			analyser.switchColoring(TR_Assignation.WORDCLASS_UNKNOWN, 0, false);
		} else if (e.getActionCommand().compareToIgnoreCase("Pie chart") == 0) {
			// das
			// Kuchendiagramm
			// soll
			// angezeigt
			// werden
			JDialog diagramDialog = new JDialog(praWo.getFrame(), "Diagram");

			// Create
			// and
			// set
			// up
			// the
			// content
			// pane.
			Diagram diagram = new Diagram(analyser, praWo, Diagram.PIECHART);
			diagramDialog.getContentPane().add(diagram, BorderLayout.CENTER);

			// Display
			// the
			// window.
			diagramDialog.pack();
			diagramDialog.setVisible(true);
		} else if (e.getActionCommand().compareToIgnoreCase("Bar chart") == 0) {
			// das
			// Balkendiagramm
			// soll
			// angezeigt
			// werden
			JDialog diagramDialog = new JDialog(praWo.getFrame(), "Diagram");

			// Create
			// and
			// set
			// up
			// the
			// content
			// pane.
			Diagram diagram = new Diagram(analyser, praWo, Diagram.BARCHART);
			JScrollPane scrollDialog = new JScrollPane(diagram);
			scrollDialog.setPreferredSize(new Dimension(500, 650));
			diagramDialog.getContentPane().add(scrollDialog,
					BorderLayout.CENTER);

			// Display
			// the
			// window.
			diagramDialog.pack();
			diagramDialog.setVisible(true);
		} else if (e.getActionCommand().compareToIgnoreCase("Density chart") == 0) {
			// das
			// Dichtediagramm
			// soll
			// angezeigt
			// werden
			JDialog diagramDialog = new JDialog(praWo.getFrame(), "Diagram");

			// Create
			// and
			// set
			// up
			// the
			// content
			// pane.
			Diagram diagram = new Diagram(analyser, praWo, Diagram.DENSITYCHART);
			JScrollPane scrollDialog = new JScrollPane(diagram);
			scrollDialog.setPreferredSize(new Dimension(500, 650));
			diagramDialog.getContentPane().add(scrollDialog,
					BorderLayout.CENTER);

			// Display
			// the
			// window.
			diagramDialog.pack();
			diagramDialog.setVisible(true);
		} else if (e.getActionCommand().compareToIgnoreCase("all_words") == 0) {
			// alle
			// Wörter
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("All words", analyser.getAll());
		} else if (e.getActionCommand().compareToIgnoreCase("all_nouns") == 0) {
			// alle
			// Nomen
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("All nouns", analyser.getNomina());
		} else if (e.getActionCommand().compareToIgnoreCase("noun1") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// E1
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("E1", analyser.getE1());
		} else if (e.getActionCommand().compareToIgnoreCase("noun2") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// E2
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("E2", analyser.getE2());
		} else if (e.getActionCommand().compareToIgnoreCase("noun3") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// E3
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("E3", analyser.getE3());
		} else if (e.getActionCommand().compareToIgnoreCase("all_verbs") == 0) {
			// alle
			// Verben
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("All verbs", analyser.getVerba());
		} else if (e.getActionCommand().compareToIgnoreCase("verb1") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// P1
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("P1", analyser.getP1());
		} else if (e.getActionCommand().compareToIgnoreCase("verb2") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// P2
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("P2", analyser.getP2());
		} else if (e.getActionCommand().compareToIgnoreCase("verb3") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// P3
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("P3", analyser.getP3());
		} else if (e.getActionCommand().compareToIgnoreCase("all_adjectives") == 0) {
			// alle
			// Adjektive
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("All adjectives", analyser.getAdjektiva());
		} else if (e.getActionCommand().compareToIgnoreCase("adjective1") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// a1
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("A1", analyser.getA1());
		} else if (e.getActionCommand().compareToIgnoreCase("adjective2") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// A2
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("A2", analyser.getA2());
		} else if (e.getActionCommand().compareToIgnoreCase("adjective3") == 0) {
			// alle
			// Wörter
			// vom
			// Typ
			// A3
			// sollen
			// angezeigt
			// werden
			Wordlists wordlists = praWo.getWordlists();
			wordlists.wordlistDialog("A3", analyser.getA3());
		}
	}
}
