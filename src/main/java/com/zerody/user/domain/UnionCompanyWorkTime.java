package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 企业上下班时间关联表
 *
 * @author chenKeFeng
 * @date 2022-08-31 11:22:16
 */
@Data
public class UnionCompanyWorkTime implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 上班打卡时间
     */
    private String workPunchTime;
    /**
     * 下班打卡时间
     */
    private String downPunchTime;
    /**
     * 类型(1:用户、0:boss)
     */
    private Integer type;

}
