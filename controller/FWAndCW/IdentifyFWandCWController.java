/*
 * Created on 13.12.2005
 *
 */

package controller.FWAndCW;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
import view.MyTextPane;
import view.FWAndCW.FWAndCWDesigner;
import additionalPrograms.WordSplitter.ListElement;
import controller.Controller;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.NoWordFoundAtPositionException;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.OverlappingException;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.PositionNotInTokenException;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.UnequalTokensException;
import de.uni_tuebingen.wsi.ct.slang2.dbc.share.exceptions.WordNotInIllocutionUnitException;

/**
 *  Controller für das Erstellen von FunctionWords und
 *         SemanticWords
 * @author shanthy
 * 
 */
public class IdentifyFWandCWController extends Controller implements
		ListSelectionListener {
	/**
	 * das zuletzt bearbeitete Wort
	 */
	private Word lastSelectedWord;

	/**
	 * das gerade bearbeitete Wort
	 */
	private Word selectedWord;
	
	private boolean mouseactive =true;
	/**
	 * Bestimmung mit der rechten Maustaste
	 */
	private JPopupMenu popMenu = new JPopupMenu();

	/**
	 * 
	 * @param model Model
	 * @param d FWAndCWDesigner
	 */
	public IdentifyFWandCWController(Model model, FWAndCWDesigner d) {
		super(model, d);
	}

	/**
	 * testet, ob alle Buchstaben dieses Wortes entweder FW oder CW sind
	 * @return boolean
	 */
	private boolean allCharactersSet() {
		if (selectedWord != null) {
			for (int i = 0; i < selectedWord.getContent().length(); i++) {
				int position = selectedWord.getStartPosition() + i;
				if (Model.getIllocutionUnitRoots().getFunctionWordAtPosition(
						position) == null
						&& Model.getIllocutionUnitRoots()
								.getConstitutiveWordAtPosition(position) == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * testet, ob alle Wörter des Kapitels bestimmt wurden
	 * @return boolean
	 */
	public boolean allWordsSet() {
		for (int i = 0; i < Model.getChapter().getWords().size(); i++) {
			Word w = (Word) Model.getChapter().getWords().get(i);
			if (Model.getIllocutionUnitRoots().getFunctionWordAtPosition(
					w.getStartPosition()) == null
					&& Model
							.getIllocutionUnitRoots()
							.getConstitutiveWordAtPosition(w.getStartPosition()) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param e ActionEvent
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("FW")) {
			popMenu.setVisible(false);
			lastSelectedWord = selectedWord;
			String selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			if (selectedText == null) {
				selectedWord = model.getIdentifyFWandCWPanel().getWord();
				model.getIdentifyFWandCWPanel().getWordField().setSelectionStart(0);
				model.getIdentifyFWandCWPanel().getWordField().setSelectionEnd(selectedWord.getContent().length());
				selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			}
			int start = selectedWord.getStartPosition()	+ model.getIdentifyFWandCWPanel().getWordField().getSelectionStart();
			int end = start + selectedText.length() - 1;
			createFW(selectedText, start, end, false);
		} else if (e.getActionCommand().equals("CW")) {
			popMenu.setVisible(false);
			lastSelectedWord = selectedWord;
			String selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			if (selectedText == null) {
				selectedWord = model.getIdentifyFWandCWPanel().getWord();
				model.getIdentifyFWandCWPanel().getWordField().setSelectionStart(0);
				model.getIdentifyFWandCWPanel().getWordField().setSelectionEnd(selectedWord.getContent().length());
				selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			}
			int start = selectedWord.getStartPosition() + model.getIdentifyFWandCWPanel().getWordField().getSelectionStart();
			int end = start + selectedText.length() - 1;
			createCW(selectedText, start, end, false);
		} else if (e.getActionCommand().equals("DELETE")) {
			lastSelectedWord = selectedWord;
			String selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			if (selectedText == null) {
				selectedWord = model.getIdentifyFWandCWPanel().getWord();
				model.getIdentifyFWandCWPanel().getWordField().setSelectionStart(0);
				model.getIdentifyFWandCWPanel().getWordField().setSelectionEnd(selectedWord.getContent().length());
				selectedText = model.getIdentifyFWandCWPanel().getWordField().getSelectedText();
			}
			int start = selectedWord.getStartPosition()
					+ model.getIdentifyFWandCWPanel().getWordField().getSelectionStart();
			int end = start + selectedText.length() - 1;
			for (int j = start; j <= end; j++) {
				ConstitutiveWord constitutiveWord = Model.getIllocutionUnitRoots()
						.getConstitutiveWordAtPosition(j);
				if (constitutiveWord != null) {
					constitutiveWord.remove();
				}
				FunctionWord functionWord = Model.getIllocutionUnitRoots()
						.getFunctionWordAtPosition(j);
				if (functionWord != null) {
					functionWord.remove();
				}
			}
			model.modelChanged(true);
		} else if (e.getActionCommand().equals("OK")) {
			lastSelectedWord = selectedWord;
			ListElement listElement = (ListElement) model
					.getIdentifyFWandCWPanel().getList().getSelectedValue();
			if (listElement != null) {
				createFWandCWFromSuggestion(listElement, selectedWord, false);
				model.modelChanged(true);
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
				if (allCharactersSet() && !allWordsSet()) {
					model.showMenu("fwAndCW");
				}
				if (allCharactersSet() && allWordsSet()) {
					String options[] = { "Yes", "No" };
					int n = JOptionPane
							.showOptionDialog(
									null,
									"Did you finish assignment of function and semantic consitutive words?",
									null, JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0]);
					if (n == JOptionPane.YES_OPTION) {
						model.createGraph();
						model.startPragWo();
					}
				}
			} else {
				JOptionPane.showMessageDialog(model,
						"Please select a suggestion from the list first.");
			}
		} else if (e.getActionCommand().equals("FORALL")) {
			lastSelectedWord = selectedWord;
			ListElement listElement = (ListElement) model
					.getIdentifyFWandCWPanel().getList().getSelectedValue();
			if (listElement != null) {
				assignAll(listElement);
				model.modelChanged(true);
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
				if (allCharactersSet() && !allWordsSet()) {
					model.showMenu("fwAndCW");
				} else if (allCharactersSet() && allWordsSet()) {
					model.createGraph();
			 		continueWithMU((byte) 0);
				}
			} else {
				JOptionPane.showMessageDialog(model,
						"Please select a suggestion from the list first.");
			}
		} else if (e.getActionCommand().equals("CANCEL")) {
			lastSelectedWord = selectedWord;
			selectedWord = null;
			model.showMenu("fwAndCW");
			if (allCharactersSet() && allWordsSet()) {
				model.createGraph();
		 		continueWithMU((byte) 0);
			}
		}else if (e.getActionCommand().equals("CONTINUE")){
			model.createGraph();
			//model.showMenu("mu");
			continueWithMU((byte) -1);
		}
		
		else
			super.actionPerformed(e);
	}

	/**
	 * Teste ob (und warne wenn nicht) alle Woerter Assignations haben, bevor MU gestartet wird.
	 * Starte wordList, wenn gewuenscht.
	 * @param a
	 */
	private void continueWithMU(int a) {
		int answer = a;
		if(a == 0)
		{
			Object[] options = {"Continue considering the words", "Continue with meaning units"};
			int tmp = JOptionPane.showOptionDialog(Model.getFrames()[0], "All words considered\nDo you want to continue?",
	 	    "All words considered",
	 	    JOptionPane.YES_NO_OPTION,
	 	    JOptionPane.QUESTION_MESSAGE,
	 	    null,
	 	    options,
	 	    options[1]);
			answer = tmp;
		}
	 	if(answer == JOptionPane.NO_OPTION || answer == -1)
	 	{
	 		ConstitutiveWord cw = model.firstNotAssignedCW();
	 		FunctionWord fw = model.firstNotAssignedFW();
	 		// decision = 0 wenn das erste notAssigned Wort ein cw ist
	 		// decision = 1 wenn das erste notAssigned Wort ein fw ist
	 		// decision = 2 wenn alle Woerter assigned sind
	 		byte decision = 2;
	 		if(cw != null)
	 			if(fw != null)
	 				if(cw.getStartPosition() < fw.getStartPosition())
	 					decision = 0;
	 				else
	 					decision = 1;
	 			else
	 				decision = 0;
	 		else if(fw != null)
	 			decision = 1;
 			
	 		if(decision == 2 )
	 			model.showMenu("mu");
	 		else {
		 		Object[] options2 = {"Assign", "Continue with meaning units"};
			   	int tmp2 = JOptionPane.showOptionDialog(Model.getFrames()[0], "There are words without assignment!",
			 	    "Missing assignments",
			 	    JOptionPane.YES_NO_OPTION,
			 	    JOptionPane.QUESTION_MESSAGE,
			 	    null,
			 	    options2,
			 	    options2[1]);	 		
			   	if(tmp2 == JOptionPane.NO_OPTION)
			   		model.showMenu("mu");
			   	else if( tmp2 == JOptionPane.YES_OPTION){
			   		if(decision == 0){
			   			model.showMenu("wordList");
			   			model.getWordListPanel().setCWForWL(cw);
			   		}
			   		else if(decision == 1){
				   		model.showMenu("fwWordList");
				   		model.getFWWordListPanel().setFWForWL(fw);
			   		}
			   	}			   		
		   	}
	 	}
	}

	/**
	 * nimmt den ausgwählten Vorschlag aus der Liste und erstellt daraus die FW- und CW-Teile
	 * @param listElement ListElement
	 * @param selectedWord Word
	 * @param overwriting boolean
	 */
	private void createFWandCWFromSuggestion(ListElement listElement,
			@SuppressWarnings("hiding")
			Word selectedWord, boolean overwriting) {
		model.getView().scrollTo(selectedWord);
		int counter = 0;
		String s = listElement.getWord();
		for (int i = 0; i < s.length(); i++) {
			if (s.startsWith("{")) {
				s = s.substring(1, s.length());
				int endOfString = s.indexOf("}");
				String string = s.substring(0, endOfString);
				int start = selectedWord.getStartPosition() + counter;
				int end = selectedWord.getStartPosition() + counter
						+ endOfString - 1;
				createCW(string, start, end, overwriting);
				counter += (endOfString - 1);
				s = s.substring(endOfString + 1, s.length());
			} else if (s.startsWith("(")) {
				s = s.substring(1, s.length());
				int endOfString = s.indexOf(")");
				String string = s.substring(0, endOfString);
				int start = selectedWord.getStartPosition() + counter;
				int end = selectedWord.getStartPosition() + counter
						+ endOfString - 1;
				createFW(string, start, end, overwriting);
				counter += (endOfString - 1);

				s = s.substring(endOfString + 1, s.length());
			}
			counter++;
		}
	}

	/**
	 * erstellt ein CW aus dem markierten Textteil
	 * @param selectedText String
	 * @param start int
	 * @param end int
	 * @param overwriting boolean
	 */
	private void createCW(String selectedText, int start, int end,
			boolean overwriting) {
		if (!Model.getIllocutionUnitRoots().existsConstitutiveWord(selectedWord, selectedText, true)) {
			try {
				ConstitutiveWord cw = new ConstitutiveWord(Model.getIllocutionUnitRoots().getRoot(selectedWord.getIllocutionUnit()), start, end);
				model.modelChanged(true);
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
				if (allCharactersSet()) {
					model.getWordListPanel().setCW(cw);
					model.showMenu("wordList");
				}
			} catch (OverlappingException e) {
				if (!overwriting) {
					for (int j = start; j <= end; j++) {
						ConstitutiveWord constitutiveWord = Model.getIllocutionUnitRoots().getConstitutiveWordAtPosition(j);
						if (constitutiveWord != null) {
							constitutiveWord.remove();
						}
					}
					try {
						ConstitutiveWord cw = new ConstitutiveWord(Model.getIllocutionUnitRoots().getRoot(selectedWord.getIllocutionUnit()), start, end);
						model.modelChanged(true);
						model.getIdentifyFWandCWPanel().setWord(selectedWord);
						if (allCharactersSet()) {
							model.getWordListPanel().setCW(cw);
							model.showMenu("wordList");
						}
					} catch (PositionNotInTokenException e2) {
						e2.printStackTrace();
					} catch (NoWordFoundAtPositionException e2) {
						e2.printStackTrace();
					} catch (WordNotInIllocutionUnitException e2) {
						e2.printStackTrace();
					} catch (OverlappingException e2) {
						e2.printStackTrace();
					} catch (UnequalTokensException e2) {
						e2.printStackTrace();
					}
				} else {
					for (int j = start; j <= end; j++) {
						ConstitutiveWord constitutiveWord = Model.getIllocutionUnitRoots().getConstitutiveWordAtPosition(j);
						if (constitutiveWord != null)
							constitutiveWord.remove();
					}
					try {
						ConstitutiveWord cw = new ConstitutiveWord(Model.getIllocutionUnitRoots().getRoot(selectedWord.getIllocutionUnit()),start, end);
						model.modelChanged(true);
						model.getIdentifyFWandCWPanel().setWord(selectedWord);
						if (allCharactersSet()) {
							model.getWordListPanel().setCW(cw);
							model.showMenu("wordList");
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			for (int j = start; j <= end; j++) {
				ConstitutiveWord constitutiveWord = Model.getIllocutionUnitRoots().getConstitutiveWordAtPosition(j);
				j = j+constitutiveWord.getContent().length();
				if (constitutiveWord != null) {
					model.modelChanged(true);
					model.getIdentifyFWandCWPanel().setWord(selectedWord);
					if (allCharactersSet()) {
						model.getWordListPanel().setCW(constitutiveWord);
					}
				}
			}
		}
	}

	/**
	 * erstellt ein FW aus dem markierten Textteil
	 * @param selectedText String
	 * @param start int 
	 * @param end int
	 * @param overwriting boolean
	 */
	private void createFW(String selectedText, int start, int end,
			boolean overwriting) {
		if (!Model.getIllocutionUnitRoots().existsFunctionWord(selectedWord, selectedText, true)) {
			try {
				@SuppressWarnings("unused")
				FunctionWord fw = new FunctionWord(Model
						.getIllocutionUnitRoots().getRoot(
								selectedWord.getIllocutionUnit()), start, end);
				model.modelChanged(true);
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
				if (allCharactersSet()) {
					model.getFWWordListPanel().setFW(fw);
					model.showMenu("fwWordList");
				}
			} catch (OverlappingException e) {
				if (!overwriting) {
						for (int j = start; j <= end; j++) {
							FunctionWord functionWord = Model.getIllocutionUnitRoots()
									.getFunctionWordAtPosition(j);
							if (functionWord != null) {
								functionWord.remove();
							}
							model.showMenu("fwAndCW");
						}
						try {
							@SuppressWarnings("unused")
							FunctionWord fw = new FunctionWord(Model.getIllocutionUnitRoots().getRoot(selectedWord.getIllocutionUnit()),start, end);
							model.modelChanged(true);
							model.getIdentifyFWandCWPanel().setWord(
									selectedWord);
							if (allCharactersSet()) {
								model.getFWWordListPanel().setFW(fw);
								model.showMenu("fwWordList");
							}
						} catch (PositionNotInTokenException e2) {
							e2.printStackTrace();
						} catch (NoWordFoundAtPositionException e2) {
							e2.printStackTrace();
						} catch (WordNotInIllocutionUnitException e2) {
							e2.printStackTrace();
						} catch (OverlappingException e2) {
							e2.printStackTrace();
						} catch (UnequalTokensException e2) {
							e2.printStackTrace();
						}
				} else {
					for (int j = start; j <= end; j++) {
						FunctionWord functionWord = Model.getIllocutionUnitRoots().getFunctionWordAtPosition(j);
						if (functionWord != null) {
							functionWord.remove();
						}
					}
					try {
						@SuppressWarnings("unused")
						FunctionWord fw = new FunctionWord(Model.getIllocutionUnitRoots().getRoot(selectedWord.getIllocutionUnit()),start, end);
						model.modelChanged(true);
						model.getIdentifyFWandCWPanel().setWord(selectedWord);
						if (allCharactersSet()) {
							model.getFWWordListPanel().setFW(fw);
							model.showMenu("fwWordList");
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			for (int j = start; j <= end; j++) {
				FunctionWord fw = Model.getIllocutionUnitRoots().getFunctionWordAtPosition(j);
				j = j+fw.getContent().length();
				model.getFWWordListPanel().setFW(fw);
			}	   			
	   	}
	}

	/**
	 * übernimmt den ausewählten Vorschlag für alle Wörter mit dem gleichen Content
	 * @param listElement ListElement
	 */
	private void assignAll(ListElement listElement) {
		boolean overwriting = model.getIdentifyFWandCWPanel().isSelected();
		createFWandCWFromSuggestion(listElement, selectedWord, overwriting);
		Vector words = Model.getChapter().getWords(selectedWord.getContent(),
				overwriting);
		if (words != null) {
			for (int i = 0; i < words.size(); i++) {
				Word word = (Word) words.get(i);
				if (word.getIndex() > selectedWord.getIndex())
					createFWandCWFromSuggestion(listElement, word, overwriting);
			}
		}
		model.getView().scrollTo(lastSelectedWord);
		model.showMenu("fwAndCW");
	}

	/**
	 * @param e CaretEvent
	 * 
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() instanceof JTextPane
				&& ((JTextPane) e.getSource()).getName() != null
				&& !((JTextPane) e.getSource()).getName().equals("word field")) {
			// TODO
		} else if (e.getSource() instanceof MyTextPane && !mouseactive) {
			Token token = Model.getChapter().getTokenAtPosition(e.getDot());
			if (token instanceof Word
					&& (selectedWord == null || allCharactersSet())) {
				selectedWord = (Word) token;
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
			}
		}
		mouseactive=false;
	}

	/**
	 * @param e ListSelectionEvent
	 * 
	 */
	public void valueChanged(@SuppressWarnings("unused")
	ListSelectionEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() ==3) {
			mouseactive=true;
			Point p = arg0.getPoint();
			int position = model.getView().getEditor().viewToModel(p);
			Token token = Model.getChapter().getTokenAtPosition(position);
			model.getView().getEditor().setCaretPosition(position);
			if (token instanceof Word
					&& (selectedWord == null || allCharactersSet())) {
				selectedWord = (Word) token;
				model.getIdentifyFWandCWPanel().setWord(selectedWord);
			}
			popMenu.setVisible(false);
			popMenu = new JPopupMenu();
			JMenuItem fw = new JMenuItem( "function word");
			fw.addActionListener(this);
			fw.setActionCommand("FW");
			popMenu.add(fw);
			JMenuItem cw = new JMenuItem( "semantic constitutive word");
			cw.addActionListener(this);
			cw.setActionCommand("CW");
			popMenu.add(cw);
			popMenu.setLocation(p);
			popMenu.setVisible(true);
		}
		else{
			mouseactive=false;
		}
	}

	/**
	 * wird jedes mal ausgefuehrt, wenn das Menue angezeigt wird
	 */
	@Override
	public void doAction() {
		ConstitutiveWord cw = model.firstNotAssignedCW();
		FunctionWord fw = model.firstNotAssignedFW();
		model.getView().designText(Model.getIllocutionUnitRoots());
		model.getRightPanel().validate();
		if (allWordsSet()) {
			if (cw == null && fw == null) {
				continueWithMU((byte) 0);
			}
			else
				continueWithMU((byte) 1);
		} else {
			model.getView().scrollTo(selectedWord);
			model.getIdentifyFWandCWPanel().reset();
		}
	}
}