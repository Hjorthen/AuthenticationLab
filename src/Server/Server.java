package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.SignedObject;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;

import Repository.ACLPolicyRepository;
import Repository.IPasswordRepository;
import Repository.PasswordMockRepository;
import Repository.PasswordRepository;
import Repository.RBPolicyRepository;
import rmi.AuthenticationException;
import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	public static final String CONFIG_PATH = "config.properties";
	
	ArrayList<Job> jobQueue = new ArrayList<Job>();
	int jobIndex = 1;
	boolean started = false;
	
	static Config config = new Config(CONFIG_PATH);
	static Authenticator authenticator;
	static IReferenceMonitor referenceMonitor;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, NumberFormatException, InvalidKeySpecException {
		
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
	
	public Server() throws Exception
	{
		try {
			authenticator = new Authenticator(
					//new PasswordMockRepository()
					new PasswordRepository(
						config.getProperty("DB_URL"),
						config.getProperty("DB_USERNAME"),
						config.getProperty("DB_PASSWORD")
					),
					config.getProperty("SERVER_NAME"),
					Integer.parseInt(config.getProperty("TOKEN_EXPIRATION_HOURS"))
					);
			String method = config.getProperty("AUTHORIZATION_METHOD");
			if(method.equals("RB")) {
				referenceMonitor = new ReferenceMonitor(new RBPolicyRepository(
						config.getProperty("DB_URL"),
						config.getProperty("DB_USERNAME"),
						config.getProperty("DB_PASSWORD")));
			}
			else if(method.equals("ACL")) {
				referenceMonitor = new ReferenceMonitor(new ACLPolicyRepository(
						config.getProperty("DB_URL"),
						config.getProperty("DB_USERNAME"),
						config.getProperty("DB_PASSWORD")));
			}
			else {
				throw new Exception("No authorization method specified");
			}
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Failed to locate hashing algorithm");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	private void log(String text, Object... args)
	{
		System.out.printf(text + System.lineSeparator(), args);
		File logFile = new File(config.getProperty("LOG_PATH"));
		FileWriter fr = null;
		try {
			if(!logFile.exists()) {
				logFile.createNewFile();
			}
			fr = new FileWriter(logFile);
			fr.append(text + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void print(String filename, String printer, SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
				try {
					if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Printing %s on %s", filename, printer);
						jobQueue.add(new Job(jobIndex, filename, printer));
						jobIndex++;
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}
	
	public String queue(SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Sending print queue");
						String result = "";
						for (Job job : jobQueue) {
							result += job.toString() + System.lineSeparator();
						}
						return result;
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
		return null;
	}

	public void topQueue(int job, SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Moving %d to top of queue", job);
						Job jobObj = jobQueue.remove(job);
						jobQueue.add(0, jobObj);
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public void start(SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						start();
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}
	
	private void start() {
		log("Starting server..");
		started = true;
	}

	public void stop(SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						stop();
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}
	
	private void stop() {
		log("Stopping server..");
		started = false;
		jobQueue.clear();
		jobIndex = 1;
	}

	public void restart(SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Restarting server..");
						stop();
						start();
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public String status(SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Sending status");
						return "STATUS";
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
		return null;
	}
	
	public String readConfig(String parameter, SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Sending config par (%s)", parameter);
						return parameter + "=" + config.getProperty(parameter);
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
		return null;
	}

	public void setConfig(String parameter, String value, SignedObject accessToken) throws AuthenticationException {
		if(verifyToken(accessToken)) {
			try {
				if(referenceMonitor.AuthorizeUser(((AccessToken)accessToken.getObject()).getHolder(), "print")) { //Authorization
						log("Setting config par (%s) to %s", parameter, value);
						config.setProperty(parameter, value);
					}
					else {
						throw new AuthenticationException("Access Denied For This Role");
					}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	@Override
	public SignedObject authenticate(String username, String hashedPassword) throws Exception {
		log("Authenticating user %s", username);
		try {
			return authenticator.AuthenticateUser(username, hashedPassword);
		} catch (InvalidKeySpecException e) {
			log("Authentication failed: Server error");
			e.printStackTrace();
			throw new Exception("Server error");
		} catch(AuthenticationException e)
		{
			log("Authentication failed: Wrong username/password");
			return null;
		}
	}
	
	private boolean verifyToken(SignedObject token) {
		if(authenticator.VerifyToken(token)) {
			log("Token verified");
			return true;
		}
		else {
			log("Invalid token!");
			return false;
		}
	}
}
