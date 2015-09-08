package com.pelletier.sftp.camel.trigger;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.handlers.AbstractRequestHandler;
import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;

/**
 * Implements FtpServerUserTrigger, this class was created to contain the
 * writeBytesToFtpResources method that was common to most classes that 
 * implemented FtpServerUserTrigger.
 * 
 * @author Ryan Pelletier
 *
 */
public abstract class AbstractFtpServerUserTrigger implements FtpServerUserTrigger {

	@Override
	public boolean shouldFire(SftpSession session, Request request) {
		return false;
	}

	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		return null;
	}
	
	/**
	 * 
	 * @param session
	 * @return TriggerPullResult with bytes from actual files on FileSystem written to the
	 * FtpResources
	 */
	protected TriggerPullResult writeBytesToFtpResources(SftpSession session){
		FileSystem fileSystem = session.getSession().getAttribute(AbstractRequestHandler.FILE_SYSTEM);
		TriggerPullResult result = session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY);

		for(int i = 0; i < result.getResources().size(); i++){
			FtpResource ftpResource = result.getResources().get(i);
			try {
				Path path = fileSystem.getPath(ftpResource.getName());
				byte[] contents = Files.readAllBytes(path);
				ftpResource.setContents(contents);
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		fileSystem = null;
		return result;
	}

}
