package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.domain.SysStaffInfo;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysStaffRelationService;
import com.zerody.user.vo.SysStaffInfoRelationVo;
import com.zerody.user.vo.SysStaffRelationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月10日 10:40
 */
@Slf4j
@RestController
@RequestMapping("/relation")
public class SysStaffRelationController {

    @Autowired
    private SysStaffRelationService sysStaffRelationService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    /**
     * 添加关系
     */
    @ResponseBody
    @PostMapping(value = "/add")
    public DataResult<Object> addRelation(@RequestBody SysStaffRelationDto sysStaffRelationDto) {
        try {
            this.sysStaffRelationService.addRelation(sysStaffRelationDto);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加员工关系错误:{}" + JSON.toJSONString(sysStaffRelationDto), e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 查询关系和推荐人
     */
    @GetMapping(value = "/query")
    public DataResult<SysStaffInfoRelationVo> queryRelationList(SysStaffRelationDto sysStaffRelationDto) {
        try {
            SysStaffInfoRelationVo sysStaffRelationVos = this.sysStaffRelationService.queryRelationList(sysStaffRelationDto);
            return R.success(sysStaffRelationVos);
        } catch (DefaultException e) {
            log.error("查询员工关系错误:{}" + JSON.toJSONString(sysStaffRelationDto), e);
            return R.error(e.getMessage());
        }
    }

    @GetMapping(value = "/query/{staffId}")
    public DataResult<SysStaffInfo> queryRelationListById(@PathVariable("staffId") String staffId){
        SysStaffInfo sysStaffRelationVo = this.sysStaffInfoService.getById(staffId);
        return R.success(sysStaffRelationVo);
    }
}
