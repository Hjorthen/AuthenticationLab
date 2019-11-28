package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignedObject;
import java.util.Scanner;

import rmi.AuthenticationException;
import rmi.PrinterInterface;


public class Client {

	public void Execute(PrinterInterface stub) throws NoSuchAlgorithmException, Exception
	{
		boolean exitCode = true;
		boolean loggedIn = false;
		
		Scanner scanner = new Scanner(System.in);
		SignedObject accessToken = null;
		
		while(exitCode) {
			System.out.println("Welcome to the printer, you need to login in order to use it!");
			System.out.println("Please enter your username:");
			String username = scanner.nextLine();
			System.out.println("Please enter your password:");
			String password = hashString(scanner.nextLine());
			accessToken = stub.authenticate(username, password);
			if(accessToken != null) {
				loggedIn = true;
				System.out.println("You have successfully been logged in!");
			}
			while(loggedIn) {
				System.out.println("Welcome to the printer, you have the following commands to use:");
		    	printHelp();
		    	
	            String[] command = scanner.nextLine().toLowerCase().split(" ");
		        try {		            
		            switch(command[0]){
		            case "print":
			            stub.print(command[1], command[2], accessToken);
		            	System.out.println(command[1] + " put in the queue of printer " + command[2]);
		            	break;
		            	
		            case "queue":
		            	System.out.println(stub.queue(accessToken));
		            	break;
		            
		            case "topqueue":
		            	stub.topQueue(Integer.parseInt(command[1]), accessToken);
		            	break;
		            
		            case "start":
		            	stub.start(accessToken);
		            	break;
		            	
		            case "stop":
		            	stub.stop(accessToken);
		            	break; 
		            	
		            case "restart":
		            	stub.restart(accessToken);
		            	break;
		            	
		            case "status":
		            	System.out.println(stub.status(accessToken));
		            	break;
		            	
		            case "readconfig":
		            	System.out.println(stub.readConfig(command[1], accessToken));
		            	break;
		            	
		            case "setconfig":
		            	stub.setConfig(command[1], command[2], accessToken);
		            	break;
		            
		            case "login":
		            	accessToken = stub.authenticate(command[1], hashString(command[2]));
		            	break;
		            	
		            case "exit":
		            case "quit":
		            	exitCode = false;
		            	loggedIn = false;
		            	System.out.println("Goodbye");
		            	break;
		            	
		            case "help":
		            	printHelp();
		            	break;
		            	
		            default:
		            	System.out.println("That command does not exits. Please enter the help command to get help.");
		            	
		            };
		        } catch (IndexOutOfBoundsException e) {
		        	e.printStackTrace();
		        } catch (Exception e) {
		            System.err.println("Client exception: " + e.toString());
		            e.printStackTrace();
		        } 
			}
		}
		scanner.close();

	}
	public static void main(String[] args) throws Exception {
		String host = (args.length < 1) ? null : args[0];
		Registry registry = LocateRegistry.getRegistry(host);
        PrinterInterface stub = (PrinterInterface) registry.lookup("PrinterInterface");
        new Client().Execute(stub);
    }
	
	static void printHelp() {
		System.out.println("You can type in the following commands:");	            	
    	System.out.println(" login [username] [password]\n print [filename] [printer]\n queue \n topqueue [id]\n start \n stop \n restart \n setconfig [parameter] [value]\n readconfig [parameter]\n status");
	}

	public static String hashString(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(md.digest(input.getBytes()));
	}
}
