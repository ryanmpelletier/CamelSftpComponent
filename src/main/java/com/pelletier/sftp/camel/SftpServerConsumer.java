package com.pelletier.sftp.camel;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.Session;
import org.apache.sshd.common.file.FileSystemView;
import org.apache.sshd.common.file.nativefs.NativeFileSystemFactory;
import org.apache.sshd.server.Command;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.pelletier.sftp.camel.filesystem.InMemoryFileSystemView;
import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.TriggerPullResultHandler;
import com.pelletier.sftp.camel.server.handlers.AbstractRequestHandler;


/**
 * Starts an Sftp Server to accept files, which get routed into Camel
 * 
 * @author Ryan Pelletier
 *
 */
public class SftpServerConsumer extends DefaultConsumer implements TriggerPullResultHandler, NamedFactory<Command> {
		
	public SftpServerConsumer(SftpServerEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		try {
			createServer();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates Camel Exchange which gets passed to the next processor
	 */
	@Override
	public void handle(TriggerPullResult result) {
        Exchange exchange = getEndpoint().createExchange();
        exchange.getIn().setBody(result);
        
        try {
			getProcessor().process(exchange);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
		}

	}
	
	/**
	 * Starts SshServer, sets the FileSystemFactory for creating in-memory file system views.
	 * @throws Exception
	 */
	private void createServer() throws Exception{
		
		SshServer sshd = getEndpoint().getCamelContext().getRegistry().lookupByNameAndType(getEndpoint().getEndpointUri(),org.apache.sshd.SshServer.class);
        
		sshd.setFileSystemFactory(new NativeFileSystemFactory() {
            @Override
            public FileSystemView createFileSystemView(final Session session) {
                try {
                	
                	FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix().toBuilder().setWorkingDirectory("/").build());
            		session.setAttribute(AbstractRequestHandler.TRIGGER_PULL_RESULT_HANDLER, SftpServerConsumer.this);
                	session.setAttribute(AbstractRequestHandler.FILE_SYSTEM, fileSystem);
                	InMemoryFileSystemView inMemoryFileSystemView = new InMemoryFileSystemView(session.getUsername(), false, fileSystem);
                	return inMemoryFileSystemView;
                	
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
            };
        });
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(this));
        sshd.start();
        System.out.println("SFTP Server Started");
	}
	

	@Override
	public Command create() {
    	return getEndpoint().getCamelContext().getRegistry().lookupByNameAndType("sftpSubsystem",org.apache.sshd.sftp.subsystem.SftpSubsystem.class);
	}

	@Override
	public String getName() {
		return "sftp";
	}
	
}
