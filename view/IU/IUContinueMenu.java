package view.IU;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import view.Superclasses.Menu;
import controller.IU.IUContinueController;

public class IUContinueMenu extends Menu {

	private static final long serialVersionUID = 1L;

	/**
	 * @param controller IUContinueController
	 */
	public IUContinueMenu(IUContinueController controller) {
		super(controller, "");
		JButton continueButton = new JButton("continue");
		continueButton.addActionListener(controller);
		continueButton.setActionCommand("CONTINUE");
		add(continueButton);
		addSeparator();

		add(new JSeparator(SwingConstants.VERTICAL));

		JButton deleteButton = new JButton("consider/reassign IU");
		deleteButton.addActionListener(controller);
		deleteButton.setActionCommand("DELETE");
		add(deleteButton);
		addSeparator();
		
		add(new JSeparator(SwingConstants.VERTICAL));

		JButton backButton = new JButton("back to sememe groups");
		backButton.addActionListener(controller);
		backButton.setActionCommand("BACK");
		add(backButton);
		addSeparator();

		JButton nextButton = new JButton("go to macro sentence");
		nextButton.addActionListener(controller);
		nextButton.setActionCommand("NEXT");
		add(nextButton);
		addSeparator();

	}
}
