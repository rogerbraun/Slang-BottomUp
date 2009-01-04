package controller.FWAndCW.WordList;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.client.DBC;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.WordListElement;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.DBC_ConnectionException;

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
	 * Ã¼bernimmt bereits gemachte Bestimmungen
	 */
	private boolean automaticAnalysis;

	/**
	 * wird zur automatischen Analyse benÃ¶tigt
	 */
//	private int position = 0;
	
	/**
	 * Bestimmung mit der rechten Maustaste
	 */
	private JPopupMenu popMenu = new JPopupMenu();
	
	private boolean simulatedClick = false;
	
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
		if(cw.getAssignation() == null)
			cw.setAssignation(new TR_Assignation());
		
		this.cw = cw;
		this.oldAssig_DB_ID = cw.getAssignation().getDB_ID();
		try {
			Model.getDBC().open();
			this.wle = Model.getDBC().loadWordListElementWithAssigID(oldAssig_DB_ID);
			this.oldWLE_DB_ID = wle.getDB_ID();
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
		try {
			dbc.open();
			if (e.getSource() == model.getWordListPanel().getResetButton()) {
				model.getWordListPanel().reset();
				model.getWordListPanel().getWLEChoice().clearSelection();
			} else if (e.getSource() == model.getWordListPanel().getAssignButton()) {
				if (cw != null) {
					// alte assignation überschreiben, wenn vorhanden
					TR_Assignation tr_assig = model.getWordListPanel().getAssignation();
			
					if(oldWLE_DB_ID != -1  && model.getWordListPanel().assigChanged()) {
						String options[] = { "Overwrite wle", "Create new wle" };
						int n = JOptionPane.showOptionDialog(
									model.getWordListPanel(),
									"Overwrite or create new word list element?",
									null, JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0]);

						if(n == JOptionPane.YES_OPTION) {
							// overwrite
							cw.setAssignation(tr_assig, oldAssig_DB_ID);
							wle.setAssignation(tr_assig, oldAssig_DB_ID);
						} else if(n == JOptionPane.NO_OPTION) {
							// create new
							cw.setAssignation(tr_assig);
							WordListElement tmp = new WordListElement(wle.getContent(), wle.getLanguage());
							tmp.setAssignation(wle.getAssignation());
							wle = tmp;
							wle.setAssignation(tr_assig);
						}					
					}	
					else {
						// new wle
						cw.setAssignation(tr_assig);
						wle.setAssignation(tr_assig);
					}
					try {
						if(wle.hasChanged())
							dbc.saveWordListElements(wle);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					model.saveWithoutMessage();
				}
	//			model.getWordListPanel();
				model.showMenu("fwAndCW");
			} else if (e.getSource() == model.getWordListPanel().getBackButton()) {
	//			model.getWordListPanel();
				model.showMenu("fwAndCW");
			} else if(e.getSource() == model.getWordListPanel().getRemoveButton()) {
				// loesche aktuelles wle komplett aus db
				popMenu.setVisible(false);
				if(wle.hasChanged())
					JOptionPane.showMessageDialog(model.getWordListPanel(), "Cannot delete wle!\n (No wle set or wle has been changed)");
				else {
					WordListElement wle = (WordListElement) model.getWordListPanel().getWLEChoice().getSelectedValue();
					if(wle == null)
						wle = this.wle;
					int wleID = wle.getDB_ID();
					TR_Assignation assig = wle.getAssignation();
					int assigID = assig.getDB_ID();
					model.saveWithoutMessage();

					String[] options = {"Yes", "No"};
					int n = JOptionPane.showOptionDialog(
							model.getWordListPanel(),
							"If you continue, the assignation_id of all constitutive words with this assignation_id will be set to null!\n" +
							"The word list element and the assignation will be removed from the database!",
							"Attention", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null,
							options, options[0]);
					if(n == JOptionPane.YES_OPTION) {
						try {
						//	System.err.println("function delete WLE turned off at the moment");
							dbc.open();
							dbc.deleteWLECW(wleID, assigID);//, cw.getDB_ID());
							dbc.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						cw = null;
					}
				//	model.saveWithoutMessage();
				//	model.getWordListPanel().setCW(cw);
					model.showMenu("fwAndCW");
				}
			} /*
				TODO wenn man ein altes wle ändern will
				else if(e.getActionCommand() == "CHANGEWLE") {
				WordListElement wle = (WordListElement) model.getWordListPanel().getWLEChoice().getSelectedValue();
				TR_Assignation assig = wle.getAssignation();
				int assigID = assig.getDB_ID();
				cw.setAssignation(null);
				assig.remove();
				wle.remove();
			}*/
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
	 * @return Returns the cw.
	 */
	public ConstitutiveWord getCw() {
		return cw;
	}
	
	/**
	 * wird jedes mal aufgerufen, wenn das MenÃ¼ angezeigt wird
	 */
	@Override
	public void doAction() {
		model.getView().designText(Model.getIllocutionUnitRoots());
		if (getCw() != null) {
			setAutomaticAnalysis(true);
		}
	}
	

	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	@Override
	public void mouseClicked(@SuppressWarnings("unused") MouseEvent e) {
		if (e.getButton() == 3) {
			try	{	// simuliere rechtsklick
					Robot robot = new java.awt.Robot();
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					simulatedClick = true;
					// oeffne popupmenu
					Point p = e.getPoint();
					p.x = p.x + this.model.getRightPanel().getX();
					p.y = p.y + this.model.getRightPanel().getY() + 115;
					popMenu.setVisible(false);
					popMenu = new JPopupMenu();
					// altes wle und alte assignation loeschen unabhängig von aktueller zuweisung
					JMenuItem delete = new JMenuItem("delete WLE from database");
					delete.addActionListener(this);
					delete.setActionCommand("DELETEWLE");
					popMenu.add(delete);
/*					// altes wle und alte assignation aendern unabhängig von aktueller zuweisung
					JMenuItem change = new JMenuItem( "change this WLE in the database");
					change.addActionListener(this);
					change.setActionCommand("CHANGEWLE");
					popMenu.add(change);*/
					popMenu.setLocation(p);
					popMenu.setVisible(true);
			}
			catch (AWTException ae) { System.out.println(ae); }
		} else if(e.getButton() == 1) {
			if(simulatedClick)
			{
				simulatedClick = false;
			}
			else
				popMenu.setVisible(false);
		}
	}
}
