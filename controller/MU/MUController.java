/*
 * Created on 01.04.2005
 */

package controller.MU;

import java.util.Vector;

import javax.swing.JOptionPane;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wordclass;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.WordNotInIllocutionUnitException;

/**
 * Controller, zum Erstellen von Meaning Units, die nur aus einem Constitutive
 * Word bestehen
 * 
 * @author shanthy
 */
public class MUController extends Controller {
	/**
	 * da sich die Analyse in einer Schleife befindet, kann diese unterbrochen
	 * werden
	 */
	private boolean breaking;

	/**
	 * Meaning Unit besteht aus zwei Teilen, ein anderes Menü muss gestartet
	 * werdn
	 */
	private boolean twoPart;
	
	/**
	 * @param m
	 *            Model
	 * @param d
	 *            Designer
	 */
	public MUController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * wird jedes mal ausgefuehrt, wenn das Menue angezeigt wird
	 */
	@Override
	public void doAction() {
		acceptFWsAndCWs();
		model.getView().designText(Model.getIllocutionUnitRoots());
		analyseMUs();
	}

	/**
	 * alle Constitutive Words und Function Words akzeptieren, damit ein anderer
	 * in der DB darau zugreifen kann
	 * 
	 */
	private void acceptFWsAndCWs() {
		for (int i = 0; i < Model.getIllocutionUnitRoots()
				.getConstitutiveWords().size(); i++) {
			ConstitutiveWord cw = (ConstitutiveWord) Model
					.getIllocutionUnitRoots().getConstitutiveWords().get(i);
			cw.setAccepted(true);
			model.modelChanged(true);
		}
		for (int i = 0; i < Model.getIllocutionUnitRoots().getFunctionWords()
				.size(); i++) {
			FunctionWord fw = (FunctionWord) Model.getIllocutionUnitRoots()
					.getFunctionWords().get(i);
			fw.setAccepted(true);
			model.modelChanged(true);
		}
	}

	/**
	 * analysieren der meaning units
	 */
	public void analyseMUs() {
		Vector cws = Model.getIllocutionUnitRoots().getConstitutiveWords();
		for (int i = 0; i < cws.size(); i++) {
			ConstitutiveWord cword = (ConstitutiveWord) cws.get(i);
			try {
				Model.getDBC().open();
				if (cword.getMeaningUnit() == null) 
				{
					model.getView().scrollTo(cword);
					String[] optionen = { "yes", "part of MU", "not sure"/*, "undo last assignation" */};
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
						twoPart = true;
						break;
					} else if (choice == JOptionPane.CLOSED_OPTION) {
						breaking = true;
						closeMUDialog();
						break;
					}
				}
				else if(cword.getMeaningUnit() != null && 
						(cword.getAssignation() == null ||
//							cword.getAssignation().getWordclassesBinary() == data.TR_Assignation.WORDCLASS_NOUN)
							cword.getAssignation().hasWordclass(Wordclass.NOUN))
						&& cword.getMeaningUnit().getNumerusPath() == 0) 
				{
				// if the numerus path isn't set ask to do so!
								
				//------------------------------------------
				//	cword.getAssignation() == null
				// entfernen wenn assignations mal gehn	
					Object[] options = {"Assign path id", "Continue"};
				 	
				   	int tmp = JOptionPane.showOptionDialog(Model.getFrames()[0], "The constitutive word " + cword.getContent() + " doesn't have a numerus path id!\nDo you want to assign a numerus path id?",
				 	    "No numerus path set",
				 	    JOptionPane.YES_NO_OPTION,
				 	    JOptionPane.QUESTION_MESSAGE,
				 	    null,
				 	    options,
				 	    options[0]);
				   	if(tmp == JOptionPane.YES_OPTION)
				   	{
						MeaningUnit mu = cword.getMeaningUnit();
						int id2 = 0;
						id2 = model.chooseNumerusPath(mu);
						mu.setNumerusPath(id2);
				   	}
				   	else if(tmp == JOptionPane.CLOSED_OPTION)
				   	{
						breaking = true;
						closeMUDialog();
						break;
				   	}
				}
				// Fuer den Fall, dass die letzte MU betrachtet wurde
				if(i == cws.size()-1)
				{
					// Wurde letzte MU richtig zugeordnet? wenn ja: weiter, wenn nein Schritt zur�ck
				   	Object[] options = {"Consider and/or reassign MU", "Continue with Sememe Groups"};
				 	
				   	int tmp = JOptionPane.showOptionDialog(Model.getFrames()[0], "All MUs considered\nDo you want to continue?",
				 	    "Last mu considered",
				 	    JOptionPane.YES_NO_OPTION,
				 	    JOptionPane.QUESTION_MESSAGE,
				 	    null,
				 	    options,
				 	    options[1]);
				 	if(tmp == JOptionPane.YES_OPTION)
				 	{
				 		breaking = true;
						closeMUDialog();
						break;
				 	}
				 	else
				 	{
				 		model.showMenu("sgCreate");
				 	}
				}
				Model.getDBC().close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		Model.getDBC().close();
		closeMUDialog();
	}

	/**
	 * am Ende des Dialogs, je nach gewähltem Knopf ein anderes Menü starten
	 */
	private void closeMUDialog() {
		model.getView().reset();
		// Meaning Unit besteht aus zwei Teilen
		if (twoPart) {
			model.showMenu("muTwoPart");
			twoPart = false;
		}
		// Analyse wurde unterbrochen
		else if (breaking) {
			model.showMenu("muContinue");
			breaking = false;
		}
		// Meaning Unit verändern
		else if (cwsNotAnalyesed())
			model.showMenu("muChange");
/*		// alle Constitutive Words analysiert, jetzt können die Sememgruppen
		// bestimmt werden
		else if (!cwsNotAnalyesed())
			model.showMenu("sgCreate");*/
	}

	/**
	 * testet, ob alle Constitutive Words einer Meaning Unit zugeordnet wurden
	 * @return boolean
	 */
	private boolean cwsNotAnalyesed() {
		for (int i = 0; i < Model.getIllocutionUnitRoots()
				.getConstitutiveWords().size(); i++) {
			ConstitutiveWord cword = (ConstitutiveWord) Model
					.getIllocutionUnitRoots().getConstitutiveWords().get(i);
			if (cword.getMeaningUnit() == null) {
				return true;
			}
		}
		return false;
	}
}