package com.pelletier.sftp.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * 
 * Camel Endpoint for creating SftpConsumers, SftpProducers are not supported
 * 
 * @author Ryan Pelletier
 *
 */
public class SftpServerEndpoint extends DefaultEndpoint {

	public SftpServerEndpoint(){}
	
	SftpServerConsumer sftpServerConsumer = null;
	
	public SftpServerEndpoint(String uri, SftpServerComponent component){
		super(uri,component);
	}
	
	public SftpServerEndpoint(String endpointUri){
		super(endpointUri);
		System.out.println("Creating SftpServerEndpoint");
	}
	

	@Override
	public Producer createProducer() throws Exception {
		throw new UnsupportedOperationException("You cannot send messages to this endpoint: " + getEndpointUri());
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		if(sftpServerConsumer == null){
			SftpServerConsumer sftpServerConsumer =  new SftpServerConsumer(this,processor);
			this.sftpServerConsumer = sftpServerConsumer;
		}
		return sftpServerConsumer;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
