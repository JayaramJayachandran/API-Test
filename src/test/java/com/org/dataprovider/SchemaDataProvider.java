package com.org.dataprovider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class SchemaDataProvider {
	public static HashMap<String, String> getTestData(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("./src/test/resources/data/schemaData.properties");
			prop.load(input);
			HashMap<String, String> map = new HashMap<String, String>();
			for (final String name: prop.stringPropertyNames())
			    map.put(name, prop.getProperty(name));
			return map;
		}catch (IOException ex) {
			ex.printStackTrace();
			return null;
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

}
