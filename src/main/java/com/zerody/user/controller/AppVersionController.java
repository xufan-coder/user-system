package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.api.bean.R;
import com.zerody.user.domain.AppVersion;
import com.zerody.user.dto.AppVersionCreateDto;
import com.zerody.user.dto.AppVersionListDto;
import com.zerody.user.dto.AppVersionUpdateDto;
import com.zerody.user.service.AppVersionService;
import com.zerody.user.vo.AppVersionListVo;
import com.zerody.user.vo.AppVersionVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app-versions")
public class AppVersionController {

    @Autowired
    private AppVersionService AppVersionervice;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("新增发布版本")
    @PostMapping
    public DataResult<Void> create(@RequestBody @Validated AppVersionCreateDto param) {
        AppVersionervice.createAppVersion(param);
        return R.success();
    }

    @ApiOperation("修改发布版本")
    @PostMapping("/{id}")
    public DataResult<Void> update(@PathVariable("id") String id, @RequestBody @Validated AppVersionUpdateDto param) {
        AppVersionervice.updateAppVersion(id, param);
        return R.success();
    }

    @ApiOperation("分页查询App版本")
    @GetMapping
    public DataResult<IPage<AppVersionListVo>> pageVersion(AppVersionListDto param,PageQueryDto pageParam) {
        Page<AppVersionListVo> result = AppVersionervice.pageVersion(param, pageParam);
        return R.success(result);
    }

    @ApiOperation("查询App版本详情")
    @GetMapping("/{id}")
    public DataResult<AppVersionVo> detail(@PathVariable("id") String id) {
        AppVersionVo result = AppVersionervice.detail(id);
        return R.success(result);
    }

    @ApiOperation("删除查询App版本")
    @DeleteMapping("/{id}")
    public DataResult<Void> delete(@PathVariable("id") String id) {
        AppVersionervice.deleteVersion(id);
        return R.success();
    }

    @ApiOperation("查询App版本")
    @GetMapping(value = "/query-detail")
    public DataResult<List<AppVersion>> queryDetail(AppVersionListDto appVersionDetatilDto) {
        List<AppVersion> appVersionVo = this.AppVersionervice.queryDetail(appVersionDetatilDto);
        return R.success(appVersionVo);
    }

    @ApiOperation("根据版本号查询")
    @GetMapping(value = "/query-version")
    public DataResult<List<AppVersion>> queryVersion(@Validated AppVersionListDto appVersionDetatilDto) {
        List<AppVersion> appVersionVo = this.AppVersionervice.queryVersion(appVersionDetatilDto);
        return R.success(appVersionVo);
    }


    @ApiOperation("查询App最新版本-官网")
    @GetMapping(value = "/query-newest")
    public DataResult<AppVersion> queryNewestVersion(AppVersionListDto appVersionListDto, ServletResponse res) {
        //解决跨域问题
        String key = "VERSION:APPVERSION";
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //查询
        AppVersion appVersion = this.AppVersionervice.queryNewestVersion(appVersionListDto);
        JSONObject redis = JSON.parseObject(String.valueOf(redisTemplate.opsForValue().get(key + appVersion.getVersion() + appVersion.getOsType())));
        AppVersion appVersion1 = JSONObject.toJavaObject(redis, AppVersion.class);
        if (!appVersion.equals(appVersion1)) {
            this.redisTemplate.opsForValue().set(key + appVersion.getVersion() + appVersion.getOsType(), JSON.toJSONString(appVersion));
            appVersion1 = JSONObject.toJavaObject(JSON.parseObject(String.valueOf(redisTemplate.opsForValue().get(key + appVersion.getVersion() + appVersion.getOsType()))), AppVersion.class);
        }
        return R.success(appVersion1);
    }
}
