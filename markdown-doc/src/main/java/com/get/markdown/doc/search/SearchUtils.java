package com.get.markdown.doc.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SearchUtils {
	
	private static Map<String, Directory> directoryMap = new HashMap<String, Directory>();
	private static Properties properties = null;
	
	private static void loadProperties() {
		InputStream input = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
	        input = cl.getResourceAsStream("lucene.properties" );
	        InputStreamReader reader = new InputStreamReader(input, "UTF-8" );
	        properties = new Properties();
	        properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	protected static Directory getDirectory(String name) {
		Directory directory = directoryMap.get(name);
		if (directory == null) {
			if (properties == null) {
				loadProperties();
			}
			try {
				directory = FSDirectory.open(Paths.get(properties.getProperty("DOCUMENT_INDEX_PATH") + name));
				directoryMap.put(name, directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return directory;
	}
	
}
