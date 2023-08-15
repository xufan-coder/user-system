package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.constant.RescindType;
import com.zerody.user.domain.Dict;
import com.zerody.user.enums.DictTypeEnum;
import com.zerody.user.service.DictService;
import com.zerody.user.vo.dict.DictQuseryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName DictController
 * @DateTime 2022/4/28_11:05
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService service;

    /**
     *
     *
     * @author               PengQiang
     * @description          获取字典
     * @date                 2022/4/28 11:13
     * @param                type
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.dict.DictQuseryVo>>
     */
    @GetMapping("/get/by-type")
    public DataResult<List<DictQuseryVo>> getListByType(@RequestParam("type") String type) {
        try {
            type = DictTypeEnum.getCodeByAlias(type);
            if (StringUtils.isEmpty(type)) {
                return R.error("字典类型错误");
            }
            List<DictQuseryVo> dict = this.service.getListByType(type);
            return R.success(dict);
        } catch (DefaultException e) {
            log.error("获取字典异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取字典异常：{}", e, e);
            return R.error("获取字典异常!请联系管理员");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取字典
     * @date                 2022/4/28 11:13
     * @param                type
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.dict.DictQuseryVo>>
     */
    @PostMapping("/add")
    public DataResult<Void> getListByType(@RequestBody List<Dict> entity) {
        try {

            this.service.addDict(entity);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加字典异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加字典异常：{}", e, e);
            return R.error("添加字典异常!请联系管理员");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: 获取个人解约申请原因
     * @Date: 2023/7/7 15:52
     */
    @GetMapping("/individual/rescind/reason")
    public DataResult<List<DictQuseryVo>> rescindReason() {
        try {
            return R.success(this.service.rescindReason());
        } catch (DefaultException e) {
            log.error("获取个人解约申请原因出错:{}", e.getMessage());
            return R.error("获取个人解约申请原因出错");
        } catch (Exception e) {
            log.error("获取个人解约申请原因出错:{}", e, e);
            return R.error("获取个人解约申请原因出错");
        }
    }

}
