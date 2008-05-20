/*
 * Created on 01.04.2005
 */

package controller.MU;

import java.awt.event.ActionEvent;

import javax.swing.event.CaretEvent;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.WordNotInIllocutionUnitException;

/**
 * Erstellen von Meaning Units, die aus einem FW und einem CW bestehen
 * 
 * @author shanthy
 */
public class MUTwoPartController extends Controller {
	/**
	 * das Funktionswort der Meaning Unit
	 */
	private FunctionWord fword;

	/**
	 * @param m
	 *            Model
	 * @param d
	 *            Designer
	 */
	public MUTwoPartController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * das Funktionswort für die Meaning Unit bestimmen
	 * 
	 * @param e
	 *            CaretEvent
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		Token token = Model.getChapter().getTokenAtPosition(e.getDot());
		if (token instanceof Word) {
			fword = Model.getIllocutionUnitRoots().getFunctionWordAtPosition(
					e.getDot());
			model.getView().design(fword, true);
		}
	}

	/**
	 * Create Meaning Unit
	 * 
	 * @param e
	 *            ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("CREATE")) {
			if (fword != null) {
				MeaningUnit mu;
				try {
					mu = new MeaningUnit(fword.getRoot(), fword, model.getConstitutiveWord());
					model.modelChanged(true);
					model.getView().reset();
					int id = 0;
					while (id == 0) {
						id = model.choosePath(mu);
					}
					mu.setPath(id);
					
					int id2 = 0;
		  			//	if(blabla.getAssignations() == data.TR_Assignation.WORDCLASS_NOUN)
						{
		  					//------------------------------------------
		  					//	cword.getAssignation() == null
		  					// entfernen wenn assignations mal gehn
							id2 = model.chooseNumerusPath(mu);
						}
					mu.setNumerusPath(id2);
					
					try {
						//gleich in db speichern
						Model.getDBC().open();
						Model.getDBC().saveIllocutionUnitRoots(Model.getIllocutionUnitRoots());
						Model.getDBC().close();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
					
					model.showMenu("mu");
				} catch (WordNotInIllocutionUnitException e1) {
					e1.printStackTrace();
				}
			}
		} else
			super.actionPerformed(e);
	}

	/**
	 * wird beim Anzeigen des Menüs aufgerufen: zum ausgewählten Constitutive
	 * Word scrollen
	 */
	@Override
	public void doAction() {
		model.getView().scrollTo(model.getConstitutiveWord());
	}
}