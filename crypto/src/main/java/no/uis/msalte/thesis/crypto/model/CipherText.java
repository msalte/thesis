package no.uis.msalte.thesis.crypto.model;

import it.unisa.dia.gas.jpbc.Element;

import org.apache.commons.lang.ArrayUtils;

public class CipherText {
	private Element[] lefts;
	private Element right;

	public CipherText(Element left, Element right) {
		appendLeft(left);

		this.right = right.getImmutable();
	}

	public CipherText() {

	}

	public CipherText appendLeft(Element element) {
		int next = ensureLeftsLength();

		if (next != -1 && next < lefts.length) {
			lefts[next] = element.getImmutable();
		}

		return this;
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

	public Element getLeft() {
		return lefts[0];
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
