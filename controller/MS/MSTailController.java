/*
 * Created on 13.07.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package controller.MS;

import java.awt.event.ActionEvent;

import javax.swing.event.CaretEvent;

import model.MS;
import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;

/**
 * @author shanthy
 */
public class MSTailController extends Controller {
	private IllocutionUnitRoot iur;

	/**
	 * 
	 */
	public MSTailController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * Auswahl und Einfärben einer IllocutionUnit 
	 **/
	@Override
	public void caretUpdate(CaretEvent e) {
		int position = e.getDot();
		IllocutionUnit iu = Model.getChapter().getIllocutionUnitAtPosition(position);
		if (iu!=null) {
			iur = Model.getIllocutionUnitRoots().getRoot(iu);
		}		
	}

	/**
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("ADD")) {  //füge aktuell ausgewählte iu dem ms hinzu 
			MacroSentence ms = model.getMakroSentenceHead();
			model.modelChanged(true);
			ms.addDependency(iur);
			MS.getInstance().getMSFrame().getMSListDialog().displayMS(ms);
			model.getView().designText(Model.getIllocutionUnitRoots());
		} else if (command.equals("NEXT")) { //nächster ms
			// füge bearbeiteten ms dem vector von ms hinzu
			System.out.println("Model beinhaltet nun " + Model.getIllocutionUnitRoots().getMacroSentences().size() + " Makrosätze.");			
			System.out.println("Sichere aktuellen ms in Model.");
		    Model.getIllocutionUnitRoots().getMacroSentences().add(model.getMakroSentenceHead());
			System.out.println("Model beinhaltet nun " + Model.getIllocutionUnitRoots().getMacroSentences().size() + " Makrosätze.");			
		    System.out.println("Bearbeite nächsten Makrosatz.");
		    model.showMenu("msHead");
		}
			super.actionPerformed(e);
	}


	@Override
	public void doAction() {
		model.getMakroSentenceTailMenu().setLabelColor(
				designer.getBackgroundColor(model.getMakroSentenceHead()));
	}
}
