package no.uis.msalte.thesis.crypto.pre.afgh;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;

public class Generator {
	private Element element;
	private ElementPowPreProcessing powPreProcessing;

	public Generator(Element element) {
		this.element = element;
		this.powPreProcessing = element.pow();
	}

	public Element getElement() {
		return element;
	}

	public ElementPowPreProcessing getPowPreProcessing() {
		return powPreProcessing;
	}

}
