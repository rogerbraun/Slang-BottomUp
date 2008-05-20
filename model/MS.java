package model;

import view.MS.MSFrame;


// Singleton, fuer Zugriff auf Daten, Views und Controller der Makrosaetze
// eingefuehrt damit nicht immer alles ins Model reingehauen wird

public class MS {
   private MSFrame msframe = new MSFrame();  // Frame in dem ein MSListDialog angezeigt wird
	
   // Singleton Implementierung
   private static MS instance = null;
   protected MS() {
      // Exists only to defeat instantiation.
   }
   public static MS getInstance() {
      if(instance == null) {
         instance = new MS();
      }
      return instance;
   }
   
   public MSFrame getMSFrame() {
	   return msframe;
   }
}
