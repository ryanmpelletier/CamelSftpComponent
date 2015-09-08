package com.pelletier.sftp.camel.trigger;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.handlers.AbstractRequestHandler;

/**
 * When a file with a .rdy extension is found, finds a file with the same
 * filename as the .rdy file and sends that file to the Camel route.
 * 
 * @author Ryan Pelletier
 *
 */
public class ReadyFileMatchFtpServerUserTrigger implements FtpServerUserTrigger {
	
	/**
	 * Returns true if a file with the .rdy extension exists.
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
	 * <b>1.</b> Finds the filename of the FtpResource with the .rdy extension<br>
	 * <b>2.</b> Finds the pair to that extension<br>
	 * <b>3.</b> Gets two Path objects from the FileSytem for those filenames<br>
	 * <b>4.</b> Creates new TriggerPullRequest object and writes the contents to an FtpResource from the file that matched the .rdy file<br>
	 * <b>5.</b> Deletes the files in the FileSystem represented by the two Path objects that were found.<br>
	 * <b>6.</b> Removes the .rdy file and its pair from the TriggerPullResult stored on the session.<br>
	 * <b>7.</b> Returns the TriggerPullRequest object that was created in step 4.<br>
	 */
	@Override
	public TriggerPullResult fireTrigger(SftpSession session) {
		
		FileSystem fileSystem = session.getSession().getAttribute(AbstractRequestHandler.FILE_SYSTEM);
		TriggerPullResult triggerPullResult = session.getSession().getAttribute(AbstractRequestHandler.TRIGGER_KEY);
		
		String filename = null;
		TriggerPullResult result = null;
		
		for(int i = 0; i < triggerPullResult.getResources().size(); i++){	 	//loop through to find the ready file
			if(triggerPullResult.getResources().get(i).getName().endsWith(".rdy")){
				filename = triggerPullResult.getResources().get(i).getName();
				for(int p = 0; p < triggerPullResult.getResources().size(); p++){	//found ready file, now loop through to find the other file
					if((!filename.equals(triggerPullResult.getResources().get(p).getName())) && sameFilename(filename,triggerPullResult.getResources().get(p).getName())){
						Path readyFilePath = fileSystem.getPath(filename);
						Path actualFilePath = fileSystem.getPath(triggerPullResult.getResources().get(p).getName());
						
						try {
							triggerPullResult.getResources().get(p).setContents(Files.readAllBytes(actualFilePath));
							Files.delete(readyFilePath);
							Files.delete(actualFilePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						List<FtpResource> ftpResources = new ArrayList<FtpResource>();
						ftpResources.add(triggerPullResult.getResources().get(p));
						result = new TriggerPullResult(ftpResources);
						result.setIp(triggerPullResult.getIp());
						result.setUsername(triggerPullResult.getUsername());
						
						triggerPullResult.getResources().remove(p);
					}
				}
				triggerPullResult.getResources().remove(i - 1);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileNameOne
	 * @param fileNameTwo
	 * @return true if the files have the same name without regard to the extension
	 */
	private boolean sameFilename(String fileNameOne, String fileNameTwo){
		String[] one = fileNameOne.split("/");
		String[] two = fileNameTwo.split("/");
		String nameOne = one[one.length - 1];
		String nameTwo = two[two.length - 1];		
		return nameOne.split("\\.")[0].equals(nameTwo.split("\\.")[0]);
	}

}
