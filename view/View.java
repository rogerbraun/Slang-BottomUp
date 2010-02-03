package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Model;
import view.FWAndCW.FWAndCWDesigner;
import view.MS.MSDesigner;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/*
 * Created on 26.01.2005
 */

/**
 * ist fuer die Darstellung des Textfensters zuständig
 * @author shanthy
 */
public class View extends JPanel {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = -1530761551255972156L;

	/**
	 * der aktuelle Controller
	 */
	private Controller controller;

	/**
	 * der aktuelle Designer
	 */
	private Designer designer;

	/**
	 * der TextPane
	 */
	private MyTextPane editor;

	/**
	 * wird für das Formatieren des Textes benötigt
	 */
	private DefaultStyledDocument doc;

	//verschiedene Formatierungsarten
	/**
	 * Standardformatierung: SansSerif, Font.BOLD, 14
	 */
	private static SimpleAttributeSet PLAIN;

	/**
	 * Textfarbe je nach Designer
	 */
	private static SimpleAttributeSet STYLE;

	/**
	 * blau hinterlegt
	 */
	private static SimpleAttributeSet MARKEDSTYLE;

	/**
	 * hier werden die aktuelle markierten Elemente gespeichert
	 */
	private Vector markedElements = new Vector();

	/**
	 * erstellt die JTextPane und die verschiedenen SimpleAttributeSets
	 * 
	 * @param controller
	 *            Controller
	 */
	public View(Controller controller) {
		// JTextEditor
		doc = new DefaultStyledDocument();
		editor = new MyTextPane(this);
		editor.setBorder(new EtchedBorder());
		editor.setBorder(new EtchedBorder());
		editor.setDocument(doc);
		editor.addCaretListener(controller);
		editor.addMouseListener(controller);
		setLayout(new BorderLayout());
		add(new JScrollPane(editor), BorderLayout.CENTER);
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

		// marked word
		MARKEDSTYLE = new SimpleAttributeSet();
		StyleConstants
				.setFontFamily(MARKEDSTYLE, Model.PLAINFONT.getFontName());
		StyleConstants.setFontSize(MARKEDSTYLE, Model.PLAINFONT.getSize());
		StyleConstants.setBackground(MARKEDSTYLE, Model.MARKEDTEXT);
		StyleConstants.setBold(MARKEDSTYLE, true);

		//formatiert den Text
		formatText(Model.getChapter().getIllocutionUnits());
	}

	/**
	 * formartiert den Text: jede Äußrungseinheit in eine Zeile
	 * 
	 * @param illocutionUnits
	 *            Vector
	 */
	public void formatText(Vector illocutionUnits) {
		String text = new String();
		for (int i = 0; i < illocutionUnits.size(); i++) {
			IllocutionUnit iu = (IllocutionUnit) illocutionUnits.get(i);
			text += iu.toString();
			if( text.endsWith("\n") )
			{
				continue;
			}
			text += "\n";
		}
		setText(text);
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
			editor.setCaretPosition(0);
		}
	}

	/**
	 * jedes Menue besitzt seinen eigenen Designer und Controller diese muessen
	 * beim Anzeigen eines neuen Menues angepasst werden
	 * 
	 * @param controller
	 *            Controller
	 */
	public void setController(Controller controller) {
		editor.removeCaretListener(this.controller);
		editor.removeCaretListener(this.controller);
		editor.removeMouseListener(this.controller);
		this.controller = controller;
		controller.setTextPart(this);
		editor.addCaretListener(controller);
		editor.addMouseListener(controller);
		designer = controller.getDesigner();
		controller.doAction();
	}

	/**
	 * beim Anzeigen eines neuen Menues wird der Designer vom Model neu gesetzt
	 * 
	 * @param d
	 *            Designer
	 */
	public void setDesigner(Designer d) {
		designer = d;
	}

	/**
	 * den Text designen: FW, CW, MS
	 * 
	 * @param iur
	 *            IllocutionUnitRoots
	 */
	public void designText(IllocutionUnitRoots iur) {
		if (designer instanceof FWAndCWDesigner) {
			doc.setCharacterAttributes(0, editor.getText().length(), PLAIN,
					true);
			Vector<ConstitutiveWord> cws = iur.getConstitutiveWords();
			for (int i = 0; i < cws.size(); i++) {
				ConstitutiveWord cw = (ConstitutiveWord) cws.get(i);
				if (cw != null)
					designCW(cw);
			}
			Vector<FunctionWord> fws = iur.getFunctionWords();
			for (int i = 0; i < fws.size(); i++) {
				FunctionWord fw = (FunctionWord) fws.get(i);
				if (fw != null)
					designFW(fw);
			}
		}
		if (designer instanceof MSDesigner) {
			for (int i = 0; i < iur.getMacroSentences().size(); i++) {
				MacroSentence ms = iur.getMacroSentenceAtIndex(i);
				if (ms != null)
					designMS(ms);
			}
		}
	}

	/**
	 * ein Wort markieren (MARKEDSTYLE), angeben, ob bisherige Formatierungen
	 * ueberschrieben werden sollen
	 * 
	 * @param o
	 *            Objekt
	 * @param overwrite
	 *            boolean
	 */
	@SuppressWarnings("unchecked")
	public void design(Object o, boolean overwrite) {
		if (o instanceof Word) {
			Word word = (Word) o;
			if (!markedElements.contains(word)) {
				markedElements.add(word);
				int start = word.getStartPosition();
				int end = word.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, MARKEDSTYLE,
						overwrite);
			} else
				reset(word);
		} else if (o instanceof ConstitutiveWord) {
			ConstitutiveWord cword = (ConstitutiveWord) o;
			if (!markedElements.contains(cword.getWord())
					&& !markedElements.contains(cword)) {
				markedElements.add(cword);
				int start = cword.getStartPosition();
				int end = cword.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, MARKEDSTYLE,
						overwrite);
			} else
				reset(cword);
		} else if (o instanceof FunctionWord) {
			FunctionWord fword = (FunctionWord) o;
			if (!markedElements.contains(fword.getWord())
					&& !markedElements.contains(fword)) {
				markedElements.add(fword);
				int start = fword.getStartPosition();
				int end = fword.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, MARKEDSTYLE,
						overwrite);
			} else
				reset(fword);
		} else if (o instanceof MeaningUnit) {
			try {
				MeaningUnit mu = (MeaningUnit) o;
				if (mu.getFunctionWord() != null) {
					FunctionWord fw = mu.getFunctionWord();
					if (!markedElements.contains(fw.getWord())) {
						markedElements.add(fw);
						int start = fw.getStartPosition();
						int end = fw.getEndPosition();
						doc.setCharacterAttributes(start, end - start + 1,
								MARKEDSTYLE, overwrite);
					} else
						reset(fw);
				}
				ConstitutiveWord cw = mu.getConstitutiveWord();
				if (!markedElements.contains(cw.getWord())) {
					markedElements.add(cw);
					int start = cw.getStartPosition();
					int end = cw.getEndPosition();
					doc.setCharacterAttributes(start, end - start + 1,
							MARKEDSTYLE, overwrite);
				} else
					reset(cw);
			} catch (NullPointerException e) {
				// e.printStackTrace();
			}
		} else if (o instanceof IllocutionUnit) {
			IllocutionUnit iu = (IllocutionUnit) o;
			if (!markedElements.contains(iu)) {
				markedElements.add(iu);
				for (int i = 0; i < iu.getTokens().size(); i++) {
					Token token = (Token) iu.getTokens().get(i);
					int start = token.getStartPosition();
					int end = token.getEndPosition();
					doc.setCharacterAttributes(start, end - start + 1,
							MARKEDSTYLE, overwrite);
				}
			} else
				reset(iu);
		}
	}

	/**
	 * das FunctionWord im FWSTYLE zeichnen
	 * 
	 * @param functionWord
	 *            FunctionWord
	 */
	private void designFW(FunctionWord functionWord) {
		int start = functionWord.getStartPosition();
		int end = functionWord.getEndPosition();
		StyleConstants.setForeground(STYLE, designer.getColor(functionWord));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, true);
	}

	/**
	 * das ConstitutiveWord im CWSTYLE zeichnen
	 * 
	 * @param constitutiveWord
	 *            ConstitutiveWord
	 */
	private void designCW(ConstitutiveWord constitutiveWord) {
		int start = constitutiveWord.getStartPosition();
		int end = constitutiveWord.getEndPosition();
		StyleConstants
				.setForeground(STYLE, designer.getColor(constitutiveWord));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, true);
	}

	/**
	 * MU im schwarz zeichen
	 * 
	 * @param meaningUnit
	 *            MeaningUnit
	 */
	private void designMU(MeaningUnit meaningUnit) {
		int start = meaningUnit.getConstitutiveWord().getStartPosition();
		int end = meaningUnit.getConstitutiveWord().getEndPosition();
		StyleConstants.setBackground(STYLE, designer
				.getBackgroundColor(meaningUnit));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, true);
		try {
			int s = meaningUnit.getFunctionWord().getStartPosition();
			int e = meaningUnit.getFunctionWord().getEndPosition();
			StyleConstants.setBackground(STYLE, designer
					.getBackgroundColor(meaningUnit));
			doc.setCharacterAttributes(s, e - s + 1, STYLE, true);
		} catch (NullPointerException npe) {
			// MeaningUnit besteht nur aus cw
		}
	}

	/**
	 * IU im schwarz zeichnen
	 * 
	 * @param illocutionUnit
	 *            IllocutionUnit
	 */
	private void designIU(IllocutionUnit illocutionUnit) {
		int start = illocutionUnit.getStartPosition();
		int end = illocutionUnit.getEndPosition();
		StyleConstants.setBackground(STYLE, designer
				.getBackgroundColor(illocutionUnit));
		doc.setCharacterAttributes(start, end - start + 1, STYLE, false);
	}

	/**
	 * MS schwarz zeichnen
	 * 
	 * @param macroSentence
	 *            MacroSentence
	 */
	private void designMS(MacroSentence macroSentence) {
		int start = macroSentence.getHead().getIllocutionUnit()
				.getStartPosition();
		int end = macroSentence.getHead().getIllocutionUnit().getEndPosition();
		Color color = designer.getBackgroundColor(macroSentence);
		StyleConstants.setBackground(STYLE, color);
		doc.setCharacterAttributes(start, end - start + 1, STYLE, false);
		for (int i = 0; i < macroSentence.getDependencies().size(); i++) {
			IllocutionUnitRoot iur = (IllocutionUnitRoot) macroSentence
					.getDependencies().get(i);
			int s = iur.getIllocutionUnit().getStartPosition();
			int e = iur.getIllocutionUnit().getEndPosition();
			StyleConstants.setBackground(STYLE, color);
			doc.setCharacterAttributes(s, e - s + 1, STYLE, false);
		}

	}

	/**
	 * die Markierung zuruecksetzen und die Woerter wieder designen
	 */
	public void reset() {
		for (int i = 0; i < markedElements.size(); i++) {
			if (markedElements.get(i) instanceof Word) {
				Word word = (Word) markedElements.get(i);
				editor.setCaretPosition(word.getEndPosition() + 1);
				int start = word.getStartPosition();
				int end = word.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
			} else if (markedElements.get(i) instanceof FunctionWord) {
				FunctionWord fword = (FunctionWord) markedElements.get(i);
			//	if(fword.getWord().getEndPosition()!= fword.getEndPosition()) {
					// fword ist teil eines gr��eren Wortes-> Endposition als caretposition verwenden
			//		editor.setCaretPosition(fword.getEndPosition() + 1);
			//	}
			//	else {
					editor.setCaretPosition(fword.getEndPosition() + 1);
			//	}
				int start = fword.getStartPosition();
				int end = fword.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
			} else if (markedElements.get(i) instanceof ConstitutiveWord) {
				ConstitutiveWord cword = (ConstitutiveWord) markedElements
						.get(i);
				editor.setCaretPosition(cword.getEndPosition() + 1);
				int start = cword.getStartPosition();
				int end = cword.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
			} else if (markedElements.get(i) instanceof IllocutionUnit) {
				IllocutionUnit iu = (IllocutionUnit) markedElements.get(i);
				for (int j = 0; j < iu.getTokens().size(); j++) {
					Token token = (Token) iu.getTokens().get(j);
					int start = token.getStartPosition();
					int end = token.getEndPosition();
					doc.setCharacterAttributes(start, end - start + 1, PLAIN,
							true);
				}
			}
		}
		markedElements.clear();
		designText(Model.getIllocutionUnitRoots());
	}

	/**
	 * setzt die Markierung fuer das Objekt zurueck
	 * 
	 * @param o
	 *            Object
	 */
	public void reset(Object o) {
		if (o instanceof Word) {
			Word word = (Word) o;
			int start = word.getStartPosition();
			int end = word.getEndPosition();
			doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
			Vector fws = Model.getIllocutionUnitRoots().getFunctionWords(word);
			Vector cws = Model.getIllocutionUnitRoots().getConstitutiveWords(
					word);
			markedElements.removeElement(word);
			if (fws.size() > 0) {
				for (int j = 0; j < fws.size(); j++) {
					FunctionWord fw = (FunctionWord) fws.get(j);
					designFW(fw);
				}
			} else if (cws.size() > 0) {
				for (int j = 0; j < cws.size(); j++) {
					ConstitutiveWord cw = (ConstitutiveWord) cws.get(j);
					designCW(cw);
				}
			}
		} else if (o instanceof FunctionWord) {
			FunctionWord fword = (FunctionWord) o;
			editor.setCaretPosition(fword.getEndPosition() + 1);
			int start = fword.getStartPosition();
			int end = fword.getEndPosition();
			if (start != 0 || end != 0) {
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
				markedElements.removeElement(fword);
				if (Model.getIllocutionUnitRoots().getFunctionWords(
						fword.getWord()).size() > 0)
					designFW(fword);
			}
		} else if (o instanceof ConstitutiveWord) {
			ConstitutiveWord cword = (ConstitutiveWord) o;
			editor.setCaretPosition(cword.getEndPosition() + 1);
			int start = cword.getStartPosition();
			int end = cword.getEndPosition();
			if (start != 0 || end != 0) {
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
				markedElements.removeElement(cword);
				if (Model.getIllocutionUnitRoots().getConstitutiveWords(
						cword.getWord()).size() > 0)
					designCW(cword);
			}
		} else if (o instanceof MeaningUnit) {
			MeaningUnit mu = (MeaningUnit) o;
			FunctionWord fw = mu.getFunctionWord();
			ConstitutiveWord cw = mu.getConstitutiveWord();
			if (fw != null) {
				int start = fw.getStartPosition();
				int end = fw.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
				markedElements.remove(fw);
			}
			if (cw != null) {
				int start = cw.getStartPosition();
				int end = cw.getEndPosition();
				doc.setCharacterAttributes(start, end - start + 1, PLAIN, true);
				markedElements.remove(cw);
			}
			designMU(mu);
		} else if (o instanceof IllocutionUnit) {
			IllocutionUnit iu = (IllocutionUnit) o;
			markedElements.remove(iu);
			designIU(iu);
		}
		designText(Model.getIllocutionUnitRoots());
	}

	/**
	 * zu dem Wort scrollen
	 * 
	 * @param word
	 *            Word
	 */
	@SuppressWarnings("unchecked")
	public void scrollTo(Word word) {
		try {
			if (!markedElements.contains(word)) {
				design(word, true);
				markedElements.add(word);
			}
			editor.setCaretPosition(word.getStartPosition() + 1);
			if (Model.getViewer() != null)
				Model.getViewer().setRoot(
						Model.getIllocutionUnitRoots().getRoot(word));
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * zum CW scrollen
	 * 
	 * @param cword
	 *            ConstitutiveWord
	 */
	@SuppressWarnings("unchecked")
	public void scrollTo(ConstitutiveWord cword) {
		try {
			if (!markedElements.contains(cword)) {
				design(cword, true);
				markedElements.add(cword);
			}
			editor.setCaretPosition(cword.getStartPosition() + 1);
			if(Model.getViewer()!= null)
				Model.getViewer().setRoot(Model.getIllocutionUnitRoots().getRoot(cword.getWord()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * zum FW scrollen
	 * 
	 * @param fword
	 *            FunctionWord
	 */
	@SuppressWarnings("unchecked")
	public void scrollTo(FunctionWord fword) {
		try {
			if (!markedElements.contains(fword)) {
				markedElements.add(fword);
				design(fword, true);
			}
			editor.setCaretPosition(fword.getStartPosition() + 1);
			if(Model.getViewer() != null)
				Model.getViewer().setRoot(Model.getIllocutionUnitRoots().getRoot(fword.getWord()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gibt die MyTextPane zurueck
	 * 
	 * @return editor MyTextPane
	 */
	public MyTextPane getEditor() {
		return editor;
	}

	/**
	 * @return doc DefaultStyledDocument
	 */
	public DefaultStyledDocument getDoc() {
		return doc;
	}

	/**
	 * @return Returns the designer.
	 */
	public Designer getDesigner() {
		return designer;
	}
}