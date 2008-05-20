package additionalPrograms.MUSG_Viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JPanel;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MeaningUnit;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.SememeGroup;

/*
 * Erstellt: 02.04.2005
 */

/**
 * @author Volker Kloebb
 */
public class MUSG_Viewer extends JPanel {

	/**
	 * zufällig erstellte Meaning Unit
	 */
	private static final long serialVersionUID = 4040439282337654459L;

	/**
	 * anzuzeigende Meaning Units
	 */
	private Vector meaningUnits;

	/**
	 * anzuzeigende Sememgruppen
	 */
	private Vector sememeGroups;

	private BasicStroke stroke;

	
	private static Vector<IllocutionUnitRoot> lastRoots;
	/**
	 * 
	 * 
	 */
	public MUSG_Viewer() {
		setBackground(Color.WHITE);
		meaningUnits = new Vector();
		sememeGroups = new Vector();
		stroke = new BasicStroke(2);
		lastRoots = new Vector<IllocutionUnitRoot>();
	}

	/** 
	 * @param root IllocutionUnitRoot
	 */
	@SuppressWarnings("unchecked")
	public void setRoot(IllocutionUnitRoot root) {
		meaningUnits.clear();
		sememeGroups.clear();
		Graphics2D g = (Graphics2D) getGraphics();
		int dy = -30;
		int maxWidth = 0;
		Rectangle2D bounds = new Rectangle2D.Float();

		Vector mus = root.getMeaningUnits();
		for (int i = 0; i < mus.size(); i++) {
			MU_Element mue = new MU_Element((MeaningUnit) mus.get(i), g);
			mue.setUpperLeftPosition(20, dy += 50);
			bounds.add(mue.getBounds());
			maxWidth = Math.max(maxWidth, mue.getWidth());
			meaningUnits.add(mue);
		}

		dy = -5;
		Vector sgs = root.getSememeGroups();
		for (int i = 0; i < sgs.size(); i++) {
			SememeGroup sg = (SememeGroup) sgs.get(i);
			SG_Element se = new SG_Element(sg, g, getMU_Element(sg.getFirst()),
					getMU_Element(sg.getSecond()));
			se.setUpperLeftPosition(maxWidth + 80, dy += 50);
			bounds.add(se.getBounds());
			sememeGroups.add(se);
		}

		setPreferredSize(new Dimension((int) bounds.getWidth() + 20,
				(int) bounds.getHeight() + 20));
		repaint();
		
		lastRoots.add(root);
	}
	
	//---------------------------------------------
	/** 
	 * @param root IllocutionUnitRoot
	 */
	@SuppressWarnings("unchecked")
	public void setLastRoot() {
		if(lastRoots.size()>1)
		{
			IllocutionUnitRoot root = lastRoots.elementAt(lastRoots.size()-1);
			root.removeLastSG();
			meaningUnits.clear();
			sememeGroups.clear();
			Graphics2D g = (Graphics2D) getGraphics();
			int dy = -30;
			int maxWidth = 0;
			Rectangle2D bounds = new Rectangle2D.Float();

			Vector mus = root.getMeaningUnits();
			for (int i = 0; i < mus.size(); i++) {
				MU_Element mue = new MU_Element((MeaningUnit) mus.get(i), g);
				mue.setUpperLeftPosition(20, dy += 50);
				bounds.add(mue.getBounds());
				maxWidth = Math.max(maxWidth, mue.getWidth());
				meaningUnits.add(mue);
			}

			dy = -5;
			Vector sgs = root.getSememeGroups();
			for (int i = 0; i < sgs.size(); i++) {
				SememeGroup sg = (SememeGroup) sgs.get(i);
				SG_Element se = new SG_Element(sg, g, getMU_Element(sg.getFirst()),
						getMU_Element(sg.getSecond()));
				se.setUpperLeftPosition(maxWidth + 80, dy += 50);
				bounds.add(se.getBounds());
				sememeGroups.add(se);
			}

			setPreferredSize(new Dimension((int) bounds.getWidth() + 20,(int) bounds.getHeight() + 20));
			repaint();
			lastRoots.removeElementAt(lastRoots.size()-1);
		}
		else if(lastRoots.size()==1)
		{
			repaint();
		}
	}
	//-----------------------------------------------

	/**
	 * 
	 * @param mu MeaningUnit
	 * @return MU_Element
	 */
	private MU_Element getMU_Element(MeaningUnit mu) {
		for (int i = 0; i < meaningUnits.size(); i++) {
			MU_Element m = (MU_Element) meaningUnits.get(i);
			if (m.getMeaningUnit() == mu)
				return m;
		}
		return null;
	}

	/**
	 * @param g Graphics
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2.setStroke(stroke);
		for (int i = 0; i < sememeGroups.size(); i++)
			((SG_Element) sememeGroups.get(i)).draw(g2);
		for (int i = 0; i < meaningUnits.size(); i++)
			((MU_Element) meaningUnits.get(i)).draw(g2);
	}
}