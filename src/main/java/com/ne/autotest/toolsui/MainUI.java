package com.ne.autotest.toolsui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ne.autotest.help.UIUtil;

public class MainUI extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(MainUI.class);
	private JPanel contentPane;
	UIUtil uiUtil=new UIUtil();
	JLabel lblreportpath;
	JLabel lblResult;
	JCheckBox chckbxNoEmail;
	JButton btnStart;
	JLabel lblAPI;
	/**
	 * Create the frame.
	 */
	public MainUI() {

		setIconImage(new ImageIcon(getClass().getResource("/logo.jpg")).getImage());
		setResizable(false);
		setTitle("ServerAutoTest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("press start button");
				uiUtil.ScenariosStart();
				btnStart.setEnabled(false);
			}
		});
		btnStart.setBounds(280, 55, 100, 25);
		contentPane.add(btnStart);
		
		JButton btnInit = new JButton("Init Config");
		btnInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(uiUtil.ReadConfig(com.ne.autotest.main.ServerTestClient.paramsBean)){
					JOptionPane.showMessageDialog(getContentPane(), "Init /Resource/Config.xml succesfully!", 
							"Message", JOptionPane.INFORMATION_MESSAGE); 
					logger.info("Init /Resource/Config.xml succesfully!");
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Init /Resource/Config.xml failed!", 
							"Message", JOptionPane.ERROR_MESSAGE); 
					logger.info("Init /Resource/Config.xml failed!");
				}
			}
		});
		btnInit.setBounds(110, 55, 100, 25);
		contentPane.add(btnInit);
		
		JLabel lbltitle = new JLabel("Sioeye2.0 Server Auto Test");
		lbltitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
		lbltitle.setForeground(Color.BLUE);
		lbltitle.setBounds(138, 10, 242, 35);
		contentPane.add(lbltitle);
		
		lblreportpath = new JLabel("Report Path :");
		lblreportpath.setVerticalAlignment(SwingConstants.TOP);
		lblreportpath.setBounds(10, 261, 475, 25);
		contentPane.add(lblreportpath);
		
		JLabel lblVersion = new JLabel("V"+com.ne.autotest.main.ServerTestClient.Version+" design by Then.");
		lblVersion.setBounds(174, 307, 166, 15);
		contentPane.add(lblVersion);
		
		lblResult = new JLabel("<html><p><h3 style=\"color:blank;font-weight:bold;\">Result:</h3></p></html>");
		lblResult.setVerticalAlignment(SwingConstants.TOP);
		lblResult.setBounds(10, 95, 475, 156);
		contentPane.add(lblResult);
		
		chckbxNoEmail = new JCheckBox("no email");
		chckbxNoEmail.setSelected(true);
		chckbxNoEmail.setBounds(386, 78, 103, 23);
		chckbxNoEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(chckbxNoEmail.isSelected()){
				//	com.main.Run.paramsBean.setEmail("false");
					logger.info("set params email=false");
				}else{
				//	com.main.Run.paramsBean.setEmail("true");
					logger.info("set params email=true");
				}
			}
		});
		contentPane.add(chckbxNoEmail);
		//open report
		JButton btnOpenReport = new JButton("Open");
		btnOpenReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("press open report button");
				if(lblreportpath.getText().equals("Report Path :")){
					logger.info("Report path is empty!");
					JOptionPane.showMessageDialog(getContentPane(), "Report path is empty!", 
							"Message", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!uiUtil.getScenariosThreadRunnable()){
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(uiUtil.OpenReport(System.getProperty("user.dir")+"/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html")){
								logger.info("Open report=/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html success.");
//								JOptionPane.showMessageDialog(getContentPane(), "Open report=/Logs/Report_"+com.main.ServerTestClient.timekey+".html success.", 
//										"Message", JOptionPane.INFORMATION_MESSAGE);
							}else{
								logger.info("Open report=/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html failed.");
								JOptionPane.showMessageDialog(getContentPane(), "Open report=/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html failed.",
										"Message", JOptionPane.ERROR_MESSAGE);
							}
						}
					}).start();

				}else{
					logger.info("Open report=/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html is generated.");
					JOptionPane.showMessageDialog(getContentPane(), "Report=/Logs/Report_"+com.ne.autotest.main.ServerTestClient.timekey+".html is generated.",
							"Message", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnOpenReport.setBounds(367, 257, 100, 25);
		contentPane.add(btnOpenReport);
		
		lblAPI = new JLabel("API :");
		lblAPI.setBounds(10, 82, 312, 15);
		contentPane.add(lblAPI);
	}
	
	public void setlblreportpath(String path){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lblreportpath.setText("Report path: "+path);
				} catch (Exception e) {
					logger.error("Exception",e);
				}
			}
		});
	}
	
	public void setlblResult(String result){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lblResult.setText(result);
				} catch (Exception e) {
					logger.error("Exception",e);
				}
			}
		});
	}
	public boolean getissendEmail(){
		return !chckbxNoEmail.isSelected();
	}
	public JButton getStart(){
		return btnStart;
	}
	public JLabel getlblAPI(){
		return lblAPI;
	}
}
