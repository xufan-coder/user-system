package com.zerody.user.controller.second;

import com.alibaba.fastjson.JSON;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.DateUtil;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.SysCompanyInfo;
import com.zerody.user.dto.SecondStaffDto;
import com.zerody.user.dto.StaffByCompanyDto;
import com.zerody.user.service.SysAddressBookService;
import com.zerody.user.service.SysCompanyInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.DateUtils;
import com.zerody.user.vo.StaffInfoByAddressBookVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2023/11/17 16:14
 */
@Slf4j
@RestController
@RequestMapping("/second")
public class AnalyzeController {

    @Autowired
    private SysAddressBookService sysAddressBookService;

    @Autowired
    private CheckUtil checkUtil;


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 查询伙伴档案二级页面
    * @Date: 2023/11/17 16:15
    */
    @GetMapping(value = "/get/by-company")
    public DataResult<List<StaffInfoByAddressBookVo>> getUserArchives(SecondStaffDto dto) {
        String addBeginTime = DateUtils.convertTime(dto.getAddBeginTime());
        String addEndTime = DateUtils.convertTime(dto.getAddEndTime());
        dto.setAddBeginTime(addBeginTime);
        dto.setAddEndTime(addEndTime);
        //log.info("签约开始时间 {}，签约结束时间 {}", addBeginTime, addEndTime);
        String removeBeginTime = DateUtils.convertTime(dto.getRemoveBeginTime());
        String removeEndTime = DateUtils.convertTime(dto.getRemoveEndTime());
        dto.setRemoveBeginTime(removeBeginTime);
        dto.setRemoveEndTime(removeEndTime);
        /*log.info("解约开始时间 {}，解约结束时间 {}", removeBeginTime, removeEndTime);
        log.info("查询伙伴档案二级页面入参 {}", JSON.toJSONString(dto));*/
        try {
            this.checkUtil.SetUserPositionInfo(dto);
            log.info("查询伙伴档案二级页面赋值入参 {}", JSON.toJSONString(dto));
            return R.success(sysAddressBookService.getUserArchives(dto));
        } catch (DefaultException e) {
            log.error("查询伙伴档案二级页面错误:{}", e.getMessage());
            return R.error("查询伙伴档案二级页面,请求异常");
        } catch (Exception e) {
            log.error("查询伙伴档案二级页面错误:{}", e, e);
            return R.error("查询伙伴档案二级页面,请求异常");
        }
    }

}
