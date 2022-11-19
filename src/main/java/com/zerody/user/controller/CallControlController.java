package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.CallUseControl;
import com.zerody.user.dto.CallControlPageDto;
import com.zerody.user.service.CallControlService;
import com.zerody.user.service.CallUseControlService;
import com.zerody.user.vo.CallControlVo;
import com.zerody.user.vo.CallTipsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 呼叫限制控制层
 * @author  DaBai
 * @date  2021/4/21 15:36
 */

@Slf4j
@RequestMapping("/call-control")
@RestController
public class CallControlController {


    @Autowired
    private CallControlService callControlService;
    @Autowired
    private CallUseControlService callUseControlService;


    /**
     *   添加或修改呼叫时间限制
     */
    @PostMapping("/addOrUpdate")
    public DataResult addOrUpdate(@RequestBody CallControlVo param){
        try {
            this.callControlService.addOrUpdate(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("保存呼叫限制错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("保存呼叫限制错误：{}", e, e);
            return R.error("保存呼叫限制错误" + e.getMessage());
        }
    }

    /**
     *   获取企业呼叫限制时间配置
     */
    @GetMapping("/get")
    public DataResult<CallControlVo> getByCompany(@RequestParam("companyId") String companyId) {
        try {
            CallControlVo data = this.callControlService.getByCompany(companyId);
            return R.success(data);
        } catch (Exception e) {
            log.error("获取企业呼叫限制时间配置错误:{}", e, e);
            return R.error("获取企业呼叫限制时间配置:"+e.getMessage());
        }
    }



    /**
     *   呼叫限制白名单列表【分页】
     */
    @GetMapping("/page")
    public DataResult<IPage<CallUseControl>> getPageList(CallControlPageDto pageDto) {
        try {
            IPage<CallUseControl> data = this.callUseControlService.getPageList(pageDto);
            return R.success(data);
        } catch (Exception e) {
            log.error("分页查询白名单列表出错:{}", e, e);
            return R.error("分页查询白名单列表出错:"+e.getMessage());
        }
    }


    /**
     *   添加白名单
     */
    @PostMapping("/name-list/add")
    public DataResult addNameList(@RequestBody List<String> userIds){
        try {
            this.callUseControlService.addNameList(userIds);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加白名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加白名单错误：{}", e, e);
            return R.error("添加白名单错误" + e.getMessage());
        }
    }


    /**
     *   移除白名单
     */
    @DeleteMapping("/name-list/remove/{id}")
    public DataResult removeNameList(@PathVariable String id){
        try {
            this.callUseControlService.removeNameList(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("移除白名单错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("移除白名单错误：{}", e, e);
            return R.error("添加白名单错误" + e.getMessage());
        }
    }

    /**
     *   提交呼叫次数/预警提示/超出强退
     */
    @PostMapping("/submit")
    public DataResult<CallTipsVo> submitCallControl(){
        try {
            CallTipsVo vo =this.callControlService.submitCallControl(UserUtils.getUser());
            return R.success(vo);
        } catch (DefaultException e) {
            log.error("提交呼叫次数提醒：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("提交呼叫次数错误：{}", e, e);
            return R.error("提交呼叫次数错误" + e.getMessage());
        }
    }
}
