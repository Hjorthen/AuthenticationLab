package Server;

import java.util.HashSet;

public class Policy {
	public Policy(HashSet<String> perms, String subject)
	{
		Subject = subject;
		Permissions = perms;
	}
	public final HashSet<String> Permissions;
	public final String Subject;
}
