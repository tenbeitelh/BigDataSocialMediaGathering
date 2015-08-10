package de.hs.osnabrueck.htenbeitel.utils;

import java.util.Iterator;
import java.util.Map;

import de.hs.osnabrueck.htenbeitel.flume.utils.StateSerDeseriliazer;

public class MapReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("File parameter is needed.");
			System.exit(0);
		} else {
			for (String path : args) {
				System.out.println(path + ":");
				Map<?, ?> map = StateSerDeseriliazer.deserilazeHashMap(path);
				Iterator<?> it = map.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					System.out.println(key + " : " + map.get(key));
				}
				System.out.println();
			}
		
		}

	}

}
