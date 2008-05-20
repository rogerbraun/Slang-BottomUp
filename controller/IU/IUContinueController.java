package controller.IU;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.DBC_ConnectionException;

public class IUContinueController extends Controller{
	/**
	 * ausgewaehlte Illocution Unit Root
	 */
	private IllocutionUnitRoot iur;
	/**
	 * @param m Model
	 * @param d Designer
	 */
	public IUContinueController(Model m, Designer d) {
		super(m,d);
	}

	/**
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("CONTINUE")) {
			model.showMenu("iu");
		} else if(e.getActionCommand().equals("DELETE")) {
			if(iur != null)
			{
				try {
					Model.getDBC().open();
	
					if(iur.getPhrastic() != -1)
					{
						// iur wurde bereits zugewiesen
						int phras = iur.getPhrastic();
						String tmp = new String();
						if(phras == 0)
						{
							tmp = "aphrastic";
						}
						else if(phras == 1)
						{
							tmp = "phrastic";
						}
						String[] optionen = { "delete assignation", "cancel" };
						String name = new String("delete IU");
						int pfadid = iur.getPath();
						String pathname = Model.getDBC().getPaths().getNode(pfadid).getFullPath();
						int choice = JOptionPane.showOptionDialog(model, 
								"The IU \"" + iur.getIllocutionUnit() + "\" is: " + tmp + 
								".\nThe path is: " + pathname,
								name,
								0,
								JOptionPane.YES_NO_OPTION, null, optionen,
								optionen[1]);
						if(choice == JOptionPane.YES_OPTION)
						{
							// delete
							reset(iur.getIllocutionUnit());
							assign(iur.getIllocutionUnit());
						//	model.showMenu("iu");
						}
						model.getView().design(iur.getIllocutionUnit(), false);
					}
					else
					{
						// keine iur ausgewaehlt 
						JOptionPane.showMessageDialog(model, "The IU \"" + iur.getIllocutionUnit() + "\" has no assignations.");
						assign(iur.getIllocutionUnit());
					}
					Model.getDBC().close();
				} catch (DBC_ConnectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e2)	{
					e2.printStackTrace();
				}
				
			}
			else
			{
				// keine iur ausgewaehlt 
			}
		} else if(e.getActionCommand().equals("NEXT")){
			model.showMenu("msHead");
		} else if(e.getActionCommand().equals("BACK")){
			model.showMenu("sgCreate");
		} else
			super.actionPerformed(e);
	}

	/**
	 * Weist der IU illocutionUnit a/phrastic und den Pfad zu
	 * @param illocutionUnit
	 */
	private void assign(IllocutionUnit illocutionUnit) {
		int phrastic = IUController.phrastic(illocutionUnit);
		if(phrastic == 1)
		{
			// phrastic => Pfad auswaehlen
			int id = model.choosePath(illocutionUnit);
	        Model.getIllocutionUnitRoots().getRoot(illocutionUnit).setPhrastic(phrastic);
	        Model.getIllocutionUnitRoots().getRoot(illocutionUnit).setPath(id);
		}
		else if(phrastic == 2)
		{
			// aphrastic
        	Model.getIllocutionUnitRoots().getRoot(illocutionUnit).setPhrastic(phrastic);
		}
	}

	/**
	    * Setzt die Werte der falschen IU zur�ck zu 0 bzw. -1
	    * @param falseIU
	    */
	   private void reset(IllocutionUnit falseIU) {
	       Model.getIllocutionUnitRoots().getRoot(falseIU).setPhrastic(-1);
	       Model.getIllocutionUnitRoots().getRoot(falseIU).setPath(0);
//	       Model.getIllocutionUnitRoots().getRoot(falseIU).setNumerusPath(0);
	}
	   
	/**
	 * @param e CaretEvent
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		if(iur != null)
			model.getView().design(iur.getIllocutionUnit(), false);

		try{
			iur = Model.getIllocutionUnitRoots().getRootAtPosition(e.getDot());
			// iur im Text markieren
			model.getView().design(iur.getIllocutionUnit(), true);
		} catch (Exception ex) {
			// es wurde auf eine Stelle die keine iur beinhaltet geklickt...tue nichts
		}
	}
}
