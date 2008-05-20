/*
 * Created on 01.04.2005
 */

package view.MU;

import view.Superclasses.Menu;
import controller.MU.MUController;

/**
 * Menü zum erstellen von Meaning Units
 * @author shanthy
 */
public class MUMenu extends Menu {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = -6960723851286029422L;

	/**
	 * @param controller MUController
	 */
	public MUMenu(MUController controller) {
		super(controller, "x for break, to go to sememe groups or to go back to FW and CW");
	}
}
