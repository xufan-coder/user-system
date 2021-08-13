package com.zerody.user.dto;

import com.zerody.user.dto.bean.UserPositionPageParam;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName FrameworkBlacListQueryPageDto
 * @DateTime 2021/8/4_11:22
 * @Deacription TODO
 */
@Data
public class FrameworkBlacListQueryPageDto extends UserPositionPageParam {

    private String userName;

    private String mobile;

    private String state;

    private String queryDimensionality;
}
