/*
 * Created on 02.04.2005
 */

package controller.MU;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.WordNotInIllocutionUnitException;


/**
 * Controller, falls das Bestimmen der Meaning Units unterbrochen wurde
 * Erweitert um die Moeglichkeit die bereits gemachten Zuweisungen zu betrachten und 
 * eventuell zu veraendern.
 * @author shanthy
 * @author extended by thomas
 */
public class MUContinueController extends Controller {

	/**
	 * @param m Model
	 * @param d Designer
	 */
	public MUContinueController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CONTINUE")) {
			model.showMenu("mu");
		} else if(e.getActionCommand().equals("NEXT")){
			model.showMenu("sgCreate");
		} else if(e.getActionCommand().equals("BACK")){
			model.showMenu("fwAndCW");
		} else
			super.actionPerformed(e);
	}

	/**
	 * @param e CaretEvent
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		MeaningUnit mu = null;
		FunctionWord fword = Model.getIllocutionUnitRoots().getFunctionWordAtPosition(e.getDot());
		if (fword != null) {
			// falls das Funktionswort zu einer MeaningUnit gehoert
			if (fword.getMeaningUnit() != null) {
				// die MU im Text markieren
				mu = fword.getMeaningUnit();
			}
		}
		ConstitutiveWord cword = Model.getIllocutionUnitRoots()
				.getConstitutiveWordAtPosition(e.getDot());
		// falls das angeklickte Wort ein CW ist
		if (cword != null) {
			if (cword.getMeaningUnit() != null) {
				// die MU im Text markieren
				mu = cword.getMeaningUnit();
			}
		}
		if(mu != null)
		{
			// zeige zuweisung und change-menu
			try {
				int pfadid = mu.getPath();
				int numeruspathid = mu.getNumerusPath();
				Model.getDBC().open();
				String pathname = Model.getDBC().getPaths().getNode(pfadid).getFullPath();
				String numerusPathName = Model.getDBC().getNumerusPaths().getNode(numeruspathid).getFullPath();
				String[] optionen = { "reset mu", "ok" };
				String name = new String();
				name = mu.toWrittenString();
				/*if(mu.getFunctionWord() != null)
					name = "MU: " + mu.getFunctionWord().getContent() + " " + mu.getConstitutiveWord().getContent();
				else 
					name = "MU: " + mu.getConstitutiveWord().getContent();
				*/
				int choice = JOptionPane.showOptionDialog(model, 
						"Path: " + pathname + "\nNumerus path: " + numerusPathName, name,
						0,
						JOptionPane.YES_NO_OPTION, null, optionen,
						optionen[1]);
				if(choice == JOptionPane.YES_OPTION)
				{
					// neue Zuweisung, setze MU zurueck
					ConstitutiveWord cwordToAssign = mu.getConstitutiveWord();
					mu.remove();
					model.modelChanged(true);
					model.saveWithoutMessage();
					analyseMU(cwordToAssign);
				}
				
				Model.getDBC().close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public void analyseMU(ConstitutiveWord cword){
		try {
			Model.getDBC().open();
			if (cword.getMeaningUnit() == null) 
			{
	//			model.getView().scrollTo(cword);
				String[] optionen = { "yes", "part of MU", "not sure"};
				int choice = JOptionPane.showOptionDialog(model, "\"" + cword
						+ "\"  seems to be a meaning unit.", "Meaning unit",
						0,
						JOptionPane.INFORMATION_MESSAGE, null, optionen,
						optionen[0]);
				if (choice == JOptionPane.YES_OPTION) 
				{
					MeaningUnit mu;
					try {
						mu = new MeaningUnit(cword.getRoot(), cword);
						Model.getViewer().setRoot(cword.getRoot());
						model.getView().reset();
						int id = 0;
						while (id == 0) {
							id = model.choosePath(mu);
						}
						mu.setPath(id);
						int id2 = 0;
			//			WordListElement wle = null;
			//			try {
			//				System.out.println("------------------------------");
			//				 wle = model.getDBC().loadWordListElement(cword.getContent());
			//				 System.out.println(wle.getContent() + "\n" + wle.toString());
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}

					/*	Vector relations = new Vector();
						for (Object assignation : wle.getAssignations()) {
						  
							try {
								relations = Model.getDBC().loadRelations((TR_Assignation) assignation);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}*/
				//		Vector<TR_Assignation> wleV = wle.getAssignations();
				//		for(int k = 0; k < wleV.size(); k++)
				//		{
				//			long a = wleV.elementAt(k).getWordclassesBinary();
				//			Vector vec = wleV.elementAt(k).getWordclasses();
				//			Object nu = wleV.elementAt(k).getWordclassOptions();
				//			if(wleV.elementAt(k).getWordclassesBinary() == data.TR_Assignation.WORDCLASS_NOUN)
							{
			  					//------------------------------------------
			  					//	cword.getAssignation() == null
			  					// entfernen wenn assignations mal gehn
								id2 = model.chooseNumerusPath(mu);
								mu.setNumerusPath(id2);
							}
				//		}
						//schreibe mu gleich in die datenbank
						Model.getDBC().saveIllocutionUnitRoots(Model.getIllocutionUnitRoots());
			//			continue
					} catch (WordNotInIllocutionUnitException e) {
						e.printStackTrace();
					}
				} else if (choice == JOptionPane.NO_OPTION) {
					model.setConstitutiveWord(cword);
					if (Model.getIllocutionUnitRoots().getMeaningUnits().isEmpty())
						JOptionPane.showMessageDialog(model, "Please select the belonging function word");
					model.showMenu("muTwoPart");
				} else if (choice == JOptionPane.CLOSED_OPTION) {
					model.getView().reset();
				}
			}
			Model.getDBC().close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
