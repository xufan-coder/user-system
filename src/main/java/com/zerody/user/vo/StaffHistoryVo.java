package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.constant.CheckCompare;
import com.zerody.user.domain.Image;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年08月26日 9:56
 */
@Data
public class StaffHistoryVo {

    /**
     * ID
     */
    private String id;
    /**
     * 描述
     */
    @CheckCompare(value = "idCardReverse", name = "描述")
    private String describe;
    /**
     * 员工ID
     */
    private String staffId;
    /**
     * 类型
     */
    private String type;
    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy年MM月")
    @CheckCompare(value = "idCardReverse", name = "时间")
    private String time;
    /**
     * 图片
     */
    @CheckCompare(value = "idCardReverse", name = "图片")
    private List<String> imageList;
}
