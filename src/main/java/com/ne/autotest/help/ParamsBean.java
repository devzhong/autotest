package com.ne.autotest.help;

import java.util.Date;

public class ParamsBean {
    //xxx.jar -retry 3 -scene 1,2 -resposetimeout 60000
    String start;
    String account;
    String password;
    int clearFile;
    int retry;
    String scene;
    String pushStreamPrivate;
    int resposetimeout;
    String resposetimeoutExclude;
    //email
    String smtp;
    String emailAccount;
    String emailPassword;
    String send;
    String cc;
    String email;
    //server
    String Server;
    String X_Sioeye_App_Id;
    String X_Sioeye_App_Key;
    String X_Sioeye_App_Production;
    int serverTimeout;


    public String ReadParams() {
        return "start=" + start + ",account=" + account + ",password=" + password + ",clearFile=" + clearFile +
                "\n,retry=" + retry + ",scene=" + scene + ",resposetimeout=" + resposetimeout + ",resposetimeoutExclude=" + resposetimeoutExclude +
                "\n,email=" + email + ",smtp=" + smtp + ",interval=" + ",emailAccount=" + emailAccount + ",emailPassword=" + emailPassword + ",send=" + send + ",cc=" + cc +
                "\n,Server=" + Server + ",X_Sioeye_App_Id=" + X_Sioeye_App_Id + ",X_Sioeye_App_Key=" + X_Sioeye_App_Key +
                "\n,X_Sioeye_App_Production=" + X_Sioeye_App_Production + ",serverTimeout=" + serverTimeout;
    }


    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getClearFile() {
        return clearFile;
    }

    public void setClearFile(int clearFile) {
        this.clearFile = clearFile;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public int getResposetimeout() {
        return resposetimeout;
    }

    public void setResposetimeout(int resposetimeout) {
        this.resposetimeout = resposetimeout;
    }

    public String getResposetimeoutExclude() {
        return resposetimeoutExclude;
    }

    public void setResposetimeoutExclude(String resposetimeoutExclude) {
        this.resposetimeoutExclude = resposetimeoutExclude;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServer() {
        return Server;
    }


    public void setServer(String server) {
        this.Server = server;
        HttpsUtil.Server = server;
    }


    public String getX_Sioeye_App_Id() {
        return X_Sioeye_App_Id;
    }


    public void setX_Sioeye_App_Id(String x_Sioeye_App_Id) {
        this.X_Sioeye_App_Id = x_Sioeye_App_Id;
        HttpsUtil.X_Sioeye_App_Id = x_Sioeye_App_Id;
    }


    public String getX_Sioeye_App_Key() {
        return X_Sioeye_App_Key;
    }


    public void setX_Sioeye_App_Key(String x_Sioeye_App_Key) {
        this.X_Sioeye_App_Key = x_Sioeye_App_Key;
        HttpsUtil.X_Sioeye_App_Key = getSignkey(x_Sioeye_App_Key);
    }


    public String getX_Sioeye_App_Production() {
        return X_Sioeye_App_Production;
    }


    public void setX_Sioeye_App_Production(String x_Sioeye_App_Production) {
        this.X_Sioeye_App_Production = x_Sioeye_App_Production;
        HttpsUtil.X_Sioeye_App_Production = x_Sioeye_App_Production;
    }


    public int getServerTimeout() {
        return serverTimeout;
    }


    public void setServerTimeout(int serverTimeout) {
        this.serverTimeout = serverTimeout;
        HttpsUtil.serverTimeout = serverTimeout;
    }

    public String getPushStreamPrivate() {
        return pushStreamPrivate;
    }


    public void setPushStreamPrivate(String pushStreamPrivate) {
        this.pushStreamPrivate = pushStreamPrivate;
    }

    //trigger
    String sponsor;

    public String getSponsor() {
        return sponsor;
    }


    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public static String getSignkey(String x_Sioeye_App_Key) {
        long time = new Date().getTime();
        return HelperUtil.MD5(x_Sioeye_App_Key + time) + "," + time;
    }
}
