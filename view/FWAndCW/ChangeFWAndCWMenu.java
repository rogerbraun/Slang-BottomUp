/*
 * Created on 06.04.2005
 */

package view.FWAndCW;

import javax.swing.JButton;

import view.Superclasses.Menu;
import controller.FWAndCW.ChangeFWAndCWController;


/**
 * Menü zum Verändern von Function Word und Constitutive Word
 * @author shanthy
 */
public class ChangeFWAndCWMenu extends Menu {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = -1750088802690984036L;

	/**
	 * @param controller ChangeFWAndCWController
	 */
	public ChangeFWAndCWMenu(ChangeFWAndCWController controller) {
		super(controller, "change FW or CW");
		JButton remove = new JButton("remove");
		JButton finished = new JButton("next step");
		remove.addActionListener(controller);
		finished.addActionListener(controller);
		remove.setActionCommand("REMOVE");
		finished.setActionCommand("NEXT");
		add(remove);
		addSeparator();
		add(finished);
	}
}
