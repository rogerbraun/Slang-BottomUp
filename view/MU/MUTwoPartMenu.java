/*
 * Created on 01.04.2005
 */

package view.MU;

import javax.swing.JButton;

import view.Superclasses.Menu;
import controller.MU.MUTwoPartController;

/**
 * Menü für die Bestimmung von Meaning Units, die aus Function Word und
 * Constitutive Word bestehen
 * 
 * @author shanthy
 */
public class MUTwoPartMenu extends Menu {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = 976183137260966505L;

	/**
	 * @param controller MUTwoPartController
	 */
	public MUTwoPartMenu(MUTwoPartController controller) {
		super(controller, "find the belonging fw");
		JButton createMU = new JButton("create MU");
		createMU.addActionListener(controller);
		createMU.setActionCommand("CREATE");
		add(createMU);
	}
}
