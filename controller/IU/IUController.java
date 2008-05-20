/*
 * Created on 22.05.2005 TODO To change the template for this generated
 * file go to Window - Preferences - Java - Code Style - Code Templates
 */

package controller.IU;

import javax.swing.JOptionPane;

import model.Model;
import view.Superclasses.Designer;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;

/**
 * Controller, um Pfade zu Illocution Units zu bestimmen
 * @author shanthy
 */
public class IUController extends Controller {
   /**
    * Wurzel der Illcoution Units
    */
   private IllocutionUnitRoots iurs;

   private static int index;
   /**
    * @param m Model
    * @param d Designer
    */
   public IUController(Model m, Designer d) {
      super(m,d);
   }

   /**
    * wird jedes mal ausgefuehrt, wenn das Menue angezeigt wird
    */
   @Override
public void doAction() {
      acceptSGs();
      model.getView().designText(iurs);
      analyseIUs();
   }

   /**
    * die Sememgruppen akzeptieren
    */
   private void acceptSGs() {
      iurs = Model.getIllocutionUnitRoots();
      for (int i = 0; i < iurs.getSememeGroups().size(); i++) {
         SememeGroup sg = (SememeGroup) iurs.getSememeGroups().get(i);
         sg.setAccepted(true);
      }
   }

   /**
    * analysieren der meaning units
    */
   public void analyseIUs() {
	 for (int i = 0; i < Model.getChapter().getIllocutionUnits().size(); i++) {
        IllocutionUnit iu = Model.getChapter().getIllocutionUnitAtIndex(i);
        model.getView().design(iu, false);
        
        index = 0;
        int phras  = phrastic(iu);
        if(phras == 1 && index == 1)
        {
        	//phrastic => Pfad auswaehlen
            int id = model.choosePath(iu);
       //   int numerusPathID = model.chooseNumerusPath(iu);
            
            Model.getIllocutionUnitRoots().getRoot(iu).setPhrastic(phras);
            Model.getIllocutionUnitRoots().getRoot(iu).setPath(id);
       //   Model.getIllocutionUnitRoots().getRoot(iu).setNumerusPath(numerusPathID);
        }
        else if(phras == 1 && index == 0)
        {
        	// sind die pfade gesetzt?
        	if(Model.getIllocutionUnitRoots().getRoot(iu).getPath() == 0)
        	{
        		// pfad nicht gesetzt
        		int id = model.choosePath(iu);
        		Model.getIllocutionUnitRoots().getRoot(iu).setPath(id);
        	}
        /*	if(Model.getIllocutionUnitRoots().getRoot(iu).getNumerusPath() == 0)
        	{
        		// pfad nicht gesetzt
        		int id = model.chooseNumerusPath(iu);
        		Model.getIllocutionUnitRoots().getRoot(iu).setNumerusPath(id);
        	}*/
        }
        else if(phras == 2 && index == 1)
        {
        	//aphrastic
        	Model.getIllocutionUnitRoots().getRoot(iu).setPhrastic(phras);
        }
        else if(phras == 3)
        	Model.getIllocutionUnitRoots().getRoot(iu).setPhrastic(-1);
  /*      else if(phras == 3)
        {
	        //Step back
	        if(i > 0)
	        {
	        	IllocutionUnit falseIU = Model.getChapter().getIllocutionUnitAtIndex(i-1);
	        	reset(falseIU);
	            i = i - 2;
	        }
	        else
	        {
	        	JOptionPane.showMessageDialog(Model.getFrames()[0],
	        	    "No Illocution unit arranged in front",
	        	    "Reached First IU",
	        	    JOptionPane.PLAIN_MESSAGE);
	        	i = i - 1;
	        }
        }*/
        else if(phras == 0){
        	model.showMenu("iuContinue");
        	break;
        }
        if(i == Model.getChapter().getIllocutionUnits().size() -1)
        {
        	//Wurde letzte IU richtig zugeordnet? wenn ja: weiter, wenn nein Schritt zur�ck
        	Object[] options = {"Consider and/or reassign IU", "Continue with Macro Sentences"};
    	   
        	// + 1 weil phrastic schon 0 ist 
        	int tmp = JOptionPane.showOptionDialog(Model.getFrames()[0], "All illocution units considered\nDo you want to continue?",
    	      "All IU considered",
    	      JOptionPane.YES_NO_OPTION,
    	      JOptionPane.QUESTION_MESSAGE,
    	      null,
    	      options,
    	      options[1]);
        	if(tmp == JOptionPane.YES_OPTION)
        	{
        		model.showMenu("iuContinue");
        	}
        	else if(tmp == JOptionPane.NO_OPTION)
        	{
        		model.getView().reset(iu);
        		model.showMenu("msHead");
        		break;
        	}
        	else 
        		i--;
        }
        model.getView().reset(iu);
	 }
   }

   /**
    * Dialog zur phrastisch/aphrastisch Abfrage
    * @return 0 bei schliessen des Fensters, 1 bei phrastic, 2 bei aphrastic
    */
   public static int phrastic(IllocutionUnit iu){
	   int phras = Model.getIllocutionUnitRoots().getRoot(iu).getPhrastic();

	   if(phras == -1)
	   {
		   Object[] options = {"Phrastic", "Aphrastic", "not sure"};
		   // + 1 weil phrastic schon 0 ist 
		   phras = JOptionPane.showOptionDialog(Model.getFrames()[0], "Is the illocution unit\n\'"+iu+"\'\nphrastic or aphrastic",
				   "Phrastic?", 
				   JOptionPane.YES_NO_CANCEL_OPTION, 
				   JOptionPane.QUESTION_MESSAGE,
				   null,
				   options,
				   options[0]) + 1;
		   index = 1;
	   }
	   return phras;
   }
}
