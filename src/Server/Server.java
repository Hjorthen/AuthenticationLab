package Server;

import java.util.PriorityQueue;

import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	public PriorityQueue<Job> jobQueue = new PriorityQueue<Job>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print(String filename, String printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queue() {
		// TODO Auto-generated method stub
		
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
