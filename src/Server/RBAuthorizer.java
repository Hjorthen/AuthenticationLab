package Server;

public class RBAuthorizer implements IAuthorizer {

	@Override
	public boolean AuthorizeUser(String username, String permission) {
		return true;
	}

}
