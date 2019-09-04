package com.ne.autotest.business;

import com.ne.autotest.help.CaseUtil;
import com.ne.autotest.help.HelperUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Others {
    private static final Logger logger = LoggerFactory.getLogger(Others.class);
    String sessiontoken;
    String userid;
    String sioeyeid;
    String qrcode;
    String causeid;
    String conversationid;
    String myconversationid;
    String searchKey = "Welcom";

    public void start() {
        //start
        third_login();
        login();
        chanage_notification_status();
        //
        search_broadcasts();
        update_video_title();
        del_video();
        //
        search_live();
        get_cause_list();
        cause_live();
        // add_feedback();
        //by server
        incr_chat_count();
        incr_favorites();
        //
        set_director();
        directed_switch();
    }

    private boolean third_login() {
        String api = "third_login";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openid", "ohkmvwweajWK9Z5p15lAwlfEuid4");
        params.put("mobile", "13800000002");
        params.put("type", "weixin");
        params.put("nickname", "test");
        params.put("avatar", "");
        params.put("unionid", "orsZIt9oGZAKvdN3eWg5cUU2W7pA");
        params.put("openid", "ohkmvwweajWK9Z5p15lAwlfEuid4");
        return CaseUtil.Test_AlwaysTrue(api, null, params);
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

                } else {
                    logger.info("sessiontoken doesn't exist!");
                }
                if (jsonObject.getJSONObject("value").has("qrcode")) {
                    qrcode = jsonObject.getJSONObject("value").getString("qrcode");

                } else {
                    logger.info("qrcode doesn't exist!");
                }
                if (jsonObject.getJSONObject("value").has("sioeyeid")) {
                    sioeyeid = jsonObject.getJSONObject("value").getString("sioeyeid");
                } else {
                    logger.info("sioeyeid doesn't exist!");
                }
                if (jsonObject.getJSONObject("value").has("objectid")) {
                    userid = jsonObject.getJSONObject("value").getString("objectid");
                    return true;
                } else {
                    logger.info("objectid doesn't exist!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean search_broadcasts() {
        String api = "search_broadcasts";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageno", "1");
        params.put("pagesize", "10");
        JSONObject jsonObject = CaseUtil.Test_ReturnJson(api, sessiontoken, params);
        if (jsonObject == null) {

        } else {
            try {
                if (jsonObject.getJSONArray("value").length() > 0 && jsonObject.getJSONArray("value").getJSONObject(0).has("conversationid")) {
                    logger.info("total has " + jsonObject.getJSONArray("value").length() + " videos.");
                    for (int i = 0; i < jsonObject.getJSONArray("value").length(); i++) {
                        if (jsonObject.getJSONArray("value").getJSONObject(i).getString("islive").equals("false")) {
                            myconversationid = jsonObject.getJSONArray("value").getJSONObject(i).getString("conversationid");
                            return true;
                        }
                    }
                } else {
                    logger.info("conversationid doesn't exist or getJSONArray.length() <0!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean update_video_title() {
        String api = "update_video_title";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", myconversationid);
        params.put("title", "Welcome to our live...");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean del_video() {
        String api = "del_video";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", myconversationid);
        return CaseUtil.Test(api, sessiontoken, params);
    }


    private boolean chanage_notification_status() {
        String api = "chanage_notification_status";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "1");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean add_feedback() {
        String api = "add_feedback";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "android");
        params.put("version", "2.1.6");
        params.put("comment", "This app is very good.");
        return CaseUtil.Test(api, sessiontoken, params);
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

    private boolean get_cause_list() {
        String api = "get_cause_list";
        Map<String, Object> params = new HashMap<String, Object>();
        JSONObject jsonObject = CaseUtil.Test_ReturnJson(api, sessiontoken, params);
        if (jsonObject == null) {

        } else {
            try {
                if (jsonObject.getJSONArray("value").length() > 0 && jsonObject.getJSONArray("value").getJSONObject(0).has("causeid")) {
                    logger.info("total has " + jsonObject.getJSONArray("value").length() + " causeid.");
                    causeid = jsonObject.getJSONArray("value").getJSONObject(0).getString("causeid");
                    return true;
                } else {
                    logger.info("causeid doesn't exist or getJSONArray.length() <0!");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }

    private boolean cause_live() {
        String api = "get_cause_list";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationid", conversationid);
        params.put("causeid", causeid);
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean incr_chat_count() {
        String api = "incr_chat_count";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationid", conversationid);
        params.put("chatcount", "1");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean incr_favorites() {
        String api = "incr_favorites";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationid", conversationid);
        params.put("favoritesnum", "1");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean set_director() {
        String api = "set_director";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("directorSwitch", "false");
        return CaseUtil.Test(api, sessiontoken, params);
    }

    private boolean directed_switch() {
        String api = "directed_switch";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conversationId", conversationid);
        params.put("liveId", "test");
        return CaseUtil.Test_AlwaysTrue(api, sessiontoken, params);
    }

}
