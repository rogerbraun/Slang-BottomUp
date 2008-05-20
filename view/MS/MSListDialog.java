package view.MS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import de.uni_tuebingen.wsi.ct.slang2.dbc.data.IllocutionUnitRoot;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.MacroSentence;

/**
 * Einfacher Dialog, der die Elemente eines MS (Head+Dependencies) darstellt.
 * Vorlage von der Sun Seite zu JList 
 * @author timo
 * 
 */

public class MSListDialog extends JDialog {
	private JList list;
    private DefaultListModel listModel;
    
	public void displayMS(MacroSentence ms) {
		listModel.removeAllElements();
		listModel.addElement(ms.getHead().getIllocutionUnit());  // Head	
		for (int i=0; i!=ms.getDependencies().size(); ++i) {  // Dependencies
			listModel.addElement(((IllocutionUnitRoot) ms.getDependencies().get(i)).getIllocutionUnit());
		}
	}
	
	public MSListDialog(Frame frame,
			Component locationComp,
			String labelText,
			String title) {
		super(frame, title, false);

		final JButton setButton = new JButton("Accept");
		setButton.setActionCommand("Set");

		getRootPane().setDefaultButton(setButton);

		listModel = new DefaultListModel();
//		main part of the dialog
		list = new JList(listModel) {
//			Subclass JList to workaround bug 4832765, which can cause the
//			scroll pane to not let the user easily scroll up to the beginning
//			of the list.  An alternative would be to set the unitIncrement
//			of the JScrollBar to a fixed value. You wouldn't get the nice
//			aligned scrolling, but it should work.
			public int getScrollableUnitIncrement(Rectangle visibleRect,
					int orientation,
					int direction) {
				int row;
				if (orientation == SwingConstants.VERTICAL &&
						direction < 0 && (row = getFirstVisibleIndex()) != -1) {
					Rectangle r = getCellBounds(row, row);
					if ((r.y == visibleRect.y) && (row != 0))  {
						Point loc = r.getLocation();
						loc.y--;
						int prevIndex = locationToIndex(loc);
						Rectangle prevR = getCellBounds(prevIndex, prevIndex);

						if (prevR == null || prevR.y >= r.y) {
							return 0;
						}
						return prevR.height;
					}
				}
				return super.getScrollableUnitIncrement(
						visibleRect, orientation, direction);
			}
		};

		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setButton.doClick(); //emulate button click
				}
			}
		});
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);

//		Create a container so that we can add a title around
//		the scroll pane.  Can't add a title directly to the
//		scroll pane because its background would be white.
//		Lay out the label and scroll pane from top to bottom.
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(labelText);
		label.setLabelFor(list);
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

//		Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);

//		Put everything together, using the content pane's BorderLayout.
		Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(locationComp);
		setVisible(true);
	}
}
