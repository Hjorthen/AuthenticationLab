package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.SignedObject;

public interface PrinterInterface extends Remote{
	void print(String filename, String printer, SignedObject accessToken) throws RemoteException,Server.AuthenticationException;   /**Prints file filename on the specified printer*/
	String queue(SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Lists the print queue on the user's display in lines of the form <job number>   <file name>
	 * @throws Server.Server.AuthenticationException */
	void topQueue(int job, SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Moves job to the top of the queue*/
	void start(SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Starts the print server*/
	void stop(SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Stops the print server*/
	void restart(SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Stops the print server, clears the print queue and starts the print server again*/
	String status(SignedObject accessToken) throws RemoteException, Server.AuthenticationException;  /**Prints status of printer on the user's display*/
	String readConfig(String parameter, SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Prints the value of the parameter on the user's display*/
	void setConfig(String parameter, String value, SignedObject accessToken) throws RemoteException, Server.AuthenticationException;   /**Sets the parameter to value*/
	SignedObject authenticate(String username, String hashedPassword) throws RemoteException, Exception; /**Authenticates a user by username and password and returns a login token*/
}
