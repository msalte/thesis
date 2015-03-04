package no.uis.msalte.thesis.crypto;

import it.unisa.dia.gas.jpbc.Element;
import no.uis.msalte.thesis.crypto.pre.model.CipherTuple;
import no.uis.msalte.thesis.crypto.pre.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.pre.scheme.ProxyReEncryptionScheme;

public class App {

	public static void main(String[] args) {
		final ProxyReEncryptionParameters parameters = 
				new ProxyReEncryptionParameters().initialize();
		
		final ProxyReEncryptionScheme scheme = new ProxyReEncryptionScheme(parameters);

		Element bobSecretKey = scheme.newSecretKey();
		Element bobPublicKey = scheme.newPublicKey(bobSecretKey);

		String m = "Denne meldingen er hemmelig";
		
		CipherTuple c = scheme.encrypt(scheme.messageToElement(m), bobPublicKey);

		String decrypted = scheme.elementToMessage(scheme.decrypt(c, bobSecretKey));
		
		System.out.println(decrypted);
	}

}
