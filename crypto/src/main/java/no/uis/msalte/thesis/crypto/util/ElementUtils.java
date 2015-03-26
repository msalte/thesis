package no.uis.msalte.thesis.crypto.util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.ArrayList;
import java.util.Arrays;
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

	public static Element[] messageToElementsInGroup(String message, Field<?> group) {
		ArrayList<Element> elements = new ArrayList<Element>();

		byte[] source = message.trim().getBytes();

		int messageLengthInBytes = source.length;
		int elementLengthInBytes = group.getLengthInBytes();

		// Need to make sure that the message length is divisible
		// by the element length.
		if (messageLengthInBytes < elementLengthInBytes) {
			// If lesser, set message length equal to element length
			source = Arrays.copyOf(source, elementLengthInBytes);
		} else if (messageLengthInBytes > elementLengthInBytes) {
			// If greater, set message length to a multiple of element length
			int factor = messageLengthInBytes / elementLengthInBytes + 1;

			source = Arrays.copyOf(source, factor * elementLengthInBytes);
		}

		messageLengthInBytes = source.length;

		// Begin finding the elements in group 2
		// representing the message
		int offset = 0;
		while (true) {
			Element e = group.newElement();
			offset += e.setFromBytes(source, offset);
			elements.add(e.getImmutable());

			if (offset >= messageLengthInBytes) {
				// Finished
				break;
			}
		}

		return elements.toArray(new Element[elements.size()]);
	}
	
	public static String elementToBase64String(Element element) {
		return Base64.getEncoder().encodeToString(element.toBytes());
	}
}
