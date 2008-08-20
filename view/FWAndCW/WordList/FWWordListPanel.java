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

import controller.FWAndCW.WordList.FWWordListController;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Case;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Genus;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Numerus;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Type;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wortart1;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wortart2;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wortart3;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.TR_Assignation.Wortart4;

public class FWWordListPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4018314766443987675L;

	/**
	 * der Controller fuer das Menue
	 */
	private FWWordListController controller;

	/**
	 * fw, das zu keinem Kapitel gehoert
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
	 * Label fuer die Infos des FW
	 */
	private JLabel fwInfo;

	/**
	 * Buttons
	 */
	private JButton assignButton, resetButton, saveButton, removeButton;

	/**
	 * ComboBoxen
	 */
	private JComboBox genusCombo, numerusCombo, wa1Combo, wa2Combo, wa3Combo, wa4Combo;

	/**
	 * Liste mit den Faellen
	 */
	private JList caseList;

	/**
	 * 
	 * @param controller WordListController
	 */
	@SuppressWarnings("static-access")
	public FWWordListPanel(FWWordListController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		
		wleChoice = new JList();
		wleChoice.addListSelectionListener(controller);
		wleChoice.addMouseListener(controller);
		wleChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp1 = new JScrollPane(wleChoice,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp1.setBorder(new TitledBorder("FW-Suggestions"));
		add(sp1, BorderLayout.WEST);

		buttonPanel = new JPanel();
		assignButton = new JButton("assign to FW");
		assignButton.addActionListener(controller);
		buttonPanel.add(assignButton);
		resetButton = new JButton("reset");
		resetButton.addActionListener(controller);
		buttonPanel.add(resetButton);
		saveButton = new JButton("save into DB");
		saveButton.addActionListener(controller);
		buttonPanel.add(saveButton);
		removeButton = new JButton("remove from DB");
		removeButton.addActionListener(controller);
		buttonPanel.add(removeButton);
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
		c.gridheight = 3;
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

		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		wa1Combo = new JComboBox(expand(TR_Assignation.Wortart1.values()));
		wa1Combo.setBorder(new TitledBorder("Wortart1:"));
		assignationPanel.add(wa1Combo, c);

		c.gridx = 1;
		c.gridy = 1;
		wa2Combo = new JComboBox(expand(TR_Assignation.Wortart2.values()));
		wa2Combo.setBorder(new TitledBorder("Wortart2:"));
		assignationPanel.add(wa2Combo, c);

		c.gridx = 1;
		c.gridy = 2;
		wa3Combo = new JComboBox(expand(TR_Assignation.Wortart3.values()));
		wa3Combo.setBorder(new TitledBorder("Wortart3:"));
		assignationPanel.add(wa3Combo, c);

		c.gridx = 1;
		c.gridy = 3;
		wa4Combo = new JComboBox(expand(TR_Assignation.Wortart4.values()));
		wa4Combo.setBorder(new TitledBorder("Wortart4:"));
		assignationPanel.add(wa4Combo, c);

		add(assignationPanel, BorderLayout.CENTER);
		
		infoPanel = new JPanel();
		fwInfo = new JLabel("<html><h2>---</h2></html>");
		infoPanel.add(fwInfo);
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
	 * das ausgewaehlte FW setzen
	 * @param fw FunctionWord
	 */
	public void setFW(FunctionWord fw) {
		if (controller.getFw() != null)
			controller.getModel().getView().reset(controller.getFw());
		controller.getModel().getView().scrollTo(fw);
		controller.setFW(fw);
		String content = fw.getContent();
		String language = fw.getWord().getLanguage();
		fwInfo.setText("<html><h2>" + content + " (" + language
				+ ")</h2></html>");
		
		setAssignation(fw.getAssignation());

	/*	TODO
	 * WordListElement nwle = new WordListElement(cw.getContent());
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
	 * das ausgewaehlte FW setzen ohne scrollTO
	 * @param fw FunctionWord
	 */
	public void setFWForWL(FunctionWord fw) {
		if (controller.getFw() != null)
			controller.getModel().getView().reset(controller.getFw());
		controller.setFW(fw);
		String content = fw.getContent();
		String language = fw.getWord().getLanguage();
		fwInfo.setText("<html><h2>" + content + " (" + language
				+ ")</h2></html>");
		
		setAssignation(fw.getAssignation());
		
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
		fwInfo.setText("<html><h2>" + controller.getFw().getContent() + "</h2></html>");
		genusCombo.setSelectedItem((a.getGenera().length > 0 ? a.getGenera()[0] : null));
		numerusCombo.setSelectedItem((a.getNumeri().length > 0 ? a.getNumeri()[0] : null));
		wa1Combo.setSelectedItem((a.getWortarten1().length > 0 ? a.getWortarten1()[0] : null));
		wa2Combo.setSelectedItem((a.getWortarten2().length > 0 ? a.getWortarten2()[0] : null));
		wa3Combo.setSelectedItem((a.getWortarten3().length > 0 ? a.getWortarten3()[0] : null));
		wa4Combo.setSelectedItem((a.getWortarten4().length > 0 ? a.getWortarten4()[0] : null));
		for(Case c : a.getCases())
		    caseList.setSelectedValue(c, false);
	}

	/**
	 * Setzt die gewaehlten Assignations und den Type auf FW
	 * @return TR_Assignation
	 */
	public TR_Assignation getAssignation() {
		TR_Assignation a = new TR_Assignation();
		a.setTypes(Type.FUNCTION_WORD);
		if(genusCombo.getSelectedItem() != null) 
			a.setGenera((Genus) genusCombo.getSelectedItem());
		if(numerusCombo.getSelectedItem() != null)
			a.setNumeri((Numerus) numerusCombo.getSelectedItem());
		if(wa1Combo.getSelectedItem() != null)
			a.setWortarten1((Wortart1) wa1Combo.getSelectedItem());
		if(wa2Combo.getSelectedItem() != null)
			a.setWortarten2((Wortart2) wa2Combo.getSelectedItem());
		if(wa3Combo.getSelectedItem() != null)
			a.setWortarten3((Wortart3) wa3Combo.getSelectedItem());
		if(wa4Combo.getSelectedItem() != null)
			a.setWortarten4((Wortart4) wa4Combo.getSelectedItem());

		if(caseList.getSelectedValues().length != 0)
		{
			int[] sel = caseList.getSelectedIndices();
			Case[] cases = TR_Assignation.Case.values();
			Case[] tmpcases = new Case[sel.length];
			for(int i = 0; i < sel.length; i++)
				if(sel[i]-1 >= 0)
					tmpcases[i] = cases[sel[i]-1];
			if(tmpcases != null)
				a.setCases(tmpcases);
		}
		controller.getModel().modelChanged(true);
		return a;
	}
	
	/**
	 * Bestimmungen zurueck setzen
	 */
	public void reset() {
		genusCombo.setSelectedIndex(0);
		numerusCombo.setSelectedIndex(0);
		wa1Combo.setSelectedIndex(0);
		wa2Combo.setSelectedIndex(0);
		wa3Combo.setSelectedIndex(0);
		wa4Combo.setSelectedIndex(0);
		caseList.setSelectedIndex(0);
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
	 * @return Returns the saveButton.
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * @return Returns the removeButton.
	 */
	public JButton getRemoveButton() {
		return removeButton;
	}

}
