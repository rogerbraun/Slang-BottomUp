package view.MS;

import javax.swing.JFrame;

public class MSFrame extends JFrame {
	MSListDialog msd;
	public MSFrame() {
		super();
		msd = new MSListDialog(this, null, null, "aktueller Makrosatz");
		msd.setAlwaysOnTop(true);
	}
	
	public MSListDialog getMSListDialog() {
		return msd;
	}
}
