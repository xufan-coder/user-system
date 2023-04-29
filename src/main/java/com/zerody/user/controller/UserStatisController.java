package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.TimeOperate;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.statis.UserStatisQueryDto;
import com.zerody.user.service.UserStatisService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.statis.UserTrendQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisController
 * @DateTime 2023/4/29 10:55
 */

@Slf4j
@RestController
@RequestMapping("/user-statis")
public class UserStatisController {

    @Autowired
    private UserStatisService service;

    @Autowired
    private CheckUtil checkUtil;


    /**
     *
     * 伙伴趋势统计
     * @author PengQiang
     * @description 伙伴趋势统计
     * @date 2023/4/29 11:39
     * @param param 请求参数
     * @return com.zerody.common.api.bean.DataResult<java.util.List < com.zerody.user.vo.statis.UserTrendQueryVo>>
     */
    @GetMapping("/user-trends")
    public DataResult<List<UserTrendQueryVo>> getUserTrendS(UserStatisQueryDto param) {
        try {
            if (DataUtil.isEmpty(param.getTimePeriod())) {
                param.setTimePeriod(TimeOperate.DAY.name());
            }
            this.checkUtil.SetUserPositionInfo(param);
            List<UserTrendQueryVo> list = this.service.getUserTrendS(param);
            return R.success(list);
        } catch (DefaultException e) {
            log.warn("伙伴趋势统计校验:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.warn("伙伴趋势统计校验:{}", e, e);
            return R.error("获取趋势统计出错!请联系管理员");
        }
    }

}
