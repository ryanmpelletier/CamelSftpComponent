package com.pelletier.sftp.camel.server;

/**
 * Implemented in FtpServerConsumer. It handles the FtpResources
 * that are pulled from FTP server.
 * 
 * @author graham1
 *
 *
 */


public interface TriggerPullResultHandler {
	
	/**
	 * Creates an Exchange message (of pulled FtpResources) and 
	 * routes the message to the directory defined in Camel.
	 * 
	 * @param result the TriggerPullResult object
	 */
	
	public void handle(TriggerPullResult result);

}
