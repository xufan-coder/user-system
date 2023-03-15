package com.zerody.user.constant;

import com.zerody.common.utils.DataUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片类型
 * @author PengQiang
 * @ClassName ImageTypeInfo
 * @DateTime 2021/8/3_19:12
 * @Deacription TODO
 */
public class ImageTypeInfo {

    public final  static  String STAFF_BLACKLIST = "staff-blacklist";

    public final  static  String STAFF_HONOR="HONOR";

    public final  static  String STAFF_PUNISHMENT="PUNISHMENT";

    public final  static  String USER_OPINION = "USER-OPINION";

    public final  static  String USER_REPLY = "USER-REPLY";

    /**
    *  合规承诺书
    */
    public final  static  String COMPLIANCE_COMMITMENT = "COMPLIANCE-COMMITMENT";

    /**
     *  学历证书
     */
    public final  static  String DIPLOMA = "DIPLOMA";

    /**
     * 合作申请
     */
    public final static String COOPERATION_APPLY = "COOPERATION_APPLY";


    private static Map<String, String> toImageType = new HashMap<>();

    static {
        toImageType.put(DIPLOMA, DIPLOMA);
        toImageType.put(COMPLIANCE_COMMITMENT, COMPLIANCE_COMMITMENT);
    }

    public static boolean isToImageType(String type) {
        if (DataUtil.isEmpty(type)) {
            return false;
        }
        return DataUtil.isNotEmpty(toImageType.get(type)) ;
    }

    public static class ImageType{

        public static final String JPG = ".jpg";

        public static final String PNG = ".png";
        private static Map<String, String> imageType = new HashMap<>();

        static {
            imageType.put(JPG, JPG);
            imageType.put(PNG, PNG);
        }

        public static boolean isImageType(String url) {
            if (DataUtil.isEmpty(url)) {
                return Boolean.FALSE;
            }
            int suffixIndex = url.lastIndexOf(".");
            if (suffixIndex == -1 || suffixIndex  == 0) {
                return Boolean.FALSE;
            }
            String suffix = url.substring(suffixIndex);
            return DataUtil.isNotEmpty(imageType.get(suffix));
        }
    }
}
