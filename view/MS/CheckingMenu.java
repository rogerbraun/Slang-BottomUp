package view.MS;

import javax.swing.JButton;

import view.Superclasses.Menu;
import controller.MS.CheckingController;

/**
 * @author shanthy
 */
public class CheckingMenu extends Menu {
	private static final long serialVersionUID = 6464651315760531057L;

	/**
	 * @param controller
	 */
	public CheckingMenu(CheckingController controller) {
		super(controller, "makro sentence");
		JButton create = new JButton("create");
		create.addActionListener(controller);
		create.setActionCommand("CREATE");
		addSeparator();
		add(create);
	}
}
