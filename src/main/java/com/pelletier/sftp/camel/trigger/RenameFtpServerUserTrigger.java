package com.pelletier.sftp.camel.trigger;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.request.SshFxpRenameRequest;

import com.pelletier.sftp.camel.server.TriggerPullResult;

/**
 * Every time the SFTP server receives a rename command it pulls all uploaded files.
 *  
 * @author Ryan Pelletier
 */
public class RenameFtpServerUserTrigger extends AbstractFtpServerUserTrigger {
	
	@Override
	public boolean shouldFire(SftpSession session, Request request) {
		if(request instanceof SshFxpRenameRequest){
			return true;
		}
		return false;
	}

	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		return writeBytesToFtpResources(session);
	}

}
