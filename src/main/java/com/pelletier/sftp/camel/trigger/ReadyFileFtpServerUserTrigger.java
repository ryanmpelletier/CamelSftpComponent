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

/**
 * Sends all FtpResources when a file is uploaded with the .rdy extension.
 * 
 * @author Ryan Pelletier
 *
 */
public class ReadyFileFtpServerUserTrigger extends AbstractFtpServerUserTrigger {

	/**
	 * Returns true if a file exists with the .rdy extension.
	 */
	@Override
	public boolean shouldFire(SftpSession session, Request request) {
		TriggerPullResult triggerPullResult = session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY);
		for(FtpResource ftpResource : triggerPullResult.getResources()){
			if(ftpResource.getName().endsWith(".rdy")){
				return true;
			}
		}
		return false;
	}


	/**
	 * Removes the .rdy file from the TriggerPullResult,
	 * then writes the contents of the files from the FileSystem 
	 * to the FtpResources on the TriggerPullResult
	 * @return The TriggerPullResult without the .rdy file
	 */
	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		
		FileSystem fileSystem2 = session.getSession().getAttribute(AbstractRequestHandler.FILE_SYSTEM);
		TriggerPullResult triggerPullResult = session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY);
		for(int i = 0; i < triggerPullResult.getResources().size(); i++){
			if(triggerPullResult.getResources().get(i).getName().endsWith(".rdy")){
				try {
					Path path = fileSystem2.getPath(triggerPullResult.getResources().get(i).getName());
					Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				triggerPullResult.getResources().remove(i);
			}
		}

		return writeBytesToFtpResources(session);
	}

}
