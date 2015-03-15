package no.uis.msalte.thesis.crypto.scheme;

import java.util.Base64;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import no.uis.msalte.thesis.crypto.model.PowableElement;

public class ProxyReEncryptionParameters {
	private static final String BASE_64_GEN = "P6PrrTuwA/qnfovO5Zf12Iolj8z4OxLe5IZkX1y0p3lTuPzMK04NJcNqmft35nYp7EK4m6CwaWFC6RWfkNf3fZg2rdbZEnYwhdNEnoSxLvfkkydF1lcSk5mce0WNlGqY43nFgPO6crpsg/BJZdxTC+Ju/QWp0jZAzbQbyvg8d/Y=";

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

		g = new PowableElement(base64StringToCurveElement(BASE_64_GEN,
				(CurveField<?>) group1));
		z = new PowableElement(e.pairing(g.element(), g.element())
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

	public Element base64StringToElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		Element element = field.newZeroElement();

		element.setFromBytes(bytes);

		return element.getImmutable();
	}

	public Element base64StringToCurveElement(String s, Field<?> field) {
		byte[] bytes = Base64.getDecoder().decode(s);

		CurveElement<?, ?> curveElement = (CurveElement<?, ?>) field
				.newZeroElement();

		curveElement.setFromBytes(bytes);

		return curveElement.getImmutable();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("Parameter e: %s", getE()));
		sb.append("\n");
		sb.append(String.format("Parameter g: %s", getG().element()));
		sb.append("\n");
		sb.append(String.format("Parameter Z: %s", getZ().element()));
		sb.append("\n");
		sb.append(String.format("Group 1: %s", getGroup1()));
		sb.append("\n");
		sb.append(String.format("Group 2: %s", getGroup2()));
		sb.append("\n");
		sb.append(String.format("Group Zq: %s", getGroupZq()));

		return sb.toString();
	}
}
