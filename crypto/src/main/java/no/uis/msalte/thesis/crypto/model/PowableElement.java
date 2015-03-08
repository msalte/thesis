package no.uis.msalte.thesis.crypto.model;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

public class PowableElement {
	private Element element;
	private ElementPowPreProcessing powPreProcessing;

	public PowableElement(Element element) {
		this.element = element;
		this.powPreProcessing = element.getElementPowPreProcessing();
	}

	public Element element() {
		return element.getImmutable();
	}

	public ElementPowPreProcessing powable() {
		return powPreProcessing;
	}

}
