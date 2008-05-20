/*
 * Created on 01.04.2005
 */

package view.MU;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import view.Superclasses.Menu;
import controller.MU.MUContinueController;


/**
 * Menü, wenn die Bestimmung der Meaning Units unterbrochen wurde
 * @author shanthy
 */
public class MUContinueMenu extends Menu {

	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = 797810877132021929L;

	/**
	 * @param controller MUContinueController
	 */
	public MUContinueMenu(MUContinueController controller) {
		super(controller, "select a word to see the paths!");
		JButton continueButton = new JButton("continue");
		continueButton.addActionListener(controller);
		continueButton.setActionCommand("CONTINUE");
		add(continueButton);
		addSeparator();
		add(new JSeparator(SwingConstants.VERTICAL));
		
		JButton backButton = new JButton("back to FW and CW");
		backButton.addActionListener(controller);
		backButton.setActionCommand("BACK");
		add(backButton);
		
		addSeparator();
		JButton nextButton =  new JButton("go to SG");
		nextButton.addActionListener(controller);
		nextButton.setActionCommand("NEXT");
		add(nextButton);
		addSeparator();
	}
}
