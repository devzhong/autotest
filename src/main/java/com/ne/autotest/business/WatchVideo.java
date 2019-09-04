package com.ne.autotest.business;

import com.ne.autotest.help.CaseUtil;
import com.ne.autotest.help.HelperUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WatchVideo {
    private static final Logger logger = LoggerFactory.getLogger(WatchVideo.class);
    String sessiontoken;
    String searchKey = "Welcom";
    String conversationid;
    String videouserid;

    public void start() {
        //start
        login();
        search_live();
        get_conversation_info();
        //
        get_ip_messages();
        get_netease_im_accesstoken();
        multi_info();
        watch_start();
        //
        baidu_push_data();//push by baidu
        get_shorturl();
        repost_video();
        //

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

    private boolean search_live() {
        String api = "search_live";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchKey", searchKey);
        params.put("pageno", "1");
        params.put("pagesize", "20");

        JSONObject jsonObject = CaseUtil.Test_ReturnJson(api, sessiontoken, params);
        if (jsonObject == null) {

        } else {
            try {
                if (jsonObject.getJSONArray("value").length() > 0 && jsonObject.getJSONArray("value").getJSONObject(0).has("objectid")) {
                    logger.info("total has " + jsonObject.getJSONArray("value").length() + " lives.");
                    videouserid = jsonObject.getJSONArray("value").getJSONObject(0).getString("conversationid");
                } else {
                    logger.info("videouserid doesn't exist or getJSONArray.length() <0!");
                }
                if (jsonObject.getJSONArray("value").length() > 0 && jsonObject.getJSONArray("value").getJSONObject(0).has("conversationid")) {
                    logger.info("total has " + jsonObject.getJSONArray("value").length() + " lives.");
                    conversationid = jsonObject.getJSONArray("value").getJSONObject(0).getString("conversationid");
                    return true;
                } else {
                    logger.info("conversationID doesn't exist or getJSONArray.length() <0!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean get_conversation_info() {
        String api = "get_conversation_info";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", conversationid);
        return CaseUtil.Test_AlwaysTrue(api, sessiontoken, params);
    }

    private boolean baidu_push_data() {
        String api = "baidu_push_data";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", conversationid);
        params.put("pushType", 0);
        return CaseUtil.Test_AlwaysTrue(api, sessiontoken, params);
    }

    private boolean get_ip_messages() {
        String api = "get_ip_messages";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationid", conversationid);
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean get_netease_im_accesstoken() {
        String api = "get_netease_im_accesstoken";
        Map<String, Object> params = new HashMap<String, Object>();
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean multi_info() {
        String api = "multi_info";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", conversationid);
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean watch_start() {
        String api = "watch_start";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", conversationid);
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean repost_video() {
        String api = "repost_video";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationid", conversationid);
        params.put("objectid", videouserid);
        return CaseUtil.Test_AlwaysTrue(api, sessiontoken, params);
    }

    private boolean get_shorturl() {
        String api = "get_shorturl";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("longUrl", "https://www.baidu.com/");
        params.put("format", "json");
        //params.put("callback","");
        return CaseUtil.Test(api, sessiontoken, params);
    }


}
