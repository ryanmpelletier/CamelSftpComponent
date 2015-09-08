package com.pelletier.sftp.camel.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.Sftplet;
import org.apache.sshd.sftp.request.SshFxpCloseRequest;

import com.pelletier.sftp.camel.server.handlers.AbstractRequestHandler;
import com.pelletier.sftp.camel.server.handlers.RequestHandler;
import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;

/**
 * Contains a map of handlers to do extra processing for different
 * types of requests to the SFTP server, also gives a chance the
 * trigger to fire after the SFTP request has been processed, and also
 * when a user disconnects.
 * 
 * @author Ryan Pelletier
 *
 */
public class SftpletImp implements Sftplet {
	
	private Map<String,AbstractRequestHandler> handlers = new HashMap<String,AbstractRequestHandler>();


	@Override
	public void onConnect(SftpSession session) {
	}

	@Override
	public Reply beforeCommand(SftpSession session, Request sftpRequest) {
		if(sftpRequest instanceof SshFxpCloseRequest){
			processTrigger(session, sftpRequest);
		}
		RequestHandler requestHandler = handlers.get(sftpRequest.getClass().getName());
		return requestHandler == null ? null : requestHandler.handle(session, sftpRequest);
	}

	@Override
	public Reply afterCommand(SftpSession session, Request sftpRequest, Reply sftpReply) throws IOException {
		if(!(sftpRequest instanceof SshFxpCloseRequest)){
			processTrigger(session, sftpRequest);
		}
		return sftpReply;
	}

	@Override
	public void onDisconnect(SftpSession session) {		
		processTrigger(session,null);
	}
	
	/**
	 * Gets the ftpServerUserTrigger and triggerPullResultHandler (Camel consumer)
	 * from the session, checks if the trigger should fire, if it should,
	 * fires the trigger. Then checks if the FtpResources should be cleared after the trigger firing,
	 * if it should, clears the FtpResources.
	 * 
	 * @param session
	 * @param request
	 */
	private void processTrigger(SftpSession session, Request request){
		FtpServerUserTrigger ftpServerUserTrigger = session.getSession().getAttribute(AbstractRequestHandler.TRIGGER);
		TriggerPullResultHandler triggerPullResultHandler =  session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_PULL_RESULT_HANDLER);
		
		if(ftpServerUserTrigger.shouldFire(session, request)){
			triggerPullResultHandler.handle(ftpServerUserTrigger.fireTrigger(session));
			session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY).getResources().clear();
		}
	}

	public Map<String, AbstractRequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, AbstractRequestHandler> handlers) {
		this.handlers = handlers;
	}


}
