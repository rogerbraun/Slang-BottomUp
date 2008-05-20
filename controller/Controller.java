package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import model.Model;
import view.View;
import view.Superclasses.Designer;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoots;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Token;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.Word;

/*
 * Created on 26.01.2005
 */

/**
 *  Superklasse für alle Controller: hier werden die Aktionen der
 *         einzelnen Menues verarbeitet
 * @author shanthy
 */
public class Controller implements WindowListener, ActionListener,
		CaretListener, MouseListener {

	/**
	 * fuer die JOptionPane-Optionen: FunctionWord
	 */
	public final static int FW = 0;

	/**
	 * fuer die JOptionPane-Optionen: ConstitutiveWord
	 */
	public final static int CW = 1;

	/**
	 * fuer die JOptionPane-Optionen: Part of word
	 */
	public final static int PART = 2;

	/**
	 * fuer die JOptionPane-Optionen: not sure
	 */
	public final static int NOTSURE = 3;

	/**
	 * fuer die JOptionPane-Optionen: for all
	 */
	public final static int FORALL = 3;

	/**
	 * fuer die JOptionPane-Optionen: close
	 */
	public final static int CLOSE = -1;

	/**
	 * Model
	 */
	protected Model model;

	/**
	 * der zugehörige Designer
	 */
	protected Designer designer;

/**
 * View
 */
	private View view;

	/**
	 * @param m Model
	 * @param designer Designer
	 */
	public Controller(Model m, Designer designer) {
		model = m;
		view = null;
		this.designer = designer;
	}

	/**
	 * die View setzen
	 * 
	 * @param textPart View
	 */
	public void setTextPart(View textPart) {
		this.view = textPart;
	}

	/**
	 * view auf den neusten Stand bringen, was den Controller angeht
	 */
	public void refreshView() {
		view.setController(this);
	}

	/**
	 * @return designer Designer
	 */
	public Designer getDesigner() {
		return designer;
	}
	
	/**
	 * @return designer Model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Quit, Save, Load
	 * 
	 * @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("QUIT")) {
			if (model.hasModelChanged()) {
				int ok = JOptionPane.showConfirmDialog(null,
						"Would you like to save the changes?", "Question",
						JOptionPane.YES_NO_OPTION);
				if (ok == JOptionPane.YES_OPTION) {
					model.save();
					model.dispose();
					Model.getDBC().close();
					System.exit(0);
				}
			}
			model.dispose();
			Model.getDBC().close();
			System.exit(0);

		} else if (e.getActionCommand().equals("SAVE")) {
			model.save();
			model.modelChanged(false);
		} else if (e.getActionCommand().equals("LOAD")) {
			if (model.hasModelChanged()) {
				int ok = JOptionPane.showConfirmDialog(null,
						"Would you like to save the changes?", "Question",
						JOptionPane.YES_NO_OPTION);
				if (ok == JOptionPane.YES_OPTION) {
					model.save();
					model.load();
				} else {
					model.load();
				}
			} else {
				model.load();
			}
		} else if (e.getActionCommand().equals("PRAG_WO")) {
			model.startPragWo();
		}
	}

	/**
	 * Fuer Aktionen im JTextPane
	 * 
	 * @param e CaretEvent
	 */
	public void caretUpdate(CaretEvent e) {
		Token token = Model.getChapter().getTokenAtPosition(e.getDot());
		IllocutionUnitRoots iurs = Model.getIllocutionUnitRoots();
		if (token instanceof Word) {
			Word word = (Word) token;
			Vector fws = iurs.getFunctionWordsAtIndex(word.getIndex());
			if (fws.size() > 0 && Model.getViewer() != null) {
				Model.getViewer().setRoot(
						Model.getIllocutionUnitRoots()
								.getFunctionWordAtPosition(e.getDot())
								.getRoot());
			}
			Vector cws = iurs.getConstitutiveWordsAtIndex(word.getIndex());
			if (cws.size() > 0) {
				int x = -1;
				try {
					x = e.getDot();
					Model.getViewer()
							.setRoot(
									Model.getIllocutionUnitRoots()
											.getConstitutiveWordAtPosition(x)
											.getRoot());
				} catch (NullPointerException ex) {
					try {
						x = e.getDot() - 1;
						Model.getViewer().setRoot(
								Model.getIllocutionUnitRoots()
										.getConstitutiveWordAtPosition(x)
										.getRoot());
					} catch (NullPointerException exe) {
						try {
							x = e.getDot() + 1;
							Model.getViewer().setRoot(
									Model.getIllocutionUnitRoots()
											.getConstitutiveWordAtPosition(x)
											.getRoot());
						} catch (NullPointerException npe) {
							// Nothing todo
						}
					}
				}
			}
		}
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowActivated(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowClosed(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowClosing(@SuppressWarnings("unused") WindowEvent e) {
		if (model.hasModelChanged()) {
			int ok = JOptionPane.showConfirmDialog(null,
					"Would you like to save the changes?", "Question",
					JOptionPane.YES_NO_OPTION);
			if (ok == JOptionPane.YES_OPTION) {
				model.save();
				model.dispose();
				System.exit(0);
			}
		}
		model.dispose();
		System.exit(0);
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowDeactivated(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowDeiconified(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowIconified(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * @param e WindowEvent
	 */
	public void windowOpened(@SuppressWarnings("unused") WindowEvent e) {
		// wird nicht benoetigt
	}

	/**
	 * Aktion, die ausgeführt wird, wenn das Menu angezeigt wird
	 */
	public void doAction() {
		// ist fuer jedes Menue anders
	}

	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	public void mouseClicked(@SuppressWarnings("unused") MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	public void mousePressed(@SuppressWarnings("unused") MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	public void mouseReleased(@SuppressWarnings("unused") MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	public void mouseEntered(@SuppressWarnings("unused") MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	/**
	 * @param arg0 MouseEvent
	 * 
	 */
	public void mouseExited(@SuppressWarnings("unused") MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
}