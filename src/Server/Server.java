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
import java.util.HashMap;
import java.util.Properties;

import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	ArrayList<Job> jobQueue = new ArrayList<Job>();
	int jobIndex = 1;
	boolean started = false;
	
	static Properties config = new Properties();
	public static final String CONFIG_PATH = "C:\\Users\\Thomas\\Desktop\\config.properties";

	public static void main(String[] args) {
		//Log init
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

	private void Log(String text, Object... args)
	{
		System.out.printf(text, args);
	}
	
	@Override
	public void print(String filename, String printer) {
		Log("Printing %s on %s", filename, printer);
		jobQueue.add(new Job(jobIndex, filename, printer));
		jobIndex++;
	}

	@Override
	public String queue() {
		Log("Sending print queue");
		String result = "";
		for (Job job : jobQueue) {
			result += job.toString() + System.lineSeparator();
		}
		return result;
	}

	@Override
	public void topQueue(int job) {
		Log("Moving %d to top of queue", job);
		Job jobObj = jobQueue.remove(job);
		jobQueue.add(0, jobObj);
	}

	@Override
	public void start() {
		Log("Starting server..");
		started = true;
	}

	@Override
	public void stop() {
		Log("Stopping server..");
		started = false;
		jobQueue.clear();
		jobIndex = 1;
	}

	@Override
	public void restart() {
		Log("Restarting server..");
		stop();
		start();
	}

	@Override
	public String status() {
		Log("Sending status");
		return "STATUS";
	}
	
	@Override
	public String readConfig(String parameter) {
		Log("Sending config par (%s)", parameter);
		return config.toString();
	}

	public void setConfig(String parameter, String value) {
		//Log("Setting config par (%s) to %s", parameter, value);
		config.setProperty(parameter, value);
		try {
			File configFile = new File(CONFIG_PATH);
			configFile.createNewFile();
			OutputStream outStream = new FileOutputStream(configFile);
			config.store(outStream, null);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
