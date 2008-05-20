/*
 * Erstellt: 02.04.2005
 */

package additionalPrograms.MUSG_Viewer;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * @author Volker Kloebb
 */
abstract class MUSG_Element {

	/**
	 * Font für die Darstellung
	 */
	protected static final Font FONT = new Font("Arial", Font.PLAIN, 14);

	protected int xPosition, yPosition;

	protected Rectangle2D.Float border;

	/**
	 * 
	 *
	 */
	protected MUSG_Element() {
		xPosition = 30;
		yPosition = 30;
	}

	/**
	 * 
	 * @return Rectangle2D
	 */
	Rectangle2D getBounds() {
		return border;
	}

	/**
	 * 
	 * @param x int
	 * @param y int
	 */
	void setUpperLeftPosition(int x, int y) {
		setPosition((int) (x + border.width / 2), (int) (y + border.height / 2));
	}

	/**
	 * 
	 * @param x int
	 * @param y int
	 */
	void setPosition(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
		border.x = x - border.width / 2;
		border.y = y - border.height / 2;
	}

	/**
	 * 
	 * @param point Point
	 */
	void setPosition(Point point) {
		setPosition(point.x, point.y);
	}

	/**
	 * 
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	boolean contains(int x, int y) {
		return border.contains(x, y);
	}

	/**
	 * 
	 * @param r Rectangle2D
	 * @return boolean
	 */
	boolean contains(Rectangle2D r) {
		return border.contains(r);
	}

	/**
	 * 
	 * @return border.width
	 */
	int getWidth() {
		return (int) border.width;
	}

	/**
	 * 
	 * @param g Graphics2D
	 */
	abstract void draw(Graphics2D g);
}