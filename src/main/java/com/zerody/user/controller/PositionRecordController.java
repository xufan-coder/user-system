package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.service.PositionRecordService;
import com.zerody.user.vo.CustomerQueryDimensionalityVo;
import com.zerody.user.vo.PositionRecordListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/position-record")
public class PositionRecordController {

    @Autowired
    private PositionRecordService service;

    /***
     * @description 查询任职记录
     * @author luolujin
     * @date 2022/11/8
     * @return
     */
    @GetMapping("/get/position/{certificateCard}")
    public DataResult<List<PositionRecordListVo>> queryPositionRecord(@PathVariable("certificateCard") String certificateCard) {
        try {
            List<PositionRecordListVo> vos = this.service.queryPositionRecord(certificateCard);
            return R.success(vos);
        } catch (DefaultException e){
            log.error("查询任职记录错误!:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("查询任职记录错误!:{}", e , e);
            return R.error(e.getMessage());
        }
    }

    /***
     * @description 查询任职记录
     * @author luolujin
     * @date 2022/11/8
     * @return
     */
    @GetMapping("/get/list/{userId}")
    public DataResult<List<PositionRecordListVo>> getPositionRecord(@PathVariable("userId") String userId) {
        try {
            List<PositionRecordListVo> vos = this.service.getPositionRecord(userId);
            return R.success(vos);
        } catch (DefaultException e){
            log.error("查询任职记录错误!:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("查询任职记录错误!:{}", e , e);
            return R.error(e.getMessage());
        }
    }
}
