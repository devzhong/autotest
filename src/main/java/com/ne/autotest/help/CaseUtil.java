package com.ne.autotest.help;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(CaseUtil.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean Test(String api, String sessiontoken, Map<String, Object> params) {
        logger.info("**********************Test: " + api + "**********************");
        boolean pass = false;
        int retry = 0;
        JSONObject jsonObject;
        String paramsBuf = new JSONObject(params).toString();
        logger.info("API=" + api + ",params: " + paramsBuf);
        try {
            do {
                retry++;
                List<Object> list = HttpsUtil.sendPostData(api, sessiontoken, paramsBuf);
                jsonObject = (JSONObject) list.get(0);
                if (jsonObject == null) {//exception
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "Exception", "NA", paramsBuf, HelperUtil.string2html((String) list.get(1)), sdf.format(new Date())});
                } else if (!jsonObject.has("success")) {//server config error
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "RequestFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error2")) {//httpfail
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "HttpFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("true")) {
                    pass = true;//pass, to next api
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Pass", "True", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("false")) {
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "False", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error1")) {//reposetimeout
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", ">" + com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout() + "ms", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                }

            } while (!pass && retry <= com.ne.autotest.main.ServerTestClient.paramsBean.getRetry());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        //judge result
        if (pass) {
            if (retry > 1) {
                com.ne.autotest.main.ServerTestClient.WarningCount = com.ne.autotest.main.ServerTestClient.WarningCount + retry - 1;
            }
            com.ne.autotest.main.ServerTestClient.PassCount++;
        } else {
            com.ne.autotest.main.ServerTestClient.FailCount++;
        }
        return pass;
    }

    public static JSONObject Test_ReturnJson(String api, String sessiontoken, Map<String, Object> params) {
        logger.info("**********************Test: " + api + "**********************");
        boolean pass = false;
        int retry = 0;
        boolean resposeflag = false;
        JSONObject jsonObject = null;
        List<Object> list = null;
        String paramsBuf = new JSONObject(params).toString();
        logger.info("API=" + api + ",params: " + paramsBuf);
        try {
            do {
                retry++;
                resposeflag = false;
                list = HttpsUtil.sendPostData(api, sessiontoken, paramsBuf);
                jsonObject = (JSONObject) list.get(0);
                if (jsonObject == null) {//exception
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "Exception", "NA", paramsBuf, HelperUtil.string2html((String) list.get(1)), sdf.format(new Date())});
                } else if (!jsonObject.has("success")) {//server config error
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "RequestFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error2")) {//httpfail
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "HttpFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("true")) {
                    pass = true;//pass, to next api
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Pass", "True", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("false")) {
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "False", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error1")) {//reposetimeout
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", ">" + com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout() + "ms", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                    resposeflag = true;
                }

            } while (!pass && retry <= com.ne.autotest.main.ServerTestClient.paramsBean.getRetry());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        //judge result
        if (pass) {
            if (retry > 1) {
                com.ne.autotest.main.ServerTestClient.WarningCount = com.ne.autotest.main.ServerTestClient.WarningCount + retry - 1;
            }
            com.ne.autotest.main.ServerTestClient.PassCount++;
            return jsonObject;
        } else {
            com.ne.autotest.main.ServerTestClient.FailCount++;
            if (resposeflag) {
                try {
                    jsonObject = new JSONObject((String) list.get(1));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    logger.error("Exception", e);
                }
                return jsonObject;
            } else {
                return null;
            }
        }
    }


    public static boolean Test_Uploadfile(String api, String sessiontoken, String formdata, String uploadfile) {
        logger.info("**********************Test: " + api + "**********************");
        boolean pass = false;
        int retry = 0;
        JSONObject jsonObject;
        logger.info("API=" + api + ",formdata: " + formdata.toString());
        try {
            do {
                retry++;
                List<Object> list = HttpsUtil.sendFormData(api, sessiontoken, formdata, uploadfile);
                jsonObject = (JSONObject) list.get(0);
                if (jsonObject == null) {//exception
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "Exception", "NA", formdata.toString(), HelperUtil.string2html((String) list.get(1)), sdf.format(new Date())});
                } else if (!jsonObject.has("success")) {//server config error
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "RequestFail", (String) list.get(2) + "ms", formdata.toString(), (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error2")) {//httpfail
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "HttpFail", (String) list.get(2) + "ms", formdata.toString(), (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("true")) {
                    pass = true;//pass, to next api
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Pass", "True", (String) list.get(2) + "ms", formdata.toString(), (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("false")) {
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "False", (String) list.get(2) + "ms", formdata.toString(), (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error1")) {//reposetimeout
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", ">" + com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout() + "ms", (String) list.get(2) + "ms", formdata.toString(), (String) list.get(1), sdf.format(new Date())});
                }

            } while (!pass && retry <= com.ne.autotest.main.ServerTestClient.paramsBean.getRetry());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        //judge result
        if (pass) {
            if (retry > 1) {
                com.ne.autotest.main.ServerTestClient.WarningCount = com.ne.autotest.main.ServerTestClient.WarningCount + retry - 1;
            }
            com.ne.autotest.main.ServerTestClient.PassCount++;
        } else {
            com.ne.autotest.main.ServerTestClient.FailCount++;
        }
        return pass;
    }


    public static boolean Test_AlwaysTrue(String api, String sessiontoken, Map<String, Object> params) {
        logger.info("**********************Test: " + api + "**********************");
        boolean pass = false;
        int retry = 0;
        JSONObject jsonObject;
        String paramsBuf = new JSONObject(params).toString();
        logger.info("API=" + api + ",params: " + paramsBuf);
        try {
            do {
                retry++;
                List<Object> list = HttpsUtil.sendPostData(api, sessiontoken, paramsBuf);
                jsonObject = (JSONObject) list.get(0);
                if (jsonObject == null) {//exception
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "Exception", "NA", paramsBuf, HelperUtil.string2html((String) list.get(1)), sdf.format(new Date())});
                } else if (!jsonObject.has("success")) {//server config error
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "RequestFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error2")) {//httpfail
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", "HttpFail", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("true")) {
                    pass = true;//pass, to next api
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Pass", "True", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("false")) {
                    pass = true;//pass, to next api
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Pass", "AlwaysTrue", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                } else if (jsonObject.getString("success").equals("error1")) {//reposetimeout
                    com.ne.autotest.main.ServerTestClient.reportUtil.InsertApibuf(new String[]{api, "Fail", ">" + com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout() + "ms", (String) list.get(2) + "ms", paramsBuf, (String) list.get(1), sdf.format(new Date())});
                }

            } while (!pass && retry <= com.ne.autotest.main.ServerTestClient.paramsBean.getRetry());
            //judge result
            if (pass) {
                if (retry > 1) {
                    com.ne.autotest.main.ServerTestClient.WarningCount = com.ne.autotest.main.ServerTestClient.WarningCount + retry - 1;
                }
                com.ne.autotest.main.ServerTestClient.PassCount++;
            } else {
                com.ne.autotest.main.ServerTestClient.FailCount++;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
        return pass;
    }
}
