package com.pelletier.sftp.camel.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class defines an object that gets created when the user's predefined trigger fires.
 *  Each object consists of an ip address (or the client address), 
 *  the user's username, and a list of pulled FtpResources.
 *  
 *  TriggerPullResults are put on the Camel Exchange
 *  
 *  @author graham1
 *  
 */

public class TriggerPullResult implements Serializable {

	private static final long serialVersionUID = -7300264212368710812L;
	
	private String ip;
	private String username;
	private List<FtpResource> resources;
	
	public TriggerPullResult (List<FtpResource> resources){
		this.resources = resources;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<FtpResource> getResources() {
		return resources;
	}

	public void setResources(List<FtpResource> resources) {
		this.resources = resources;
	}
	
	public void addResource(FtpResource resource){
		if(this.resources == null){
			resources = new ArrayList<FtpResource>();
		}
		resources.add(resource);
	}
	
	@Override
	public String toString(){
		String returnString = "";
		for(FtpResource ftpResource : this.resources){
			returnString += (ftpResource.toString() + "  ");
		}
		return returnString;
	}

}
