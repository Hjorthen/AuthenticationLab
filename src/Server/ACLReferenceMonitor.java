package Server;

public class ACLReferenceMonitor implements IReferenceMonitor {

	@Override
	public boolean AuthorizeUser(String username, String permission) {
		return true;
	}

}
