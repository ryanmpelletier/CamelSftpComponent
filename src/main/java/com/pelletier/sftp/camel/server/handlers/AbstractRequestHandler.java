package com.pelletier.sftp.camel.server.handlers;

import java.nio.file.FileSystem;
import java.util.Map;

import org.apache.sshd.common.Session;
import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.TriggerPullResultHandler;
import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;

/**
 * Contains keys to retrieve certain objects that are stored on the session.
 * 
 * @author Ryan Pelletier
 *
 */
public abstract class AbstractRequestHandler implements RequestHandler 
{
	public static final Session.AttributeKey<TriggerPullResult> TRIGGER_KEY = new Session.AttributeKey<TriggerPullResult>();
	public static final Session.AttributeKey<TriggerPullResultHandler> TRIGGER_PULL_RESULT_HANDLER = new Session.AttributeKey<TriggerPullResultHandler>();
	public static final Session.AttributeKey<FtpServerUserTrigger> TRIGGER = new Session.AttributeKey<FtpServerUserTrigger>();
	public static final Session.AttributeKey<FileSystem> FILE_SYSTEM = new Session.AttributeKey<FileSystem>();

}