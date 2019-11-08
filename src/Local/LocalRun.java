package Local;

import java.security.NoSuchAlgorithmException;

import Client.Client;
import Server.Server;

public class LocalRun {

	public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
		new Client().Execute(new Server());
	}

}
