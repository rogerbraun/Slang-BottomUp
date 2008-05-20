/*
 * Created on 06.04.2005
 */

package controller.FWAndCW;

import java.awt.event.ActionEvent;

import javax.swing.event.CaretEvent;

import model.Model;
import view.FWAndCW.FWAndCWDesigner;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/**
 * Controller, um Function Word oder Constitutive Word zu ändern
 * @author shanthy
 */
public class ChangeFWAndCWController extends Controller {
	/**
	 * das zu ändernde FW
	 */
	private FunctionWord functionWord;

	/**
	 * das zu ändernde CW
	 */
	private ConstitutiveWord constitutiveWord;

	/**
	 * @param m Model
	 * @param d FWAndCWDesigner
	 */
	public ChangeFWAndCWController(Model m, FWAndCWDesigner d) {
		super(m,d);
	}

	/**
	 * @param e CaretEvent
	 */ 
	@Override
	public void caretUpdate(CaretEvent e) {
		FunctionWord fword = Model.getIllocutionUnitRoots()
				.getFunctionWordAtPosition(e.getDot());
		if (fword != null) {
			model.getView().design(fword, false);
			functionWord = fword;
		}
		ConstitutiveWord cword = Model.getIllocutionUnitRoots()
				.getConstitutiveWordAtPosition(e.getDot());
		if (cword != null) {
			model.getView().design(cword, false);
			constitutiveWord = cword;
		}
	}

	/**
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("REMOVE")) {
			if (functionWord != null) {
				functionWord.remove();
				model.getView().reset(functionWord);
				functionWord = null;
				model.modelChanged(true);
				model.showMenu("fwAndCW");
			} else if (constitutiveWord != null) {
				constitutiveWord.remove();
				model.getView().reset(constitutiveWord);
				constitutiveWord = null;
				model.modelChanged(true);
				model.showMenu("fwAndCW");
			}
		} else if (command.equals("NEXT")) {
			model.startPragWo();
		} else
			super.actionPerformed(e);
	}


	/**
	 * wird jedes mal ausgefuehrt, wenn das Menue angezeigt wird
	 */
	@Override
	public void doAction() {
		try {
			if (Model.getIllocutionUnitRoots().getMeaningUnits().isEmpty()
					&& !(Model.getIllocutionUnitRoots()
							.getConstitutiveWordAtPosition(
									((Word) Model.getChapter().getWords()
											.get(0)).getStartPosition())
							.isAccepted() || Model.getIllocutionUnitRoots()
							.getFunctionWordAtPosition(
									((Word) Model.getChapter().getWords()
											.get(0)).getStartPosition())
							.isAccepted())) {
				model.getView().scrollTo(
						(Word) Model.getChapter().getWords().firstElement());
				Model.getViewer().setRoot(
						Model.getIllocutionUnitRoots().getRoot(0));
				model.getView().designText(Model.getIllocutionUnitRoots());
			} else {
				model.startPragWo();
			}
		} catch (NullPointerException npe) {
			model.startPragWo();
		}
	}
}