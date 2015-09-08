package com.pelletier.sftp.camel.server.handlers;

import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.request.SshFxpOpenRequest;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;

/**
 * Handles open requests
 * 
 * @author Ryan Pelletier
 *
 */
public class SshFxpOpenRequestHandler extends AbstractRequestHandler{

	/**
	 * Puts an FtpResource on the session with the same name as the file being uploaded to the server
	 */
	@Override
	public Reply handle(SftpSession session, Request request) {
		
		TriggerPullResult triggerPullResult = session.getSession().getAttribute(TRIGGER_KEY);
		FtpResource ftpResource = new FtpResource(((SshFxpOpenRequest) request).getPath());
		triggerPullResult.addResource(ftpResource);
		
		return null;
	}

}
