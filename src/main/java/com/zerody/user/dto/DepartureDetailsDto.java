package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2023/5/3 18:23
 */
@Data
public class DepartureDetailsDto extends PageQueryDto {

    /**
     * 企业ID
     */
    private String companyId;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 是否部门（0 是部门 1团队）
     */
    private Integer isDepartment;

    /**
     * 关联企业隔离数据
     */
    private List<String> companyIds;

    /**
     * 状态:签约中、1离职、2删除 3合作
     */
    private Integer status;

    /**
     * 查询伙伴状态  0-查询有效伙伴   1-查询含离职伙伴
     */
    private Integer isShowLeave;

    /**
     * 搜索名称
     */
    private String searchName;

    /**
     * 伙伴角色(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private Integer userType;

    /**
     * 离职类型id集合
     */
    private List<String> leaveTypeIds;

    /**
     * 起始时间
     */
    private String begin;

    /**
     * 结束时间
     */
    private String end;

    /**
     * 时间数组
     */
    private List<String> time;

}
