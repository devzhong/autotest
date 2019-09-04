package com.ne.autotest.main;

import java.awt.EventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.autotest.toolsui.MainUI;

public class StartwithUI {
	private static final Logger logger = LoggerFactory.getLogger(StartwithUI.class);
	
	
	public void Init(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					com.ne.autotest.main.ServerTestClient.mainui = new MainUI();
					com.ne.autotest.main.ServerTestClient.mainui.setVisible(true);

				} catch (Exception e) {
					logger.error("Exception",e);
				}
			}
		});
	}
	
}
