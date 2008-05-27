package controller.FWAndCW.WordList;

import java.awt.event.ActionEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
import view.FWAndCW.WordList.WordListPanel;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.WordListElement;

/**
 * Controller, um die Wortlisten zu bestimmen
 * @author shanthy
 *
 */
public class WordListController extends Controller implements
		ListSelectionListener {

	/**
	 * das zu bestimmende Constitutive Word
	 */
	private ConstitutiveWord cw;

	/**
	 * Constitutive Word, das unabhaengig von Kapitel gespeichert ist
	 */
	private WordListElement wle;

	/**
	 * übernimmt bereits gemachte Bestimmungen
	 */
	private boolean automaticAnalysis;

	/**
	 * wird zur automatischen Analyse benötigt
	 */
	private int position = 0;
	
	private int oldAssig_DB_ID = -1;
	
	private int oldWLE_DB_ID = -1;

	/**
	 * @param model Model
	 * @param designer 
	 */
	public WordListController(Model model) {
		super(model, new Designer());
	}

	/**
	 * die CWs aus der DB laden, die bereits in der DB vorhanden sind
	 */
	public void loadWLEs() {
		DBC dbc = Model.getDBC();
		try {
			dbc.open();
			model.getWordListPanel().getWLEChoice().setListData(dbc.loadWordListElement(cw.getContent(), cw.getWord().getLanguage()));
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param cw ConstitutiveWord
	 */
	public void setCW(ConstitutiveWord cw) {
		this.cw = cw;
		this.oldAssig_DB_ID = cw.getAssignation().getDB_ID();
		try {
			Model.getDBC().open();
			this.wle = Model.getDBC().loadWordListElementWithAssigID(oldAssig_DB_ID);
			Model.getDBC().close();
		} catch (Exception e) {
			System.out.println("No match found in WLEs for assigID: " + oldAssig_DB_ID);
		//	e.printStackTrace();
			this.wle = new WordListElement(cw.getContent());
		}
	}

	/**
	 * 
	 * @param wle frueher lcw LonleyConstitutiveWord
	 */
	private void setWLE(WordListElement wle) {
		this.wle = wle;
		// alte zeile: model.getWordListPanel().setAssignation(wle.getAssignation());
		//model.getWordListPanel().setAssignation((TR_Assignation) wle.getAssignation());
		this.oldWLE_DB_ID = wle.getDB_ID();
	}

	/**
	 * setzt die entsprechenden Assignations, wenn man ein WLE aus der Liste auswaehlt
	 * @param wle
	 */
	private void setWLEAssignation(WordListElement wle) {
		TR_Assignation assig = wle.getAssignation();
		model.getWordListPanel().setAssignation(assig);
	}
	
	/**
	 * @param e ListSelectionEvent
	 * 
	 */
	public void valueChanged(@SuppressWarnings("unused") ListSelectionEvent e) {
		Object o = model.getWordListPanel().getWLEChoice().getSelectedValue();
		if (o != null && o != wle) {
			setWLEAssignation((WordListElement) o);
		}
	}

	/**
	 * @param e ActionEvent
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		DBC dbc = Model.getDBC();
		if (e.getSource() == model.getWordListPanel().getResetButton()) {
			model.getWordListPanel().reset();
			model.getWordListPanel().getWLEChoice().clearSelection();
		} else if (e.getSource() == model.getWordListPanel().getAssignButton()) {
			if (cw != null) {
				// alte assignation �berschreiben, wenn vorhanden
				TR_Assignation tr_assig = model.getWordListPanel().getAssignation();
				if(oldAssig_DB_ID != -1)
					cw.setAssignation(tr_assig, oldAssig_DB_ID);
				else
					cw.setAssignation(tr_assig);

				int assigID = tr_assig.getDB_ID();
				if(oldWLE_DB_ID != -1)
					wle.setAssignation(tr_assig, oldWLE_DB_ID);
				else
					wle.setAssignation(tr_assig);
				
				
//				if (automaticAnalysis) {
					/*ConstitutiveWord constitutiveWord = (ConstitutiveWord) Model
							.getIllocutionUnitRoots().getConstitutiveWords()
							.get(position);
					if (constitutiveWord == null) {
						model.getWordListPanel();
						model.showMenu("fwAndCW");
					} else {*/
						//TR_Assignation assig = constitutiveWord.getAssignation();
//						TR_Assignation assig = cw.getAssignation();
						
		/*				if (assig != null && 
//							assig.getGenusBinary() == 0 && assig.getNumerusBinary() == 0 && 
//							assig.getDeterminationBinary() == 0 && assig.getCasesBinary() == 0 && 
//							assig.getPersonsBinary() == 0 && assig.getConjugationsBinary() == 0 &&
//							assig.getTempusBinary() == 0 && assig.getDiathesesBinary() == 0 && 
//							assig.getWordclassesBinary() == 0 && assig.getWordsubclassConnectorsBinary() ==0 &&
//							assig.getWordsubclassVerbsBinary() == 0 && assig.getWordsubclassPrepositionsBinary() == 0 &&
//							assig.getWordsubclassSignsBinary() == 0)
							assig.getGenera().length == 0 && assig.getNumeri().length == 0 &&
							assig.getDeterminations().length == 0 && assig.getCases().length == 0 && 
							assig.getPersons().length == 0 && assig.getConjugations().length == 0 &&
							assig.getTempora().length == 0 && assig.getDiatheses().length == 0 && 
							assig.getWordclasses().length == 0 && assig.getWordsubclassesConnector().length ==0 &&
							assig.getWordsubclassesVerb().length == 0 && assig.getWordsubclassesPreposition().length == 0 &&
							assig.getWordsubclassesSign().length == 0
							)
						{*/
				//			position++;
				//			setCW(constitutiveWord);
//						setCW(cw);
//						System.out.println();
				//		}
				//	}
//				}// else {
				//	model.getWordListPanel();
				//	model.showMenu("fwAndCW");
				//}
			}
		
		// ----------------------------------
/*		} else if (e.getSource() == model.getWordListPanel().getRemoveButton()) {
			if (wle != null) {
				try {
					dbc.open();
					dbc.removeLonleyConstitutiveWord(wle);
					dbc.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
				loadWLEs();
			}
*/		} else if (e.getSource() == model.getWordListPanel().getSaveButton()) {
			if (cw != null) {
				WordListElement nwle = new WordListElement(cw.getContent());
				nwle.setAssignation(model.getWordListPanel().getAssignation());

				try {
					dbc.open();
					dbc.saveWordListElements(nwle);
					dbc.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
				loadWLEs();
			}
		}
		model.getWordListPanel();
		model.showMenu("fwAndCW");
	}

	/**
	 * @return Returns the automaticAnalysis.
	 */
	public boolean isAutomaticAnalysis() {
		return automaticAnalysis;
	}

	/**
	 * @param automaticAnalysis
	 *            The automaticAnalysis to set.
	 */
	public void setAutomaticAnalysis(boolean automaticAnalysis) {
		this.automaticAnalysis = automaticAnalysis;
	}

	/**
	 * @return Returns the cw.
	 */
	public ConstitutiveWord getCw() {
		return cw;
	}
	
	/**
	 * wird jedes mal aufgerufen, wenn das Menü angezeigt wird
	 */
	@Override
	public void doAction() {
		model.getView().designText(Model.getIllocutionUnitRoots());
		if (getCw() != null) {
			setAutomaticAnalysis(true);
		}
	}


}
