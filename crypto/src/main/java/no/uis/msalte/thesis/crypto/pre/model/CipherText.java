package no.uis.msalte.thesis.crypto.pre.model;

import it.unisa.dia.gas.jpbc.Element;

public class CipherText {
	private Element c1;
	private Element c2;

	public CipherText(Element c1, Element c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	public Element getC1() {
		return c1.getImmutable();
	}

	public Element getC2() {
		return c2.getImmutable();
	}
}
