package com.pelletier.sftp.camel.server;

import java.io.Serializable;

/**
 * 
 * Objects of this class are stored in the TriggerPullResult that gets put on the Camel route,
 * these objects store the contents of the files.
 *
 */
public class FtpResource implements Serializable {

	private static final long serialVersionUID = 3121917117225409113L;
	
	private byte[] contents;
	private String name;
	
	public FtpResource(String name){
		this.name = name;
	}
	
	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public String toString(){
		return new String(this.contents);
	}
	

}
