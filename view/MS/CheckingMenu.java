package view.MS;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

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
		
		add(new JSeparator(SwingConstants.VERTICAL));

		JButton deleteButton = new JButton("consider/reassign MS");
		deleteButton.addActionListener(controller);
		deleteButton.setActionCommand("DELETE");
		add(deleteButton);
		addSeparator();
		
		add(new JSeparator(SwingConstants.VERTICAL));

		JButton backButton = new JButton("back to IU");
		backButton.addActionListener(controller);
		backButton.setActionCommand("BACK");
		add(backButton);
		addSeparator();

		JButton nextButton = new JButton("go to TGU"); // TGU = textgrammatische Einheit
													   // U fuer "Unit"
		nextButton.addActionListener(controller);
		nextButton.setActionCommand("NEXT");
		add(nextButton);
		addSeparator();
	}
}
