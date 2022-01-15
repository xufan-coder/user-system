package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 *
 *  月初在职人数实体
 * @author               PengQiang
 * @description          DELL
 * @date                 2022/1/15 14:27
 */
@Data
public class MonthActivityUser {
    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 企业id */
    private String companyId;

    /** 部门id */
    private String departId;

    /** 用户id */
    private String userId;

    /** 年 */
    private Integer year;

    /** 月 */
    private Integer month;

    /** 类型(COMPANY:企业维度、DEPART：部门维度、TEAM：团队维度、INDIVIDUAL：个人维度) */
    private String type;

    /** 数量 */
    private Integer number;

    /** 创建时间 */
    private Date createTime;


    @Override
    public boolean equals(Object o) {
        if (this != o) {
            return Boolean.FALSE;
        }
        MonthActivityUser that = (MonthActivityUser) o;
        if (!Objects.equals(year, that.year) ||
            !Objects.equals(month, that.month) ||
            !Objects.equals(type, that.type)) {
            return Boolean.FALSE;
        }
        if (!Objects.equals(companyId, that.companyId) || !Objects.equals(companyId, that.companyId) ||  !Objects.equals(userId, that.userId) ) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyId, departId, userId, year, month, type, number, createTime);
    }
}