/*
 * Created on 28.03.2005
 */

package view.FWAndCW;

import javax.swing.JButton;

import view.Superclasses.Menu;
import controller.FWAndCW.IdentifyFWandCWController;

/**
 * Menü zum Bestimmen von FW und CW
 * @author shanthy, erweitert durch thomas
 */
public class FWAndCWMenu extends Menu {

	/**
	 * zufällig erstelle ID
	 */
	private static final long serialVersionUID = 3901025711201459428L;

	/**
	 * @param controller IdentifyFWandCWController
	 */
	public FWAndCWMenu(IdentifyFWandCWController controller) {
		super(controller, "<html>select if a word is a<font color=\"00c800\"> function word</font> or a<font color=\"ff0000\"> semantic constitutive word</font></html>");
		
		JButton continueButton = new JButton("go to MU");
		continueButton.setActionCommand("CONTINUE");
		continueButton.addActionListener(controller);
		addSeparator();
		add(continueButton);
	}
}