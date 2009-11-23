package view.FWAndCW.WordList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import controller.FWAndCW.WordList.WordListController;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Case;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Determination;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Konjugation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Diathese;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Genus;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Numerus;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Person;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Tempus;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Type;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wordclass;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.WordsubclassAdjective;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.WordsubclassPronoun;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.WordsubclassPunctuationMark;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.WordsubclassVerb;

/**
 * Menue, fuer die Bestimmung der Wortlisten
 * @author shanthy
 * 
 */
public class WordListPanel extends JPanel {
	/**
	 * der Controller fuer das Menue
	 */
	private WordListController controller;

	/**
	 * Constitutive Word, das zu keinem Kapitel gehoert
	 */
	private JList wleChoice;

	/**
	 * Panel fuer die Knoepfe
	 */
	private JPanel buttonPanel;

	/**
	 * Panel fuer die Bestimmungen
	 */
	private JPanel assignationPanel;

	/**
	 * Panel fuer die Infos
	 */
	private JPanel infoPanel;

	/**
	 * Label fuer die Infos des CW
	 */
	private JLabel cwInfo;

	/**
	 * Buttons
	 */
	private JButton assignButton, resetButton, backButton, removeButton;

	/**
	 * ComboBoxen
	 */
	private JComboBox genusCombo, numerusCombo, determinationCombo, personCombo, wordclassCombo,
						pronounCombo, verbCombo, adjectiveCombo,
						signCombo, tempusCombo, diatheseCombo, konjugationCombo;

	/**
	 * Liste mit den Faellen
	 */
	private JList caseList;

	/**
	 * aktuelle assignation, wird benoetigt zum test auf Veraenderung selbiger;
	 * wenn sie veraendert wurde: neue assignation, wenn nicht: alte assig_id übernehmen
	 */
	private TR_Assignation assignation;
	
	private boolean assignation_changed = false;
	/**
	 * zufaellig generierte ID
	 */
	private static final long serialVersionUID = 1008683569839425231L;

	/**
	 * 
	 * @param controller WordListController
	 */
//	@SuppressWarnings("static-access")
	public WordListPanel(WordListController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		
		wleChoice = new JList();
		wleChoice.addListSelectionListener(controller);
		wleChoice.addMouseListener(controller);
		wleChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp1 = new JScrollPane(wleChoice,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp1.setBorder(new TitledBorder("CW-Suggestions"));
		add(sp1, BorderLayout.WEST);

		buttonPanel = new JPanel();
		assignButton = new JButton("assign to CW");
		assignButton.addActionListener(controller);
		buttonPanel.add(assignButton);
				
		removeButton = new JButton("remove wle and assignation from DB");
		removeButton.addActionListener(controller);
		buttonPanel.add(removeButton);

		resetButton = new JButton("reset");
		resetButton.addActionListener(controller);
		buttonPanel.add(resetButton);

		backButton = new JButton("go back");
		backButton.addActionListener(controller);
		buttonPanel.add(backButton);
		add(buttonPanel, BorderLayout.SOUTH);

		assignationPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 1;

		c.gridx = 0;
		c.gridy = 0;
		genusCombo = new JComboBox(expand(TR_Assignation.Genus.values()));
		genusCombo.setBorder(new TitledBorder("Genus:"));
		assignationPanel.add(genusCombo, c);

		c.gridx = 0;
		c.gridy = 1;
		numerusCombo = new JComboBox(expand(TR_Assignation.Numerus.values()));
		numerusCombo.setBorder(new TitledBorder("Numerus:"));
		assignationPanel.add(numerusCombo, c);

		c.gridx = 0;
		c.gridy = 2;
		determinationCombo = new JComboBox(expand(TR_Assignation.Determination.values()));
		determinationCombo.setBorder(new TitledBorder("Determination:"));
		assignationPanel.add(determinationCombo, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 4;
		caseList = new JList();
		Case[] cases = TR_Assignation.Case.values();
		String[] test = new String[cases.length+1];
		test[0] = " ";
		for(int i = 1; i < test.length; i++)
		{
			test[i] = cases[i-1].toString();
		}
		
		caseList.setListData(test);
		JScrollPane sp2 = new JScrollPane(caseList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp2.setPreferredSize(new Dimension(200, 200));
		sp2.setBorder(new TitledBorder("Case:"));
		assignationPanel.add(sp2, c);
		
/*		c.gridx = 0;
		c.gridy = 6;
		prepositionCombo = new JComboBox(expand(TR_Assignation.WordsubclassPreposition.values()));
		prepositionCombo.setBorder(new TitledBorder("Preposition:"));
		assignationPanel.add(prepositionCombo, c);
*/
		c.gridx = 0;
		c.gridy = 7;
		adjectiveCombo = new JComboBox(expand(TR_Assignation.WordsubclassAdjective.values()));
		adjectiveCombo.setBorder(new TitledBorder("Adjective:"));
		assignationPanel.add(adjectiveCombo, c);
				
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		personCombo = new JComboBox(expand(TR_Assignation.Person.values()));
		personCombo.setBorder(new TitledBorder("Person:"));
		assignationPanel.add(personCombo, c);

		c.gridx = 1;
		c.gridy = 1;
		wordclassCombo = new JComboBox(expand(TR_Assignation.Wordclass.values()));
		wordclassCombo.setBorder(new TitledBorder("Wordclass:"));
		assignationPanel.add(wordclassCombo, c);

		c.gridx = 1;
		c.gridy = 2;
		verbCombo = new JComboBox(expand(TR_Assignation.WordsubclassVerb.values()));
		verbCombo.setBorder(new TitledBorder("Verb:"));
		assignationPanel.add(verbCombo, c);
		
		c.gridx = 1;
		c.gridy = 3;
		tempusCombo = new JComboBox(expand(TR_Assignation.Tempus.values()));
		tempusCombo.setBorder(new TitledBorder("Tempus:"));
		assignationPanel.add(tempusCombo, c);

		c.gridx = 1;
		c.gridy = 4;
		diatheseCombo = new JComboBox(expand(TR_Assignation.Diathese.values()));
		diatheseCombo.setBorder(new TitledBorder("Diathese:"));
		assignationPanel.add(diatheseCombo, c);

		c.gridx = 1;
		c.gridy = 5;
		pronounCombo = new JComboBox(expand(TR_Assignation.WordsubclassPronoun.values()));
		pronounCombo.setBorder(new TitledBorder("Pronoun:"));
		assignationPanel.add(pronounCombo, c);
		
		c.gridx = 1;
		c.gridy = 6;
		konjugationCombo = new JComboBox(expand(TR_Assignation.Konjugation.values()));
		konjugationCombo.setBorder(new TitledBorder("Konjugation:"));
		assignationPanel.add(konjugationCombo, c);
		
		c.gridx = 1;
		c.gridy = 7;
		signCombo = new JComboBox(expand(TR_Assignation.WordsubclassPunctuationMark.values()));
		signCombo.setBorder(new TitledBorder("Sign:"));
		assignationPanel.add(signCombo, c);

		add(assignationPanel, BorderLayout.CENTER);
		
		infoPanel = new JPanel();
		cwInfo = new JLabel("<html><h2>---</h2></html>");
		infoPanel.add(cwInfo);
		add(infoPanel, BorderLayout.NORTH);
	}
	
	/**
	 * fuegt den werten noch ein leeres element hinzu
	 */
	private Object[] expand(Object[] strings) {
		Object[] array = new Object[strings.length + 1];
		array[0] = null;
		for(int i = 0; i < strings.length; i++)
		{
			array[i + 1] = strings[i];
		}
		return array;
	}

	/**
	 * das ausgewaehlte CW setzen
	 * @param cw ConstitutiveWord
	 */
	public void setCW(ConstitutiveWord cw) {
		if (controller.getCw() != null)
			controller.getModel().getView().reset(controller.getCw());
		controller.getModel().getView().scrollTo(cw);
		controller.setCW(cw);
		String content = cw.getContent();
		String language = cw.getWord().getLanguage();
		cwInfo.setText("<html><h2>" + content + " (" + language
				+ ")</h2></html>");
		
		setAssignation(cw.getAssignation());

		controller.loadWLEs();
	}
	/**
	 * das ausgewaehlte CW setzen ohne scrollTO
	 * @param cw ConstitutiveWord
	 */
	public void setCWForWL(ConstitutiveWord cw) {
		if (controller.getCw() != null)
			controller.getModel().getView().reset(controller.getCw());
		controller.setCW(cw);
		String content = cw.getContent();
		String language = cw.getWord().getLanguage();
		cwInfo.setText("<html><h2>" + content + " (" + language
				+ ")</h2></html>");
		
		setAssignation(cw.getAssignation());
		
//TODO
		/*WordListElement nwle = new WordListElement(cw.getContent());
		nwle.setAssignation(controller.getModel().getWordListPanel().getAssignation());
		

		try {
			Model.getDBC().open();
			Model.getDBC().saveWordListElements(nwle);
			Model.getDBC().close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		*/
		
		controller.loadWLEs();
	}	

	/**
	 * die Bestimmungen anzeigen
	 * @param a TR_Assignation
	 */
	public void setAssignation(TR_Assignation a) {
		cwInfo.setText("<html><h2>" + controller.getCw().getContent() + "</h2></html>");
		genusCombo.setSelectedItem((a.getGenera().length > 0 ? a.getGenera()[0] : null));
		numerusCombo.setSelectedItem((a.getNumeri().length > 0 ? a.getNumeri()[0] : null));
		determinationCombo.setSelectedItem((a.getDeterminations().length > 0 ? a.getDeterminations()[0] : null));
		personCombo.setSelectedItem((a.getPersons().length > 0 ? a.getPersons()[0] : null));
		wordclassCombo.setSelectedItem((a.getWordclasses().length > 0 ? a.getWordclasses()[0] : null));
		pronounCombo.setSelectedItem((a.getWordsubclassesPronoun().length > 0 ? a.getWordsubclassesPronoun()[0] : null));
		verbCombo.setSelectedItem((a.getWordsubclassesVerb().length > 0 ? a.getWordsubclassesVerb()[0] : null));
		signCombo.setSelectedItem((a.getWordsubclassesPunctuationMark().length > 0 ? a.getWordsubclassesPunctuationMark()[0] : null));
		tempusCombo.setSelectedItem((a.getTempora().length > 0 ? a.getTempora()[0] : null));
		konjugationCombo.setSelectedItem((a.getKonjugation().length > 0 ? a.getKonjugation()[0] : null));
		diatheseCombo.setSelectedItem((a.getDiatheses().length > 0 ? a.getDiatheses()[0] : null));
		adjectiveCombo.setSelectedItem(a.getWordsubclassesAdjective().length > 0 ? a.getWordsubclassesAdjective()[0] : null);
		for(Case c : a.getCases())
		    caseList.setSelectedValue(c, true);
	
		assignation = a;
	}

	/**
	 * Setzt die gewaehlten Assignations und den Type auf CW
	 * @return TR_Assignation
	 */
	public TR_Assignation getAssignation() {
		TR_Assignation a = new TR_Assignation();
		a.setTypes(Type.CONSTITUTIVE_WORD);
		if(genusCombo.getSelectedItem() != null) {
			a.setGenera((Genus) genusCombo.getSelectedItem());
			if(!a.hasGenus(assignation.getGenera().length > 0 ? assignation.getGenera()[0] : null))
				assignation_changed = true;
		} else { // selected item = null, test if it was something else before
			if(assignation.getGenera().length > 0)
		//	if(!a.hasGenus(assignation.getGenera().length > 0 ? assignation.getGenera()[0] : null)) <- old one...didn't make sense for me
				assignation_changed = true;
		}
		if((Numerus) numerusCombo.getSelectedItem() != null) {
			a.setNumeri((Numerus) numerusCombo.getSelectedItem());
			if(!a.hasNumerus(assignation.getNumeri().length > 0 ? assignation.getNumeri()[0] : null))
				assignation_changed  = true;
		}else { // look at comment in the genus case above
			if(assignation.getNumeri().length > 0)
				assignation_changed = true;
		}
		if((Determination) determinationCombo.getSelectedItem() != null) {
			a.setDeterminations((Determination) determinationCombo.getSelectedItem());
			if(!a.hasDetermination(assignation.getDeterminations().length > 0 ? assignation.getDeterminations()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getDeterminations().length > 0)
				assignation_changed = true;
		}
		if((Person) personCombo.getSelectedItem() != null) {
			a.setPersons((Person) personCombo.getSelectedItem());
			if(!a.hasPerson(assignation.getPersons().length > 0 ? assignation.getPersons()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getPersons().length > 0)
				assignation_changed = true;
		}
		if((Wordclass) wordclassCombo.getSelectedItem() != null) {
			a.setWordclasses((Wordclass) wordclassCombo.getSelectedItem());
			if(!a.hasWordclass(assignation.getWordclasses().length > 0 ? assignation.getWordclasses()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordclasses().length > 0)
				assignation_changed = true;
		}
		if((WordsubclassPronoun) pronounCombo.getSelectedItem() != null) {
			a.setWordsubclassesPronoun((WordsubclassPronoun) pronounCombo.getSelectedItem());
			if(!a.hasWordsubclassPronoun(assignation.getWordsubclassesPronoun().length > 0 ? assignation.getWordsubclassesPronoun()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordsubclassesPronoun().length > 0)
				assignation_changed = true;
		}
		if((WordsubclassVerb) verbCombo.getSelectedItem() != null) {
			a.setWordsubclassesVerb((WordsubclassVerb) verbCombo.getSelectedItem());
			if(!a.hasWordsubclassVerb(assignation.getWordsubclassesVerb().length > 0 ? assignation.getWordsubclassesVerb()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordsubclassesVerb().length > 0)
				assignation_changed = true;
		}
/*		if((WordsubclassPreposition) prepositionCombo.getSelectedItem() != null) { 
			a.setWordsubclassesPreposition((WordsubclassPreposition) prepositionCombo.getSelectedItem());
			if(!a.hasWordsubclassPreposition(assignation.getWordsubclassesPreposition().length > 0 ? assignation.getWordsubclassesPreposition()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordsubclassesPreposition().length > 0)
				assignation_changed = true;
		}
*/		if((WordsubclassPunctuationMark) signCombo.getSelectedItem() != null) {
			a.setWordsubclassesPunctuationMark((WordsubclassPunctuationMark) signCombo.getSelectedItem());
			if(!a.hasWordsubclassPunctuationMark(assignation.getWordsubclassesPunctuationMark().length > 0 ? assignation.getWordsubclassesPunctuationMark()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordsubclassesPunctuationMark().length > 0)
				assignation_changed = true;
		}
		if((Tempus) tempusCombo.getSelectedItem() != null) {
			a.setTempora((Tempus) tempusCombo.getSelectedItem());
			if(!a.hasTempus(assignation.getTempora().length > 0 ? assignation.getTempora()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getTempora().length > 0)
				assignation_changed = true;
		} 
		if((Konjugation) konjugationCombo.getSelectedItem() != null) {
			a.setKonjugation((Konjugation) konjugationCombo.getSelectedItem());
			if(!a.hasKonjugation(assignation.getKonjugation().length > 0 ? assignation.getKonjugation()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getKonjugation().length > 0)
				assignation_changed = true;
		} 
		if((Diathese) diatheseCombo.getSelectedItem() != null) {
			a.setDiatheses((Diathese) diatheseCombo.getSelectedItem());
			if(!a.hasDiathese(assignation.getDiatheses().length > 0 ? assignation.getDiatheses()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getDiatheses().length > 0)
				assignation_changed = true;
		}
		if((WordsubclassAdjective) adjectiveCombo.getSelectedItem() != null) {
			a.setWordsubclassesAdjective((WordsubclassAdjective) adjectiveCombo.getSelectedItem());
			if(!a.hasWordsubclassAdjective(assignation.getWordsubclassesAdjective().length > 0 ? assignation.getWordsubclassesAdjective()[0] : null))
				assignation_changed  = true;
		} else { // look at comment in the genus case above
			if(assignation.getWordsubclassesAdjective().length > 0)
				assignation_changed = true;
		}

		if(caseList.getSelectedValues().length != 0)
		{
			int[] sel = caseList.getSelectedIndices();
			Case[] cases = TR_Assignation.Case.values();
			Case[] tmpcases = new Case[sel.length];
			for(int i = 0; i < sel.length; i++)
				if(sel[i]-1 >= 0)
					tmpcases[i] = cases[sel[i]-1];
			if(tmpcases != null && tmpcases[0] != null) {
				a.setCases(tmpcases);
				if(!a.hasCase(assignation.getCases().length > 0 ? assignation.getCases()[0] : null))
					assignation_changed  = true;
			} else { // look at comment in the genus case above
				if(assignation.getCases().length > 0)
					assignation_changed = true;
			}
		}
		controller.getModel().modelChanged(true);
		if(assignation_changed)
			return a;
		else 
			return assignation;
	}
	
	/**
	 * gibt zurück ob die assignation veraendert wurde
	 * @return boolean
	 */
	public boolean assigChanged() {
		return this.assignation_changed;
	}
	
	/**
	 * Bestimmungen zurueck setzen
	 */
	public void reset() {
		genusCombo.setSelectedIndex(0);
		numerusCombo.setSelectedIndex(0);
		determinationCombo.setSelectedIndex(0);
		personCombo.setSelectedIndex(0);
		wordclassCombo.setSelectedIndex(0);
		konjugationCombo.setSelectedIndex(0);
		pronounCombo.setSelectedIndex(0);
		verbCombo.setSelectedIndex(0);
		signCombo.setSelectedIndex(0);
		tempusCombo.setSelectedIndex(0);
		diatheseCombo.setSelectedIndex(0);
		adjectiveCombo.setSelectedIndex(0);
		caseList.clearSelection();
	}

	/**
	 * @return Returns the wleChoice.
	 */
	public JList getWLEChoice() {
		return wleChoice;
	}

	/**
	 * @return Returns the resetButton.
	 */
	public JButton getResetButton() {
		return resetButton;
	}

	/**
	 * @return Returns the assignButton.
	 */
	public JButton getAssignButton() {
		return assignButton;
	}

	/**
	 * @return Returns the backButton.
	 */
	public JButton getBackButton() {
		return backButton;
	}

	/**
	 * @return Returns the removeButton.
	 */
	public JButton getRemoveButton() {
		return removeButton;
	}
}