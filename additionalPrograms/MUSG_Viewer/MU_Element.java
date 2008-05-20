/*
 * Erstellt: 02.04.2005
 */

package additionalPrograms.MUSG_Viewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
/**
 * @author Volker Kloebb
 */
class MU_Element extends MUSG_Element {

	private static Color color = new Color(0, 200, 0);

	private MeaningUnit mu;

	private String content;

	/**
	 * 
	 * @param mu MeaningUnit
	 * @param g Graphics2D
	 */
	MU_Element(MeaningUnit mu, Graphics2D g) {
		this.mu = mu;

		if (mu.getFunctionWord() == null)
			content = mu.getConstitutiveWord().getContent();
		else
			content = mu.getFunctionWord().getContent() + " "
					+ mu.getConstitutiveWord().getContent();

		Rectangle2D.Float textBorder = (Rectangle2D.Float) FONT
				.getStringBounds(content, g.getFontRenderContext());
		int width = (int) textBorder.width + 20;
		int heigth = (int) textBorder.height + 10;

		border = new Rectangle2D.Float(xPosition - width / 2, yPosition
				- heigth / 2, width, heigth);
	}

	/**
	 * 
	 * @return MeaningUnit
	 */
	MeaningUnit getMeaningUnit() {
		return mu;
	}

	/**
	 * @param g Graphics2D
	 */
	@Override void draw(Graphics2D g) {
		g.setColor(color);
		g.fill(border);
		g.setColor(Color.BLACK);
		g.draw(border);
		g.setFont(FONT);
		g.drawString(content, border.x + 10, border.y + 18);
	}

}