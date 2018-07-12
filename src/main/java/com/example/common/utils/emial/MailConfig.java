package com.example.common.utils.emial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailConfig {
    private static final String PROPERTIES_DEFAULT = "mailConfig.properties";
    public static String host;
    public static Integer port;
    public static String userName;
    public static String passWord;
    public static String emailForm;
    public static String timeout;
    public static Properties properties;
    static{
        init();
    }

    /**
     * 初始化
     */
    private static void init() {
        properties = new Properties();
        try{
            InputStream inputStream = MailConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_DEFAULT);
            properties.load(inputStream);
            inputStream.close();
            host = properties.getProperty("mailHost");  //服务器
            port = Integer.parseInt(properties.getProperty("mailPort"));  //端口
            userName = properties.getProperty("mailUsername");
            passWord = properties.getProperty("mailPassword");
            emailForm = properties.getProperty("mailFrom");  //发送人
            timeout = properties.getProperty("mailTimeout");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
