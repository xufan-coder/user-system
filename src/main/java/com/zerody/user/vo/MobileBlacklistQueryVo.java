package com.zerody.user.vo;

import com.zerody.common.utils.CollectionUtils;
import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName MobileBlacklistQueryVo
 * @DateTime 2021/8/5_15:41
 * @Deacription TODO
 */
@Data
public class MobileBlacklistQueryVo {

    /** 是否被拉黑 */
    private Boolean isBlock;

    /** 拉黑的企业名称 */
    private String companyName;

    /** 拉黑的原因 */
    private String reason;
}
