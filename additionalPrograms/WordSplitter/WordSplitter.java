package additionalPrograms.WordSplitter;

import java.util.Vector;


/**
 * @author volker, geändert von Shanthy ;-)
 */
public class WordSplitter {

	/**
	 * bereits gemachte Bestimmungen von dem Wort
	 */
	private String[] cws;

	/**
	 *  bereits gemachte Bestimmungen von dem Wort
	 */
	private String[] fws;

	/**
	 * @param cws Vector
	 * @param fws Vector
	 */
	public WordSplitter(Vector cws, Vector fws) {
		this.cws = new String[cws.size()];
		this.fws = new String[fws.size()];
/*		for (int i = 0; i < this.cws.length; i++)
			this.cws[i] = ((String) cws.get(i)).toLowerCase();
		for (int i = 0; i < this.fws.length; i++)
			this.fws[i] = ((String) fws.get(i)).toLowerCase();		*/
		for (int i = 0; i < this.cws.length; i++){
			if(cws.get(i).toString().startsWith("content:"))
			{
				// wert ist in hashtabel mit key content: abgespeichert und muss ausgeschnitten werden
				String tmp = new String();
				tmp = cws.get(i).toString();
				this.cws[i] = tmp.substring(8, tmp.length()).toLowerCase();
			}
			else
				this.cws[i] = ((String) cws.get(i)).toLowerCase();
							
		}
		for (int i = 0; i < this.fws.length; i++)
			{
				if(fws.get(i).toString().startsWith("content:"))
				{
					// wert ist in hashtabel mit key content: abgespeichert und muss ausgeschnitten werden
					String tmp = new String();
					tmp = fws.get(i).toString();
					System.out.println(tmp);
					this.fws[i] = tmp.substring(8, tmp.length()).toLowerCase();
					System.out.println(this.fws[i]);
				}
				else
					this.fws[i] = ((String) fws.get(i)).toLowerCase();
			}
			
	}

	/**
	 * teilt das Wort in Teilwörter
	 * @param word Word
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector split(String word) {
		Vector results = new Vector();
		Vector cwResults = new Vector();

		splitCWs(word.toLowerCase(), "", "", cwResults);

		for (int i = 0; i < cwResults.size(); i++) {
			String r = (String) cwResults.get(i);
			Vector undef = getUndefined(r);
			boolean onlyCW = true;
			for (int j = 0; j < undef.size(); j++) {
				String u = (String) undef.get(j);
				if (!u.startsWith("{")) {
					onlyCW = false;
					Vector fwResults = new Vector();
					splitFWs(u, "", "", fwResults);

					for (int k = 0; k < fwResults.size(); k++)
						addResult(results,
								reconstructCapitalization(word, makeString(
										undef, j, (String) fwResults.get(k))));
				}
			}
			if (onlyCW)
				addResult(results, reconstructCapitalization(word, makeString(
						undef, undef.size(), "")));
		}
		sort(results);

		Vector listElementResults = new Vector();
		for (int i = 0; i < results.size(); i++) {
			String s = (String) results.get(i);
			if (s.length() > word.length()) {
				ListElement listElement = new ListElement(s);
				listElementResults.add(listElement);
			}
		}
		return listElementResults;
	}

	/**
	 * 
	 * @param v Vector
	 */
	@SuppressWarnings("unchecked")
	private static void sort(Vector v) {
		int[] weights = new int[v.size()];
		for (int i = 0; i < weights.length; i++)
			weights[i] = calculateWeight((String) v.get(i));

		for (int i = 0; i < weights.length; i++) {
			int change = getMax(weights, i, weights.length);

			int t1 = weights[i];
			weights[i] = weights[change];
			weights[change] = t1;

			Object t2 = v.get(i);
			v.set(i, v.get(change));
			v.set(change, t2);
		}
	}

	/**
	 * 
	 * @param a int[]
	 * @param start int
	 * @param end int
	 * @return int
	 */
	private static int getMax(int[] a, int start, int end) {
		int index = -1;
		int found = Integer.MIN_VALUE;
		for (int i = start; i < end; i++)
			if (a[i] >= found) {
				index = i;
				found = a[i];
			}
		return index;
	}

	/**
	 * 
	 * @param s String
	 * @return int
	 */
	private static int calculateWeight(String s) {
		int fw = countChar(s, '(', ')');
		int cw = countChar(s, '{', '}');
		return fw + cw + cw;
	}

	/**
	 * 
	 * @param s String
	 * @param start char
	 * @param end char
	 * @return int
	 */
	private static int countChar(String s, char start, char end) {
		char[] cs = s.toCharArray();
		int found = 0;
		boolean count = false;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == start) {
				count = true;
				continue;
			}
			if (cs[i] == end) {
				count = false;
				continue;
			}

			if (count)
				found++;
		}
		return found;
	}

	/**
	 * 
	 * @param original String
	 * @param combination String
	 * @return String
	 */
	private static String reconstructCapitalization(String original,
			String combination) {
		char[] ocs = original.toCharArray();
		char[] ccs = combination.toCharArray();
		char[] res = new char[ccs.length];

		for (int i = 0, j = 0; i < ccs.length; i++, j++) {
			if (ccs[i] == '{' || ccs[i] == '}' || ccs[i] == '('
					|| ccs[i] == ')') {
				j--;
				res[i] = ccs[i];
			} else
				res[i] = ocs[j];
		}

		return new String(res);
	}

	/**
	 * 
	 * @param v Vector
	 * @param index int
	 * @param s String
	 * @return String
	 */
	private String makeString(Vector v, int index, String s) {
		String temp = "";
		for (int i = 0; i < v.size(); i++) {
			if (i == index)
				temp += s;
			else
				temp += (String) v.get(i);
		}
		return temp;
	}

	/**
	 * 
	 * @param todo String
	 * @param left String
	 * @param right String
	 * @param results Vector
	 */
	private void splitCWs(String todo, String left, String right, Vector results) {
		for (int i = 0; i < cws.length; i++) {
			String cw = cws[i];
			String[] split = split(todo, cw);

			if (split == null)
				continue;

			if (split.length == 1) {
				addResult(results, left + "{" + split[0] + "}" + right);
				continue;
			}

			String l = "";
			String w = "";
			String r = "";
			for (int j = 0; j < split.length; j++) {
				if (split[j].equals(cw))
					w = split[j];
				else {
					if (j == 0)
						l = split[0];
					else
						r = split[j];
				}
			}
			if (l.length() > 0)
				splitCWs(l, left, "{" + w + "}" + r + right, results);
			if (r.length() > 0)
				splitCWs(r, left + l + "{" + w + "}", right, results);
		}

		addResult(results, left + todo + right);
	}

	/**
	 * 
	 * @param todo String
	 * @param left String
	 * @param right String
	 * @param results Vector
	 */
	private void splitFWs(String todo, String left, String right, Vector results) {
		boolean found = false;
		for (int i = 0; i < fws.length; i++) {
			String fw = fws[i];
			String[] split = split(todo, fw);

			if (split == null)
				continue;

			found = true;

			if (split.length == 1) {
				addResult(results, left + "(" + split[0] + ")" + right);
				continue;
			}

			String l = "";
			String w = "";
			String r = "";
			for (int j = 0; j < split.length; j++) {
				if (split[j].equals(fw))
					w = split[j];
				else {
					if (j == 0)
						l = split[0];
					else
						r = split[j];
				}
			}
			if (l.length() > 0)
				splitFWs(l, left, "(" + w + ")" + r + right, results);
			// if (w.length() > 0)
			// splitFWs(w, left + l, r + right, results);
			if (r.length() > 0)
				splitFWs(r, left + l + "(" + w + ")", right, results);
		}

		if (!found)
			addResult(results, left + todo + right);
	}

	/**
	 * 
	 * @param results Vector
	 * @param result String
	 */
	@SuppressWarnings("unchecked")
	private static void addResult(Vector results, String result) {
		if (!results.contains(result))
			results.add(result);
	}

	/**
	 * 
	 * @param s String
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	private static Vector getUndefined(String s) {
		Vector res = new Vector();
		char[] cs = s.toCharArray();
		String temp = "";

		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '{') {
				if (temp.length() > 0)
					res.add(temp);
				temp = "" + cs[i];
			} else if (cs[i] == '}') {
				temp += cs[i];
				res.add(temp);
				temp = "";
			} else
				temp += cs[i];
		}

		if (temp.length() > 0)
			res.add(temp);

		return res;
	}

	/**
	 * 
	 * @param a String
	 * @param b String 
	 * @return String[]
	 */
	private static String[] split(String a, String b) {
		int index = contains(a, b);
		if (index < 0)
			return null;
		if (a.equals(b))
			return new String[] { a };
		if (index == 0)
			return new String[] { b, a.substring(b.length()) };
		if (index + b.length() == a.length())
			return new String[] { a.substring(0, index), b };
		if (index > 0)
			return new String[] { a.substring(0, index), b,
					a.substring(index + b.length()) };
		return null;
	}

	/**
	 * 
	 * @param a String
	 * @param b String
	 * @return int
	 */
	private static int contains(String a, String b) {
		char[] ca = a.toCharArray();
		char[] cb = b.toCharArray();
		int j = 0;

		if (cb.length > ca.length)
			return -1;

		for (int i = 0; i < ca.length; i++) {
			if (ca[i] != cb[j]) {
				if (j > 0)
					i--;
				j = 0;
			} else if (++j == cb.length)
				return i - j + 1;
		}
		return -1;
	}
}
