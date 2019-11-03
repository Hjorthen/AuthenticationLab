package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.PrinterInterface;


public class Client {

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		int exitCode = 1;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the printer, you have the following commands to use:");
    	System.out.println("print \nqueue \ntopqueue \nstart \nstop \nrestart \nsetconfig \nreadconfig \nstatus");
		while(exitCode == 1) {
            String command = scanner.nextLine().toLowerCase();
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            PrinterInterface stub = (PrinterInterface) registry.lookup("PrinterInterface");
	            String param;
	            
	            switch(command){
	            case "print":
	            	System.out.println("Please enter the filename:");
	            	String filename = scanner.nextLine();
	            	System.out.println("Please enter the printer name:");
	            	String printer = scanner.nextLine();
	            	stub.print(filename, printer);
	            	System.out.println(filename + " put in the queue of printer " + printer);
	            	break;
	            	
	            case "queue":
	            	System.out.println(stub.queue());
	            	break;
	            
	            case "topqueue":
	            	System.out.println("Please enter the job id to move to the front:");
	            	int id = scanner.nextInt();
	            	stub.topQueue(id);
	            	break;
	            
	            case "start":
	            	stub.start();
	            	break;
	            	
	            case "stop":
	            	stub.stop();
	            	break; 
	            	
	            case "restart":
	            	stub.restart();
	            	break;
	            	
	            case "status":
	            	System.out.println(stub.status());
	            	break;
	            	
	            case "readconfig":
	            	param = scanner.nextLine();
	            	stub = (PrinterInterface) registry.lookup("readConfig");
	            	System.out.println(stub.readConfig(param));
	            	break;
	            	
	            case "setconfig":
	            	System.out.println("Please enter the parameter you want to change:");
	            	param = scanner.nextLine();
	            	System.out.println("Please enter the new value of the chosen paramter:");
	            	String value = scanner.nextLine();
	            	stub.setConfig(param, value);
	            	break;
	            	
	            case "exit":
	            case "quit":
	            	exitCode = 0;
	            	System.out.println("Goodbye");
	            	break;
	            	
	            case "help":
	            	System.out.println("You can type in the following commands:");	            	
	            	System.out.println("print \n queue \n topqueue \n start \n stop \n restart \n setconfig \n readconfig \n status");
	            	break;
	            	
	            default:
	            	System.out.println("That command does not exits. Please enter the help command to get help.");
	            	
	            };
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
		}
		scanner.close();

	}

}
