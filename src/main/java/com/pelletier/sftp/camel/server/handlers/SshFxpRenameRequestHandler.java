package com.pelletier.sftp.camel.server.handlers;

import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.request.SshFxpRenameRequest;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;

/**
 * 
 * Handles rename requests
 * 
 * @author Ryan Pelletier
 *
 */
public class SshFxpRenameRequestHandler extends AbstractRequestHandler {

	/**
	 * Renames the corresponding FtpResource when an upload request is received.
	 */
	@Override
	public Reply handle(SftpSession session, Request request) {
		String newPath = ((SshFxpRenameRequest)request).getNewPath();
		String oldPath = ((SshFxpRenameRequest)request).getOldPath();
		
		TriggerPullResult triggerPullResult = session.getSession().getAttribute(TRIGGER_KEY);
		for(FtpResource ftpResource : triggerPullResult.getResources()){
			if(ftpResource.getName().equals(oldPath)){
				ftpResource.setName(newPath);
			}
		}
        return null;
	}
}
