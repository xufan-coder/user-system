package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.constant.YesNo;
import com.zerody.user.domain.AppVersionInfo;
import com.zerody.user.dto.AppVersionInfoAddDto;
import com.zerody.user.dto.AppVersionInfoListDto;
import com.zerody.user.dto.AppVersionInfoModifyDto;
import com.zerody.user.dto.AppVersionInfoPageDto;
import com.zerody.user.service.AppVersionInfoService;
import com.zerody.user.vo.AppVersionInfoPageVo;
import com.zerody.user.vo.AppVersionInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年12月16日 15:27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app-version-info")
public class AppVersionInfoController {
    @Autowired
    private AppVersionInfoService appVersionInfoService;

    /***
     * @description 添加
     * @author zhangpingping
     * @date 2021/12/16
     * @param [param]
     * @return
     */
    @PostMapping("/add")
    public DataResult<Object> addAppVersionInfo(@Validated @RequestBody AppVersionInfoAddDto param) {
        this.appVersionInfoService.addAppVersionInfo(param);
        return R.success();
    }

    /***
     * @description 修改
     * @author zhangpingping
     * @date 2021/12/16
     * @param [param]
     * @return
     */
    @PutMapping("/modify")
    public DataResult<Object> modifyAppVersionInfo(@Validated @RequestBody AppVersionInfoModifyDto param) {
        this.appVersionInfoService.modifyAppVersionInfo(param);
        return R.success();
    }

    /***
     * @description 删除
     * @author zhangpingping
     * @date 2021/12/16
     * @param [id]
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public DataResult<Object> removeAppVersionInfo(@PathVariable("id") String id) {
        this.appVersionInfoService.removeAppVersionInfo(id);
        return R.success();
    }

    /***
     * @description 分页查询
     * @author zhangpingping
     * @date 2021/12/16
     * @param [param]
     * @return
     */
    @GetMapping("/page")
    public DataResult<IPage<AppVersionInfoPageVo>> queryAppVersionInfoPage(AppVersionInfoPageDto param) {
        IPage<AppVersionInfoPageVo> appVersionInfoPageVoIPage = this.appVersionInfoService.queryAppVersionInfoPage(param);
        return R.success(appVersionInfoPageVoIPage);
    }

    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/12/16
     * @param [param]
     * @return
     */
    @GetMapping("/query")
    public DataResult<IPage<AppVersionInfoPageVo>> queryAppVersionInfoList(AppVersionInfoListDto param) {
        IPage<AppVersionInfoPageVo> appVersionInfoPageVoIPage = this.appVersionInfoService.queryAppVersionInfoList(param);
        return R.success(appVersionInfoPageVoIPage);
    }


    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/12/16
     * @param [param]
     * @return
     */
    @GetMapping("/query/list")
    public DataResult<List<AppVersionInfoPageVo>> queryAppVersionInfoArrayList(AppVersionInfoListDto param) {
        List<AppVersionInfoPageVo> appVersionInfoPageVoIPage = this.appVersionInfoService.queryAppVersionInfoArrayList(param);
        return R.success(appVersionInfoPageVoIPage);
    }


    /***
     * @description 查询App
     * @author zhangpingping
     * @date 2021/12/21
     * @param [param]
     * @return
     */
    @GetMapping("/page-app")
    public DataResult<IPage<AppVersionInfoPageVo>> queryAppVersionInfoAppPage(AppVersionInfoPageDto param) {
        IPage<AppVersionInfoPageVo> appVersionInfoPageVoIPage = this.appVersionInfoService.queryAppVersionInfoPage(param);
        return R.success(appVersionInfoPageVoIPage);
    }

    /***
     * @description 查询app详情
     * @author zhangpingping
     * @date 2021/12/21
     * @param [param]
     * @return
     */
    @GetMapping("/query-detail-app/{id}")
    public DataResult<AppVersionInfoVo> queryAppVersionInfoAppDetail(@PathVariable("id")  String id) {
        AppVersionInfoListDto param=new AppVersionInfoListDto();
        param.setStatus(YesNo.YES);
        param.setId(id);
        AppVersionInfoVo appVersionInfoeVos = this.appVersionInfoService.queryAppVersionInfoDetail(param);
        return R.success(appVersionInfoeVos);
    }

    /***
     * @description 查询后台详情
     * @author zhangpingping
     * @date 2021/12/21
     * @param [param]
     * @return
     */
    @GetMapping("/query-detail/{id}")
    public DataResult<AppVersionInfoVo> queryAppVersionInfoDetail(@PathVariable("id")  String id) {
        AppVersionInfoListDto param=new AppVersionInfoListDto();
        param.setId(id);
        AppVersionInfoVo appVersionInfoeVos = this.appVersionInfoService.queryAppVersionInfoDetail(param);
        return R.success(appVersionInfoeVos);
    }
}
