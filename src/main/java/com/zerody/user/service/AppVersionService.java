package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.domain.AppVersion;
import com.zerody.user.dto.AppVersionCreateDto;
import com.zerody.user.dto.AppVersionListDto;
import com.zerody.user.dto.AppVersionUpdateDto;
import com.zerody.user.vo.AppVersionListVo;
import com.zerody.user.vo.AppVersionVo;

import java.util.List;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
public interface AppVersionService extends IService<AppVersion> {

    void createAppVersion(AppVersionCreateDto param);

    void updateAppVersion(String id, AppVersionUpdateDto param);

    Page<AppVersionListVo> pageVersion(AppVersionListDto param, PageQueryDto pageParam);

    AppVersionVo detail(String id);

    void deleteVersion(String id);

    List<AppVersion> queryDetail(AppVersionListDto param);

    /**
     * @param
     * @return
     * @description 查询最新版本号
     * @author zhangpingping
     * @date 2021/9/2
     */
    AppVersion queryNewestVersion(AppVersionListDto appVersionListDto);


}
