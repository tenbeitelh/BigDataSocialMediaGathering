package de.hs.osnabrueck.htenbeitel.flume.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileAppender {
	public static void appendToFile(String filePath, String text) throws IOException {
		FileUtils.writeStringToFile(new File(filePath), text, true);
	}
}
