package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

public class Config {
	
	public static final Map<String, String> DEFAULT_VALUES = Map.of(
			"LOG_PATH", "log.txt",
			"DB_URL", "jdbc:mysql://localhost:3306/",
			"DB_USERNAME", "root",
			"DB_PASSWORD", "root",
			"TOKEN_EXPIRATION_HOURS", "8"
			);
	
	Properties props = new Properties();
	File configFile;
	
	public Config(String path) {
		configFile = new File(path);
		try {
			if(configFile.exists()) {
				InputStream inStream = new FileInputStream(path);
				props.load(inStream);
			}
			else {
				configFile.createNewFile();
				DEFAULT_VALUES.forEach((k,v)-> setProperty(k,v));
				//log("Created config file: " + configFile.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setProperty(String key, String value) {
		props.setProperty(key, value);
		try {
			OutputStream outStream = new FileOutputStream(configFile);
			props.store(outStream, null);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
