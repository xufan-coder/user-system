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
    private SysCompanyInfoService sysCompanyInfoService;
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
        /*String addBeginTime = DateUtil.getDateTimeH24(dto.getAddBeginTime());
        String addEndTime = DateUtil.getDateTimeH24(dto.getAddBeginTime());
        log.info("签约开始时间 {}，签约结束时间 {}",addBeginTime, addEndTime);
        String removeBeginTime = DateUtil.getDateTimeH24(dto.getRemoveBeginTime());
        String removeEndTime = DateUtil.getDateTimeH24(dto.getRemoveEndTime());
        log.info("解约开始时间 {}，解约结束时间 {}",removeBeginTime, removeEndTime);*/

        String addBeginTime = convertTime(dto.getAddBeginTime());
        String addEndTime = convertTime(dto.getAddEndTime());
        dto.setAddBeginTime(addBeginTime);
        dto.setAddEndTime(addEndTime);
        log.info("签约开始时间 {}，签约结束时间 {}",addBeginTime, addEndTime);
        String removeBeginTime = convertTime(dto.getRemoveBeginTime());
        String removeEndTime = convertTime(dto.getRemoveEndTime());
        dto.setRemoveBeginTime(removeBeginTime);
        dto.setRemoveEndTime(removeEndTime);
        log.info("解约开始时间 {}，解约结束时间 {}",removeBeginTime, removeEndTime);
        log.info("查询伙伴档案二级页面入参 {}", JSON.toJSONString(dto));
        try {
            this.checkUtil.SetUserPositionInfo(dto);
            log.info("查询伙伴档案二级页面赋值入参 {}", JSON.toJSONString(dto));
            return R.success(sysAddressBookService.getUserArchives(dto));
        } catch (DefaultException e) {
            log.error("查询伙伴档案二级页面错误:{}", JSON.toJSONString(dto), e.getMessage());
            return R.error("查询伙伴档案二级页面,请求异常");
        } catch (Exception e) {
            log.error("查询伙伴档案二级页面错误:{}", e, e);
            return R.error("查询伙伴档案二级页面,请求异常");
        }
    }


    public String convertTime(String dateString){
        if (DataUtil.isNotEmpty(dateString)) {
            // 使用 DateTimeFormatter 解析字符串为 LocalDate 对象，并格式化输出
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
            LocalDate localDate = LocalDate.parse(dateString, inputFormatter);

            // 使用另一个格式化器来输出日期字符串，确保月份显示为两位数
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return localDate.format(outputFormatter);
        } else {
            return null;
        }

    }

    public static void main(String[] args) {
        // 输入的日期字符串
        String dateString = "2022-3-01";

        // 使用 DateTimeFormatter 解析字符串为 LocalDate 对象，并格式化输出
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
        LocalDate localDate = LocalDate.parse(dateString, inputFormatter);

        // 使用另一个格式化器来输出日期字符串，确保月份显示为两位数
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDate.format(outputFormatter);

        System.out.println("格式化后的日期字符串: " + formattedDate);
    }

}
