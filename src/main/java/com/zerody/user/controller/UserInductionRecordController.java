package com.zerody.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserInductionPage;
import com.zerody.user.service.UserInductionRecordService;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import com.zerody.user.vo.UserInductionRecordInfoVo;
import com.zerody.user.vo.UserInductionRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kuang
 * 入职申请记录
 */
@RestController
@RequestMapping("/induction")
@Slf4j
public class UserInductionRecordController {

    @Autowired
    private UserInductionRecordService inductionRecordService;

    /**
     * @author kuang
     * @description 入职申请列表
     * @date  2022-08-20
     **/
    @GetMapping("/page")
    public DataResult<Page<UserInductionRecordVo>> getInductionPage(UserInductionPage queryDto){

        try {
            queryDto.setUserId(queryDto.getUserId());
            Page<UserInductionRecordVo> page = this.inductionRecordService.getInductionPage(queryDto);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("查询入职申请列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询入职申请列表错误：{}", e, e);
            return R.error("查询入职申请列表错误" + e.getMessage());
        }

    }

    /**
     * @author kuang
     * @description 入职申请信息
     **/
    @GetMapping("/info")
    public DataResult<UserInductionRecordInfoVo> getInductionInfo(@RequestParam("id") String id){

        try {
            UserInductionRecordInfoVo infoVo = this.inductionRecordService.getInductionInfo(id);
            return R.success(infoVo);
        } catch (DefaultException e) {
            log.error("查询入职申请信息错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询入职申请信息错误：{}", e, e);
            return R.error("查询入职申请信息错误" + e.getMessage());
        }

    }


}
