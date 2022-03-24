package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;

import com.zerody.user.dto.AdvertisingUpdateDto;
import com.zerody.user.dto.BannerCreateDto;
import com.zerody.user.dto.BannerListDto;
import com.zerody.user.service.BannerService;
import com.zerody.user.vo.BannerListVo;
import com.zerody.user.vo.BannerVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
public class BannerController {

    private final BannerService BannerService;

    @ApiOperation("添加banner")
    @PostMapping
    public DataResult<Void> create(@RequestBody @Validated BannerCreateDto param){
        BannerService.create(param);
        return R.success();
    }

    @ApiOperation("分页查询banner")
    @GetMapping
    public DataResult<IPage<BannerListVo>> pageAd(BannerListDto param, PageQueryDto pageParam){
        IPage<BannerListVo> result = BannerService.pageAd(param, pageParam);
        return R.success(result);
    }

    @ApiOperation("分页查询banner")
    @GetMapping(value = "/pageApp")
    public DataResult<IPage<BannerListVo>> pageApp(BannerListDto param, PageQueryDto pageParam){
        IPage<BannerListVo> result = BannerService.pageApp(param, pageParam);
        return R.success(result);
    }

    @ApiOperation("查询banner详情")
    @GetMapping("/{id}")
    public DataResult<BannerVo> detail(@PathVariable("id") String id){
        BannerVo result = BannerService.detail(id);
        return R.success(result);
    }

    @ApiOperation("更新banner")
    @PutMapping("/{id}")
    public DataResult<Void> update(@PathVariable("id") String id,@RequestBody @Validated AdvertisingUpdateDto param){
        BannerService.update(id, param);
        return R.success();
    }

    @ApiOperation("删除banner")
    @DeleteMapping("/{id}")
    public DataResult<Void> delete(@PathVariable("id") String id){
        BannerService.delete(id);
        return R.success();
    }

}
