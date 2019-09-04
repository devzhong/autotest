package com.ne.autotest.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.autotest.help.ReportUtil;

public class StartwithCMD {
	private static final Logger logger = LoggerFactory.getLogger(StartwithCMD.class);
	
	public void Init(){
        //reprot init
        com.ne.autotest.main.ServerTestClient.reportUtil=new ReportUtil();
        com.ne.autotest.main.ServerTestClient.reportUtil.InitHtmlReport(com.ne.autotest.main.ServerTestClient.timekey);
		//run
        Scenarios scenarios=new Scenarios();
        scenarios.SceneStart();
        logger.info("==========================CMD end!==================================");
	}
}
