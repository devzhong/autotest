package com.ne.autotest.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);
    String htmlreportpath;

    private final static String TR = "<tr>";
    private final static String _TR = "</tr>";
    private final static String TD = "<td>";
    private final static String _TD = "</td>";
    private final static String TH = "<th>";
    private final static String _TH = "</th>";

    private boolean apibuf_failFlag = false;
    private boolean apibufFlag = false;
    StringBuffer apibuf_fail = new StringBuffer();
    StringBuffer apibuf = new StringBuffer();
    StringBuffer resultbuf = new StringBuffer();
    StringBuffer explainbuf = new StringBuffer();

    //init
    public void InitHtmlReport(String time) {
        //clear buffer
        apibuf_fail.setLength(0);
        apibuf.setLength(0);
        //
        htmlreportpath = System.getProperty("user.dir") + "/Logs/Report_" + time + ".html";
        logger.info("delete report=" + clearReports() + ",delete logs=" + clearLogs());
        InsertHeader();
    }

    //html header
    public void InsertHeader() {
        logger.info("Start to init Report...");
        StringBuffer htmloutput = new StringBuffer();
        htmloutput.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>ServerAutoTest</title>");
        //css
        htmloutput.append("<style type=\"text/css\">");
        htmloutput.append(".Returnlimit {width:500px;height:70px;overflow:auto;word-wrap:break-word;word-break:break-all;}");
        htmloutput.append(".Paramslimit {width:350px;height:70px;overflow:auto;word-wrap:break-word;word-break:break-all;}");
        htmloutput.append(".TestCaselimit {word-wrap:break-word;word-break:break-all;}");
        htmloutput.append("</style></head><body>");
        htmloutput.append("<h1 style=\"font-weight:bold;text-align:center;\">Sioeye2.0 Server Auto Test Report</h1>");
        HelperUtil.writetoFile(htmlreportpath, htmloutput.toString(), true);
        //init apibuf title
        apibuf.append("<p><h3 style=\"font-weight:bold;\">Total List: </h3>");
        apibuf.append("<table border=\"1\" bordercolor=\"blank\"><tr style=\"background-color:LightSkyBlue;\">" +
                "<th style=\"width:100px\">TestCase</th>" +
                "<th>Result</th>" +
                "<th>Type</th>" +
                "<th>Respose</th>" +
                "<th>Params</th>" +
                "<th>Return</th>" +
                "<th>Date</th>" +
                "</tr>");
        //init livebuf title
        apibuf_fail.append("<p><h3 style=\"color:red;font-weight:bold;\">Fail List: </h3>");
        apibuf_fail.append("<table border=\"1\" bordercolor=\"blank\"><tr style=\"background-color:LightSkyBlue;\">" +
                "<th style=\"width:100px\">TestCase</th>" +
                "<th>Result</th>" +
                "<th>Type</th>" +
                "<th>Respose</th>" +
                "<th>Params</th>" +
                "<th>Return</th>" +
                "<th>Date</th>" +
                "</tr>");
    }

    //html tail
    public void EndReport() {
        logger.info("end report...");
        MixReport();
        StringBuffer htmloutput = new StringBuffer();
        htmloutput.append("<p style=\"text-align:center;\">" + com.ne.autotest.main.ServerTestClient.Version + ", design by Then.</p>");
        htmloutput.append("</body></html>");
        HelperUtil.writetoFile(htmlreportpath, htmloutput.toString(), true);
    }

    //intert Apibuf
    public void InsertApibuf(String[] strarray) {
        apibufFlag = true;
        int count = 0;
        if (strarray[1].equals("Fail")) {
            InsertApibuf_fail(strarray);//write to InsertApibuf_fail
            apibuf.append("<tr style=\"color:red;\">");
            for (String str : strarray) {
                if (count == 0) {
                    apibuf.append(TD + "<div class=\"TestCaselimit\">" + str + "</div>" + _TD);
                } else if (count == 4) {
                    apibuf.append(TD + "<div class=\"Paramslimit\">" + str + "</div>" + _TD);
                } else if (count == 5) {
                    apibuf.append(TD + "<div class=\"Returnlimit\">" + str + "</div>" + _TD);
                } else {
                    apibuf.append(TD + str + _TD);
                }
                //apibuf.append(TD+str+_TD);
                count++;
            }
        } else {
            apibuf.append(TR);
            for (String str : strarray) {
                if (count == 0) {
                    apibuf.append(TD + "<div class=\"TestCaselimit\">" + str + "</div>" + _TD);
                } else if (count == 4) {
                    apibuf.append(TD + "<div class=\"Paramslimit\">" + str + "</div>" + _TD);
                } else if (count == 5) {
                    apibuf.append(TD + "<div class=\"Returnlimit\">" + str + "</div>" + _TD);
                } else {
                    apibuf.append(TD + str + _TD);
                }
                //apibuf.append(TD+str+_TD);
                count++;
            }
        }
        apibuf.append(_TR);
    }

    //intert Apibuf_fail
    public void InsertApibuf_fail(String[] strarray) {
        apibuf_failFlag = true;
        apibuf_fail.append(TR);
        int count = 0;
        for (String str : strarray) {
            if (count == 0) {
                apibuf_fail.append(TD + "<div class=\"TestCaselimit\">" + str + "</div>" + _TD);
            } else if (count == 4) {
                apibuf_fail.append(TD + "<div class=\"Paramslimit\">" + str + "</div>" + _TD);
            } else if (count == 5) {
                apibuf_fail.append(TD + "<div class=\"Returnlimit\">" + str + "</div>" + _TD);
            } else {
                apibuf_fail.append(TD + str + _TD);
            }
            //apibuf_fail.append(TD+str+_TD);
            count++;
        }
        apibuf_fail.append(_TR);
    }

    //intert Resultbuf
    public void InsertResultbuf() {
        //time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy_MMdd_HHmm_ss");
        String starttime = null;
        long endtime = new Date().getTime();
        long costtime = 0;
        try {
            Date date = sdf2.parse(com.ne.autotest.main.ServerTestClient.timekey);
            costtime = endtime - date.getTime();
            starttime = sdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        //result table
        resultbuf.append("<p><h3 style=\"color:blank;font-weight:bold;\">Result:</h3>");
        resultbuf.append("<table border=\"1\" bordercolor=\"blank\">" + "<tr style=\"font-weight:bold;background-color:LightSkyBlue;\">");
        resultbuf.append(TH + "Total" + _TH);
        resultbuf.append(TH + "Pass" + _TH);
        resultbuf.append(TH + "Fail" + _TH);
        resultbuf.append(TH + "Warning" + _TH);
        resultbuf.append(TH + "StartTime" + _TH);
        resultbuf.append(TH + "EndTime" + _TH);
        resultbuf.append(TH + "UseTime" + _TH);
        resultbuf.append(_TR);
        resultbuf.append(TR);
        resultbuf.append(TD + (com.ne.autotest.main.ServerTestClient.PassCount + com.ne.autotest.main.ServerTestClient.FailCount) + _TD);
        resultbuf.append(TD + com.ne.autotest.main.ServerTestClient.PassCount + _TD);
        resultbuf.append(TD + "<span style=\"color:red;font-weight:bold;\">" + com.ne.autotest.main.ServerTestClient.FailCount + "</span>" + _TD);
        resultbuf.append(TD + (com.ne.autotest.main.ServerTestClient.WarningCount) + _TD);
        resultbuf.append(TD + starttime + _TD);
        resultbuf.append(TD + sdf.format(new Date(endtime)) + _TD);
        resultbuf.append(TD + costtime / 1000 + "s" + _TD);
        resultbuf.append(_TR + "</table><hr></p>");
        HelperUtil.writetoFile(htmlreportpath, resultbuf.toString(), true);
    }

    public void Explain() {
        explainbuf.append("<p><h3 style=\"color:blank;font-weight:bold;\">Explain:</h3>");
        explainbuf.append("<span style=\"font-size:12pt;\">Result--Pass: how many cases are passed.<br>");
        explainbuf.append("Result--Fail: how many cases are failed after " + (com.ne.autotest.main.ServerTestClient.paramsBean.getRetry() + 1) + " attempts.<br>");
        explainbuf.append("Result--Warning: how many failed attempts before the case is passed.<br>");
        explainbuf.append("Total List/Fail List--Type:<br>");
        explainbuf.append("&nbsp;&nbsp;&nbsp;True: success=true<br>");
        explainbuf.append("&nbsp;&nbsp;&nbsp;False: success=false<br>");
        explainbuf.append("&nbsp;&nbsp;&nbsp;AlwaysTrue: success=true or false<br>");
        explainbuf.append("&nbsp;&nbsp;&nbsp;>" + com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout() + "ms: Respose time is bigger than the defined<br></span>");
        explainbuf.append("<hr></p>");
        HelperUtil.writetoFile(htmlreportpath, explainbuf.toString(), true);
    }

    //mix report
    public void MixReport() {
        InsertResultbuf();
        if (apibuf_failFlag) {
            apibuf_fail.append("</table><hr></p>");
            HelperUtil.writetoFile(htmlreportpath, apibuf_fail.toString(), true);
        }
        if (apibufFlag) {
            apibuf.append("</table><hr></p>");
            HelperUtil.writetoFile(htmlreportpath, apibuf.toString(), true);
        }
        Explain();
    }


    //clear reports
    public int clearReports() {
        int count = 0;
        File file = new File(System.getProperty("user.dir") + "/Logs");
        File tempfile = null;
        if (file.exists()) {
            List<String> templist = new ArrayList<String>(Arrays.asList(file.list()));
            Iterator<String> iter = templist.iterator();
            while (iter.hasNext()) {
                String s = iter.next();
                if (!s.contains("Report_")) {
                    iter.remove();
                }
            }
            if (templist.size() > com.ne.autotest.main.ServerTestClient.paramsBean.getClearFile()) {
                for (int i = 0; i < templist.size() - com.ne.autotest.main.ServerTestClient.paramsBean.getClearFile(); i++) {
                    tempfile = new File(file.getAbsolutePath() + "/" + templist.get(i).toString());
                    if (tempfile.isFile()) {
                        tempfile.delete();
                        count++;
                    }
                }
            }
        }
        return count;
    }

    //clear log
    public int clearLogs() {
        int count = 0;
        File file = new File(System.getProperty("user.dir") + "/Logs");
        File tempfile = null;
        if (file.exists()) {
            List<String> templist = new ArrayList<String>(Arrays.asList(file.list()));
            Iterator<String> iter = templist.iterator();
            while (iter.hasNext()) {
                String s = iter.next();
                if (!s.contains("Log_")) {
                    iter.remove();
                }
            }
            if (templist.size() > com.ne.autotest.main.ServerTestClient.paramsBean.getClearFile()) {
                for (int i = 0; i < templist.size() - com.ne.autotest.main.ServerTestClient.paramsBean.getClearFile(); i++) {
                    tempfile = new File(file.getAbsolutePath() + "/" + templist.get(i).toString());
                    if (tempfile.isFile()) {
                        tempfile.delete();
                        count++;
                    }
                }
            }
        }
        return count;
    }

    //get api fail buf
    public StringBuffer getApibuf_fail() {
        return apibuf_fail;
    }

    //get resultbuf
    public StringBuffer getResultbuf() {
        return resultbuf;
    }

    //get explainbuf
    public StringBuffer getExplainbuf() {
        return explainbuf;
    }

}
