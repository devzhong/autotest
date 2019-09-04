package com.ne.autotest.main;



import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.autotest.help.ParamsBean;
import com.ne.autotest.help.Log4j2Util;
import com.ne.autotest.help.ParamsUtil;
import com.ne.autotest.help.ReportUtil;
import com.ne.autotest.toolsui.MainUI;


public class ServerTestClient {
	//public static  LogManager logManager = LogManager.getLogManager();
	public static int PassCount=0;
	public static int FailCount=0;
	public static int WarningCount;//任意一次错误
	public static ParamsBean paramsBean;
	public static ReportUtil reportUtil;
	public static int pushStreamFlag=0;
	public static String timekey;
	public static String Version="20160919";
	public static MainUI mainui;
	public static boolean isWinOS;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_HHmm_ss"); 
		timekey=sdf.format(new Date());
		//log init
		Log4j2Util log4j2Util=new Log4j2Util();
		if(!log4j2Util.InitLog4j2(timekey)){
			System.out.println("Init Logger System fail, pls retry!");
			return;
		}
        Logger logger = LoggerFactory.getLogger(ServerTestClient.class);
        logger.info("Sioeye2.0 Server auto Test "+com.ne.autotest.main.ServerTestClient.Version+", design by Then.");
		//params init
        logger.info("Start to init params...");
        PassCount=0;
        FailCount=0;
        WarningCount=0;
		com.ne.autotest.main.ServerTestClient.paramsBean=new ParamsBean();
        ParamsUtil paramsUtil=new ParamsUtil();
        if(!paramsUtil.InitParams(com.ne.autotest.main.ServerTestClient.paramsBean)){
        	logger.error("Config.xml has wrong items, Stop!");
        	return;
        }
        
    	 String osName = System.getProperty("os.name");
	     if(osName.toLowerCase().indexOf("windows")>-1){
	    	 isWinOS=true;
	     }else{
	    	 isWinOS=false;
	     }
         logger.info("SystemOS="+osName);
         
         
        if(args.length>0){
        	if(args[0].contains("@")){
            	logger.info("Start with Trigger...");
        		paramsBean.setSponsor(args[0]);
            	StartwithCMD startwithCMD=new StartwithCMD();
            	startwithCMD.Init();
        	}else{
            	logger.error("Trigger params has wrong, Stop!");
            	return;
        	}
        }else{
        
        
        if(paramsBean.getStart().equalsIgnoreCase("CMD")){
        	logger.info("start with CMD...");
        	StartwithCMD startwithCMD=new StartwithCMD();
        	startwithCMD.Init();
        }else{
        	logger.info("start with UI...");
        	StartwithUI startwithUI=new StartwithUI();
        	startwithUI.Init();
        }

        }
		
	}
}
