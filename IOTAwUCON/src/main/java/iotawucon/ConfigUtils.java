package iotawucon;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class ConfigUtils {
	/**
	 * String corresponding to the configuration,
	 * to be printed by getPropValues.
	 */
	private String result = "";
	/**
	 * to connect to the config.properties file.
	 */
	private InputStream inputStream;
 
	public String getPropValues() throws IOException {
 
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			Date time = new Date(System.currentTimeMillis());
			// get the property value and print it out
			String user = prop.getProperty("user");
			String url = prop.getProperty("url");
			result = "Cassandra url = " + url;
			System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
	/**
	 * works as a getter, function
	 * to be called outside the class to
	 * get the properties.
	 * @throws IOException
	 */
	public static void readConfig() throws IOException {
		ConfigUtils properties = new ConfigUtils();
		properties.getPropValues();
	};
	/**
	 * string that returns the url
	 * of the cassandra database
	 * as a string.
	 */
	String resultUrl = "";
	private InputStream inputStreamUrl;
	public String getUrl() throws IOException {
	try {
		Properties prop = new Properties();
		String propFileName = "config.properties";

		inputStreamUrl = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStreamUrl != null) {
			prop.load(inputStreamUrl);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		String url = prop.getProperty("url");
		resultUrl = url;
	} catch (Exception e) {
		System.out.println("Exception: " + e);
	} finally {
		inputStreamUrl.close();
	}
	return resultUrl;
}

	public static String readUrl() throws IOException {
		ConfigUtils properties = new ConfigUtils();
		return properties.getUrl();
	};
}



