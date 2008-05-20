package view.IU;

import view.Superclasses.Menu;
import controller.IU.IUController;

/**
 * Menü zum Bestimmen der Pfade von Illocution Units
 * @author shanthys
 */
public class IUMenu extends Menu {
	private static final long serialVersionUID = -802734514387515531L;

	/**
	 * @param controller
	 */
	public IUMenu(IUController controller) {
		super(controller, "x for break");
	}
}
