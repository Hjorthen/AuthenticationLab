package Repository;

import java.util.HashSet;

import Server.Policy;

public interface IPolicyRepository {
	Policy GetPermissions(String subject);
}
