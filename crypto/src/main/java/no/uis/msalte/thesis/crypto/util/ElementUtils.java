package no.uis.msalte.thesis.crypto.util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.Base64;

public class ElementUtils {
	public static Element base64StringToElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		Element element = field.newZeroElement();

		element.setFromBytes(bytes);

		return element.getImmutable();
	}
}
