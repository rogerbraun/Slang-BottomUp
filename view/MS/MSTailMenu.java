/*
 * Created on 13.07.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package view.MS;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;

import view.Superclasses.Menu;
import controller.MS.MSTailController;


/**
 *  
 * @author shanthy
 */
public class MSTailMenu extends Menu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051971223657608215L;

	private JLabel label;

	/**
	 * @param controller
	 */
	public MSTailMenu(MSTailController controller) {
		super(controller, "add illocution units to: ");
		label = new JLabel();
		JButton create = new JButton("add");
		create.addActionListener(controller);
		create.setActionCommand("ADD");
		JButton nextMS = new JButton("next ms");
		nextMS.addActionListener(controller);
		nextMS.setActionCommand("NEXT");
		addSeparator();
		add(label);
		addSeparator();
		add(create);
		addSeparator();
		add(nextMS);	
	}

	/**
	 * @param color
	 */
	public void setLabelColor(Color color) {
		label.setForeground(color);
	}
}
