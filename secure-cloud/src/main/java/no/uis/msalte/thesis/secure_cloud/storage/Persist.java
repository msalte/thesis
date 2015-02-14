package no.uis.msalte.thesis.secure_cloud.storage;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.uis.msalte.thesis.secure_cloud.model.KeyTuple;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Persist {
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
			.create();
	private static final Type GSON_TYPE = new TypeToken<ArrayList<KeyTuple>>() {
	}.getType();

	private static final Logger LOGGER = Logger.getLogger(Persist.class
			.getName());
	private static final String DB_FILE = "C:\\Users\\Morten\\Desktop\\db\\test";

	public static final String MAP_TORRENTS = "torrents";
	public static final String MAP_KEY_TUPLES = "key_tuples";

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

	public void storeTorrent(String fileName, String fileBytes) {
		final boolean torrentExists = getMap(MAP_TORRENTS).get(fileName) != null;

		if (!torrentExists) {
			getMap(MAP_TORRENTS).put(fileName, fileBytes);

			LOGGER.log(Level.INFO,
					String.format("Stored new torrent file [%s]", fileName));

			db.commit();
			return;
		}

		LOGGER.log(Level.INFO, String.format(
				"The torrent file [%s] is already stored", fileName));
	}

	public boolean storeKeysTuple(String fileName, KeyTuple keysTuple) {
		final boolean torrentExists = hasKey(MAP_TORRENTS, fileName);

		if (torrentExists) {
			ArrayList<KeyTuple> tuples = GSON.fromJson(getMap(MAP_KEY_TUPLES)
					.get(fileName), GSON_TYPE);

			if (tuples == null) {
				tuples = new ArrayList<KeyTuple>();
			}

			tuples.add(keysTuple);

			getMap(MAP_KEY_TUPLES).put(fileName, GSON.toJson(tuples));

			LOGGER.log(
					Level.INFO,
					String.format("Stored [%s] for file [%s]",
							GSON.toJson(keysTuple), fileName));

			db.commit();
			return true;
		}

		LOGGER.log(Level.WARNING, String.format(
				"The file [%s] does not exist in map [%s]", fileName,
				MAP_TORRENTS));

		return false;
	}

	public String readTorrent(String fileName) {
		return getMap(MAP_TORRENTS).get(fileName);
	}

	public ArrayList<KeyTuple> readKeyTuples(String fileName) {
		final String json = getMap(MAP_KEY_TUPLES).get(fileName);

		if (json != null) {
			return GSON.fromJson(json, GSON_TYPE);
		}

		return null;
	}

	private void deleteKey(String map, String key) {
		getMap(map).remove(key);

		LOGGER.log(Level.INFO,
				String.format("Deleted key [%s] from map [%s]", key, map));

		db.commit();
	}

	public void reset() {
		for (String key : getMap(MAP_TORRENTS).keySet()) {
			deleteKey(MAP_TORRENTS, key);
		}

		for (String key : getMap(MAP_KEY_TUPLES).keySet()) {
			deleteKey(MAP_KEY_TUPLES, key);
		}
	}

	private boolean hasKey(String map, String key) {
		return getMap(map).containsKey(key);
	}

	public String formatMap(String map) {
		final StringBuilder sb = new StringBuilder();

		sb.append(map);
		sb.append("\n");
		sb.append("---------------------");
		sb.append("\n");

		sb.append(String.format("%-60s %s", "KEY:", "VALUE:"));
		sb.append("\n");

		int length = sb.length();

		for (String key : getMap(map).keySet()) {
			sb.append(String.format("%-60s %s", key, getMap(map).get(key)));
			sb.append("\n");
		}

		if (sb.length() == length) {
			sb.append("empty");
			sb.append("\n");
		}

		return sb.toString();
	}

	private ConcurrentNavigableMap<String, String> getMap(String map) {
		return db.getTreeMap(map);
	}

}
