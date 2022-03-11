package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author               PengQiang
 * @description          家庭成员实体
 * @date                 2022/3/3 14:33
 */
@Data
public class FamilyMember {

    /** 家庭成员id */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 姓名 */
    private String name;

    /** 号码 */
    private String mobile;

    /** 关系 */
    private String relationship;

    /** 职位 */
    private String profession;

    /** 联系地址 */
    private String contactAddress;

    /** 创建时间 */
    private Date createTime;

    /** 排序号 升序 */
    private Integer orderNum;

    /** 用户id */
    private String userId;

    /** 员工id */
    private String staffId;
}