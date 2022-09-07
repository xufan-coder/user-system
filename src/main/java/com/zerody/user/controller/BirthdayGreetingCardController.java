package com.zerody.user.controller;


import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.BirthdayGreetingCard;
import com.zerody.user.dto.BirthdayGreetingCardDto;
import com.zerody.user.service.BirthdayGreetingCardService;
import com.zerody.user.vo.BirthdayGreetingCardVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

/**
 * @author kuang
 */
@RestController
@RequestMapping("/greeting")
@Slf4j
public class BirthdayGreetingCardController {

    @Autowired
    private BirthdayGreetingCardService birthdayGreetingCardService;

    /**
     * @author kuang
     * @description 添加贺卡模板
     * @date  2022-08-20
     **/
    @PostMapping("/add")
    public DataResult<Object> addGreeting(@RequestBody BirthdayGreetingCardDto cardDto){
        try {
            if(StringUtils.isEmpty(cardDto.getCardUrl())) {
                return R.error("贺卡图片不能为空");
            }
            this.birthdayGreetingCardService.addGreeting(UserUtils.getUser(),cardDto);
            return R.success();
        } catch (DefaultException e) {
            log.error("新增贺卡图片错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增贺卡图片错误：{}", e, e);
            return R.error("新增贺卡图片错误:" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 修改贺卡模板
     * @date  2022-08-20
     **/
    @PutMapping("/edit")
    public DataResult<Object> modifyGreeting(@RequestBody BirthdayGreetingCardDto cardDto){
        try {
            if(StringUtils.isEmpty(cardDto.getId())) {
                return R.error("id不能为空");
            }
            this.birthdayGreetingCardService.modifyTemplate(cardDto);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改贺卡图片错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改贺卡图片错误：{}", e, e);
            return R.error("修改贺卡图片错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 查询贺卡列表
     * @date  2022-08-20
     **/
    @GetMapping("/list")
    public DataResult<List<BirthdayGreetingCardVo>> getGreetingList(){

        try {
            List<BirthdayGreetingCardVo> page = this.birthdayGreetingCardService.getGreetingList(null);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("查询贺卡列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询贺卡列表错误：{}", e, e);
            return R.error("查询贺卡列表错误" + e.getMessage());
        }

    }

    /**
     * @author kuang
     * @description 查询仅启用贺卡列表
     * @date  2022-08-20
     **/
    @GetMapping("/open/list")
    public DataResult<List<BirthdayGreetingCardVo>> getGreetingOpenList(){

        try {
            List<BirthdayGreetingCardVo> page = this.birthdayGreetingCardService.getGreetingList(YesNo.YES);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("查询贺卡列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询贺卡列表错误：{}", e, e);
            return R.error("查询贺卡列表错误" + e.getMessage());
        }

    }

    /**
     * @author kuang
     * @description 删除贺卡
     * @date  2022-08-20
     **/
    @DeleteMapping("/del/{id}")
    public DataResult<Object> modifyGreetingById(@PathVariable String id){
        try {
            this.birthdayGreetingCardService.modifyGreetingById(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("删除贺卡图片错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除贺卡图片错误：{}", e, e);
            return R.error("删除贺卡图片错误" + e.getMessage());
        }
    }

}
