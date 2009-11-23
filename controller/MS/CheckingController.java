/*
 * Created on 13.06.2005
 */

package controller.MS;

import java.awt.event.ActionEvent;

import javax.swing.event.CaretEvent;

import model.MS;
import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Checking;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;

/**
 * @author shanthy
 */
public class CheckingController extends Controller {
	private MeaningUnit mu = null;

	/**
	 * @param m
	 * @param d
	 */
	public CheckingController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * @param e
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		int position = e.getDot();
		IllocutionUnit iu =  Model.getChapter().getIllocutionUnitAtPosition(position);
		IllocutionUnitRoots iurs = Model.getIllocutionUnitRoots();
		IllocutionUnitRoot iur = iurs.getRoot(iu);
		System.out.println("MS: illocution unit:" + iur);
		System.out.println("MS: caret position:" + position);
		mu = iur.getMeaningUnitAtPosition(position);
	}

	/**
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("CREATE")) {
			if (mu==null) {
				System.out.println("MS: keine mu an dieser Stelle");
				return;
			}
			Checking checking = new Checking(Model.getIllocutionUnitRoots(), mu);
			 //TODO
			MacroSentence ms = new MacroSentence(Model.getIllocutionUnitRoots(), checking.getRoot()); 
			model.setMakroSentenceHead(ms);
			model.modelChanged(true);
			model.showMenu("msTail");
			MS.getInstance().getMSFrame().getMSListDialog().displayMS(ms);
			int id = 0;
			id = model.choosePath(ms);
			
			ms.setPath(id);
		} else if (e.getActionCommand().equals("CONTINUE")) {
			model.showMenu("msHead");
			//  Nicht sicher ob hier nicht auch mal msTail rein muss!
			// TODO: überprüfen
		} else if(e.getActionCommand().equals("NEXT")){
			model.showMenu("tgu");
		} else if(e.getActionCommand().equals("BACK")){
			model.showMenu("iu");
		} else
			super.actionPerformed(e);
	}

}
