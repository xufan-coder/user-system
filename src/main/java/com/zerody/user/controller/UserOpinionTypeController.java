package com.zerody.user.controller;

import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.util.UserUtils;
import com.zerody.user.domain.UserOpinionType;
import com.zerody.user.service.UserOpinionTypeService;
import com.zerody.user.vo.UserOpinionTypeNameVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Slf4j
@RestController
@RequestMapping("/opinion-type")
public class UserOpinionTypeController {

    @Autowired
    private UserOpinionTypeService userOpinionTypeService;

    /**
     * @author kuang
     * @description 添加问题反馈分类
     * @date 2022-07-11
     **/
    @PostMapping("")
    public DataResult<Object> save(@RequestBody UserOpinionType opinionType) {
        try {
            opinionType.setCreateBy(UserUtils.getUserId());
            opinionType.setCreateTime(new Date());
            userOpinionTypeService.addOpinionType(opinionType);
            return R.success();
        } catch (Exception e) {
            log.error("新增问题反馈分类出错:{}", e, e);
            return R.error("新增问题反馈分类出错:" + e.getMessage());
        }

    }

    /**
     * @author kuang
     * @description 修改问题反馈分类
     * @date 2022-07-11
     **/
    @PutMapping("")
    public DataResult<Object> update(@RequestBody UserOpinionType opinionType) {
        try {
            if(StringUtils.isEmpty(opinionType.getId())){
                return R.error("分类id不能为空");
            }
            opinionType.setUpdateBy(UserUtils.getUserId());
            opinionType.setUpdateTime(new Date());
            userOpinionTypeService.updateOpinionType(opinionType);
            return R.success();
        } catch (Exception e) {
            log.error("修改问题反馈分类出错:{}", e, e);
            return R.error("修改问题反馈分类出错:" + e.getMessage());
        }

    }


    /**
     * @author kuang
     * @description 问题反馈分类列表
     * @date 2022-07-11
     **/
    @GetMapping("/list")
    public DataResult<List<UserOpinionTypeNameVo>> getTypeAll(){
        try {
            List<UserOpinionType> page = userOpinionTypeService.getTypeAll();
            List<UserOpinionTypeNameVo> list = new ArrayList<>();
            if(page !=null && page.size() > 0) {
                //更改成APP所需要的字段名称进行返回
                page.forEach(t->{
                    UserOpinionTypeNameVo vo = new UserOpinionTypeNameVo();
                    vo.setCode(t.getId());
                    vo.setText(t.getName());
                    list.add(vo);
                });
            }
            return R.success(list);
        } catch (Exception e) {
            log.error("获取问题反馈出错:{}", e, e);
            return R.error("获取问题反馈出错:" + e.getMessage());
        }
    }
}
