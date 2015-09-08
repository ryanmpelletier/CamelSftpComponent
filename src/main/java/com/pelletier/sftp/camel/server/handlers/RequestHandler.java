package com.pelletier.sftp.camel.server.handlers;

import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
/**
 * For handling an SFTP request.
 * 
 * @author Ryan Pelletier
 *
 */
public interface RequestHandler {
	public Reply handle(SftpSession session, Request sftpRequest);
}
