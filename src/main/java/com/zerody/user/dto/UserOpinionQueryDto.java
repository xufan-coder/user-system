package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author kuang
 */
@Data
public class UserOpinionQueryDto extends PageQueryDto {

    /**提交人名称*/
    private String searchName;

    /**用户id*/
    private String userId;

    /**分类id*/
    private String typeId;

    /**是否是boos账号*/
    private boolean isCEO;


}
