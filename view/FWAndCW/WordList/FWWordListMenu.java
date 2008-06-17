package view.FWAndCW.WordList;

import view.Superclasses.Menu;
import controller.FWAndCW.WordList.FWWordListController;

public class FWWordListMenu extends Menu {
	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param controller WordListController
	 */
	public FWWordListMenu(FWWordListController controller) {
		super(controller, "fwWordList");
	}
}
