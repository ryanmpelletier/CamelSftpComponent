package com.pelletier.sftp.camel.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.sshd.common.file.SshFile;

/**
 * Implementation of SshFile for in-memory file system use.
 * <br><b>NOTE: </b>Relies on java 7 for the java.nio.file package, <b>also</b>, not all methods have implementations
 * @author Ryan Pelletier
 */
public class InMemorySshFile implements SshFile {
	
	InMemoryFileSystemView inMemoryFileSystemView;
	Path path;
	String filename;
	String username;
	
	public InMemorySshFile(){}
	
	public InMemorySshFile(InMemoryFileSystemView inMemoryFileSystemView, String filename, Path path, String username){
    	this.filename = filename;
    	this.username = username;
    	this.path = path;
    	this.inMemoryFileSystemView = inMemoryFileSystemView;
	}

	@Override
	public boolean create() throws IOException {
		Files.createFile(this.path);
		return true;
	}

	@Override
	public InputStream createInputStream(long offset) throws IOException {
		return Files.newInputStream(this.path);
	}

	@Override
	public OutputStream createOutputStream(long offset) throws IOException {
		try{
			OutputStream outputStream = Files.newOutputStream(this.path);
			return outputStream;
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean delete() {
		try {
			if(Files.deleteIfExists(this.path)){
				System.out.println("FILE WAS DELETED");
				return true;
			}
			System.out.println("NOT DELETED");
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean doesExist() {
		return Files.exists(this.path);
	}

	@Override
	public String getAbsolutePath() {
		return this.path.toString();
	}

	@Override
	public Object getAttribute(Attribute arg0, boolean arg1) throws IOException {
		return null;
	}

	@Override
	public Map<Attribute, Object> getAttributes(boolean arg0) throws IOException {
		return null;
	}

	@Override
	public long getLastModified() {
		try {
			return Files.getLastModifiedTime(this.path).toMillis();
		} catch (IOException e) {
			return new Date().getTime();
		}
	}

	@Override
	public String getName() {
		return this.filename;
	}

	@Override
	public String getOwner() {
		return this.username;
	}

	@Override
	public SshFile getParentFile() {
		Path path = this.path.getParent();
		return new InMemorySshFile(this.inMemoryFileSystemView, path.getFileName().toString(),path,this.username);
	}

	@Override
	public long getSize() {
		try {
			return Files.size(this.path);
		} catch (IOException e) {
			return 0;
		}
	}

	//TODO : create actual implementation
	@Override
	public void handleClose() throws IOException {
	}

	@Override
	public boolean isDirectory() {
		return Files.isDirectory(this.path);
	}

	@Override
	public boolean isExecutable() {
		return Files.isExecutable(this.path);
	}

	@Override
	public boolean isFile() {
		return Files.isRegularFile(this.path);
	}

	@Override
	public boolean isReadable() {
		return Files.isReadable(this.path);
	}
	
	//TODO : create actual implementation, there are issues with all Paths that are not files or directories being un-writable 
	@Override
	public boolean isRemovable() {
		return true;
	}

	//TODO : create actual implementation, there are issues with all Paths that are not files or directories being un-writable 
	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public List<SshFile> listSshFiles() {
		List<SshFile> sshFileList = new ArrayList<SshFile>();
		try {
			Files.list(this.path).forEach(path -> {
				try {
					sshFileList.add(new InMemorySshFile(this.inMemoryFileSystemView, path.getFileName().toString(),path,this.username));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			return null;
		}
		return sshFileList;
	}

	@Override
	public boolean mkdir() {
		try {
			Files.createDirectory(this.path);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean move(SshFile sshFile) {
		try {
			Files.move(this.path,this.inMemoryFileSystemView.getFileSystem().getPath(sshFile.getName()));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readSymbolicLink() throws IOException {
        Path link = Files.readSymbolicLink(path);
        return link.toString();
	}
	
	//TODO : create actual implementation
	@Override
	public void setAttribute(Attribute arg0, Object arg1) throws IOException {
	}

	//TODO : create actual implementation
	@Override
	public void setAttributes(Map<Attribute, Object> arg0) throws IOException {
	}

	@Override
	public boolean setLastModified(long millis) {
		try {
			Files.setLastModifiedTime(this.path, FileTime.fromMillis(millis));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//TODO : create actual implementation
	@Override
	public void truncate() throws IOException {
	}

}
