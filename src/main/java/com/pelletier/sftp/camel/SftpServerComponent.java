package com.pelletier.sftp.camel;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * SftpComponent, Camel uses this as a factory for creating endpoints
 * 
 * @author Ryan Pelletier
 *
 */
public class SftpServerComponent extends DefaultComponent {
	
	@Override
	protected Endpoint createEndpoint(String fullUri, String shortUri, Map<String, Object> parameters) throws Exception {
		return new SftpServerEndpoint(shortUri, this);
	}
}
