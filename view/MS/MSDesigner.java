/*
 * Created on 13.06.2005
 */

package view.MS;

import java.awt.Color;

import view.Superclasses.Designer;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;

/**
 * Designer für die Darstellung der Macrosätze
 * @author shanthy
 */
public class MSDesigner extends Designer {

	/**
	 * cyan
	 */
	private static Color MS0 = Color.CYAN;

	/**
	 * gelb
	 */
	private static Color MS1 = Color.YELLOW;

	/**
	 * blau
	 */
	private static Color MS2 = Color.BLUE;

	/**
	 * grau
	 */
	private static Color MS3 = Color.GRAY;

	/**
	 * grün
	 */
	private static Color MS4 = Color.GREEN;

	/**
	 * magenta
	 */
	private static Color MS5 = Color.MAGENTA;

	/**
	 * orange
	 */
	private static Color MS6 = Color.ORANGE;

	/**
	 * pink
	 */
	private static Color MS7 = Color.PINK;

	/**
	 * hellgrau
	 */
	private static Color MS8 = Color.LIGHT_GRAY;

	/**
	 * grau
	 */
	private static Color MS9 = Color.RED;

	/**
	 * @param o
	 *            Object
	 * @return white
	 */
	@Override
	public Color getBackgroundColor(Object o) {
		if (o instanceof MacroSentence) {
			MacroSentence ms = (MacroSentence) o;
			int index = ms.getIndex() % 10;
			switch (index) {
			case 0:
				return MS0;
			case 1:
				return MS1;
			case 2:
				return MS2;
			case 3:
				return MS3;
			case 4:
				return MS4;
			case 5:
				return MS5;
			case 6:
				return MS6;
			case 7:
				return MS7;
			case 8:
				return MS8;
			case 9:
				return MS9;
			}
		}
		return super.getBackgroundColor(o);
	}

	/**
	 * @param o
	 *            Object
	 * @return false
	 */
	@Override
	public boolean hasBackground(Object o) {
		if (o instanceof MacroSentence)
			return true;
		return false;
	}
}
