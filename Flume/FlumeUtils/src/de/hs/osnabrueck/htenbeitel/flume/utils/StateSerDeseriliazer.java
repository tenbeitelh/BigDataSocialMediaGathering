package de.hs.osnabrueck.htenbeitel.flume.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateSerDeseriliazer {

	

	private static final Logger LOG = LoggerFactory
			.getLogger(StateSerDeseriliazer.class);

	public static void serilazeDateMap(Map<String, Date> dateMap, String mapName) {
		LOG.info("creating " + mapName + " file");
		serilazeHashMap(dateMap, mapName);
	}

	public static void serilazeHashMap(Map<?, ?> map, String filename) {
		try (FileOutputStream fileOut = new FileOutputStream(new File(filename));
				ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

			objectOut.writeObject(map);

		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Date> deserilazeDateMap(String mapName) {
		if (new File(mapName).exists()) {
			return (Map<String, Date>) deserilazeHashMap(mapName);
		} else {
			LOG.error("No serilazation file avaialble");
			return null;
		}
	}

	public static Map<?, ?> deserilazeHashMap(String filename) {
		try (FileInputStream fInput = new FileInputStream(filename);
				ObjectInputStream oInput = new ObjectInputStream(fInput)) {
			return (Map<?, ?>) oInput.readObject();
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

}
