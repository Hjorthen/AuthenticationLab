package Server;

import Repository.IPolicyRepository;

public class ReferenceMonitor implements IReferenceMonitor {
	IPolicyRepository policyStore;
	public ReferenceMonitor(IPolicyRepository policyStore) {
		this.policyStore = policyStore;
	}
	
	@Override
	public boolean AuthorizeUser(String username, String permission) {
		return policyStore.IsSubjectAuthorized(username, permission);
	}

}
