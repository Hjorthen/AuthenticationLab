package Server;

import Repository.IPolicyRepository;

public class RBReferenceMonitor implements IReferenceMonitor {
	IPolicyRepository policyStore;
	public RBReferenceMonitor(IPolicyRepository policyStore) {
		this.policyStore = policyStore;
	}
	@Override
	public boolean AuthorizeUser(String username, String permission) {
		return true;
	}

}
