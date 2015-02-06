package no.uis.msalte.thesis.crypto.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class ProxyReEncryptionParams {
	private Pairing pairing;
	private Field g1, g2, zr;
	private Element g, z;
	private ElementPowPreProcessing gPowPreProcessing, zPowPreProcessing;

	public void initialize() {
		pairing = PairingFactory
				.getPairing("C:\\source\\jpbc-params\\curves\\a1.properties");

		g1 = pairing.getG1();
		g2 = pairing.getG2();
		zr = pairing.getZr();

		g = ((CurveField) g1).getGen().getImmutable();
		z = pairing.pairing(g, g).getImmutable();

		gPowPreProcessing = g.getElementPowPreProcessing();
		zPowPreProcessing = z.getElementPowPreProcessing();
	}

	public Pairing getPairing() {
		return pairing;
	}

	public Field getG1() {
		return g1;
	}

	public Field getG2() {
		return g2;
	}

	public Field getZr() {
		return zr;
	}

	public Element getG() {
		return g;
	}

	public Element getZ() {
		return z;
	}

	public ElementPowPreProcessing getGPowPreProcessing() {
		return gPowPreProcessing;
	}

	public ElementPowPreProcessing getZPowPreProcessing() {
		return zPowPreProcessing;
	}

}
