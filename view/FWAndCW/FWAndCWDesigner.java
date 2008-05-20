/*
 * Created on 13.06.2005
 */

package view.FWAndCW;

import java.awt.Color;
import java.util.Vector;

import model.Model;
import view.Superclasses.Designer;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.ConstitutiveWord;
import de.uni_tuebingen.wsi.ct.slang2.dbc.data.FunctionWord;

/**
 * Designer für Function Words und Constitutive Words
 * @author shanthy
 */
public class FWAndCWDesigner extends Designer {

	/**
	 * @param o
	 *            Object
	 * @return black
	 */
	@Override
	public Color getColor(Object o) {
		if (o instanceof ConstitutiveWord) {
			int index = getIndex((ConstitutiveWord) o);
			if (index == 0)
				return Model.RED1;
			return Model.RED2;
		} else if (o instanceof FunctionWord) {
			int index = getIndex((FunctionWord) o);
			if (index == 0)
				return Model.GREEN1;
			return Model.GREEN2;
		} else
			return super.getColor(o);
	}

	/**
	 * @param cw Constitutive Word
	 * @return int index
	 */
	private int getIndex(ConstitutiveWord cw) {
		Vector cws = cw.getRoot().getConstitutiveWords();
		return cws.indexOf(cw) % 2;
	}

	/**
	 * @param fw FunctionWord
	 * @return int index
	 */
	private int getIndex(FunctionWord fw) {
		Vector fws = fw.getRoot().getFunctionWords();
		return fws.indexOf(fw) % 2;
	}
}
