/*
 * Created on 28.03.2005
 */

package view.Superclasses;

import java.awt.Color;

import model.Model;


/**
 * Superklasse für die Designer
 * gibt für das übergebene Objekt die entsprechende Hintergrundfarbe oder Vordergrundfarbe zurück
 * @author shanthy
 */
public class Designer {

	/**
	 * @param o
	 *            Object
	 * @return black
	 */
	public Color getColor(@SuppressWarnings("unused") Object o) {
		return Model.BLACK;
	}

	/**
	 * @param o
	 *            Object
	 * @return white
	 */
	public Color getBackgroundColor(@SuppressWarnings("unused") Object o) {
		return Model.WHITE;
	}

	/**
	 * @param o
	 *            Object
	 * @return false
	 */
	public boolean hasBackground(@SuppressWarnings("unused") Object o) {
		return false;
	}
}
