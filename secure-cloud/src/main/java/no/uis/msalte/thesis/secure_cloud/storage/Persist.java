package no.uis.msalte.thesis.secure_cloud.storage;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Persist {
	private final static Logger LOGGER = Logger.getLogger(Persist.class
			.getName());
	private static final String DB_FILE = "C:\\Users\\Morten\\Desktop\\db\\test";
	private static final String SEPARATOR = ",";

	public static final String MAP_TORRENTS = "torrents"; // torrentId, file
	public static final String MAP_PUBLIC_KEYS = "public_keys"; // torrentId, pk
	public static final String MAP_RE_ENCRYPTION_KEYS = "re_encryption_keys"; // pk,
																				// rek

	private static Persist instance;

	private DB db;

	protected Persist() {
		init();
	}

	public static Persist getInstance() {
		if (instance == null) {
			instance = new Persist();
		}

		return instance;
	}

	private void init() {
		db = DBMaker.newFileDB(new File(DB_FILE)).closeOnJvmShutdown().make();
	}

	public void write(String map, String key, String value) {
		String current = getMap(map).get(key);

		if (current != null) {
			String[] parts = current.split(SEPARATOR);

			for (String p : parts) {
				if (p.trim().equals(value.trim())) {
					LOGGER.log(
							Level.INFO,
							String.format(
									"Value [%s] already exists for key [%s] in map [%s]",
									value, key, map));
					return;
				}
			}

			current = appendValue(current, value);
		} else {
			current = new String(value);
		}

		getMap(map).put(key, current);

		LOGGER.log(Level.INFO, String.format(
				"Wrote value [%s] to key [%s] in map [%s]", value, key, map));

		db.commit();
	}

	public String read(String map, String key) {
		return getMap(map).get(key);
	}

	public void deleteValue(String map, String key, String value) {
		String current = read(map, key);

		if (current.contains(value)) {
			getMap(map).put(key, removeValue(current, value));

			LOGGER.log(Level.INFO, String.format(
					"Deleted value [%s] from key [%s] in map [%s]", value, key,
					map));

			db.commit();

			return;
		}

		LOGGER.log(
				Level.INFO,
				String.format(
						"Tried to delete value [%s] from key [%s] in map [%s], but it did not exist",
						value, key, map));
	}

	public void deleteKey(String map, String key) {
		getMap(map).remove(key);

		LOGGER.log(Level.INFO,
				String.format("Deleted key [%s] from map [%s]", key, map));

		db.commit();
	}

	public boolean hasKey(String map, String key) {
		return getMap(map).containsKey(key);
	}

	public boolean hasValue(String map, String key, String value) {
		return getMap(map).get(key).contains(value);
	}

	public void reset() {
		for (String key : getMap(MAP_TORRENTS).keySet()) {
			deleteKey(MAP_TORRENTS, key);
		}

		for (String key : getMap(MAP_PUBLIC_KEYS).keySet()) {
			deleteKey(MAP_PUBLIC_KEYS, key);
		}

		for (String key : getMap(MAP_RE_ENCRYPTION_KEYS).keySet()) {
			deleteKey(MAP_RE_ENCRYPTION_KEYS, key);
		}
	}

	public String formatMap(String map) {
		final StringBuilder sb = new StringBuilder();

		sb.append(map);
		sb.append("\n");
		sb.append("---------------------");
		sb.append("\n");

		sb.append(String.format("%-45s %s", "KEY:", "VALUE:"));
		sb.append("\n");

		int length = sb.length();

		for (String key : getMap(map).keySet()) {
			sb.append(String.format("%-45s %s", key, read(map, key)));
			sb.append("\n");
		}

		if (sb.length() == length) {
			sb.append("empty");
			sb.append("\n");
		}

		return sb.toString();
	}

	private String appendValue(String current, String value) {
		return String.format("%s%s%s", current, SEPARATOR, value);
	}

	private String removeValue(String current, String value) {
		int startIndex = current.indexOf(value);

		if (startIndex >= 0) {
			String before = current.substring(0, startIndex);
			String after = current.substring(startIndex + value.length(),
					current.length());

			return before.concat(after.substring(SEPARATOR.length(),
					after.length()));
		}

		return current;
	}

	private ConcurrentNavigableMap<String, String> getMap(String map) {
		return db.getTreeMap(map);
	}
}
