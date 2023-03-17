package com.zerody.user.util;

import com.zerody.common.constant.YesNo;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhangpingping
 * @date 2021年09月15日 14:27
 */
 
public class CommonUtils {

    /**
     * 姓名长度
     */
    private static Integer RESULT_NAME_LENGTH =2;


    public static final String[] CHINESE_LIST = {"零","一","二","三","四","五","六","七","八","九","十"};

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String idCard, int prefix, int suffix) {
        if (StringUtils.isEmpty(idCard) || (idCard.length() < 8)) {
            return idCard;
        }
        return idCard.replaceAll("(?<=\\w{" + prefix +"})\\w(?=\\w{"+ suffix +"})", "*");
    }


    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    //姓名脱敏
    public  static String protectedName(String userName){
        if(userName.length()== YesNo.YES){
            userName=userName+"*";
        }
        userName = userName.trim();
        char[] r = userName.toCharArray();
        String resultName = "";
        if(r.length == RESULT_NAME_LENGTH){
            resultName =  r[0]+"*";
        }
        if (r.length > RESULT_NAME_LENGTH) {
            String star = "";
            for (int i = 0; i < r.length-RESULT_NAME_LENGTH; i++) {
                star=star+"*";
            }
            resultName = r[0]+star+r[r.length-1];
        }
        return resultName;
    }
}
