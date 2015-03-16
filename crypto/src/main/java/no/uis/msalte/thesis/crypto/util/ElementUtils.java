package no.uis.msalte.thesis.crypto.util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.Base64;

public class ElementUtils {
	/**
	 * Looks up an Element in the specified Field that represents the given
	 * Base64 encoded string
	 * 
	 * @param string
	 *            A Base64 encoded string for which to find an Element
	 * @param field
	 *            The Field in which to look up the Element
	 * @return An Element object representing the given Base64 encoded string
	 */
	public static Element base64StringToElement(String string, Field<?> field) {
		final byte[] bytes = Base64.getDecoder().decode(string);
		
		final Element element = field.newZeroElement();

		element.setFromBytes(bytes);

		return element.getImmutable();
	}
}
