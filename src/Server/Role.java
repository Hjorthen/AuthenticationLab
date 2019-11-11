package Server;

import java.io.Serializable;

public class Role implements Serializable{

	private String title;
	private boolean canPrint, canQueue, canTopQueue, canStart, canStop, canRestart, canStatus, canReadConfig, canSetConfig;
	
	public Role(String title, boolean canPrint, boolean canQueue, boolean canTopQueue, boolean canStart,
			boolean canStop, boolean canRestart, boolean canStatus, boolean canReadConfig, boolean canSetConfig) {
		this.title = title;
		this.canPrint = canPrint;
		this.canQueue = canQueue;
		this.canTopQueue = canTopQueue;
		this.canStart = canStart;
		this.canStop = canStop;
		this.canRestart = canRestart;
		this.canStatus = canStatus;
		this.canReadConfig = canReadConfig;
		this.canSetConfig = canSetConfig;
	}

	public String getTitle() {
		return title;
	}

	public boolean canPrint() {
		return canPrint;
	}

	public boolean canQueue() {
		return canQueue;
	}

	public boolean canTopQueue() {
		return canTopQueue;
	}

	public boolean canStart() {
		return canStart;
	}

	public boolean canStop() {
		return canStop;
	}

	public boolean canRestart() {
		return canRestart;
	}

	public boolean canStatus() {
		return canStatus;
	}

	public boolean canReadConfig() {
		return canReadConfig;
	}

	public boolean canSetConfig() {
		return canSetConfig;
	}
	
	
}
