package com.zerody.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.BirthdayBlessing;
import com.zerody.user.dto.BlessIngParam;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserBirthdayTemplateDto;
import com.zerody.user.service.BirthdayBlessingService;
import com.zerody.user.service.UnionBirthdayMonthService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

/**
 * @author kuang
 */
@RestController
@RequestMapping("/template")
@Slf4j
public class UserBirthdayTemplateController {

    @Autowired
    private UserBirthdayTemplateService service;

    @Autowired
    private BirthdayBlessingService birthdayBlessingService;

    @Autowired
    private UnionBirthdayMonthService unionBirthdayMonthService;


    /**
     * @author kuang
     * @description 添加生日祝福模板
     * @date  2022-08-20
     **/
    @PostMapping("/add")
    public DataResult<Object> addTemplate(@RequestBody UserBirthdayTemplateDto template){
        try {
            if(template.getType()== YesNo.NO){
                if(template.getMonthList() == null || template.getMonthList().size() == 0 ) {
                    return R.error("生日月份不能为空");
                }
                for(Integer month : template.getMonthList()) {
                    if( Calendar.MINUTE < month || month < Calendar.SUNDAY) {
                        return R.error("生日月份输入有误");
                    }
                }
            }
            if(template.getType()== YesNo.YES){
                if(template.getYearList() == null || template.getYearList().size() == 0 ) {
                    return R.error("入职年份不能为空");
                }
            }

            if(StringUtils.isEmpty(template.getBlessing())){
                return R.error("祝福内容不能为空");
            }
            if(template.getPushTime() == null ){
                return R.error("推送时间不能为空");
            }
            if(StringUtils.isEmpty(template.getPosterUrl())){
                return R.error("海报背景图不能为空");
            }

            this.service.addTemplate(UserUtils.getUser(),template);
            return R.success();
        } catch (DefaultException e) {
            log.error("新增模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增模板错误：{}", e, e);
            return R.error("新增模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 修改生日祝福模板
     * @date  2022-08-20
     **/
    @PutMapping("/edit")
    public DataResult<Object> modifyTemplate(@RequestBody UserBirthdayTemplateDto template){
        try {
            if(StringUtils.isEmpty(template.getId())){
                return R.error("模板id不能为空");
            }
            if(template.getType()== YesNo.NO){
                if(template.getMonthList() == null || template.getMonthList().size() == 0 ) {
                    return R.error("生日月份不能为空");
                }
                for(Integer month : template.getMonthList()) {
                    if( Calendar.MINUTE < month || month < Calendar.SUNDAY) {
                        return R.error("生日月份输入有误");
                    }
                }
            }
            if(template.getType()== YesNo.YES){
                if(template.getYearList() == null || template.getYearList().size() == 0 ) {
                    return R.error("入职年份不能为空");
                }
            }
            if(StringUtils.isEmpty(template.getBlessing())){
                return R.error("祝福内容不能为空");
            }
            if(template.getPushTime() == null){
                return R.error("推送时间不能为空");
            }
            if(StringUtils.isEmpty(template.getPosterUrl())){
                return R.error("海报背景图不能为空");
            }
            this.service.modifyTemplate(UserUtils.getUser(),template);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改模板错误：{}", e, e);
            return R.error("修改模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 查询生日模板列表
     * @date  2022-08-20
     **/
    @GetMapping("/page")
    public DataResult<Page<UserBirthdayTemplateVo>> getTemplate(TemplatePageDto queryDto){

        try {
            Page<UserBirthdayTemplateVo> page = this.service.getTemplate(queryDto);
            return R.success(page);
        } catch (DefaultException e) {
            log.error("查询模板列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询模板列表错误：{}", e, e);
            return R.error("查询模板列表错误" + e.getMessage());
        }

    }

    /**
     * @author kuang
     * @description 查询生日祝福语句列表
     * @date  2022-08-20
     **/
    @GetMapping("/blessing/list")
    public DataResult<List<BirthdayBlessing>> getBlessingList(@RequestParam("type") Integer type){
        try {
            List<BirthdayBlessing> list = this.birthdayBlessingService.getBlessingList(type);
            return R.success(list);
        } catch (DefaultException e) {
            log.error("查询祝福语句列表错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询祝福语句列表错误：{}", e, e);
            return R.error("查询祝福语句列表错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 查询已有生日模板的月份
     * @date  2022-08-20
     **/
    @GetMapping("/get/exist/month")
    public DataResult<List<String>> getExistMonthList(@RequestParam("templateId") String templateId,@RequestParam("type") Integer type){
        try {
            List<String> list = this.unionBirthdayMonthService.getExistMonthList(templateId,type);
            return R.success(list);
        } catch (DefaultException e) {
            log.error("查询已有生日模板的月份错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询已有生日模板的月份错误：{}", e, e);
            return R.error("查询已有生日模板的月份错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 根据模板id获取详情
     * @date    2022-08-20
     **/
    @GetMapping("/get/template/{templateId}")
    public DataResult<UserBirthdayTemplateVo> getTemplateInfo(@PathVariable String templateId){
        try {
            UserBirthdayTemplateVo templateVo = this.service.getTemplateInfo(templateId);
            return R.success(templateVo);
        } catch (DefaultException e) {
            log.error("获取祝福模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取祝福模板错误：{}", e, e);
            return R.error("获取祝福模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 获取生日祝福模板-APP
     * @date    2022-08-20
     **/
    @GetMapping("/get/template/info")
    public DataResult<UserBirthdayTemplateVo> getTemplateInfo(){
        try {
            UserBirthdayTemplateVo templateVo = this.service.getTemplateInfo();
            return R.success(templateVo);
        } catch (DefaultException e) {
            log.error("获取生日祝福模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取生日祝福模板错误：{}", e, e);
            return R.error("获取生日祝福模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 获取同事生日祝福模板-APP
     * @date  2022-08-20
     **/
    @GetMapping("/get/notice/info")
    public DataResult<UserBirthdayTemplateVo> getNoticeInfo(@RequestParam("userId") String userId){
        try {
            UserBirthdayTemplateVo templateVo = this.service.getNoticeInfo(userId);
            return R.success(templateVo);
        } catch (DefaultException e) {
            log.error("获取同事生日祝福模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取同事生日祝福模板错误：{}", e, e);
            return R.error("获取同事生日祝福模板错误" + e.getMessage());
        }
    }


    /**
     * @author kuang
     * @description 删除生日模板
     * @date  2022-08-20
     **/
    @DeleteMapping("/del/{id}")
    public DataResult<Object> modifyTemplateById(@PathVariable String id){
        try {

            this.service.modifyTemplateById(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("删除模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除模板错误：{}", e, e);
            return R.error("删除模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 批量删除生日模板
     * @date  2022-08-20
     **/
    @DeleteMapping("/batch/del")
    public DataResult<Object> modifyTemplate(@RequestBody List<String> ids){
        try {

            this.service.modifyTemplate(ids);
            return R.success();
        } catch (DefaultException e) {
            log.error("批量删除模板错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量删除模板错误：{}", e, e);
            return R.error("批量删除模板错误" + e.getMessage());
        }
    }

    /**
     * @author kuang
     * @description 获取今天是否生日
     * @date  2022-08-20
     **/
    @GetMapping("/get/whether-birthday")
    public DataResult<Object> whetherBirthday(@RequestParam(required = false) String userId){

        try {
            if(StringUtils.isEmpty(userId)) {
                userId = UserUtils.getUserId();
            }
            return R.success(this.service.whetherBirthday(userId));
        } catch (DefaultException e) {
            log.error("获取今天是否生日错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取今天是否生日错误：{}", e, e);
            return R.error("获取今天是否生日错误" + e.getMessage());
        }
    }
    /**
    *
    *  @description   获取今天是否入职周年
    *  @author        YeChangWei
    *  @date          2022/11/16 19:00
    *  @return        com.zerody.common.api.bean.DataResult<java.lang.Object>
    */
    @GetMapping("/get/whether-entry")
    public DataResult<Object> whetherEntry(){
        try {
            return R.success(this.service.whetherEntry(UserUtils.getUserId()));
        } catch (DefaultException e) {
            log.error("获取今天是否入职周年错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取今天是否入职周年错误：{}", e, e);
            return R.error("获取今天是否入职周年错误！");
        }
    }

    /**
     * @author kuang
     * @description 发送生日祝福
     * @date
     * @Param
     **/
    @PostMapping("/add/blessing")
    public DataResult<Object> addBlessing(@RequestBody BlessIngParam param){

        try {
            param.setUserId(UserUtils.getUserId());
            this.service.addBlessing(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("发送生日祝福错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("发送生日祝福错误：{}", e, e);
            return R.error("发送生日祝福错误" + e.getMessage());
        }
    }
}
