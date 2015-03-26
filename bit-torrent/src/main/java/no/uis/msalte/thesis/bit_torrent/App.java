package no.uis.msalte.thesis.bit_torrent;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;

public class App {

	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		 BitTorrentTracker tracker = new BitTorrentTracker(6969).start();
		 tracker.start();
		
		 File torrent = new File("C:\\Users\\Morten\\Desktop\\d4a23310-aaf6-4518-aaf7-30b20c5eb74d.torrent");
		 tracker.announce(torrent);
		//
		// BitTorrentClient c1 = new BitTorrentClient(
		// "C:\\Users\\Morten\\Desktop\\yeah.torrent",
		// "C:\\Users\\Morten\\Desktop\\ttorrent\\owner_dir\\");
		//
		// c1.getClient().share();
		//
		// BitTorrentClient c2 = new BitTorrentClient(
		// "C:\\Users\\Morten\\Desktop\\yeah.torrent",
		// "C:\\Users\\Morten\\Desktop\\ttorrent\\recipient_dir\\");
		//
		// c2.getClient().download();

//		byte[] keyBytes = "mykey".getBytes();
//
//		String message = "Denne meldingen er rimelig lang, men egentlig ikkje særlig lang allikevel hehe :D";
//
//		Cipher encryptionCipher = Cipher.getInstance("RC4");
//
//		SecretKeySpec key = new SecretKeySpec(keyBytes, "RC4");
//
//		encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
//
//		byte[] ciphertext = encryptionCipher.update(message.getBytes());
//
//		Cipher decryptionCipher = Cipher.getInstance("RC4");
//
//		decryptionCipher.init(Cipher.DECRYPT_MODE, key);
//
//		byte[] plaintext = decryptionCipher.update(ciphertext);
//
//		System.out.println("decrypted: " + new String(plaintext));
	}
}
