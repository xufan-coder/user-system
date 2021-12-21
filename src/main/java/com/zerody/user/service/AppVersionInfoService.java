package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.AppVersionInfo;
import com.zerody.user.dto.AppVersionInfoAddDto;
import com.zerody.user.dto.AppVersionInfoListDto;
import com.zerody.user.dto.AppVersionInfoModifyDto;
import com.zerody.user.dto.AppVersionInfoPageDto;
import com.zerody.user.vo.AppVersionInfoPageVo;
import com.zerody.user.vo.AppVersionInfoVo;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年12月16日 14:19
 */

public interface AppVersionInfoService extends IService<AppVersionInfo> {
    /***
     * @description 添加
     * @author zhangpingping
     * @date 2021/12/16
     * @param [appVersionInfoAddDto]
     * @return
     */
    void addAppVersionInfo(AppVersionInfoAddDto appVersionInfoAddDto);

    /***
     * @description 修改
     * @author zhangpingping
     * @date 2021/12/16
     * @param [appVersionInfoModifyDto]
     * @return
     */
    void modifyAppVersionInfo(AppVersionInfoModifyDto appVersionInfoModifyDto);

    /***
     * @description 删除
     * @author zhangpingping
     * @date 2021/12/16
     * @param [id]
     * @return
     */
    void removeAppVersionInfo(String id);

    /***
     * @description 分页查询
     * @author zhangpingping
     * @date 2021/12/16
     * @param [appVersionInfoPageDto]
     * @return
     */
    IPage<AppVersionInfoPageVo> queryAppVersionInfoPage(AppVersionInfoPageDto appVersionInfoPageDto);

    /***
     * @description 查询
     * @author zhangpingping
     * @date 2021/12/16
     * @param [appVersionInfoListDto]
     * @return
     */
    List<AppVersionInfoPageVo> queryAppVersionInfoList(AppVersionInfoListDto appVersionInfoListDto);

    /***
     * @description 查询详情
     * @author zhangpingping
     * @date 2021/12/21
     * @param [appVersionInfoListDto]
     * @return
     */
    AppVersionInfoVo queryAppVersionInfoDetail(AppVersionInfoListDto appVersionInfoListDto);

}
