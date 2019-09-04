package com.ne.autotest.main;


import java.text.SimpleDateFormat;


import com.ne.autotest.business.WatchVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.autotest.business.Loginout;
import com.ne.autotest.business.Others;

import com.ne.autotest.help.MailUtil;

public class Scenarios {
    private static final Logger logger = LoggerFactory.getLogger(Scenarios.class);
    private final String BR = "<br>";
    //    private  final  String TR = "<tr>";
//    private  final  String _TR = "</tr>"; 
//    private  final  String TD = "<td>"; 
//    private  final  String _TD = "</td>"; 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy_MMdd_HHmm_ss");

    public void SceneStart() {
        logger.info("Start to run auto test...");
        if (com.ne.autotest.main.ServerTestClient.paramsBean.getScene().equalsIgnoreCase("all")) {
            Others others = new Others();
            others.start();
        } else {
            String[] scene = com.ne.autotest.main.ServerTestClient.paramsBean.getScene().split(",");
            for (String str : scene) {
                switch (str) {
                    case "2":
                        Loginout loginout = new Loginout();
                        loginout.start();
                        break;
                    case "3":
                        WatchVideo watchVideo = new WatchVideo();
                        watchVideo.start();
                        break;
                    case "9":
                        Others others = new Others();
                        others.start();
                        break;
                    default:
                        break;
                }
            }
        }
        logger.info("***********************************************************");
        //end report
        com.ne.autotest.main.ServerTestClient.reportUtil.EndReport();
        if (com.ne.autotest.main.ServerTestClient.paramsBean.getSponsor() != null && com.ne.autotest.main.ServerTestClient.FailCount == 0) {
            if (!SendEmailWithnoFail()) {
                //	logger.info((SendEmail()==true)?"":"Send email fialed again!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    logger.error("Exception", e);
                }
                if (!SendEmailWithnoFail()) {
                    logger.error("Send email failed again!");
                }
            }

        } else if (com.ne.autotest.main.ServerTestClient.FailCount > 0) {
            if (com.ne.autotest.main.ServerTestClient.paramsBean.getEmail().equalsIgnoreCase("true")) {
                if (!SendEmail()) {
                    //	logger.info((SendEmail()==true)?"":"Send email fialed again!");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        logger.error("Exception", e);
                    }
                    if (!SendEmail()) {
                        logger.error("Send email failed again!");
                    }
                }
            } else {
                logger.info("Email=false, don't send email!");
            }
        }

        logger.info("Sioeye2.0 Server auto test has been completed!");
    }


    public boolean SendEmail() {
        logger.warn("Send email...");
        //hwpop.qiye.163.com         hwsmtp.qiye.163.com
        String smtp = com.ne.autotest.main.ServerTestClient.paramsBean.getSmtp();
        String from = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailAccount();
        String to = com.ne.autotest.main.ServerTestClient.paramsBean.getSend();
        String copyto = com.ne.autotest.main.ServerTestClient.paramsBean.getCc();
        String subject = "Sioeye2.0 Server Auto Report_" + "Fail=" + com.ne.autotest.main.ServerTestClient.FailCount + "_Date=" + com.ne.autotest.main.ServerTestClient.timekey;
        String username = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailAccount();
        String password = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailPassword();
        String logfile = System.getProperty("user.dir") + "/Logs/Log_" + com.ne.autotest.main.ServerTestClient.timekey + ".txt";
        String reportfile = System.getProperty("user.dir") + "/Logs/Report_" + com.ne.autotest.main.ServerTestClient.timekey + ".html";
        //content
        StringBuffer content = new StringBuffer();
        content.append("<html>");
        content.append("<p><h3 style=\"color:Blank;font-weight:bold;\">Notice: </h3>");
        content.append("<span style=\"color:Blank;font-size:10pt;font-weight:bold;\">There are some fail cases if you recivie this email, pls check the server!" + BR);
        content.append("Here is the report for Sioeye2.0 Server Auto Test.</span></p>");
        //result table
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getResultbuf().toString());
        //Fail Item
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getApibuf_fail().toString());
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getExplainbuf().toString());
        content.append("</html>");
        if (SendMails(smtp, from, to, copyto, subject, content.toString(), username, password, logfile, reportfile)) {
            return true;
        } else {
            logger.warn("Send email failed!");
            return false;
        }
    }

    public boolean SendEmailWithnoFail() {
        logger.warn("Send email with no fail...");
        //hwpop.qiye.163.com         hwsmtp.qiye.163.com
        String smtp = com.ne.autotest.main.ServerTestClient.paramsBean.getSmtp();
        String from = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailAccount();
        String to = com.ne.autotest.main.ServerTestClient.paramsBean.getSponsor();
        String copyto = "";
        String subject = "Sioeye2.0 Server Auto Report_" + "Fail=" + com.ne.autotest.main.ServerTestClient.FailCount + "_Date=" + com.ne.autotest.main.ServerTestClient.timekey;
        String username = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailAccount();
        String password = com.ne.autotest.main.ServerTestClient.paramsBean.getEmailPassword();
        String logfile = System.getProperty("user.dir") + "/Logs/Log_" + com.ne.autotest.main.ServerTestClient.timekey + ".txt";
        String reportfile = System.getProperty("user.dir") + "/Logs/Report_" + com.ne.autotest.main.ServerTestClient.timekey + ".html";
        //content
        StringBuffer content = new StringBuffer();
        content.append("<html>");
        content.append("<p><h3 style=\"color:Blank;font-weight:bold;\">Notice: </h3>");
        content.append("<span style=\"color:Blank;font-size:10pt;font-weight:bold;\">");
        content.append("Here is the report for Sioeye2.0 Server Auto Test.</span></p>");
        //result table
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getResultbuf().toString());
        //Fail Item
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getApibuf_fail().toString());
        content.append(com.ne.autotest.main.ServerTestClient.reportUtil.getExplainbuf().toString());
        content.append("</html>");
        if (SendMails(smtp, from, to, copyto, subject, content.toString(), username, password, logfile, reportfile)) {
            return true;
        } else {
            logger.warn("Send email failed!");
            return false;
        }
    }

    /**
     * 调用sendOut方法完成邮件发送
     *
     * @param smtp
     * @param from
     * @param to
     * @param subject
     * @param content
     * @param username
     * @param password
     * @return boolean
     */
    public boolean send(String smtp, String from, String to, String subject, String content, String username, String password) {
        MailUtil theMail = new MailUtil(smtp);
        theMail.setNeedAuth(true); //需要验证

        if (!theMail.setSubject(subject)) return false;
        if (!theMail.setBody(content)) return false;
        if (!theMail.setTo(to)) return false;
        if (!theMail.setFrom(from)) return false;
        theMail.setNamePass(username, password);

        if (!theMail.sendOut()) return false;
        return true;
    }

    /**
     * 调用sendOut方法完成邮件发送,带抄送
     *
     * @param smtp
     * @param from
     * @param to
     * @param copyto
     * @param subject
     * @param content
     * @param username
     * @param password
     * @return boolean
     */
    public boolean sendAndCc(String smtp, String from, String to, String copyto, String subject, String content, String username, String password) {
        MailUtil theMail = new MailUtil(smtp);
        theMail.setNeedAuth(true); //需要验证

        if (!theMail.setSubject(subject)) return false;
        if (!theMail.setBody(content)) return false;
        if (!theMail.setTo(to)) return false;
        if (!theMail.setCopyTo(copyto)) return false;
        if (!theMail.setFrom(from)) return false;
        theMail.setNamePass(username, password);

        if (!theMail.sendOut()) return false;
        return true;
    }

    /**
     * 调用sendOut方法完成邮件发送,带附件
     *
     * @param smtp
     * @param from
     * @param to
     * @param subject
     * @param content
     * @param username
     * @param password
     * @param filename 附件路径
     * @return
     */
    public boolean send(String smtp, String from, String to, String subject, String content, String username, String password, String filename) {
        MailUtil theMail = new MailUtil(smtp);
        theMail.setNeedAuth(true); //需要验证

        if (!theMail.setSubject(subject)) return false;
        if (!theMail.setBody(content)) return false;
        if (!theMail.addFileAffix(filename)) return false;
        if (!theMail.setTo(to)) return false;
        if (!theMail.setFrom(from)) return false;
        theMail.setNamePass(username, password);

        if (!theMail.sendOut()) return false;
        return true;
    }

    /**
     * 调用sendOut方法完成邮件发送,带附件和抄送
     *
     * @param smtp
     * @param from
     * @param to
     * @param copyto
     * @param subject
     * @param content
     * @param username
     * @param password
     * @param filename
     * @return
     */
    public boolean sendAndCc(String smtp, String from, String to, String copyto, String subject, String content, String username, String password, String filename) {
        MailUtil theMail = new MailUtil(smtp);
        theMail.setNeedAuth(true); //需要验证

        if (!theMail.setSubject(subject)) return false;
        if (!theMail.setBody(content)) return false;
        if (!theMail.addFileAffix(filename)) return false;
        if (!theMail.setTo(to)) return false;
        if (!theMail.setCopyTo(copyto)) return false;
        if (!theMail.setFrom(from)) return false;
        theMail.setNamePass(username, password);

        if (!theMail.sendOut()) return false;
        return true;
    }

    //then diy
    public boolean SendMails(String smtp, String from, String to, String copyto, String subject, String content, String username, String password, String logfile, String reportfile) {
        MailUtil theMail = new MailUtil(smtp);
        theMail.setNeedAuth(true); //需要验证

        if (!theMail.setSubject(subject)) return false;
        if (!theMail.setBody(content)) return false;
        if (!theMail.setTo(to)) return false;
        if (!theMail.setCopyTo(copyto)) return false;
        if (!theMail.setFrom(from)) return false;
        if (!theMail.addFileAffix(logfile)) return false;
        if (!theMail.addFileAffix(reportfile)) return false;
        theMail.setNamePass(username, password);

        if (!theMail.sendOut()) return false;
        return true;
    }


}
