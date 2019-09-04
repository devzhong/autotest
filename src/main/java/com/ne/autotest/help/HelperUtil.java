package com.ne.autotest.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


public class HelperUtil {
    private static final Logger logger = LoggerFactory.getLogger(HelperUtil.class);

    /**
     * 功能：检测当前URL是否可连接或是否有效,
     * 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    public static boolean URLisConnect(String urlStr) {
        URL url = null;
        HttpURLConnection con;
        int state = -1;
        int counts = 0;
        if (urlStr == null || urlStr.length() <= 0) {
            return false;
        }
        while (counts < 5) {
            try {
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                state = con.getResponseCode();
                if (state == 200) {
                    logger.info("URL=" + urlStr + " is available!");
                    return true;
                }
                logger.info(counts + "= " + state);
            } catch (Exception e) {
                counts++;
                logger.error("URL is unavailable,counts=" + counts, e);
                continue;
            }
        }
        return false;
    }


    //MD5加密
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toString().toLowerCase();
        } catch (Exception e) {
            logger.error("Exception", e);
            return null;
        }
    }

    //随即
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    //获取时间戳
    public static String getTime(String timestr) {
        if (timestr == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timestr);
            return date.getTime() / 1000 + "";
        } catch (ParseException e) {
            logger.error("Exception", e);
        }
        return null;
    }

    // AES加密
    public static String AESEncrypt(String sSrc, String sKey) {
        if (sKey == null) {
            System.out.println("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.println("Key长度不是16位");
            return null;
        }
        String result = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] raw = md.digest(sKey.getBytes("utf-8"));
            //  byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            result = Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } //为了和服务器nodejs加密结果一直 必须将密钥先进行md5校验
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //String result=bytesToHexString(encrypted);
        logger.info("Str=" + sSrc + ",key=" + sKey + ",AESEncrypt=" + result);
        return result;//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    //write to file
    public static void writetoFile(String filepath, String content, boolean isappend) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(filepath, isappend);
            //  content=content+"\r\n";
            byte[] initContent = content.getBytes("UTF-8");
            fileOutputStream.write(initContent);
            fileOutputStream.close();
            fileOutputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Exception", e);
        }
    }

    //string to json
    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    ///n to <br>
    public static String string2html(String s) {
        s = s.replace("\r\n", "<br>");
        return s;
    }

    //interBR 10
    public static String interBR(String str, int i) {
        i++;
        char[] charArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        int count = 0;
        for (char c : charArray) {
            count++;
            if (count % i == 0) {
                stringBuffer.append(String.valueOf(c) + "<br>");
            } else {
                stringBuffer.append(String.valueOf(c));
            }
        }
        return stringBuffer.toString();
    }

    //open a file
    public static boolean OpenFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            Desktop desk = Desktop.getDesktop();
            try {
                desk.open(file);
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("Exception", e);
            }
        }
        return false;
    }
}
