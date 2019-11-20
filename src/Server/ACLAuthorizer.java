package Server;

public class ACLAuthorizer implements IAuthorizer {

	@Override
	public boolean AuthorizeUser(String username, String permission) {
		return true;
	}

}
