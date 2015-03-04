package no.uis.msalte.thesis.crypto.pre.model;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

public class CipherTuple {
	private Element[] tuple;

	public CipherTuple(Element[] tuple) {
		this.tuple = Arrays.copyOf(tuple, tuple.length);
	}

	public Element get(int index) {
		if (index < 0 || index > tuple.length - 1) {
			return null;
		}

		return tuple[index].getImmutable();
	}
}
