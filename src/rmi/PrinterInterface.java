package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.SignedObject;

public interface PrinterInterface extends Remote{
	void print(String filename, String printer) throws RemoteException;   /**Prints file filename on the specified printer*/
	String queue() throws RemoteException;   /**Lists the print queue on the user's display in lines of the form <job number>   <file name>*/
	void topQueue(int job) throws RemoteException;;   /**Moves job to the top of the queue*/
	void start() throws RemoteException;   /**Starts the print server*/
	void stop() throws RemoteException;   /**Stops the print server*/
	void restart() throws RemoteException;   /**Stops the print server, clears the print queue and starts the print server again*/
	String status() throws RemoteException;  /**Prints status of printer on the user's display*/
	String readConfig(String parameter) throws RemoteException;   /**Prints the value of the parameter on the user's display*/
	void setConfig(String parameter, String value) throws RemoteException;   /**Sets the parameter to value*/
	SignedObject authenticate(String username, String hashedPassword) throws RemoteException; /**Authenticates a user by username and password and returns a login token*/
}
