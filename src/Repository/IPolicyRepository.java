package Repository;

public interface IPolicyRepository {
	boolean IsSubjectAuthorized(String subject, String resource);
}
