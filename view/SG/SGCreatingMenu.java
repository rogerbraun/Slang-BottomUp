/*
 * Created on 02.04.2005
 */

package view.SG;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import view.Superclasses.Menu;
import controller.SG.SGCreatingController;

/**
 * @author shanthy
 */
public class SGCreatingMenu extends Menu {

	/**
	 * automatisch generierte ID
	 */
	private static final long serialVersionUID = -3309553178002003087L;

	/**
	 * Label fuer die erste Meaning Unit
	 */
	private JLabel mu1;

	/**
	 * Label fuer die zweite Meaning Unit
	 */
	private JLabel mu2;

	/**
	 * Label fuer das Function Word
	 */
	private JLabel fw;

	/**
	 * @param controller
	 *            SGCreatingController
	 */
	public SGCreatingMenu(SGCreatingController controller) {
		super(controller, "");
		mu1 = new JLabel("first MU");
		add(mu1);
		addSeparator();
		fw = new JLabel("optional fw");
		add(fw);
		addSeparator();
		mu2 = new JLabel("second MU");
		add(mu2);
		addSeparator();

		JButton createButton = new JButton("create/reassign relation");
		createButton.setActionCommand("CREATE");
		createButton.addActionListener(controller);
		add(createButton);
		addSeparator();
		
		addSeparator();
		
		JButton backButton = new JButton("delete existing SG");
		backButton.setActionCommand("DELETE");
		backButton.addActionListener(controller);
		addSeparator();
		add(backButton);
		addSeparator();
	
		add(new JSeparator(SwingConstants.VERTICAL));
		
		JButton previousButton = new JButton("go back to meaning units");
		previousButton.setActionCommand("PREVIOUS");
		previousButton.addActionListener(controller);
		addSeparator();
		add(previousButton);
		
		JButton continueButton = new JButton("go to illocution units");
		continueButton.setActionCommand("CONTINUE");
		continueButton.addActionListener(controller);
		addSeparator();
		add(continueButton);
	}

	/**
	 * zeig den Text des Strings auf dem Label an
	 * 
	 * @param s
	 *            String
	 */
	public void setMU1(String s) {
		mu1.setText(s);
	}

	/**
	 * zeig den Text des Strings auf dem Label an
	 * 
	 * @param s
	 *            String
	 */
	public void setMU2(String s) {
		mu2.setText(s);
	}

	/**
	 * zeig den Text des Strings auf dem Label an
	 * 
	 * @param s
	 *            String
	 */
	public void setFW(String s) {
		fw.setText(s);
	}

	/**
	 * @return Returns the Text of fw.
	 */
	public String getFw() {
		return fw.getText();
	}

	/**
	 * @return Returns the Text of mu1.
	 */
	public String getMu1() {
		return mu1.getText();
	}

	/**
	 * @return Returns the Text of mu2.
	 */
	public String getMu2() {
		return mu2.getText();
	}

	/**
	 * setzt wieder auf die anfangswerte
	 */
	public void reset() {
		mu1.setText("first MU");
		mu2.setText("second MU");
		fw.setText("optional fw");
	}

	/**
	 * setzt das fw-Label auf den Anfangswert
	 */
	public void resetFW() {
		fw.setText("optional fw");
	}

	/**
	 * setzt das mu1-Label auf den Anfangswert
	 */
	public void resetMU1() {
		mu1.setText("first MU");
	}

	/**
	 * setzt das mu2-Label auf den Anfangswert
	 */
	public void resetMU2() {
		mu2.setText("second MU");
	}
}