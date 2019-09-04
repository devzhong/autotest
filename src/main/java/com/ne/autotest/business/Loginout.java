package com.ne.autotest.business;

import com.ne.autotest.help.CaseUtil;
import com.ne.autotest.help.HelperUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Loginout {
    private static final Logger logger = LoggerFactory.getLogger(Loginout.class);
    String sessiontoken;
    String installationid;

    public void start() {
        //start
        get_version();
        get_ad_place_list();
        login();
        get_new_notification_count();
        search_update_relation();
        search_recommend_user();
        search_recommend_live();
        create_platform_endpoint();
        account_bind();
        search_relation_video();
        get_notification_list();
        get_new_systemmsg_count();
        get_system_msg();
        logout();
    }

    private boolean get_version() {
        String api = "get_version";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("devtype", "1");
        params.put("versionname", "2.0.16");
        params.put("versioncode", "200000016");
        return CaseUtil.Test(api, null, params);
    }


    private boolean login() {
        String api = "login";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", com.ne.autotest.main.ServerTestClient.paramsBean.getAccount());
        params.put("password", HelperUtil.MD5(com.ne.autotest.main.ServerTestClient.paramsBean.getPassword()));
        params.put("type", "app");
        JSONObject jsonObject = CaseUtil.Test_ReturnJson(api, null, params);
        if (jsonObject == null) {

        } else {
            try {
                if (jsonObject.getJSONObject("value").has("sessiontoken")) {
                    sessiontoken = jsonObject.getJSONObject("value").getString("sessiontoken");
                    return true;
                } else {
                    logger.info("sessiontoken doesn't exist!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean get_new_notification_count() {
        String api = "get_new_notification_count";
        Map<String, Object> params = new HashMap<String, Object>();
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean search_update_relation() {
        String api = "search_update_relation";
        Map<String, Object> params = new HashMap<String, Object>();
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean get_ad_place_list() {
        String api = "get_ad_place_list";
        Map<String, Object> params = new HashMap<String, Object>();
        return CaseUtil.Test(api, null, params);
    }

    private boolean search_recommend_user() {
        String api = "search_recommend_user";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("israndom", "true");
        params.put("pageno", "1");
        params.put("pagesize", "20");
        return CaseUtil.Test(api, null, params);
    }

    private boolean search_recommend_live() {
        String api = "search_recommend_live";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageno", "1");
        params.put("pagesize", "20");
        return CaseUtil.Test(api, null, params);
    }

    private boolean create_platform_endpoint() {
        String api = "create_platform_endpoint";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceid", "351901031057494");
        params.put("devicetoken", "cCW0J6LUGCw:APA91bFMSL3pZ222zXMwgpJy8BllzJbkEV0UX6IkMVXUeEJ74TLFqkygExfT7naQMQ1hV8B1PLd6e0HkmP-F3TXN_6Not5Y7UgMku5IG5OKaeXiP22WiKgCgAc4IuT1XpjsuaCrwJ4BP");
        params.put("devtype", "1");
        params.put("appversion", "200100000");
        JSONObject jsonObject = CaseUtil.Test_ReturnJson(api, sessiontoken, params);
        if (jsonObject == null) {

        } else {
            try {
                if (jsonObject.getJSONObject("value").has("installationid")) {
                    installationid = jsonObject.getJSONObject("value").getString("installationid");
                    return true;
                } else {
                    logger.info("installationid doesn't exist!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean account_bind() {
        String api = "account_bind";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("installationid", installationid);
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean search_relation_video() {
        String api = "search_relation_video";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageno", "1");
        params.put("pagesize", "20");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean get_notification_list() {
        String api = "get_notification_list";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageno", "1");
        params.put("pagesize", "20");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean get_new_systemmsg_count() {
        String api = "get_new_systemmsg_count";
        Map<String, Object> params = new HashMap<String, Object>();
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean get_system_msg() {
        String api = "get_system_msg";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageno", "1");
        params.put("pagesize", "20");
        return CaseUtil.Test(api, sessiontoken, params);
    }


    private boolean logout() {
        String api = "logout";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "app");
        return CaseUtil.Test(api, sessiontoken, params);
    }
}
