/*
 * Created on 06.04.2005
 */

package controller.MU;

import java.awt.event.ActionEvent;

import javax.swing.event.CaretEvent;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/**
 * Controller, um Meaning Units zu verändern
 * @author shanthy
 */
public class ChangeMUController extends Controller {
	/**
	 * die zu verändernte Meaning Unit
	 */
	private MeaningUnit meaningUnit;

	/**
	 * @param m Model
	 * @param d Design
	 */
	public ChangeMUController(Model m, Designer d) {
		super(m, d);
	}

	/**
	 * @param e CaretEvent
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		Token token = Model.getChapter().getTokenAtPosition(e.getDot());
		if (token instanceof Word) {
			FunctionWord functionWord = Model.getIllocutionUnitRoots()
					.getFunctionWordAtPosition(e.getDot());
			ConstitutiveWord constitutiveWord = Model.getIllocutionUnitRoots()
					.getConstitutiveWordAtPosition(e.getDot());
			if (functionWord != null) {
				meaningUnit = functionWord.getMeaningUnit();
			} else if (constitutiveWord != null) {
				meaningUnit = constitutiveWord.getMeaningUnit();
			}
		}
	}

	/**
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("REMOVE")) {
			if (meaningUnit != null) {
				meaningUnit.remove();
				meaningUnit = null;
				model.getView().reset(meaningUnit);
				model.modelChanged(true);
				model.startPragWo();
			}
		} else if (command.equals("NEXT")) {
			model.showMenu("rel");
		} else
			super.actionPerformed(e);
	}

	/**
	 * wird jedes mal ausgefuehrt, wenn das Menue angezeigt wird
	 */
	@Override
	public void doAction() {
		if (Model.getIllocutionUnitRoots().getSememeGroups().isEmpty()) {
			Model.getViewer()
					.setRoot(Model.getIllocutionUnitRoots().getRoot(0));
			model.getView().designText(Model.getIllocutionUnitRoots());
		} else {
			model.showMenu("sg");
		}
	}
}