package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {
	
	public static final String DEFAULT_LOG_PATH = "log.txt";
	public static final String DEFAULT_PRINT_PATH = "print.txt";
	
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
				setProperty("LOG_PATH", DEFAULT_LOG_PATH);
				setProperty("PRINT_PATH", DEFAULT_PRINT_PATH);
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
