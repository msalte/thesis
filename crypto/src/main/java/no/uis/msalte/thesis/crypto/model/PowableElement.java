package no.uis.msalte.thesis.crypto.model;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

public class PowableElement {
	private Element element;
	private ElementPowPreProcessing powPreProcessing;

	public PowableElement(Element element) {
		this.element = element;
		this.powPreProcessing = element.pow();
	}

	public Element getElement() {
		return element.getImmutable();
	}

	public ElementPowPreProcessing getPowPreProcessing() {
		return powPreProcessing;
	}

}
