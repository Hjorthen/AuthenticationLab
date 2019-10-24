package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Properties;

import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	ArrayList<Job> jobQueue = new ArrayList<Job>();
	int jobIndex = 1;
	boolean started = false;
	
	static Properties config = new Properties();
	public static final String CONFIG_PATH = "config.properties";
	public static final String DEFAULT_LOG_PATH = "log.txt";
	public static final String DEFAULT_PRINT_PATH = "print.txt";

	public static void main(String[] args) {
		//Config init
		File configFile = new File(CONFIG_PATH);
		if(configFile.exists()) {
			try {
				InputStream inStream = new FileInputStream(CONFIG_PATH);
				config.load(inStream);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		//RMI init
		try {
			Server server = new Server();
			PrinterInterface printer = (PrinterInterface)UnicastRemoteObject.exportObject(server, 0);
			Registry registry  = LocateRegistry.getRegistry();
			registry.bind("PrinterInterface", printer);
			System.out.println("Server is ready");	
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void log(String text, Object... args)
	{
		System.out.printf(text, args);
	}
	
	@Override
	public void print(String filename, String printer) {
		log("Printing %s on %s", filename, printer);
		jobQueue.add(new Job(jobIndex, filename, printer));
		jobIndex++;
	}

	@Override
	public String queue() {
		log("Sending print queue");
		String result = "";
		for (Job job : jobQueue) {
			result += job.toString() + System.lineSeparator();
		}
		return result;
	}

	@Override
	public void topQueue(int job) {
		log("Moving %d to top of queue", job);
		Job jobObj = jobQueue.remove(job);
		jobQueue.add(0, jobObj);
	}

	@Override
	public void start() {
		log("Starting server..");
		started = true;
	}

	@Override
	public void stop() {
		log("Stopping server..");
		started = false;
		jobQueue.clear();
		jobIndex = 1;
	}

	@Override
	public void restart() {
		log("Restarting server..");
		stop();
		start();
	}

	@Override
	public String status() {
		log("Sending status");
		return "STATUS";
	}
	
	@Override
	public String readConfig(String parameter) {
		log("Sending config par (%s)", parameter);
		return config.toString();
	}

	public void setConfig(String parameter, String value) {
		log("Setting config par (%s) to %s", parameter, value);
		config.setProperty(parameter, value);
		try {
			File configFile = new File(CONFIG_PATH);
			if(configFile.createNewFile()) {
				log("Created config file: " + configFile.getAbsolutePath());
			}
			OutputStream outStream = new FileOutputStream(configFile);
			config.store(outStream, null);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
