package no.uis.msalte.thesis.secure_share.storage;

import static no.uis.msalte.thesis.common.AppConstants.DIR_APP;
import static no.uis.msalte.thesis.common.AppConstants.DIR_DB;
import static no.uis.msalte.thesis.common.AppConstants.FILE_DB;
import static no.uis.msalte.thesis.common.AppConstants.IS_LOG_ENABLED;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.uis.msalte.thesis.secure_share.model.KeyTuple;

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

	private static Persist instance;

	private DB db;

	private Persist() {
		init();
	}

	public static Persist getInstance() {
		if (instance == null) {
			instance = new Persist();
		}

		return instance;
	}

	private void init() {
		String path = String.format("%s\\%s\\%s", DIR_APP, DIR_DB, FILE_DB);
		
		db = DBMaker.newFileDB(new File(path)).closeOnJvmShutdown().make();
	}

	public void storeTorrent(String fileName, String fileBytes) {
		final boolean torrentExists = getMap(Maps.TORRENTS.name())
				.get(fileName) != null;

		if (!torrentExists) {
			getMap(Maps.TORRENTS.name()).put(fileName, fileBytes);

			if (IS_LOG_ENABLED) {
				LOGGER.log(Level.INFO,
						String.format("Stored new torrent file [%s]", fileName));
			}

			db.commit();
			return;
		}

		if (IS_LOG_ENABLED) {
			LOGGER.log(Level.INFO, String.format(
					"The torrent file [%s] is already stored", fileName));
		}
	}

	public boolean storeKeysTuple(String fileName, KeyTuple keysTuple) {
		final boolean torrentExists = hasKey(Maps.TORRENTS.name(), fileName);

		if (torrentExists) {
			ArrayList<KeyTuple> tuples = GSON.fromJson(
					getMap(Maps.SHARES.name()).get(fileName), GSON_TYPE);

			if (tuples == null) {
				tuples = new ArrayList<KeyTuple>();
			}

			tuples.add(keysTuple);

			getMap(Maps.SHARES.name()).put(fileName, GSON.toJson(tuples));

			if (IS_LOG_ENABLED) {
				LOGGER.log(
						Level.INFO,
						String.format("Stored [%s] for file [%s]",
								GSON.toJson(keysTuple), fileName));
			}

			db.commit();
			return true;
		}

		if (IS_LOG_ENABLED) {
			LOGGER.log(Level.WARNING, String.format(
					"The file [%s] does not exist in map [%s]", fileName,
					Maps.TORRENTS.name()));
		}

		return false;
	}

	public String readTorrent(String fileName) {
		return getMap(Maps.TORRENTS.name()).get(fileName);
	}

	public ArrayList<KeyTuple> readKeyTuples(String fileName) {
		final String json = getMap(Maps.SHARES.name()).get(fileName);

		if (json != null) {
			return GSON.fromJson(json, GSON_TYPE);
		}

		return null;
	}

	public void reset() {
		for (Maps m : Maps.values()) {
			for (String key : getMap(m.name()).keySet()) {
				getMap(m.name()).remove(key);

				if (IS_LOG_ENABLED) {
					LOGGER.log(Level.INFO, String.format(
							"Deleted key [%s] from map [%s]", key, m.name()));
				}
			}
		}

		db.commit();
	}

	private boolean hasKey(String map, String key) {
		return getMap(map).containsKey(key);
	}

	public String formatMap(Maps m) {
		final StringBuilder sb = new StringBuilder();
		final String map = m.name();

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

	public enum Maps {
		TORRENTS, SHARES
	}
}
