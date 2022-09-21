package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.dto.data.DataAddDto;
import com.zerody.user.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author PengQiang
 * @ClassName DataController
 * @DateTime 2022/9/15_14:38
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService service;

    /**
     *
     *
     * @author               PengQiang
     * @description           添加键值对数据
     * @date                 2022/9/15 14:44
     * @param                param
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Void>
     */
    @PostMapping("/add")
    public DataResult<Void> addData(@Validated @RequestBody DataAddDto param) {
        try {
            param.setDataKey(param.getDataKey().trim());
            param.setUser(UserUtils.getUser());
            this.service.addData(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加键值对出错:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加键值对出错:{}", e, e);
            return R.error("添加键值对出错");
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          通过key获取value
     * @date                 2022/9/15 14:52
     * @param                key
     * @return               com.zerody.common.api.bean.DataResult<java.lang.String>
     */
    @GetMapping("/get/value/by-key/{key}")
    public DataResult<String> getValueByKey(@PathVariable("key") String key) {
        log.info("通过key获取value入参{}", key);
        try {
            String value = this.service.getValueByKey(key, UserUtils.getUser());
            return R.success(value);
        } catch (DefaultException e) {
            log.error("获取键值对校验不通过:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取键值对出错:{}", e, e);
            return R.error("获取键值对出错");
        }
    }

}
