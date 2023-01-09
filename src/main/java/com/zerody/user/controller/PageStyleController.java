package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.domain.PageStyle;
import com.zerody.user.dto.PageStyleDto;
import com.zerody.user.service.PageStyleService;
import com.zerody.user.vo.PageStyleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:36
 */
@Slf4j
@RestController
@RequestMapping("/page-style")
public class PageStyleController {

    @Autowired
    private PageStyleService pageStyleService;

    /**
    *
    *  @description   编辑页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 9:55
    *  @return        com.zerody.common.api.bean.DataResult<java.lang.Object>
    */
    @PutMapping("/update")
    public DataResult<Object> updatePageStyle(@RequestBody @Valid PageStyleDto dto){
        try {
            if(StringUtils.isEmpty(dto.getId())){
                return R.error("id不能为空");
            }
            if(DataUtil.isEmpty(dto.getState())){
                return R.error("状态不能为空");
            }
            if(DataUtil.isEmpty(dto.getStartTime())){
                return R.error("生效时间不能为空");
            }
            if(DataUtil.isEmpty(dto.getEndTime())){
                return R.error("结束时间不能为空");
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(format.parse(dto.getEndTime()).before(format.parse(dto.getStartTime()))){
                return R.error("失效时间不能小于生效时间");
            }
            this.pageStyleService.updatePageStyle(dto);
            return R.success();
        } catch (DefaultException e) {
            log.error("编辑页面风格错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("编辑页面风格错误：{}", e, e);
            return R.error("编辑页面风格错误！");
        }
    }

    /**
    *
    *  @description   查询页面风格详情
    *  @author        YeChangWei
    *  @date          2022/12/30 11:57
    *  @return        com.zerody.common.api.bean.DataResult<com.zerody.user.vo.PageStyleVo>
    */
    @GetMapping("/get/info/{id}")
    public DataResult<PageStyleVo> getPageStyleInfo(@PathVariable String id){
        try {
            PageStyleVo pageStyleVo = this.pageStyleService.getPageStyleInfo(id);
            return R.success(pageStyleVo);
        } catch (DefaultException e) {
            log.error("查询页面风格详情错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询页面风格详情错误：{}", e, e);
            return R.error("查询页面风格详情错误！");
        }
    }
    /**
    *
    *  @description   获取当前时段的页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 13:44
    *  @return        com.zerody.common.api.bean.DataResult<java.lang.String>
    */
    @GetMapping("/get-now")
    public DataResult<PageStyle> getNowPageStyle(){
        try {
            PageStyle pageStyle = this.pageStyleService.getNowPageStyle();
            return R.success(pageStyle);
        } catch (DefaultException e) {
            log.error("获取当前时段的页面风格错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取当前时段的页面风格错误：{}", e, e);
            return R.error("获取当前时段的页面风格错误！");
        }
    }
    /**
    *
    *  @description    分页查询所有页面风格
    *  @author        YeChangWei
    *  @date          2022/12/30 16:08
    *  @return        com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.PageStyleVo>>
    */
    @GetMapping("/get-page")
    public DataResult<IPage<PageStyleVo>> getAllPageStyle(PageQueryDto dto){
        try {
            IPage<PageStyleVo> styleVoIPage = this.pageStyleService.getAllPageStyle(dto);
            return R.success(styleVoIPage);
        } catch (DefaultException e) {
            log.error("分页查询所有页面风格错误：{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("分页查询所有页面风格错误：{}", e, e);
            return R.error("分页查询所有页面风格错误！");
        }
    }

}
