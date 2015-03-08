package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import no.uis.msalte.thesis.crypto.model.PowableElement;

public class ProxyReEncryptionParameters {
	private Pairing e;
	private Field<?> group1;
	private Field<?> group2;
	private Field<?> groupZq;
	private PowableElement g, z;

	public ProxyReEncryptionParameters initialize() {
		e = PairingFactory
				.getPairing("C:\\source\\jpbc-params\\curves\\a.properties");

		group1 = e.getG1();
		group2 = e.getGT();
		groupZq = e.getZr();

		g = new PowableElement(((CurveField<?>) group1).getGen().getImmutable());
		z = new PowableElement(e.pairing(g.getElement(), g.getElement())
				.getImmutable());

		return this;
	}

	public Pairing getE() {
		return e;
	}

	public Field<?> getGroup1() {
		return group1;
	}

	public Field<?> getGroup2() {
		return group2;
	}

	public Field<?> getGroupZq() {
		return groupZq;
	}

	public PowableElement getG() {
		return g;
	}

	public PowableElement getZ() {
		return z;
	}
}
