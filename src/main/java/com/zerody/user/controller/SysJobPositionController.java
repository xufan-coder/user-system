package com.zerody.user.controller;

import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.pojo.SysJobPosition;
import com.zerody.user.service.SysJobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author PengQiang
 * @ClassName SysJobPositionController
 * @DateTime 2020/12/18_18:11
 * @Deacription TODO
 */
@RestController
@RequestMapping("/jobPosition")
public class SysJobPositionController {

    @Autowired
    private SysJobPositionService sysJobPositionService;

    //分页查询岗位
    @GetMapping("/getPageJob")
    public DataResult getPageJob(SysJobPositionDto sysJobPositionDto){
        return  sysJobPositionService.getPageJob(sysJobPositionDto);
    }

    //添加岗位
    @PostMapping("/addJob")
    public DataResult addJob(@RequestBody @Validated SysJobPosition sysJobPosition){

        return sysJobPositionService.addOrUpdateJob(sysJobPosition);
    }

    //修改岗位
    @PostMapping("/updateJob")
    public DataResult updateJob(@RequestBody @Validated SysJobPosition sysJobPosition){
        return sysJobPositionService.addOrUpdateJob(sysJobPosition);
    }

    //根据岗位id删除岗位
    @DeleteMapping("/deleteJobById")
    public DataResult deleteJobById(String jobId){

        return sysJobPositionService.deleteJobById(jobId);
    }
}
