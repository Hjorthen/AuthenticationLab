package Server;

import Repository.IPolicyRepository;

public class ReferenceMonitor implements IReferenceMonitor {
	IPolicyRepository policyStore;
	public ReferenceMonitor(IPolicyRepository policyStore) {
		this.policyStore = policyStore;
	}
	
	@Override
	public boolean AuthorizeUser(String username, String permission) {
		Policy policy =  policyStore.GetPermissions(username);
		if(policy.Subject == username && policy.Permissions.contains(permission))
			return true;
		return false;
	}
}
