package com.zerody.user.vo;

import com.zerody.user.domain.StaffBlacklistApprover;
import lombok.Data;

import java.util.List;

/**
 * @Author : xufan
 * @create 2023/9/21 15:12
 */
@Data
public class StaffBlacklistApproverDetailVo extends StaffBlacklistApprover{
    /** 违规图片 */
    private List<String> images;

    /** 视频证据 */
    private String video;
}
