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

import Repository.IPasswordRepository;
import Repository.PasswordMockRepository;
import Repository.PasswordRepository;
import rmi.AuthenticationException;
import rmi.PrinterInterface;

public class Server implements PrinterInterface {
	
	public static final String CONFIG_PATH = "config.properties";
	
	ArrayList<Job> jobQueue = new ArrayList<Job>();
	int jobIndex = 1;
	boolean started = false;
	
	static Config config = new Config(CONFIG_PATH);
	static Authenticator auth;

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
	public Server() throws NumberFormatException, InvalidKeySpecException
	{
		try {
			auth = new Authenticator(
					new PasswordMockRepository()
					/* new PasswordRepository(
						config.getProperty("DB_URL"),
						config.getProperty("DB_USERNAME"),
						config.getProperty("DB_PASSWORD")
					)*/,
					config.getProperty("SERVER_NAME"),
					Integer.parseInt(config.getProperty("TOKEN_EXPIRATION_HOURS"))
					);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Failed to locate hashing algorithm");
		}
		
	}
	
	private void log(String text, Object... args)
	{
		System.out.printf(text, args);
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
	
	public void print(String filename, String printer, SignedObject accessToken) {
		if(auth.VerifyToken(accessToken)) {
			log("Printing %s on %s", filename, printer);
			jobQueue.add(new Job(jobIndex, filename, printer));
			jobIndex++;
		}
	}

	public String queue(SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			log("Sending print queue");
			String result = "";
			for (Job job : jobQueue) {
				result += job.toString() + System.lineSeparator();
			}
			return result;
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public void topQueue(int job, SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			log("Moving %d to top of queue", job);
			Job jobObj = jobQueue.remove(job);
			jobQueue.add(0, jobObj);
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public void start(SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			start();
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
		if(auth.VerifyToken(accessToken)) {
			stop();
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
		if(auth.VerifyToken(accessToken)) {
			log("Restarting server..");
			stop();
			start();
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public String status(SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			log("Sending status");
			return "STATUS";
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}
	
	public String readConfig(String parameter, SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			log("Sending config par (%s)", parameter);
			return parameter + "=" + config.getProperty(parameter);
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	public void setConfig(String parameter, String value, SignedObject accessToken) throws AuthenticationException {
		if(auth.VerifyToken(accessToken)) {
			log("Setting config par (%s) to %s", parameter, value);
			config.setProperty(parameter, value);
		}
		else {
			throw new AuthenticationException("Invalid access token");
		}
	}

	@Override
	public SignedObject authenticate(String username, String hashedPassword) throws Exception {
		try {
			return auth.AuthenticateUser(username, hashedPassword);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("Server error");
		} catch(AuthenticationException e)
		{
			log("User" + username + " attempted to login with wrong username or password! \n" + hashedPassword);
			return null;
		}
	}
}
