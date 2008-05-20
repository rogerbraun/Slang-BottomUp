/*
 * Created on 13.12.2005
 */

package additionalPrograms.WordSplitter;

/**
 * ein Element im WordSplitter
 * @author shanthy
 */
public class ListElement {

	/**
	 * der unformatierte String
	 */
	private String word;

	/**
	 * der formatierte String
	 */
	private String toString;

	/**
	 * war der letzte Teil dunkelrot?
	 */
	private boolean darkRed = false;

	/**
	 * war der letzte Teil hellrot?
	 */
	private boolean lightRed = false;

	/**
	 * @param word Word
	 */
	public ListElement(String word) {
		this.word = word;
		createToString();
	}

	/**
	 * 
	 *formatierten String erstellen
	 */
	private void createToString() {
		String s = word;
		toString = "<html>";
		while (s.length() > 0) {
			if (s.startsWith("{") && !darkRed) {
				s = s.substring(1, s.length());
				int end = s.indexOf("}");
				String cw = s.substring(0, end);
				toString += "<font color=\"#ff2222\">" + cw + "</font>";
				s = s.substring(end + 1, s.length());
				darkRed = true;
			} else if (s.startsWith("{") && darkRed) {
				s = s.substring(1, s.length());
				int end = s.indexOf("}");
				String cw = s.substring(0, end);
				toString += "<font color=\"#cc0909\">" + cw + "</font>";
				s = s.substring(end + 1, s.length());
				darkRed = false;
			} else if (s.startsWith("(") && !lightRed) {
				s = s.substring(1, s.length());
				int end = s.indexOf(")");
				String fw = s.substring(0, end);
				toString += "<font color=\"#00c800\">" + fw + "</font>";
				s = s.substring(end + 1, s.length());
				lightRed = true;
			} else if (s.startsWith("(") && lightRed) {
				s = s.substring(1, s.length());
				int end = s.indexOf(")");
				String fw = s.substring(0, end);
				toString += "<font color=\"#0a8c0a\">" + fw + "</font>";
				s = s.substring(end + 1, s.length());
				lightRed = false;
			} else {
				int start1 = s.indexOf("{");
				int start2 = s.indexOf("(");
				if (start2 > start1 && start1 != -1) {
					String undefined = s.substring(0, start1);
					toString += undefined;
					s = s.substring(start1, s.length());
				} else if (start1 > start2 && start2 != -1) {
					String undefined = s.substring(0, start2);
					toString += undefined;
					s = s.substring(start2, s.length());
				} else if (start2 == -1 && start1 > -1) {
					String undefined = s.substring(0, start1);
					toString += undefined;
					s = s.substring(start1, s.length());
				} else if (start1 == -1 && start2 > -1) {
					String undefined = s.substring(0, start2);
					toString += undefined;
					s = s.substring(start2, s.length());
				} else if (start1 == -1 && start2 == -1) {
					toString += s;
					s = "";
				}
			}
		}
		toString += "</html>";
		if (toString.equals("<html></html>"))
			toString = null;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		return toString;
	}

	/**
	 * @return Returns the word.
	 */
	public String getWord() {
		return word;
	}
}
