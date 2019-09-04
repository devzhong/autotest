package com.ne.autotest.help;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

/**
 * Created by then on 2016/08/31.
 */
public class HttpsUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);
    public static String Server;
    public static String X_Sioeye_App_Id;
    public static String X_Sioeye_App_Key;
    public static String X_Sioeye_App_Production;
    public static int serverTimeout;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static String CR_LF = "\r\n";
    private final static String TWO_DASHES = "--";
    public static boolean HttpsDebug = true;

    //琛ㄥ崟璇锋眰
    public static List<Object> sendFormData(String api, String sessiontoken, String formdata, String uploadfile) {
        List<Object> list = new ArrayList<Object>();
        String BOUNDARY = setBOUNDARY();
        String result = null;
        InputStream inptStream = null;
        InputStream errorStream = null;
        OutputStream outputStream = null;
        FileInputStream fStream = null;
        URL url = null;
        long Stime = 0, Etime = 0;
        int resposeOK = -1;
        int resposeCode = -1;
        HttpsURLConnection httpsURLConnection = null;
        JSONObject jsonObject = null;
        //exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            url = new URL(Server + api);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setConnectTimeout(serverTimeout);       //璁剧疆杩炴帴瓒呮椂鏃堕棿
            httpsURLConnection.setReadTimeout(serverTimeout);
            httpsURLConnection.setDoInput(true);                  //鎵撳紑杈撳叆娴侊紝浠ヤ究浠庢湇鍔″櫒鑾峰彇鏁版嵁
            httpsURLConnection.setDoOutput(true);                 //鎵撳紑杈撳嚭娴侊紝浠ヤ究鍚戞湇鍔″櫒鎻愪氦鏁版嵁
            httpsURLConnection.setRequestMethod("POST");   //璁剧疆浠ost鏂瑰紡鎻愪氦鏁版嵁
            httpsURLConnection.setUseCaches(false);               //浣跨敤Post鏂瑰紡涓嶈兘浣跨敤缂撳瓨
            //SSLContext
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //SSLSocketFactory
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            httpsURLConnection.setSSLSocketFactory(ssf);
            httpsURLConnection.setRequestProperty("X_Sioeye_App_Id", X_Sioeye_App_Id);
            httpsURLConnection.setRequestProperty("x_sioeye_app_sign_key", X_Sioeye_App_Key);
            httpsURLConnection.setRequestProperty("X_Sioeye_App_Production", X_Sioeye_App_Production);
            if (sessiontoken != null) {
                httpsURLConnection.setRequestProperty("X_sioeye_sessiontoken", sessiontoken);
            }
            httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY + "; charset=UTF-8");
            if (com.ne.autotest.main.ServerTestClient.paramsBean.getStart().equalsIgnoreCase("UI")) {
                com.ne.autotest.main.ServerTestClient.mainui.getlblAPI().setText("API : " + api);
            }
            Stime = new Date().getTime();//寮?濮嬫椂闂?
            //鑾峰緱杈撳嚭娴侊紝鍚戞湇鍔″櫒鍐欏叆鏁版嵁
            outputStream = httpsURLConnection.getOutputStream();
            StringBuffer form = new StringBuffer();
            form.append(TWO_DASHES + BOUNDARY + CR_LF);
            form.append(formdata);
            form.append(CR_LF);
//			   form.append(TWO_DASHES + BOUNDARY + CR_LF);  
//             form.append("Content-Disposition:form-data;name=\"upload\";filename=\"test.jpg\"" + CR_LF);  
//             form.append("Content-Type:image/jpeg; charset=UTF-8"+ CR_LF);  
//             form.append("Content-Transfer-Encoding: binary"+ CR_LF);  
//             form.append(CR_LF); 
            outputStream.write(form.toString().getBytes("UTF-8"));
            fStream = new FileInputStream(uploadfile);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.write(CR_LF.getBytes("UTF-8"));
            outputStream.write((TWO_DASHES + BOUNDARY + TWO_DASHES + CR_LF).getBytes("UTF-8"));
            outputStream.flush();
            resposeCode = httpsURLConnection.getResponseCode();            //鑾峰緱鏈嶅姟鍣ㄧ殑鍝嶅簲鐮?
            Etime = new Date().getTime();//缁撴潫鏃堕棿
            String encode = httpsURLConnection.getContentEncoding();
            if (resposeCode == HttpsURLConnection.HTTP_OK) {
                resposeOK = 1;
                inptStream = httpsURLConnection.getInputStream();
                result = dealResponseResult(inptStream, encode);                     //澶勭悊鏈嶅姟鍣ㄧ殑鍝嶅簲缁撴灉
                //resposetimeout
                boolean isexclude = false;
                String[] exclued = com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeoutExclude().split(",");
                for (String str : exclued) {
                    if (str.equals(api)) {
                        isexclude = true;
                        break;
                    }
                }
                if (!isexclude && Etime - Stime > com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout()) {
                    jsonObject = new JSONObject("{\"success\":\"error1\",\"value\":\"ResposeTimeout\"}");
                } else {
                    jsonObject = new JSONObject(result);
                }
            } else {
                resposeOK = 0;
                errorStream = httpsURLConnection.getErrorStream();
                result = dealResponseResult(errorStream, encode);
                jsonObject = new JSONObject("{\"success\":\"error2\",\"value\":\"HttpFail\"}");
            }
            httpsURLConnection.disconnect();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } finally {
            try {
                if (inptStream != null) {
                    inptStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fStream != null) {
                    fStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
                if (sw != null) {
                    sw.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                logger.error("Exception", e);
            }

        }

        if (HttpsDebug) {
            if (resposeOK == -1) {//exception
                logger.warn("API=" + api + ",type=sendFormData" + ",get info from server failed with exception!");
            } else if (resposeOK == 1) {//ok
                logger.info("API=" + api + ",Resposetime=" + (Etime - Stime) + "ms,Code=" + resposeCode + ",type=sendFormData");
                logger.info("API=" + api + ",result=" + result);
            } else if (resposeOK == 0) {//http fail
                logger.warn("API=" + api + ",Resposetime=" + (Etime - Stime) + "ms,ErrorCode=" + resposeCode + ",type=sendFormData");
                logger.warn("API=" + api + ",result=" + result);
            }
        }
        list.add(jsonObject);
        list.add(result);
        list.add((Etime - Stime) + "");
        list.add(resposeCode);
        return list;
    }

    //鐧婚檰鍚庤姹?
    public static List<Object> sendPostData(String api, String sessiontoken, String params) {
        List<Object> list = new ArrayList<Object>();
        String result = null;
        InputStream inptStream = null;
        InputStream errorStream = null;
        OutputStream outputStream = null;
        URL url = null;
        long Stime = 0, Etime = 0;
        int resposeOK = -1;
        int resposeCode = -1;
        HttpsURLConnection httpsURLConnection = null;
        JSONObject jsonObject = null;
        //exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            url = new URL(Server + api);
            byte[] data = params.getBytes();//鑾峰緱璇锋眰浣?
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setConnectTimeout(serverTimeout);       //璁剧疆杩炴帴瓒呮椂鏃堕棿
            httpsURLConnection.setReadTimeout(serverTimeout);
            httpsURLConnection.setDoInput(true);                  //鎵撳紑杈撳叆娴侊紝浠ヤ究浠庢湇鍔″櫒鑾峰彇鏁版嵁
            httpsURLConnection.setDoOutput(true);                 //鎵撳紑杈撳嚭娴侊紝浠ヤ究鍚戞湇鍔″櫒鎻愪氦鏁版嵁
            httpsURLConnection.setRequestMethod("POST");   //璁剧疆浠ost鏂瑰紡鎻愪氦鏁版嵁
            httpsURLConnection.setUseCaches(false);               //浣跨敤Post鏂瑰紡涓嶈兘浣跨敤缂撳瓨
            //SSLContext
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //SSLSocketFactory
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            httpsURLConnection.setSSLSocketFactory(ssf);
            httpsURLConnection.setRequestProperty("X_Sioeye_App_Id", X_Sioeye_App_Id);
            httpsURLConnection.setRequestProperty("x_sioeye_app_sign_key", X_Sioeye_App_Key);
            httpsURLConnection.setRequestProperty("X_Sioeye_App_Production", X_Sioeye_App_Production);
            if (sessiontoken != null) {
                httpsURLConnection.setRequestProperty("X_sioeye_sessiontoken", sessiontoken);
            }
            //璁剧疆璇锋眰浣撶殑绫诲瀷鏄枃鏈被鍨?
            httpsURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //璁剧疆璇锋眰浣撶殑闀垮害
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            if (com.ne.autotest.main.ServerTestClient.paramsBean.getStart().equalsIgnoreCase("UI")) {
                com.ne.autotest.main.ServerTestClient.mainui.getlblAPI().setText("API : " + api);
            }
            Stime = new Date().getTime();//寮?濮嬫椂闂?
            //鑾峰緱杈撳嚭娴侊紝鍚戞湇鍔″櫒鍐欏叆鏁版嵁
            outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            resposeCode = httpsURLConnection.getResponseCode();            //鑾峰緱鏈嶅姟鍣ㄧ殑鍝嶅簲鐮?
            Etime = new Date().getTime();//缁撴潫鏃堕棿
            String encode = httpsURLConnection.getContentEncoding();
            if (resposeCode == HttpsURLConnection.HTTP_OK) {
                resposeOK = 1;
                inptStream = httpsURLConnection.getInputStream();
                result = dealResponseResult(inptStream, encode);                     //澶勭悊鏈嶅姟鍣ㄧ殑鍝嶅簲缁撴灉
                //resposetimeout
                boolean isexclude = false;
                String[] exclued = com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeoutExclude().split(",");
                for (String str : exclued) {
                    if (str.equals(api)) {
                        isexclude = true;
                        break;
                    }
                }
                if (!isexclude && Etime - Stime > com.ne.autotest.main.ServerTestClient.paramsBean.getResposetimeout()) {
                    jsonObject = new JSONObject("{\"success\":\"error1\",\"value\":\"ResposeTimeout\"}");
                } else {
                    jsonObject = new JSONObject(result);
                }
            } else {
                resposeOK = 0;
                errorStream = httpsURLConnection.getErrorStream();
                result = dealResponseResult(errorStream, encode);
                jsonObject = new JSONObject("{\"success\":\"error2\",\"value\":\"HttpFail\"}");
            }
            httpsURLConnection.disconnect();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
            e.printStackTrace(pw);
            result = sw.toString();
        } finally {
            try {
                if (inptStream != null) {
                    inptStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
                if (sw != null) {
                    sw.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                logger.error("Exception", e);
            }

        }
        if (HttpsDebug) {
            if (resposeOK == -1) {//exception
                logger.warn("API=" + api + ",type=sendPostData" + ",get info from server failed with exception!");
            } else if (resposeOK == 1) {//ok
                logger.info("API=" + api + ",Resposetime=" + (Etime - Stime) + "ms,Code=" + resposeCode + ",type=sendPostData");
                logger.info("API=" + api + ",result=" + result);
            } else if (resposeOK == 0) {//http fail
                logger.warn("API=" + api + ",Resposetime=" + (Etime - Stime) + "ms,ErrorCode=" + resposeCode + ",type=sendPostData");
                logger.warn("API=" + api + ",result=" + result);
            }
        }
        list.add(jsonObject);
        list.add(result);
        list.add((Etime - Stime) + "");
        list.add(resposeCode);
        return list;
    }



    /*
     * Function  :   灏佽璇锋眰浣撲俊鎭?
     * Param     :   params璇锋眰浣撳唴瀹癸紝encode缂栫爜鏍煎紡
     */
//    public static String getRequestData(Map<String, Object> params) {
//        StringBuffer stringBuffer = new StringBuffer();        //瀛樺偍灏佽濂界殑璇锋眰浣撲俊鎭?
//        try {
//            for(Map.Entry<String, String> entry : params.entrySet()) {
//                stringBuffer.append(entry.getKey())
//                        .append("=")
//                        .append(URLEncoder.encode(entry.getValue()+"", encode))
//                        .append("&");
//            }
//            if(stringBuffer.length()>0) {
//                stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //鍒犻櫎鏈?鍚庣殑涓?涓?"&"
//            }
//        } catch (Exception e) {
//            logger.error("Exception",e);
//        }
//        JSONObject jsonObj = new JSONObject(params);
//       
//        return jsonObj.toString();
//    }

    /*
     * Function  :   澶勭悊鏈嶅姟鍣ㄧ殑鍝嶅簲缁撴灉锛堝皢杈撳叆娴佽浆鍖栨垚瀛楃涓诧級
     * Param     :   inputStream鏈嶅姟鍣ㄧ殑鍝嶅簲杈撳叆娴?
     */
    public static String dealResponseResult(InputStream inputStream, String encode) {
        if (inputStream != null) {
            String resultData = null;      //瀛樺偍澶勭悊缁撴灉
            GZIPInputStream gZIPInputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            try {
                if (encode != null && encode.equals("gzip")) {
                    gZIPInputStream = new GZIPInputStream(inputStream);//gzip
                    while ((len = gZIPInputStream.read(data)) != -1) {
                        byteArrayOutputStream.write(data, 0, len);
                    }
                } else {
                    while ((len = inputStream.read(data)) != -1) {
                        byteArrayOutputStream.write(data, 0, len);
                    }
                }
            } catch (IOException e) {
                logger.error("Exception", e);
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (gZIPInputStream != null) {
                        gZIPInputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("Exception", e);
                }
            }
            try {
                resultData = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                byteArrayOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
            return resultData;
        } else {
            return null;
        }
    }

    //淇′换璇佷功
    public static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    //HTTP璇锋眰鍒嗛殧瀛楃涓?
    public static String setBOUNDARY() {
        char[] MULTIPART_CHARS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        String BOUNDARY = buffer.toString();
        logger.info("BOUNDARY= " + BOUNDARY);
        return BOUNDARY;
    }


}
