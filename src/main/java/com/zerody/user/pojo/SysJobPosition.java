package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysJobPosition  extends BaseModel {

    //企业id
    private String compId;

    //岗位名称
    @NotEmpty(message = "岗位名称不能为空")
    private String positionName;

    //职责范围
    private String jobScope;

    //父级岗位id
    @NotNull(message = "父级id不能为null")
    private String parentId;

    //岗位级别
    private Integer level;

    private String departId;

    //备注/描述
    private String positionDesc;
}