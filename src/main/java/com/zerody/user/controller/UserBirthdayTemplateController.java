package com.zerody.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.contract.api.vo.SignOrderDataVo;
import com.zerody.user.domain.BirthdayBlessing;
import com.zerody.user.domain.UserBirthdayTemplate;
import com.zerody.user.dto.BlessIngParam;
import com.zerody.user.dto.TemplatePageDto;
import com.zerody.user.dto.UserBirthdayTemplateDto;
import com.zerody.user.feign.ContractFeignService;
import com.zerody.user.feign.CustomerFeignService;
import com.zerody.user.mapper.SysDepartmentInfoMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.BirthdayBlessingService;
import com.zerody.user.service.UnionBirthdayMonthService;
import com.zerody.user.service.UserBirthdayTemplateService;
import com.zerody.user.vo.AppUserNotPushVo;
import com.zerody.user.vo.UserBirthdayTemplateVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
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


    @Autowired
    private UserBirthdayTemplateService userBirthdayTemplateService;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysDepartmentInfoMapper sysDepartmentInfoMapper;
    @Autowired
    private ContractFeignService contractFeignService;
    @Autowired
    private CustomerFeignService customerFeignService;



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

    private AppUserNotPushVo getEntryPullData(String userId){

        //查询当前入职周年的信息
        List<AppUserNotPushVo> lists = sysUserInfoMapper.getAnniversaryUserList(userId);
        AppUserNotPushVo entryData;
        if(lists.size() == 0) {
           throw new DefaultException("查找周年庆错误");
        }
        entryData = lists.get(0);
        //获取模板
        UserBirthdayTemplate template = userBirthdayTemplateService.getEntryTimeTemplate(entryData.getNum().toString(),new Date(), YesNo.YES);
        if(template == null) {
            throw new DefaultException("未配置"+entryData.getNum()+"周年模板");
        }
        entryData.setBlessing(template.getBlessing());
        entryData.setPosterUrl(template.getPosterUrl());
        if (StringUtils.isNotEmpty(entryData.getCompanyId())) {
            //总经理 查询该企业年统计量

            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(entryData.getCompanyId(), null, null);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(entryData.getCompanyId(), null, null);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }
        } else if (StringUtils.isNotEmpty(entryData.getDepartmentId())) {
            //副总或团队长  查询该部门年统计量

            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, entryData.getDepartmentId(), null);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, entryData.getDepartmentId(), null);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }

        }else {
            //伙伴数据
            //查询签单数量 和放款金额 和放款数
            DataResult<SignOrderDataVo> signOrderData = contractFeignService.getSignOrderData(null, null, userId);
            if (signOrderData.isSuccess()) {
                entryData.setSignOrderNum(signOrderData.getData().getSignOrderNum());
                entryData.setLoansMoney(signOrderData.getData().getLoansMoney());
                entryData.setLoansNum(signOrderData.getData().getLoansNum());
            }
            //查询录入客户数量
            DataResult<Integer> importCustomerNum = customerFeignService.getImportCustomerNum(null, null, userId);
            if (importCustomerNum.isSuccess()) {
                entryData.setImportCustomerNum(importCustomerNum.getData());
            }
        }
        entryData.setContent("亲爱的"+entryData.getUserName()+"小微集团祝您签约"+entryData.getNum()+"快乐");
        return entryData;
    }
}
