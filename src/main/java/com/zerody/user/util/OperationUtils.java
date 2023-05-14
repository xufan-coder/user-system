package com.zerody.user.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author : chenKeFeng
 * @date : 2023/5/11 15:33
 */
public class OperationUtils {

    /**
     * 保留两位小数
     *
     * @param singleNum 单数
     * @param num 总数
     * @return 占比
     */
    public static BigDecimal reserveTwo(Integer singleNum, Integer num){
        if (singleNum.equals(0) || num.equals(0)) {
            return BigDecimal.ZERO;
        }
        BigDecimal b = new BigDecimal(singleNum).divide(new BigDecimal(num), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(b);
        return new BigDecimal(format);
    }

}
