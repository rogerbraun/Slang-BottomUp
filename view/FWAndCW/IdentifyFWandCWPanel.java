/*
 * Created on 13.12.2005
 *
 */

package view.FWAndCW;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Model;
import additionalPrograms.WordSplitter.WordSplitter;
import controller.FWAndCW.IdentifyFWandCWController;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/**
 * Menue zum Bestimmen von Function Words und Constitutive Words
 * 
 * @author shanthy
 * 
 */
public class IdentifyFWandCWPanel extends JPanel {

	/**
	 * zufaellig generierte ID
	 */
	private static final long serialVersionUID = 5630985937948209299L;

	/**
	 * der Controller zu diesem Menue
	 */
	private IdentifyFWandCWController controller;

	/**
	 * hier wird das gerade bearbeitete Wort angezeigt
	 */
	private JTextPane wordField;

	/**
	 * zum Formatieren des Wortes
	 */
	private DefaultStyledDocument doc;

	/**
	 * normale Formatierung
	 */
	private static SimpleAttributeSet PLAIN;

	/**
	 * rot fuer CW, blau fuer FW
	 */
	private static SimpleAttributeSet STYLE;

	/**
	 * Liste mit den Vorschlaegen fuer die Bestimmung
	 */
	private JList list;

	/**
	 * das gerade bearbeitete Wort
	 */
	private Word word;

	/**
	 * Vorschlaege fuer die Bestimmung
	 */
	private Vector listData;

	/**
	 * Buttons fuer das Menue
	 */
	private JButton ok, fw, cw, delete, cancel, forall;

	/**
	 * automatisch fuer alle Woerter uebernehmen oder nicht
	 */
	private JCheckBox check;

	/**
	 * @param controller
	 *            IdentifyFWandCWController
	 * 
	 */
	@SuppressWarnings("unchecked")
	public IdentifyFWandCWPanel(IdentifyFWandCWController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		// JTextEditor
		doc = new DefaultStyledDocument();
		wordField = new JTextPane();
		wordField.setName("Word field");
		wordField.setBorder(new EtchedBorder());
		wordField.setDocument(doc);
		setLayout(new BorderLayout());
		// Standard-Textdarstellung
		PLAIN = new SimpleAttributeSet();
		StyleConstants.setFontFamily(PLAIN, Model.PLAINFONT.getFontName());
		StyleConstants.setFontSize(PLAIN, Model.PLAINFONT.getSize());
		StyleConstants.setBold(PLAIN, true);

		// function word
		STYLE = new SimpleAttributeSet();
		StyleConstants.setFontFamily(STYLE, Model.PLAINFONT.getFontName());
		StyleConstants.setFontSize(STYLE, Model.PLAINFONT.getSize());
		StyleConstants.setBold(STYLE, true);

		wordField.setMinimumSize(new Dimension(50, 30));
		wordField.setPreferredSize(new Dimension(50, 30));
		wordField.addCaretListener(controller);
		wordField.setEditable(false);
		top.add(wordField, BorderLayout.NORTH);

		JPanel buttonPanel1 = new JPanel(new GridLayout(1, 3));
		buttonPanel1.setBorder(new BevelBorder(10));
		fw = new JButton("function word");
		fw.addActionListener(controller);
		fw.setActionCommand("FW");
		cw = new JButton("sem. const. word");
		cw.addActionListener(controller);
		cw.setActionCommand("CW");
		delete = new JButton("delete");
		delete.addActionListener(controller);
		delete.setActionCommand("DELETE");
		buttonPanel1.add(fw);
		buttonPanel1.add(cw);
		buttonPanel1.add(delete);
		top.add(buttonPanel1, BorderLayout.SOUTH);

		add(top, BorderLayout.NORTH);

		listData = new Vector();
		listData.add("suggestions");
		list = new JList(listData);
		list.setBorder(new BevelBorder(10));
		list.addListSelectionListener(controller);
		JScrollPane scrollPane = new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel2 = new JPanel(new GridLayout(2, 3));
		ok = new JButton("ok");
		ok.setActionCommand("OK");
		ok.addActionListener(controller);
		cancel = new JButton("cancel");
		cancel.setActionCommand("CANCEL");
		cancel.addActionListener(controller);
		forall = new JButton("for all");
		forall.setActionCommand("FORALL");
		forall.addActionListener(controller);
		JLabel empty1 = new JLabel();
		JLabel empty2 = new JLabel();
		check = new JCheckBox("overwrite assignments");
		check.addActionListener(controller);
		check.setSelected(false);
		buttonPanel2.add(ok);
		buttonPanel2.add(cancel);
		buttonPanel2.add(forall);
		buttonPanel2.add(empty1);
		buttonPanel2.add(empty2);
		buttonPanel2.add(check);

		add(buttonPanel2, BorderLayout.SOUTH);
	}

	/**
	 * Bisherigen Textinhalt loeschen und neuen im Style Plain einfuegen.
	 * 
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		try {
			doc.remove(0, doc.getLength());
			doc.insertString(0, text, PLAIN);
		} catch (BadLocationException ignore) {
			ignore.printStackTrace();
			wordField.setCaretPosition(0);
		}
	}

	/**
	 * das FunctionWord im FWSTYLE zeichnen
	 * 
	 * @param functionWord
	 *            FunctionWord
	 */
	private void designFW(FunctionWord functionWord) {
		int start = functionWord.getStartPosition()
				- functionWord.getWord().getStartPosition();
		int end = functionWord.getEndPosition()
				- functionWord.getWord().getStartPosition();
		StyleConstants.setForeground(STYLE, controller.getModel().getView()
				.getDesigner().getColor(functionWord));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, true);
	}

	/**
	 * das ConstitutiveWord im CWSTYLE zeichnen
	 * 
	 * @param constitutiveWord
	 *            ConstitutiveWord
	 */
	private void designCW(ConstitutiveWord constitutiveWord) {
		int start = constitutiveWord.getStartPosition()
				- constitutiveWord.getWord().getStartPosition();
		int end = constitutiveWord.getEndPosition()
				- constitutiveWord.getWord().getStartPosition();
		StyleConstants.setForeground(STYLE, controller.getModel().getView()
				.getDesigner().getColor(constitutiveWord));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, true);
	}

	/**
	 * das angeklickt Wort in das Textfeld schreiben
	 * 
	 * @param word
	 *            Word
	 */
	public void setWord(Word word) {
		wordField.setSelectionStart(wordField.getSelectionEnd());
		if (this.word == null || !this.word.equals(word)) {
			this.word = word;
			setText(word.getContent());
			controller.getModel().getView().scrollTo(word);
			controller.getModel().getView().getEditor().setHighlighted(word,
					true);
			doc.setCharacterAttributes(0, wordField.getText().length(), PLAIN,
					true);
			Vector cws = Model.getIllocutionUnitRoots().getConstitutiveWords(
					word);
			Vector fws = Model.getIllocutionUnitRoots().getFunctionWords(word);
			for (int i = 0; i < fws.size(); i++) {
				designFW((FunctionWord) fws.get(i));
			}
			for (int i = 0; i < cws.size(); i++) {
				designCW((ConstitutiveWord) cws.get(i));
			}
			fillList();
		} else if (this.word != null && this.word.equals(word)) {
			setText(word.getContent());
			controller.getModel().getView().scrollTo(word);
			controller.getModel().getView().getEditor().setHighlighted(word,
					true);
			doc.setCharacterAttributes(0, wordField.getText().length(), PLAIN,
					true);
			Vector cws = Model.getIllocutionUnitRoots().getConstitutiveWords(
					word);
			Vector fws = Model.getIllocutionUnitRoots().getFunctionWords(word);
			for (int i = 0; i < fws.size(); i++) {
				designFW((FunctionWord) fws.get(i));
			}
			for (int i = 0; i < cws.size(); i++) {
				designCW((ConstitutiveWord) cws.get(i));
			}
			wordField.repaint();
		}
	}

	/**
	 * die Vorschlagsliste fuellen
	 */
	private void fillList() {
		listData.clear();
		controller.getModel().getView().scrollTo(word);
		WordSplitter wordSplitter = new WordSplitter(controller.getModel()
				.getConstitutiveWordsForLanguage(word.getLanguage()),
				controller.getModel().getFunctionWordsForLanguage(
						word.getLanguage()));
		listData = wordSplitter.split(word.getContent());
		list.setListData(listData);
	}

	/**
	 * @return Returns the wordField.
	 */
	public JEditorPane getWordField() {
		return wordField;
	}

	/**
	 * @return Returns the list.
	 */
	public JList getList() {
		return list;
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isSelected() {
		return check.isSelected();
	}

	/**
	 * setzt die Reste von der letzten Bestimmung zurueck
	 * 
	 */
	public void reset() {
		wordField.setText("word to analyze");
		check.setSelected(false);
		listData.clear();
		list.setListData(listData);
		word = null;
	}

	/**
	 * 
	 * @return word Word
	 */
	public Word getWord() {
		return word;
	}

	/**
	 * wird beim Anzeigen des Menues aufgerufen
	 */
	public void doAction() {
		if (controller.allWordsSet()) {
			if (controller.getModel().firstNotAssignedCW() == null) {
				controller.getModel().startPragWo();
			} else
				controller.getModel().showMenu("wordList");
		}
	}
}
