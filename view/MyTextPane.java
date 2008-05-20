/*
 * Created on 09.12.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Model;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/**
 * ein erweitertes JTextPane, in dem Textteile hervorgehoben und der Rest grau
 * gemacht werden kann
 * 
 * @author shanthy
 */
public class MyTextPane extends JTextPane {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = 236954777915132145L;

	/**
	 * damit der Text formatiert werden kann
	 */
	private DefaultStyledDocument doc;

	/**
	 * Text grau machen
	 */
	private static SimpleAttributeSet GREY;

	/**
	 * Text in der Standardformatierung
	 */
	private static SimpleAttributeSet NORMAL;

	/**
	 * @param view
	 *            View
	 */
	public MyTextPane(View view) {
		super();
		doc = view.getDoc();
		GREY = new SimpleAttributeSet();
		StyleConstants.setFontFamily(GREY, Model.PLAINFONT.getFontName());
		StyleConstants.setFontSize(GREY, Model.PLAINFONT.getSize());
		StyleConstants.setBold(GREY, true);
		StyleConstants.setForeground(GREY, Color.LIGHT_GRAY);

		NORMAL = new SimpleAttributeSet();
		StyleConstants.setFontFamily(NORMAL, Model.PLAINFONT.getFontName());
		StyleConstants.setFontSize(NORMAL, Model.PLAINFONT.getSize());
		StyleConstants.setBold(NORMAL, true);
		StyleConstants.setForeground(NORMAL, Color.BLACK);
		setEditable(false);
	}

	/**
	 * ein Wort hervorheben, der Rest in grau einfärben
	 * 
	 * @param word
	 *            Word das hervorzuhebende Wort
	 * @param b
	 *            boolean soll die bereits vorhandene Formatierung überschrieben
	 *            werden
	 */
	public void setHighlighted(Word word, boolean b) {
		doc.setCharacterAttributes(0, word.getStartPosition() - 1, GREY, b);
		doc.setCharacterAttributes(word.getEndPosition() + 1, super.getText()
				.length(), GREY, b);
		repaint();
	}

	/**
	 * Graufärbung zurück setzen
	 */
	public void reset() {
		doc.setCharacterAttributes(0, super.getText().length(), NORMAL, false);
	}
}
