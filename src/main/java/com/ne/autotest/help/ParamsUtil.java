package com.ne.autotest.help;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParamsUtil {
    private static final Logger logger = LoggerFactory.getLogger(ParamsUtil.class);

    public boolean InitParams(ParamsBean paramsBean) {
//		for(int i=0;i<args.length;i++){
//			if(args[i].equals("-retry")){
//			 	configBean.setRetry(Integer.parseInt(args[i+1]));
//			}else if(args[i].equals("-scene")){
//			 	configBean.setScene(args[i+1]);
//			}else if(args[i].equals("-resposetimeout")){
//				configBean.setResposeTimeout(Integer.parseInt(args[i+1]));
//			}
//		}
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
}
