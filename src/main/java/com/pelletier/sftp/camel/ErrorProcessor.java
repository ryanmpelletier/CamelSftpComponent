package com.pelletier.sftp.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A class for printing errors caught in the Camel Route
 * 
 * @author Ryan Pelletier
 *
 */
public class ErrorProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getException().printStackTrace();
	}

}
