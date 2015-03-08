package no.uis.msalte.thesis.crypto.model;

import it.unisa.dia.gas.jpbc.Element;

import org.apache.commons.lang.ArrayUtils;

public class CipherText {
	private Element[] lefts;
	private Element right;

	public CipherText() {

	}

	public CipherText(Element left, Element right) {
		appendLeft(left);

		this.right = right.getImmutable();
	}

	public CipherText appendLeft(Element element) {
		int next = ensureLeftsLength();

		if (next != -1 && next < lefts.length) {
			lefts[next] = element.getImmutable();
		}

		return this;
	}

	public byte[][] toByteArrays() {
		int n = getLefts().length;

		// Create a set of byte arrays representing the entire cipher text
		// - the first N arrays represents the message's N left parts
		// - the last array represents the message's single right part
		byte[][] bytes = new byte[n + 1][1];

		for (int i = 0; i < n; i++) {
			bytes[i] = getLefts()[i].toBytes();
		}

		bytes[n] = getRight().toBytes();

		return bytes;
	}

	private int ensureLeftsLength() {
		Element[] array = new Element[1];

		if (lefts == null) {
			lefts = array;
		} else {
			lefts = (Element[]) ArrayUtils.addAll(lefts, array);
		}

		return lefts.length - 1;
	}
	
	public Element[] getLefts() {
		return lefts;
	}

	public Element getRight() {
		return right;
	}

	public void setLefts(Element[] lefts) {
		this.lefts = lefts;
	}

	public void setRight(Element right) {
		this.right = right.getImmutable();
	}
}
