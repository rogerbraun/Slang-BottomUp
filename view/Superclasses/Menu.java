/*
 * Created on 28.03.2005
 */

package view.Superclasses;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import controller.Controller;

/**
 * Superklasse für die Menüs
 * @author shanthy
 */
public class Menu extends JPanel {

	/**
	 * zufällig erstelle ID
	 */
	private static final long serialVersionUID = 125593878433554229L;

	/**
	 * der Controller, der zu diesem Menü gehört
	 */
	private Controller controller;

	/**
	 * Toolbar für die Buttons
	 */
	private JToolBar menuToolBar;

	/**
	 * Oberklasse aller Menues: Festlegen der Rahmenbedingungen
	 * 
	 * @param controller
	 *            Controller
	 * @param label
	 *            String
	 */
	public Menu(Controller controller, String label) {
		super(new BorderLayout());
		menuToolBar = new JToolBar();
		add(menuToolBar, BorderLayout.PAGE_START);
		this.controller = controller;
		JLabel jLabel = new JLabel(label);
		jLabel.setOpaque(true);
		add(jLabel);
		addSeparator();
		addSeparator();
	}

	/**
	 * add ueberschreiben
	 * 
	 * @param comp
	 *            JComponent
	 * @return Component
	 */
	public Component add(JComponent comp) {
		comp.setMinimumSize(new Dimension(25, 25));
		return menuToolBar.add(comp);
	}

	/**
	 * remvoe ueberschreiben
	 * 
	 * @param comp
	 */
	public void remove(JComponent comp) {
		menuToolBar.remove(comp);
	}

	/**
	 * einen Separator in die ToolBar einfuegen
	 */
	public void addSeparator() {
		menuToolBar.addSeparator();
	}

	/**
	 * gibt den jeweiligen Controller zurueck
	 * 
	 * @return Controller
	 */
	public Controller getController() {
		return controller;
	}
}
