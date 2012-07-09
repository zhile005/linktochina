package com.oa.action;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

import com.oa.common.JsonBaseAction;
import com.oa.common.util.MailSenderInfo;
import com.oa.common.util.SimpleMailSender;

public class MainAction extends JsonBaseAction {

	@Resource
	public SimpleMailSender simpleMailSender;
	
	private Logger log = Logger.getLogger(getClass());

	private MailSenderInfo mailInfo;

	/**
	 * @return
	 * @throws Exception
	 */
	public String index() {
		return SUCCESS;
	}

	public String activities() {
		return SUCCESS;
	}

	public String aboutUs() {
		return SUCCESS;
	}
	
	public String advantangeUs() {
		return SUCCESS;
	}

	public String contactUs() {
		return SUCCESS;
	}
	public String sendMail() {
		jsonMap.put("success", false);
		try {
			simpleMailSender.sendHtmlMail(mailInfo);
			jsonMap.put("success", true);
		} catch (AddressException e) {
			jsonMap.put("errorCode", "001");
		} catch (SendFailedException e) {
			jsonMap.put("errorCode", "001");
		} catch (MessagingException e) {
			jsonMap.put("errorCode", "002");
			log.error("sendMail提交异常", e);
		}
		return JSON_MAP;
	}

	public MailSenderInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailSenderInfo mailInfo) {
		this.mailInfo = mailInfo;
	}
}
