/*
 * Erstellt: 02.04.2005
 */

package additionalPrograms.MUSG_Viewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;

/**
 * @author Volker Kloebb
 */
class SG_Element extends MUSG_Element {

	/**
	 * Zeichenfarbe
	 */
	private static Color color = new Color(200, 200, 0);

	/**
	 * erstes Element
	 */
	private MU_Element start;

	/**
	 * zweites Element
	 */
	private MU_Element end;

	/**
	 * Funktionswort
	 */
	private String content;
	
	/**
	 * 
	 * @param sg SememeGroup
	 * @param g Graphics2D
	 * @param start MU_Element
	 * @param end MU_Element
	 */
	SG_Element(SememeGroup sg, Graphics2D g, MU_Element start, MU_Element end) {
		this.start = start;
		this.end = end;

		if (sg.getFunctionWord() == null)
			content = "---";
		else
			content = sg.getFunctionWord().getContent();

		Rectangle2D.Float textBorder = (Rectangle2D.Float) FONT
				.getStringBounds(content, g.getFontRenderContext());
		int width = (int) textBorder.width + 20;
		int heigth = (int) textBorder.height + 10;

		border = new Rectangle2D.Float(xPosition - width / 2, yPosition
				- heigth / 2, width, heigth);
	}

	/**
	 * @param g Graphics2D
	 */
	@Override void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.drawLine(xPosition, yPosition, start.xPosition, start.yPosition);
		g.drawLine(xPosition, yPosition, end.xPosition, end.yPosition);

		g.setColor(color);
		g.fill(border);
		g.setColor(Color.BLACK);
		g.draw(border);
		g.setFont(FONT);
		g.drawString(content, border.x + 10, border.y + 18);
	}

}
