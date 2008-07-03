package controller.FWAndCW.WordList;

import java.awt.event.ActionEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
import view.Superclasses.Designer;

import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.WordListElement;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.DBC_ConnectionException;

public class FWWordListController extends Controller implements
ListSelectionListener {

	/**
	 * das zu bestimmende Function Word
	 */
	private FunctionWord fw;

	/**
	 * Constitutive Word, das unabhaengig von Kapitel gespeichert ist
	 */
	private WordListElement wle;

	/**
	 * Ã¼bernimmt bereits gemachte Bestimmungen
	 */
	private boolean automaticAnalysis;

	/**
	 * wird zur automatischen Analyse benÃ¶tigt
	 */
	private int position = 0;
	
	private int oldAssig_DB_ID = -1;
	
	private int oldWLE_DB_ID = -1;

	/**
	 * @param model Model
	 * @param designer 
	 */
	public FWWordListController(Model model) {
		super(model, new Designer());
	}

	/**
	 * die WLEs aus der DB laden, die bereits in der DB vorhanden sind
	 */
	public void loadWLEs() {
		DBC dbc = Model.getDBC();
		try {
			dbc.open();
			model.getFWWordListPanel().getWLEChoice().setListData(dbc.loadWordListElement(fw.getContent(), fw.getWord().getLanguage()));
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fw FunctionWord
	 */
	public void setFW(FunctionWord fw) {
		this.fw = fw;
		this.oldAssig_DB_ID = fw.getAssignation().getDB_ID();
		try {
			Model.getDBC().open();
			this.wle = Model.getDBC().loadWordListElementWithAssigID(oldAssig_DB_ID);
			Model.getDBC().close();
		} catch (Exception e) {
			System.out.println("No match found in WLEs for assigID: " + oldAssig_DB_ID);
		//	e.printStackTrace();
			this.wle = new WordListElement(fw.getContent());
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
		model.getFWWordListPanel().setAssignation(assig);
	}
	
	/**
	 * @param e ListSelectionEvent
	 * 
	 */
	public void valueChanged(@SuppressWarnings("unused") ListSelectionEvent e) {
		Object o = model.getFWWordListPanel().getWLEChoice().getSelectedValue();
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
		try {
			dbc.open();
			if (e.getSource() == model.getFWWordListPanel().getResetButton()) {
				model.getFWWordListPanel().reset();
				model.getFWWordListPanel().getWLEChoice().clearSelection();
			} else if (e.getSource() == model.getFWWordListPanel().getAssignButton()) {
				if (fw != null) {
					// alte assignation überschreiben, wenn vorhanden
					TR_Assignation tr_assig = model.getFWWordListPanel().getAssignation();
					if(oldAssig_DB_ID != -1)
						fw.setAssignation(tr_assig, oldAssig_DB_ID);
					else
						fw.setAssignation(tr_assig);

					int assigID = tr_assig.getDB_ID();
					if(oldWLE_DB_ID != -1)
						wle.setAssignation(tr_assig, oldWLE_DB_ID);
					else
						wle.setAssignation(tr_assig);

					try {
						dbc.saveWordListElements(wle);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
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
	*/		} else if (e.getSource() == model.getFWWordListPanel().getSaveButton()) {
				if (fw != null) {
					WordListElement nwle = new WordListElement(fw.getContent());
					nwle.setAssignation(model.getFWWordListPanel().getAssignation());

					try {
						dbc.open();
						// TODO: nur abspeichern wenn es Veränderungen gab
						dbc.saveWordListElements(nwle);
						dbc.close();
					} catch (Exception exp) {
						exp.printStackTrace();
					}
					loadWLEs();
				}
			}
			model.getFWWordListPanel();
			model.showMenu("fwAndCW");
			dbc.close();
		} catch (DBC_ConnectionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
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
	 * @return Returns the fw.
	 */
	public FunctionWord getFw() {
		return fw;
	}
	
	/**
	 * wird jedes mal aufgerufen, wenn das MenÃ¼ angezeigt wird
	 */
	@Override
	public void doAction() {
		model.getView().designText(Model.getIllocutionUnitRoots());
		if (getFw() != null) {
			setAutomaticAnalysis(true);
		}
	}
}
