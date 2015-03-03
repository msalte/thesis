package no.uis.msalte.thesis.crypto.pre.afgh;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.util.Random;

public class ProxyReEncryptionParameters {
	private static final int SOLINAS_PRIME_BITS = 256;
	private static final int PRIME_ORDER_BITS = 1024;

	private Pairing e;
	private Field<?> group1;
	private Field<?> group2;
	private Field<?> groupZr;
	private Powable g, z;

	public ProxyReEncryptionParameters initialize() {
		final CurveParameters cp = new TypeACurveGenerator(new Random(),
				SOLINAS_PRIME_BITS, PRIME_ORDER_BITS, false).generate();

		e = PairingFactory.getPairing(cp);

		group1 = e.getG1();
		group2 = e.getG2();
		groupZr = e.getZr();

		g = new Powable(((CurveField<?>) group1).getGen().getImmutable());
		z = new Powable(e.pairing(g.getElement(), g.getElement())
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

	public Field<?> getGroupZr() {
		return groupZr;
	}

	public Powable getG() {
		return g;
	}

	public Powable getZ() {
		return z;
	}

}
