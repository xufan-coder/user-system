package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工黑名单控制类
 * @author PengQiang
 * @ClassName StaffBlacklistControlller
 * @DateTime 2021/8/4_9:28
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/staff-blacklist")
public class StaffBlacklistControlller {

    @Autowired
    private StaffBlacklistService service;

    @Autowired
    private CheckUtil checkUtil;

    /**
     *
     *
     * @author               PengQiang
     * @description          添加员工黑名单
     * @date                 2021/8/4 9:57
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @PostMapping("/add")
    public DataResult<StaffBlacklistAddDto> addStaffBlaklist(@RequestBody StaffBlacklistAddDto param){
        try {
            this.checkUtil.getCheckAddBlacListParam(param);
            StaffBlacklistAddDto result = this.service.addStaffBlaklist(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("添加员工黑名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加员工黑名单错误：{}", e, e);
            return R.error("添加员工黑名单错误" + e.getMessage());
        }
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          根据组织架构查询黑名单
     * @date                 2021/8/4 11:44
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.FrameworkBlacListQueryPageVo>>
     */
    @GetMapping("/framework/page")
    public DataResult<IPage<FrameworkBlacListQueryPageVo>> getFrameworkPage(FrameworkBlacListQueryPageDto param){
        try {
            this.checkUtil.SetUserPositionInfo(param);
            param.setState(StaffBlacklistApproveState.BLOCK.name());
            IPage<FrameworkBlacListQueryPageVo> result = this.service.getPageBlackList(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据组织架构查询黑名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据组织架构查询黑名单错误：{}", e, e);
            return R.error("根据组织架构查询黑名单错误" + e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          查询全部黑名单员工
     * @date                 2021/8/4 11:44
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.FrameworkBlacListQueryPageVo>>
     */
    @GetMapping("/all/page")
    public DataResult<IPage<FrameworkBlacListQueryPageVo>> getAllPage(FrameworkBlacListQueryPageDto param){
        try {
            if (!UserUtils.getUser().isBackAdmin()) {
                param.setCompanyId(UserUtils.getUser().getCompanyId());
            }
            param.setState(StaffBlacklistApproveState.BLOCK.name());
            IPage<FrameworkBlacListQueryPageVo> result = this.service.getPageBlackList(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("查询黑名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询黑名单错误：{}", e, e);
            return R.error("查询黑名单错误" + e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          解除黑名单
     * @date                 2021/8/4 16:01
     * @param                [staffId]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @DeleteMapping("/relieve/{id}")
    public DataResult<Object> doRelieveByStaffId(@PathVariable("id") String userId){
        try {

            this.service.doRelieveByStaffId(userId);
            return R.success();
        } catch (DefaultException e) {
            log.error("解除黑名单出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("解除黑名单出错：{}", e, e);
            return R.error("解除黑名单出错" + e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          根据手机号码查询是否被拉黑
     * @date                 2021/8/4 16:01
     * @param                [staffId]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @GetMapping("/get-by-mobile")
    public DataResult<MobileBlacklistQueryVo> getBlacklistByMobile(@RequestParam("mobile") String mobile){
        try {

            MobileBlacklistQueryVo result = this.service.getBlacklistByMobile(mobile);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error("根据手机号码查询是否被拉黑出错" + e.getMessage());
        }
    }

}
