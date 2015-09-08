package com.pelletier.sftp.camel.trigger;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.handlers.AbstractRequestHandler;

/**
 * Sends TriggerPullResult to Camel route when the user disconnects.
 * 
 * @author Ryan Pelletier
 *
 */
public class OnDisconnectFtpServerUserTrigger extends AbstractFtpServerUserTrigger {

	@Override
	public boolean shouldFire(SftpSession session, Request request) {
		if((session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY).getResources().size() !=0) && (request == null)){
			return true;
		}
		return false;
	}

	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		return writeBytesToFtpResources(session);
	}


}
