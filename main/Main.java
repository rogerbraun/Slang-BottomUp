package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.dialogs.assignation.AssignationPane;
import de.uni_tuebingen.wsi.ct.slang2.dbc.tools.dialogs.assignation.ConstitutiveWordAssignationDialog;
import model.Model;

/**
 * Start des Programms
 * @author shanthy
 */
public class Main {

	/**
	 * main-Funktion
	 * 
	 * @param argv
	 *            String[]
	 */
	public static void main(String[] argv) {
		new Model();
/*		JFrame bla = new JFrame();
		JPanel blabla = new JPanel();
   		AssignationPane assigPane = new AssignationPane(new TR_Assignation(), AssignationPane.SubsetVariant.FW);
   		blabla.add(assigPane);
   		bla.add(blabla);
   		bla.setVisible(true);
   		bla.validate();
  */ 		
	}
}
