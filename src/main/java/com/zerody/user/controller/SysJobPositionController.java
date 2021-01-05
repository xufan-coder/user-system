package com.zerody.user.controller;

import com.zerody.common.api.bean.R;
import com.zerody.common.bean.DataResult;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.domain.SysJobPosition;
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
@RequestMapping("/position")
public class SysJobPositionController {

    @Autowired
    private SysJobPositionService sysJobPositionService;

    /**
    *    按部门查询岗位
    */
    @GetMapping("/get")
    public DataResult getJob(@RequestParam(value = "departId") String departId){
        return  new DataResult(sysJobPositionService.getJob(departId));
    }

    /**
    *    添加岗位
    */
    @PostMapping("/add")
    public DataResult addJob(@RequestBody @Validated SysJobPosition sysJobPosition){
         return  sysJobPositionService.addJob(sysJobPosition);
    }

    /**
    *    修改岗位
    */
    @PutMapping("/update")
    public DataResult updateJob(@RequestBody @Validated SysJobPosition sysJobPosition){
        return sysJobPositionService.updateJob(sysJobPosition);
    }

    /**
    *    根据岗位id删除岗位
    */
    @DeleteMapping("/delete/{id}")
    public DataResult deleteJobById(@PathVariable(value = "id") String id){
        return sysJobPositionService.deleteJobById(id);
    }
}
