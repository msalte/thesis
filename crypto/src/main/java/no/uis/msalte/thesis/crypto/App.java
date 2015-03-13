package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ProxyReEncryptionParameters parameters = new ProxyReEncryptionParameters().initialize();
		ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl(parameters);
		
//		String aliceSecretKey = "LoNbBsHp7hxdMsFJ0OxlWa+plno=";
//		String alicePublicKey = "O/+kXRX3afh70jOTi+Q7sPiYARdVguTakoBNwBnqEyBn8E7481XtuyBO5ZpMEhpsL6x3SGNa4WmIhML5KWnb2IbxuIwuM6MIRQs9ppgRcBiNmDrqKDXM8rjrXYth0ajcoNxboVFLZSXVwZterX5HxE19fhqfVFJ9Q8dxFhXhERY=";
		
		String aliceSecretKey = scheme.newSecretKey();
		String alicePublicKey = scheme.newPublicKey(aliceSecretKey);
		
		String bobSecretKey = scheme.newSecretKey();
		String bobPublicKey = scheme.newPublicKey(bobSecretKey);
		
		String aliceToBobReEncryptionKey = scheme.newReEncryptionKey(aliceSecretKey, bobPublicKey);
		
		String email = "This is a secret e-mail";
		
		String encryptedForAlice = scheme.encryptReEncryptable(email, alicePublicKey);
		String reEncryptedForBob = scheme.reEncrypt(encryptedForAlice, aliceToBobReEncryptionKey);
		
		String decryptedByAlice = scheme.decryptReEncryptable(encryptedForAlice, aliceSecretKey);
		String decryptedByBob = scheme.decrypt(reEncryptedForBob, bobSecretKey);
		
		System.out.println(decryptedByAlice);
		System.out.println(decryptedByBob);
		
	}

}
