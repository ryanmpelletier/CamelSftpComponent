package com.pelletier.sftp.camel.trigger;

import org.apache.sshd.common.file.SshFile;
import org.apache.sshd.sftp.Handle;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;
import org.apache.sshd.sftp.request.SshFxpCloseRequest;
import org.apache.sshd.sftp.subsystem.FileHandle;
import org.apache.sshd.sftp.subsystem.SftpSubsystem;

import com.pelletier.sftp.camel.server.TriggerPullResult;

/**
 * Fires trigger after a file is written to the SFTP Server
 * 
 * @author Ryan Pelletier
 */
public class PlainFtpServerUserTrigger extends AbstractFtpServerUserTrigger {

	/**
	 * Should fire if a Close request is made to the SFTP Server and
	 * the file handle is a regular file.
	 */
	@Override
	public boolean shouldFire(SftpSession session, Request request) {
		
		if(((request != null) && (request instanceof SshFxpCloseRequest))){
			String handle = ((SshFxpCloseRequest) request).getHandleId();
			Handle p = ((SftpSubsystem) session).getHandle(handle);
			try{
				FileHandle fileHandle = (FileHandle) p;
				SshFile sshFile = fileHandle.getFile();
				if(sshFile.isFile()){
					return true;
				}
			}catch(Exception e){
				return false;
			}

		}
		return false;
	}

	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		return writeBytesToFtpResources(session);
	}

}
