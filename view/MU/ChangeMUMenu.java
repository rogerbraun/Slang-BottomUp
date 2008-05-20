/*
 * Created on 06.04.2005
 */

package view.MU;

import javax.swing.JButton;

import view.Superclasses.Menu;
import controller.Controller;


/**
 * Menü, zum Verändern von Meaning Units
 * @author shanthy
 */
public class ChangeMUMenu extends Menu {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = -5520624351960762573L;

	/**
	 * @param controller Controller
	 */
	public ChangeMUMenu(Controller controller) {
		super(controller, "change MUs");
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
