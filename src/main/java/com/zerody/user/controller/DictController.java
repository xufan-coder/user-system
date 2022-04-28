package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.enums.DictTypeEnum;
import com.zerody.user.service.DictService;
import com.zerody.user.vo.dict.DictQuseryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
