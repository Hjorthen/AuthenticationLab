package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.PrinterInterface;


public class Client {

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		int exitCode = 1;
		System.out.println("Welcome to the printer, you have the following commands to use:");
		Scanner scanner = new Scanner(System.in);
		while(exitCode == 1) {
            String command = scanner.nextLine();
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            PrinterInterface stub;
	            String param;
	            
	            switch(command){
	            case "print":
	            case "Print":
	            case "PRINT":
	            	System.out.println("Please enter the filename:");
	            	String filename = scanner.nextLine();
	            	System.out.println("Please enter the printer name:");
	            	String printer = scanner.nextLine();
	            	stub = (PrinterInterface) registry.lookup("print");
	            	stub.print(filename, printer);
	            	System.out.println(filename + " put in the queue of printer " + printer);
	            	break;
	            	
	            case "queue":
	            case "Queue":
	            case "QUEUE":
	            	stub = (PrinterInterface) registry.lookup("queue");
	            	System.out.println(stub.queue());
	            	break;
	            
	            case "topqueue":
	            case "Topqueue":
	            case "topQueue":
	            case "TOPQUEUE":
	            	System.out.println("Please enter the job id to move to the front:");
	            	int id = scanner.nextInt();
	            	stub = (PrinterInterface) registry.lookup("topQueue");
	            	stub.topQueue(id);
	            	break;
	            
	            case "start":
	            case "Start":
	            case "START":
	            	stub = (PrinterInterface) registry.lookup("start");
	            	stub.start();
	            	break;
	            	
	            case "stop":
	            case "Stop":
	            case "STOP":
	            	stub = (PrinterInterface) registry.lookup("stop");
	            	stub.stop();
	            	break; 
	            	
	            case "restart":
	            case "Restart":
	            case "RESTART":
	            	stub = (PrinterInterface) registry.lookup("restart");
	            	stub.restart();
	            	break;
	            	
	            case "status":
	            case "Status":
	            case "STATUS":
	            	stub = (PrinterInterface) registry.lookup("status");
	            	System.out.println(stub.status());
	            	break;
	            	
	            case "readConfig":
	            case "readconfig":
	            case "Readconfig":
	            case "READCONFIG":
	            	System.out.println("Please enter the parameter:");
	            	param = scanner.nextLine();
	            	stub = (PrinterInterface) registry.lookup("readConfig");
	            	System.out.println(stub.readConfig(param));
	            	break;
	            	
	            case "setconfig":
	            case "Setconfig":
	            case "setConfig":
	            case "SETCONFIG":
	            	System.out.println("Please enter the parameter you want to change:");
	            	param = scanner.nextLine();
	            	System.out.println("Please enter the new value of the chosen paramter:");
	            	String value = scanner.nextLine();
	            	stub = (PrinterInterface) registry.lookup("setConfig");
	            	stub.setConfig(param, value);
	            	break;
	            	
	            case "exit":
	            case "EXIT":
	            case "Exit":
	            case "quit":
	            case "Quit":
	            case "QUIT":
	            	exitCode = 0;
	            	System.out.println("Goodbye");
	            	break;
	            	
	            case "help":
	            case "Help":
	            case "HELP":
	            	System.out.println("Print help");	            	
	            	break;
	            	
	            default:
	            	System.out.println("That command does not exits. Please enter the help command to get help.");
	            	
	            };
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		}

	}

}
