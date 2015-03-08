package no.uis.msalte.thesis.secure_cloud.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;
import no.uis.msalte.thesis.bit_torrent.util.TorrentUtil;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionParameters;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.model.KeyTuple;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class SecureCloudShareImpl implements SecureCloudShare {
	private BitTorrentTracker tracker = new BitTorrentTracker(6969);
	private ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl(
			new ProxyReEncryptionParameters().initialize());

	public String newSecretKey() {
		return scheme.newSecretKey();
	}

	public String newPublicKey(String secretKey) {
		return scheme.newPublicKey(secretKey);
	}

	public String newReEncryptionKey(String srcSecretKey, String destPublicKey) {
		return scheme.newReEncryptionKey(srcSecretKey, destPublicKey);
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

//			file = scheme.reEncrypt(file, reEncryptionKey);

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
