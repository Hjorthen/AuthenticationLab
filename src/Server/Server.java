package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.PriorityQueue;

import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	PriorityQueue<Job> jobQueue = new PriorityQueue<Job>();
	int jobIndex = 1;

	public static void main(String[] args) {
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

	}

	@Override
	public void start() {
		Log("Starting server..");
	}

	@Override
	public void stop() {
		Log("Stopping server..");
	}

	@Override
	public void restart() {
		Log("Restarting server..");
	}

	@Override
	public String status() {
		Log("Sending status");
		return "STATUS";
	}

	HashMap<String, String> config = new HashMap<String, String>();
	
	@Override
	public String readConfig(String parameter) {
		Log("Sending config par (%s)", parameter);
		return config.getOrDefault(parameter,  "");
	}

	@Override
	public void setConfig(String parameter, String value) {
		Log("Setting config par (%s) to %s", parameter, value);
		config.put(parameter, value);
	}
}
