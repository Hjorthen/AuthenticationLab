package Server;

public interface IAuthorizer {
	boolean AuthorizeUser(String username, String permission);
}
