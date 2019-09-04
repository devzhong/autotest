package com.ne.autotest.help;

import com.ne.autotest.main.Scenarios;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UIUtil {
    private static final Logger logger = LoggerFactory.getLogger(UIUtil.class);
    boolean ScenariosThreadRunnable = false;

    public boolean ReadConfig(ParamsBean paramsBean) {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(System.getProperty("user.dir") + "/Resource/Config.xml");
            Element root = doc.getRootElement();
            List<Element> childElements = root.elements();
            if (childElements.size() != 20) {
                logger.warn("the number of params is " + childElements.size() + "(not 20), pls check Config.xml!");
                return false;
            }
            for (Element child : childElements) {
                switch (child.getName()) {
                    case "start":
                        paramsBean.setStart(child.getText());
                        break;
                    case "account":
                        paramsBean.setAccount(child.getText());
                        break;
                    case "password":
                        paramsBean.setPassword(child.getText());
                        break;
                    case "clearFile":
                        paramsBean.setClearFile(Integer.parseInt(child.getText()));
                        break;
                    case "retry":
                        paramsBean.setRetry(Integer.parseInt(child.getText()));
                        break;
                    case "scene":
                        paramsBean.setScene(child.getText());
                        break;
                    case "pushStreamPrivate":
                        paramsBean.setPushStreamPrivate(child.getText());
                        break;
                    case "resposetimeout":
                        paramsBean.setResposetimeout(Integer.parseInt(child.getText()));
                        break;
                    case "resposetimeoutExclude":
                        paramsBean.setResposetimeoutExclude(child.getText());
                        break;
                    case "email":
                        paramsBean.setEmail(child.getText());
                        break;
                    case "smtp":
                        paramsBean.setSmtp(child.getText());
                        break;
                    case "emailAccount":
                        paramsBean.setEmailAccount(child.getText());
                        break;
                    case "emailPassword":
                        paramsBean.setEmailPassword(child.getText());
                        break;
                    case "send":
                        paramsBean.setSend(child.getText());
                        break;
                    case "cc":
                        paramsBean.setCc(child.getText());
                        break;
                    case "Server":
                        paramsBean.setServer(child.getText());
                        break;
                    case "X_Sioeye_App_Id":
                        paramsBean.setX_Sioeye_App_Id(child.getText());
                        break;
                    case "X_Sioeye_App_Key":
                        paramsBean.setX_Sioeye_App_Key(child.getText());
                        break;
                    case "X_Sioeye_App_Production":
                        paramsBean.setX_Sioeye_App_Production(child.getText());
                        break;
                    case "serverTimeout":
                        paramsBean.setServerTimeout(Integer.parseInt(child.getText()));
                        break;
                    default:
                        logger.warn(child.getName() + "=" + child.getText() + " is not the correct parmas, pls check Config.xml");
                        return false;
                }
            }
            logger.info("Config.xml params: " + paramsBean.ReadParams());
            return true;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        return false;
    }

    public void ScenariosStart() {
        //init thread
        com.ne.autotest.main.ServerTestClient.PassCount = 0;
        com.ne.autotest.main.ServerTestClient.FailCount = 0;
        com.ne.autotest.main.ServerTestClient.WarningCount = 0;
        ScenariosThread scenariosThread = new ScenariosThread();
        new Thread(scenariosThread).start();
        logger.info("ScenariosThread start...");
    }


    class ScenariosThread implements Runnable {
        public void run() {
            ScenariosThreadRunnable = true;
            com.ne.autotest.main.ServerTestClient.paramsBean.setEmail(com.ne.autotest.main.ServerTestClient.mainui.getissendEmail() + "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_HHmm_ss");
            com.ne.autotest.main.ServerTestClient.timekey = sdf.format(new Date());
            //reprot init
            com.ne.autotest.main.ServerTestClient.reportUtil = new ReportUtil();
            com.ne.autotest.main.ServerTestClient.reportUtil.InitHtmlReport(com.ne.autotest.main.ServerTestClient.timekey);
            com.ne.autotest.main.ServerTestClient.mainui.setlblreportpath("/Logs/Report_" + com.ne.autotest.main.ServerTestClient.timekey + ".html");
            //run
            Scenarios scenarios = new Scenarios();
            scenarios.SceneStart();
            com.ne.autotest.main.ServerTestClient.mainui.setlblResult("<html>" + com.ne.autotest.main.ServerTestClient.reportUtil.getResultbuf().toString() + "</html>");
            // com.main.ServerTestClient.mainui.getlblAPI().setText("Scene="+com.main.ServerTestClient.paramsBean.getScene()+" test finished!");
            logger.info("==============================Update UI end!================================");
            ScenariosThreadRunnable = false;
            com.ne.autotest.main.ServerTestClient.mainui.getStart().setEnabled(true);
            JOptionPane.showMessageDialog(com.ne.autotest.main.ServerTestClient.mainui, "Test finished!",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean OpenReport(String path) {
        return HelperUtil.OpenFile(path);
    }

    public boolean getScenariosThreadRunnable() {
        return ScenariosThreadRunnable;
    }
}
