package no.uis.msalte.thesis.secure_cloud.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.uis.msalte.thesis.bit_torrent.tracker.BitTorrentTracker;
import no.uis.msalte.thesis.bit_torrent.util.TorrentUtils;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionScheme;
import no.uis.msalte.thesis.crypto.scheme.ProxyReEncryptionSchemeImpl;
import no.uis.msalte.thesis.secure_cloud.access.AccessControl;
import no.uis.msalte.thesis.secure_cloud.model.KeyTuple;
import no.uis.msalte.thesis.secure_cloud.storage.Persist;

public class SecureCloudShareImpl implements SecureCloudShare {
	private static final Logger LOGGER = Logger
			.getLogger(SecureCloudShareImpl.class.getName());
	private static final boolean IS_LOG_ENABLED = true;

	private BitTorrentTracker tracker = new BitTorrentTracker(6969);
	private ProxyReEncryptionScheme scheme = new ProxyReEncryptionSchemeImpl();

	public String newSecretKey() {
		return scheme.newSecretKey();
	}

	public String newPublicKey(String secretKey) {
		return scheme.newPublicKey(secretKey);
	}

	public String newReEncryptionKey(String srcSecretKey, String destPublicKey) {
		return scheme.newReEncryptionKey(srcSecretKey, destPublicKey);
	}

	public String upload(File file, String publicKey) {
		if (TorrentUtils.isValidTorrent(file)) {
			final String fileName = file.getName();

			try {
				final Path path = Paths.get(file.getAbsolutePath());
				final byte[] bytes = Files.readAllBytes(path);

				final String encodedFile = Base64.getEncoder().encodeToString(
						bytes);

				final String encryptedFile = scheme.encryptReEncryptable(
						encodedFile, publicKey);

				if (IS_LOG_ENABLED) {
					LOGGER.log(Level.INFO, String.format(
							"Encrypted torrent %s with public key %s",
							fileName, publicKey));
				}

				Persist.getInstance().storeTorrent(fileName, encryptedFile);

				tracker.start();
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

		boolean isShareSuccess = Persist.getInstance().storeKeysTuple(fileName,
				new KeyTuple(publicKey, reEncryptionKey));

		if (IS_LOG_ENABLED) {
			LOGGER.log(Level.INFO, String.format(
					"%s file %s with public key %s",
					isShareSuccess ? "Successfully shared" : "Failed to share",
					fileName, publicKey));
		}

		return isShareSuccess;
	}

	public String download(String fileName, String publicKey) {
		final String reEncryptionKey = AccessControl.getReEncryptionKey(
				fileName, publicKey);

		final boolean hasAccess = reEncryptionKey != null;

		if (hasAccess) {
			String file = Persist.getInstance().readTorrent(fileName);

			file = scheme.reEncrypt(file, reEncryptionKey);

			if (IS_LOG_ENABLED) {
				LOGGER.log(Level.INFO, String.format(
						"Re-encrypted file %s for download by public key %s",
						fileName, publicKey));
			}

			return file;
		} else if (IS_LOG_ENABLED) {
			LOGGER.log(
					Level.INFO,
					String.format(
							"Someone tried to download file %s with public key %s, but no access was granted",
							fileName, publicKey));
		}

		return null;
	}

	public String newTorrent(File file) {
		final String torrentName = UUID.randomUUID().toString();

		// TODO generate token randomly
		// TODO store hash
		// TODO compare hash on the tracker to validate clients
		final String token = "security_token";
		
		final String torrent = TorrentUtils.create(torrentName, token, file);

		if (torrent != null && IS_LOG_ENABLED) {
			LOGGER.log(Level.INFO, String.format(
					"Created new torrent file %s.torrent", torrentName));
		}

		return torrent;
	}
}
