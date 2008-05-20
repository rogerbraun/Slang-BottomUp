/*
 * Created on 02.04.2005
 */

package controller.SG;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;

/**
 * Controller zum Erstellen der Sememgruppen
 * @author shanthy
 */
public class SGCreatingController extends Controller {
	/**
	 * erste Meaning Unit
	 */
	private MeaningUnit mu1;

	/**
	 * zweite Meaning Unit
	 */
	private MeaningUnit mu2;

	/**
	 * meaning unit die zuletzt ausgew�hlt wurde
	 */
	private MeaningUnit lastMU;
	/**
	 * optionales Function Word
	 */
	private FunctionWord functionWord;

	/**
	 * Wurzeln der Illocution Units
	 */
	private IllocutionUnitRoots iurs;

	/**
	 * @param m Model
	 * @param d Designer
	 */
	public SGCreatingController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * @param e CaretEvent
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		FunctionWord fword = Model.getIllocutionUnitRoots().getFunctionWordAtPosition(e.getDot());
		if (fword != null) {
			if (fword.getMeaningUnit() != null) {
				// falls das Funktionswort zu einer MeaningUnit gehoert
				writeLabel(fword.getMeaningUnit());
			}
			else {
				writeFWLabel(fword);
			}
		}
		ConstitutiveWord cword = Model.getIllocutionUnitRoots().getConstitutiveWordAtPosition(e.getDot());
		if (cword != null) {
			// falls das angeklickte Wort ein CW ist
			writeLabel(cword.getMeaningUnit());
		}
	}

	private void writeFWLabel(FunctionWord fword) {
		if (functionWord != fword) {
			// fword im Text markieren
			model.getView().design(functionWord, false);
			model.getView().reset(functionWord);
			model.getView().design(fword, true);
			// fword auf das Label schreiben
			model.getSGCreatingMenu().setFW(fword.getContent());
			// fword setzen
			functionWord = fword;
		} else if(functionWord == fword){
			model.getView().reset(functionWord);
			model.getSGCreatingMenu().resetFW();
			functionWord = null;
		}

		if(mu1 != null)
			model.getView().design(mu1, true);
		
		if(mu2 != null)
			model.getView().design(mu2, true);
	}

	/**
	 * die Meaning Unit auf das Label schreiben
	 * @param mu MeaningUnit
	 */
	private void writeLabel(MeaningUnit mu) {
		if(mu != null && mu != mu1 && mu != mu2)
		{
			if(mu1 == null)
			{
				// setze mu1
				setMu1(mu);
				lastMU = mu;
				model.getView().design(mu, true);
			}
			else if(mu2 == null)
			{
				//setze mu2
				setMu2(mu);
				lastMU = mu;
				model.getView().design(mu, true);
			}
			else
			{
				model.getView().reset(mu1);
				model.getView().reset(mu2);
				model.getSGCreatingMenu().resetMU1();
				model.getSGCreatingMenu().resetMU2();
				// mu1 und mu2 schon besetzt setzte sie mit lastMU und mu neu
				if(mu.getStartPosition() < lastMU.getStartPosition())
				{
					setMu1(mu);
					setMu2(lastMU);
					lastMU = mu;
				}
				else
				{
					setMu1(lastMU);
					setMu2(mu);
					lastMU = mu;
				}
				if(functionWord != null)
				{
					model.getView().reset(functionWord);
				}
				model.getView().design(mu1, true);
				model.getView().design(mu2, true);
				model.getView().design(functionWord, true);
			}
		}
	}

	/**
	 * Setzt das Label von MU1
	 * @param mu
	 */
	private void setMu1(MeaningUnit mu) {
		// falls die MU aus CW und FW besteht
		if (mu.getFunctionWord() != null) {
			model.getSGCreatingMenu().setMU1(
					mu.getFunctionWord() + " "
							+ mu.getConstitutiveWord());
		}
		// falls die MU nur aus CW besteht
		else {
			model.getSGCreatingMenu().setMU1(
				mu.getConstitutiveWord().toString());
		}
		model.getView().design(mu, true);
		mu1 = mu;
	}

	/**
	 * Setzt das Label von MU2
	 * @param mu
	 */
	private void setMu2(MeaningUnit mu) {
		// falls die MU aus CW und FW besteht
		if (mu.getFunctionWord() != null) {
			model.getSGCreatingMenu().setMU2(mu.getFunctionWord() + " " + mu.getConstitutiveWord());
		}
		// falls die MU nur aus CW besteht
		else {
			model.getSGCreatingMenu().setMU2(mu.getConstitutiveWord().toString());
		}
		model.getView().design(mu, true);
		mu2 = mu;								
	}
	/**
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		// existiert bereits eine SG mit dieser mu1, mu2, fw
		try {
			Model.getDBC().open();
			// hole alle sememe gruppen der aktuellen root
			Vector<SememeGroup> sgroups = model.getIllocutionUnitRoots().getSememeGroups();
			boolean found = false;
			int index = -1;
			for(int i = 0; i < sgroups.size(); i++)
			{
				// ist ausgewaehlte SG gleich einer existierenden? ja -> loeschen!
				if(sgroups.elementAt(i).getFirst() == mu1)
				{
					if(sgroups.elementAt(i).getPartner(sgroups.elementAt(i).getFirst()) == mu2)
					{
						if(sgroups.elementAt(i).getFunctionWord() == functionWord || sgroups.elementAt(i).getFunctionWord() == null)
						{
							index = i;
							found = true;
							break;
						}
					}
				}
				else if(sgroups.elementAt(i).getFirst() == mu2)
				{
					if(sgroups.elementAt(i).getPartner(sgroups.elementAt(i).getFirst()) == mu1)
					{
						if(sgroups.elementAt(i).getFunctionWord() == functionWord || sgroups.elementAt(i).getFunctionWord() == null)
						{
							index = i;
							found = true;
							break;
						}
					}
				}
			}
				
			if (command.equals("CREATE")) {
			// wenn die sg bereits erstellt wurde, muss der benutzer darauf hingewiesen werden
				boolean go = true;
				if(found)
				{
					int pfadid = sgroups.elementAt(index).getPath();
					int numeruspathid = sgroups.elementAt(index).getNumerusPath();
					String pathname = Model.getDBC().getPaths().getNode(pfadid).getFullPath();
					String numerusPathName = Model.getDBC().getNumerusPaths().getNode(numeruspathid).getFullPath();
					String[] optionen = { "reassign", "cancel" };
					String name = new String();
					name = sgroups.elementAt(index).toWrittenString();
					int choice = JOptionPane.showOptionDialog(model, 
							"The path is: " + pathname + "\nThe numerus path is: " + numerusPathName, name,
							0,
							JOptionPane.YES_NO_OPTION, null, optionen,
							optionen[1]);
					if(choice == JOptionPane.YES_OPTION)
					{
						// reassign
						sgroups.elementAt(index).remove();
						model.saveWithoutMessage();
						
					}
					else
					{
						// sg nicht neu zuweisen
						go = false;
						resetAll();
					}
				}
				if (functionWord != null && go) {
					if (mu1 != null) {
						if (mu2 != null) {
							// test ob alle aus gleicher IU kommen
							if(mu1.getRoot() != mu2.getRoot() || mu1.getRoot() != functionWord.getRoot())
							{
								// unterschiedliche IUs
								JOptionPane.showMessageDialog(model, "The meaning units and /or the function word don't have the same root");
								resetAll();
							}
							else
							{
								SememeGroup sg = new SememeGroup(functionWord.getRoot(), functionWord, mu1, mu2);
								//model.modelChanged(true);
								Model.getViewer().setRoot(functionWord.getRoot());
								int id = 0;
								while (id == 0) {
									id = model.choosePath(sg);
								}
								sg.setPath(id);
								int id2 = model.chooseNumerusPath(sg);
								sg.setNumerusPath(id2);
								model.saveWithoutMessage();
								
								resetAll();
							}
						} else {
							JOptionPane.showMessageDialog(model, "Please select the second meaning unit");
						}
					} else {
						if(go)
							JOptionPane.showMessageDialog(model, "Please select the first meaning unit");
					}
				} else if (mu1 != null && go) {
					if (mu2 != null) {
						// test ob alle aus gleicher IU kommen
						if(mu1.getRoot() != mu2.getRoot())
						{
							// unterschiedliche IUs
							JOptionPane.showMessageDialog(model, "The meaning units and /or the function word don't have the same root");
							
							resetAll();
						}
						else
						{
							SememeGroup sg = new SememeGroup(mu1.getRoot(), mu1, mu2);
							//model.modelChanged(true);
							int id = 0;
							while (id == 0) {
								id = model.choosePath(sg);
							}
							sg.setPath(id);
							int id2 = model.chooseNumerusPath(sg);
							sg.setNumerusPath(id2);
							model.saveWithoutMessage();
							Model.getViewer().setRoot(mu1.getRoot());
						
							resetAll();
						}
					} else
						JOptionPane.showMessageDialog(model, "Please select the second meaning unit");
				} else
				{
					if(go)
						JOptionPane.showMessageDialog(model, "Please select the first meaning unit");
				}
			} else if (command.equals("CONTINUE")) {
					model.showMenu("iu");
			} else if(command.equals("DELETE")) {
				if(!found)
				{
					if(mu1 == null || mu2 == null)
					{
						JOptionPane.showMessageDialog(model, "No sememe group selected!");
					}
					else
					{
						if(functionWord != null)
							JOptionPane.showMessageDialog(model, "This sememe group does not exist!");
						else
							JOptionPane.showMessageDialog(model, "This sememe group does not exist!");
					}
				}
				else
				{
					String[] optionen = {"Yes", "No"};
					int choice = JOptionPane.showOptionDialog(model, 
							"Are you sure you want to delete this sememe group:\n" + 
							sgroups.elementAt(index).toWrittenString() + " ?", "Delete",
							0,
							JOptionPane.YES_NO_OPTION, null, optionen,
							optionen[0]);
					if(choice == JOptionPane.YES_OPTION)
					{
						// aus der DB loeschen
						sgroups.elementAt(index).remove();
						model.saveWithoutMessage();
						// update des Viewers
						Model.getViewer().setRoot(mu1.getRoot());
					}
				}
				// Markierung der Woerter aufheben
				resetAll();
			} else if(command.equals("PREVIOUS")){
				model.showMenu("muContinue");
			} 
			else
				super.actionPerformed(e);
			Model.getDBC().close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * setzt alle mus und fws auf null zurueck und resetet die view
	 */
	private void resetAll() {
		if(mu1 != null)
			model.getView().reset(mu1);
		mu1 = null;
		
		if(mu2 != null)
			model.getView().reset(mu2);
		mu2 = null;
		
		if(functionWord != null)
			model.getView().reset(functionWord);
		functionWord = null;
		
		model.getSGCreatingMenu().reset();
	}

	/**
	 * testet, ob alle Meaning Units zu einer Sememgruppe zugeordnet wurden
	 * @return boolean
	 */
	private boolean allMUsSet() {
		Vector meaningUnits = Model.getIllocutionUnitRoots().getMeaningUnits();
		for (int i = 0; i < meaningUnits.size(); i++) {
			MeaningUnit mu = (MeaningUnit) meaningUnits.get(i);
			Vector sememeGroups = mu.getSememeGroups();
			if (sememeGroups.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * wird jedes Mal aufgerufen, wenn das Menue das angezeigt wird
	 */
	@Override
	public void doAction() {
		acceptMUs();
		Model.getViewer().setRoot(iurs.getRoot(0));
	}

	/**
	 *akzeptiert alle Meaning Units, damit diese in der DB weiterverwendet werden können
	 */
	private void acceptMUs() {
		iurs = Model.getIllocutionUnitRoots();
		for (int i = 0; i < iurs.getMeaningUnits().size(); i++) {
			MeaningUnit mu = (MeaningUnit) iurs.getMeaningUnits().get(i);
			mu.setAccepted(true);
		}
	}
}