package com.pelletier.sftp.camel.filesystem;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.sshd.common.file.FileSystemView;
import org.apache.sshd.common.file.SshFile;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * In-memory file system view for getting in-memory file system implementations
 * of the SshFile interface.
 * 
 * <br><b>NOTE: </b>Relies on java 7 for the java.nio.file package
 * 
 * @author Ryan Pelletier
 *
 */
public class InMemoryFileSystemView implements FileSystemView {
	
	private FileSystem fileSystem;
    private Path current;
    private String username;
    private boolean caseInsensitive = false;
	
    /**
     * <b>NOTE: </b> Do not use directly, called by factory
     * @param userName
     * @throws IOException
     */
    protected InMemoryFileSystemView(String userName) throws IOException {
        this(userName, false, Jimfs.newFileSystem(Configuration.unix().toBuilder().setWorkingDirectory("/").build()));
    }

    /**
     * <b>NOTE: </b> Do not use directly, called by factory
     * @param userName
     * @param caseInsensitive
     * @throws IOException
     */
    public InMemoryFileSystemView(String userName, boolean caseInsensitive, FileSystem fileSystem) throws IOException {
        if (userName == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        this.username = userName;
        this.caseInsensitive = caseInsensitive;
        this.fileSystem = fileSystem;
        current = fileSystem.getRootDirectories().iterator().next();
    }

    /**
     * Get (or create) an SshFile object using a given filename
     */
	@Override
	public SshFile getFile(String filename) {
		return getFile(this,filename,fileSystem.getPath(filename),this.username);
	}
	
	/**
	 * Get (or create) a given SshFile given an SshFile and filename.
	 * <b>NOTE: </b> I have not found a case where this gets called
	 */
	@Override
	public SshFile getFile(SshFile directory, String filename) {
		Path path = fileSystem.getPath(directory.getAbsolutePath());
		return getFile(this,filename,path,this.username);
	}
	
	/**
	 * 
	 * @param inMemoryFileSystemView this 
	 * @param filename 
	 * @param path
	 * @param userName 
	 * @return
	 */
	protected SshFile getFile(InMemoryFileSystemView inMemoryFileSystemView, String filename, Path path, String userName){
		return new InMemorySshFile(inMemoryFileSystemView, filename, path, userName);	
	}
    
	public Path getCurrDir() {
		return current;
	}

	public void setCurrDir(Path currDir) {
		this.current = currDir;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}  
}
