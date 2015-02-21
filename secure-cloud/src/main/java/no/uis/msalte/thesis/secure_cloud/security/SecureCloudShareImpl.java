package no.uis.msalte.thesis.secure_cloud.security;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;
import no.uis.msalte.thesis.bit_torrent.util.TorrentUtil;
import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.model.KeyTuple;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class SecureCloudShareImpl implements SecureCloudShare {
	private BitTorrentTracker tracker = new BitTorrentTracker(6969);
	private ElGamalParams params = new ElGamalParams(512);

	public String newSecretKey() {
		return params.newSecretKey().toString();
	}

	public String newPublicKey(String secretKey) {
		return params.newPublicKey(new BigInteger(secretKey)).toString();
	}

	public String newReEncryptionKey(String secretKey, String publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public String upload(File file) {
		if (TorrentUtil.isValidTorrent(file)) {
			final String fileName = String.format("%s.torrent", UUID
					.randomUUID().toString());

			try {
				final Path path = Paths.get(file.getAbsolutePath());
				final byte[] bytes = Files.readAllBytes(path);

				final String encodedFile = Base64.getEncoder().encodeToString(
						bytes);

				Persist.getInstance().storeTorrent(fileName, encodedFile);

				if (!tracker.isStarted()) {
					tracker.start();
				}

				tracker.announce(file);

				return fileName;
			} catch (IOException e) {
				// ignore
			}
		}

		return null;
	}

	public boolean share(String fileName, String publicKey,
			String reEncryptionKey) {
		return Persist.getInstance().storeKeysTuple(fileName,
				new KeyTuple(publicKey, reEncryptionKey));
	}

	public String download(String fileName, String publicKey) {
		final String reEncryptionKey = AccessControl.getReEncryptionKey(
				fileName, publicKey);

		final boolean hasAccess = reEncryptionKey != null;

		if (hasAccess) {
			String file = Persist.getInstance().readTorrent(fileName);

			// TODO re-encrypt

			return file;
		}

		return null;
	}

	public String newTorrent(File file, String extension) {
		final String fileName = UUID.randomUUID().toString();

		try {
			final Path path = Paths.get(file.getAbsolutePath());
			final byte[] bytes = Files.readAllBytes(path);

			final String encodedFile = Base64.getEncoder()
					.encodeToString(bytes);

			return TorrentUtil.create(fileName, extension, encodedFile);
		} catch (IOException e) {
			// ignore
		}

		return null;
	}

}
