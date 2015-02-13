package no.uis.msalte.thesis.secure_cloud.util;

import java.io.FileOutputStream;
import java.util.Base64;

public class FilesUtil {
	private static final String OUTPUT_DIRECTORY = "C:\\Users\\Morten\\Desktop\\output\\files";

	public static String encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static byte[] decode(String string) {
		return Base64.getDecoder().decode(string);
	}

	public static boolean writeFile(String fileName, String bytes) {
		try {
			final byte[] file = Base64.getDecoder().decode(bytes);
			FileOutputStream fos = new FileOutputStream(String.format("%s//%s",
					OUTPUT_DIRECTORY, fileName));

			fos.write(file);
			fos.close();

			return true;
		} catch (Exception e) {
			// ignore
		}

		return false;
	}
}
