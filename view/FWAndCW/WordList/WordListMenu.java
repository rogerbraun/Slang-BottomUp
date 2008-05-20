package view.FWAndCW.WordList;

import view.Superclasses.Menu;
import controller.FWAndCW.WordList.WordListController;

/**
 * Menü zum Bestimmen der Wortlisten
 * @author shanthy
 *
 */
public class WordListMenu extends Menu {
	/**
	 * zufällig erstellte ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param controller WordListController
	 */
	public WordListMenu(WordListController controller) {
		super(controller, "word list");
	}
}
