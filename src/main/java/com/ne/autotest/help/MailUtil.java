package com.ne.autotest.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;


public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    private MimeMessage mimeMsg; //MIME邮件对象
    private Session session; //邮件会话对象
    private Properties props; //系统属性
    private boolean needAuth = false; //smtp是否需要认证
    //smtp认证用户名和密码
    private String username;
    private String password;
    private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    /**
     * Constructor
     *
     * @param smtp 邮件发送服务器
     */
    public MailUtil(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    /**
     * 设置邮件发送服务器
     *
     * @param hostName String
     */
    public void setSmtpHost(String hostName) {
        logger.info("set email system:mail.smtp.host = " + hostName);
        if (props == null)
            props = System.getProperties(); //获得系统属性对象
        props.put("mail.smtp.host", hostName); //设置SMTP主机
    }


    /**
     * 创建MIME邮件对象
     *
     * @return
     */
    public boolean createMimeMessage() {
        try {
            logger.info("prepare to get email session  !");
            session = Session.getDefaultInstance(props, null); //获得邮件会话对象
        } catch (Exception e) {
            logger.error("create email session failed!", e);
            return false;
        }

        logger.info("prepare to get MIME!");
        try {
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象
            mp = new MimeMultipart();

            return true;
        } catch (Exception e) {
            logger.error("create MIME failed!", e);
            return false;
        }
    }

    /**
     * 设置SMTP是否需要验证
     *
     * @param need
     */
    public void setNeedAuth(boolean need) {
        logger.info("set smtp authentication: mail.smtp.auth = " + need);
        if (props == null) props = System.getProperties();
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * 设置用户名和密码
     *
     * @param name
     * @param pass
     */
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    /**
     * 设置邮件主题
     *
     * @param mailSubject
     * @return
     */
    public boolean setSubject(String mailSubject) {
        logger.info("set email Subject!");
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            logger.error("set email Subject failed!", e);
            return false;
        }
    }

    /**
     * 设置邮件正文
     *
     * @param mailBody String
     */
    public boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("" + mailBody, "text/html;charset=GBK");
            mp.addBodyPart(bp);

            return true;
        } catch (Exception e) {
            logger.error("set email text failed!", e);
            return false;
        }
    }

    /**
     * 添加附件
     *
     * @param filename String
     */
    public boolean addFileAffix(String filename) {

        logger.info("add email attachment:" + filename);
        File file = new File(filename);
        if (file.exists()) {
            try {
                BodyPart bp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(filename);
                bp.setDataHandler(new DataHandler(fileds));
                bp.setFileName(fileds.getName());

                mp.addBodyPart(bp);

                return true;
            } catch (Exception e) {
                logger.error("add email attachment: " + filename + " failed!", e);
                return false;
            }
        } else {
            logger.warn("email attachment=" + filename + " doesn't exist!");
            return true;
        }
    }

    /**
     * 设置发信人
     *
     * @param from String
     */
    public boolean setFrom(String from) {
        logger.info("set email from!");
        try {
            mimeMsg.setFrom(new InternetAddress(from)); //设置发信人
            return true;
        } catch (Exception e) {
            logger.error("set email from: " + from + " failed!", e);
            return false;
        }
    }

    /**
     * 设置收信人
     *
     * @param to String
     */
    public boolean setTo(String to) {
        if (to == null) return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            logger.error("set email sendto: " + to + " failed!", e);
            return false;
        }
    }

    /**
     * 设置抄送人
     *
     * @param copyto String
     */
    public boolean setCopyTo(String copyto) {
        if (copyto == null) return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            logger.error("set email ccto: " + copyto + " failed!", e);
            return false;
        }
    }

    /**
     * 发送邮件
     */
    public boolean sendOut() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            logger.info("email sending....");

            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            if (!com.ne.autotest.main.ServerTestClient.paramsBean.getCc().equals("") && com.ne.autotest.main.ServerTestClient.paramsBean.getSponsor() == null) {
                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
            }
            //transport.send(mimeMsg);


            logger.info("send email successful!");
            transport.close();

            return true;
        } catch (Exception e) {
            logger.error("send email failed!", e);
            return false;
        }
    }


} 

