package no.uis.msalte.thesis.crypto.scheme;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import no.uis.msalte.thesis.crypto.App;
import no.uis.msalte.thesis.crypto.model.PowableElement;
import no.uis.msalte.thesis.crypto.util.ElementUtils;

public class ProxyReEncryptionParams {
	private static final String GENERATOR_BASE_64 = "P6PrrTuwA/qnfovO5Zf12Iolj8z4OxLe5IZkX1y0p3lTuPzMK04NJcNqmft35nYp7EK4m6CwaWFC6RWfkNf3fZg2rdbZEnYwhdNEnoSxLvfkkydF1lcSk5mce0WNlGqY43nFgPO6crpsg/BJZdxTC+Ju/QWp0jZAzbQbyvg8d/Y=";

	private static final String CURVE_DIR = "jpbc-curves";
	private static final String CURVE = "a.properties";

	private Pairing e;
	private Field<?> group1;
	private Field<?> group2;
	private Field<?> groupZq;
	private PowableElement g, z;

	public ProxyReEncryptionParams initialize() {
		e = PairingFactory.getPairing(String.format("%s\\%s\\%s", App.DIR,
				CURVE_DIR, CURVE));

		group1 = e.getG1();
		group2 = e.getGT();
		groupZq = e.getZr();

		g = new PowableElement(ElementUtils.base64StringToElement(
				GENERATOR_BASE_64, (CurveField<?>) group1));
		z = new PowableElement(e.pairing(g.element(), g.element()));

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
