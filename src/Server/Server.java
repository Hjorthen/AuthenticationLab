package Server;

import java.util.PriorityQueue;

import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	PriorityQueue<Job> jobQueue = new PriorityQueue<Job>();
	int jobIndex = 1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print(String filename, String printer) {
		jobQueue.add(new Job(jobIndex, filename, printer));
		jobIndex++;
	}

	@Override
	public String queue() {
		String result = "";
		for (Job job : jobQueue) {
			result += job.toString() + System.lineSeparator();
		}
		return result;
	}

	@Override
	public void topQueue(int job) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void status() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readConfig(String parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfig(String parameter, String value) {
		// TODO Auto-generated method stub
		
	}

}
