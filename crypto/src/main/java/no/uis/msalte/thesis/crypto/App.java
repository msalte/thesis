package no.uis.msalte.thesis.crypto;

import it.unisa.dia.gas.jpbc.Element;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) {
		ProxyReEncryptionSchemeImpl scheme = new ProxyReEncryptionSchemeImpl(
				new ProxyReEncryptionParameters().initialize());

		String message = new String("Hello there. This message is short and secret.");
		
		Element[] elements = scheme.msgToElementsInGroup2(message);

		System.out.println(elements.length);

		for (int i = 0; i < elements.length; i++) {
			String byteString = new String(elements[i].toBytes());
			
			System.out.println(new String(byteString).trim());
		}

	}

}
