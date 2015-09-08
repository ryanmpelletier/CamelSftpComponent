package com.pelletier.sftp.camel.server.handlers;

import java.util.Date;
import java.util.List;

import org.apache.sshd.common.file.SshFile;
import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.request.SshFxpWriteRequest;
import org.apache.sshd.sftp.subsystem.FileHandle;
import org.apache.sshd.sftp.subsystem.SftpSubsystem;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;

/**
 * Does nothing. Probably can just delete this crap.
 * 
 * @author Ryan Pelletier
 *
 */
public class SshFxpWriteRequestHandler extends AbstractRequestHandler {

	/*
	 *If I was doing stream based maybe I could just do a test for a certain trigger, if so, start writing the stream
	 *to the Camel route, then when I am done send it...not sure how that would work though. I would also have to return something from this 
	 *so that it doesn't write it to the in mem filesystem. I think it needs a handle on a file though so I am not sure how well this would work.
	 */
	@Override
	public Reply handle(SftpSession session, Request sftpRequest) {
		String handle = ((SshFxpWriteRequest) sftpRequest).getHandleId();
		Handle p = ((SftpSubsystem) session).getHandle(handle);
		FileHandle fileHandle = (FileHandle) p;
		
		SshFile sshFile = fileHandle.getFile();
		sshFile.setLastModified(new Date().getTime());	//ask if I really want to do this, probably not...	
		
		return null;
	}
}
