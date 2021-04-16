package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 *
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/1/19 14:53
 * @param
 * @return
 */
@Data
public class SysJobPosition  extends BaseModel {

    /** 企业id **/
    private String compId;

    /** 岗位名称 **/
    @NotEmpty(message = "岗位名称不能为空")
    private String positionName;

    /** 职责范围 **/
    private String jobScope;

    /** 父级岗位id **/
    private String parentId;

    /** 岗位级别 **/
    private Integer level;

    private String departId;

    /** 备注/描述 **/
    private String positionDesc;
}