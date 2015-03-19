package no.uis.msalte.thesis.crypto.model;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

public class PowableElement {
	private Element element;
	private ElementPowPreProcessing powPreProcessing;

	public PowableElement(Element element) {
		this.element = element.getImmutable();
		this.powPreProcessing = this.element.getElementPowPreProcessing();
	}

	public Element element() {
		return element;
	}

	public ElementPowPreProcessing powable() {
		return powPreProcessing;
	}

}
