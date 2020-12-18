package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    private String parentId;

    //岗位级别
    private Integer level;

    //部门id
    private String departId;

}