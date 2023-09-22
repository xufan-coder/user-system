package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.InternalControlDto;
import com.zerody.user.dto.MobileAndIdentityCardDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.enums.BlacklistTypeEnum;
import com.zerody.user.service.StaffBlacklistService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.DateUtils;
import com.zerody.user.vo.BlackListCount;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.InternalControlVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
     * 原子服务按手机号添加伙伴内控流程
     * @author  DaBai
     * @date  2022/11/8 11:57
     */
    @PostMapping("/relieve-mobile")
    public DataResult<List<StaffBlacklist>> updateRelieveByMobile(@RequestBody StaffBlacklist param){
        try {
            List<StaffBlacklist> result = this.service.updateRelieveByMobile(param);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("解除内控名单申请错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("解除内控名单申请错误：{}", e, e);
            return R.error("解除内控名单申请错误" + e.getMessage());
        }
    }


    /**
     * 原子服务按手机号解除伙伴内控状态
     * @author  DaBai
     * @date  2022/11/8 12:03
     */
    @PostMapping("/emp/relieve-mobile/{mobile}")
    public DataResult<Object> doRelieveByMobile(@PathVariable("mobile") String mobile,
                                                @RequestParam("state") Integer state,
                                                @RequestParam("relieveId") String relieveId){
        try {
            this.service.doRelieveByMobile(mobile,state,relieveId);
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
     *流程原子服务调用
     *
     * @author               PengQiang
     * @description          添加内控名单
     * @date                 2021/8/4 9:57
     * @param                [param]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @PostMapping("/add")
    public DataResult<StaffBlacklistAddDto> addStaffBlaklist(@RequestBody StaffBlacklistAddDto param){
        try {
            this.checkUtil.getCheckAddBlacListParam(param);
            StaffBlacklistAddDto result = this.service.addStaffBlaklist(param,null);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("添加内控名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加内控名单错误：{}", e, e);
            return R.error("添加内控名单错误" + e.getMessage());
        }
    }

    /**************************************************************************************************
     **
     *  pc流程发起内控名单审批
     *
     * @param param
     * @return {@link null }
     * @author DaBai
     * @date 2023/9/21  11:37
     */
    @PostMapping("/process/join")
    public DataResult<Object> addStaffBlaklistProcessJoin(@RequestBody StaffBlacklistAddDto param){
        try {
            this.checkUtil.getCheckAddBlacListParam(param);
            UserVo user = UserUtils.getUser();
            param.getBlacklist().setSubmitUserName(user.getUserName());
            param.getBlacklist().setSubmitUserId(user.getUserId());
            if(BlacklistTypeEnum.EXTERNAL.getValue()== param.getBlacklist().getType()){
                if (StringUtils.isEmpty(param.getBlacklist().getMobile())) {
                    return R.error("请输入手机号码");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getCompanyId())) {
                    return R.error("请选择企业");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getUserName())) {
                    return R.error("请输入名称");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getIdentityCard())) {
                    return R.error("请输入身份证号码");
                }
            }
            this.service.addStaffBlaklistProcessJoin(param,user);
            return R.success();
        } catch (DefaultException e) {
            log.error("pc流程发起内控名单审批错误：{}", e.getMessage(), e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("内控添加请求出错：{}", e, e);
            return R.error("内控添加请求出错" + e.getMessage());
        }
    }





    /**
     * 添加员工黑名单 pc 后台
     * @author  DaBai
     * @date  2021/11/24 14:31
     */

    @PostMapping("/join")
    public DataResult<StaffBlacklistAddDto> addStaffBlaklistJoin(@RequestBody StaffBlacklistAddDto param){
        try {
            UserVo user = UserUtils.getUser();
            this.checkUtil.getCheckAddBlacListParam(param);
            param.getBlacklist().setSubmitUserName(user.getUserName());
            param.getBlacklist().setSubmitUserId(user.getUserId());
            if(BlacklistTypeEnum.EXTERNAL.getValue()== param.getBlacklist().getType()){
                if (StringUtils.isEmpty(param.getBlacklist().getMobile())) {
                    return R.error("请输入手机号码");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getCompanyId())) {
                    return R.error("请选择企业");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getUserName())) {
                    return R.error("请输入名称");
                }
                if (StringUtils.isEmpty(param.getBlacklist().getIdentityCard())) {
                    return R.error("请输入身份证号码");
                }
                this.service.addStaffBlaklistJoin(param);
            }else {
                this.service.addStaffBlaklist(param,user);
            }
            return R.success();
        } catch (DefaultException e) {
            log.error("pc后台添加伙伴内控名单错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("pc后台添加伙伴内控名单错误：{}", e, e);
            return R.error("添加内控名单错误" + e.getMessage());
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
            param.setState(StaffBlacklistApproveState.BLOCK.name());
            this.checkUtil.SetUserPositionInfo(param);
            param.setQueryDimensionality("sumbmitUser");
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
//            if (!UserUtils.getUser().isBackAdmin()) {
//                if(DataUtil.isEmpty(param.getCompanyId())) {
//                    param.setCompanyId(UserUtils.getUser().getCompanyId());
//                }
//            }else {
//                // 设置组织架构条件值
//                param.setCompanyIds(this.checkUtil.setBackCompany(UserUtils.getUserId()));
//            }
            param.setQueryDimensionality("blockUser");
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
      * @author  DaBai
      * @date  2022/10/11 10:46
      */
    @GetMapping("/app/page")
    public DataResult<IPage<FrameworkBlacListQueryPageVo>> getAppPage(FrameworkBlacListQueryPageDto param){
        try {
            UserVo user = UserUtils.getUser();
            if (!user.isCEO()) {
                if(DataUtil.isEmpty(param.getCompanyId())) {
                    log.info("入参0000----->");
                    param.setCompanyId(UserUtils.getUser().getCompanyId());
                }
                if(user.isCompanyAdmin()){
                    log.info("入参1111----->");
                    param.setCompanyId(UserUtils.getUser().getCompanyId());
                }else if(user.isDeptAdmin()){
                    log.info("入参2222----->");
                    String deptId = user.getDeptId();
                    if(DataUtil.isNotEmpty(param.getDepartId())){
                        if(param.getDepartId().contains(deptId)){

                        }else {
                            param.setDepartId(deptId);
                        }
                    }else {
                        log.info("入参3333----->");
                        param.setDepartId(deptId);
                    }
                }else {
                    log.info("入参4444----->");
                    String deptId = user.getDeptId();
                    param.setDepartId(deptId);
                }
            }else {
                // 设置组织架构条件值
                param.setCompanyIds(this.checkUtil.setCeoCompany(UserUtils.getUserId()));
            }
            param.setQueryDimensionality("blockUser");
            log.info("入参5555----->{}", JsonUtils.toString(param));
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
    public DataResult<Object> doRelieveByStaffId(@PathVariable("id") String id){
        try {

            this.service.doRelieveByStaffId(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("解除黑名单出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("解除黑名单出错：{}", e, e);
            return R.error("解除黑名单出错" + e.getMessage());
        }
    }

    /**************************************************************************************************
     **
     *  原子服务解除黑名单 按钮控制
     *
     * @param id【主键id】
     * @return {@link null }
     * @author DaBai
     * @date 2022/1/21  14:16
     */
    @PostMapping("/emp/relieve/{id}")
    public DataResult<Object> doRelieve(@PathVariable("id") String id,@RequestParam("state") Integer state){
        try {
            this.service.doRelieve(id,state);
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
     * @description          获取黑名单详情
     * @date                 2021/8/4 16:01
     * @param                [staffId]
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @GetMapping("/get/{id}")
    public DataResult<FrameworkBlacListQueryPageVo> getInfoById(@PathVariable("id") String id){
        try {

            FrameworkBlacListQueryPageVo result = this.service.getInfoById(id);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("获取黑名单详情出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取黑名单详情出错：{}", e, e);
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
    public DataResult<MobileBlacklistQueryVo> getBlacklistByMobile(MobileAndIdentityCardDto dto){
        try {

            MobileBlacklistQueryVo result = this.service.getBlacklistByMobile(dto, UserUtils.getUser(),true);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error("根据手机号码查询是否被拉黑出错" + e.getMessage());
        }
    }

    /**
     * @Description:  根据手机号码查询是否被拉黑-app
     * @param dto
     * @return: com.zerody.common.api.bean.DataResult<com.zerody.user.vo.MobileBlacklistQueryVo>
     * @Author: luolujin
     * @Date: 2023/5/17 18:07
     */
    @GetMapping("/app/get-by-mobile")
    public DataResult<MobileBlacklistQueryVo> getBlacklistByMobileApp(MobileAndIdentityCardDto dto){
        try {
            MobileBlacklistQueryVo result = this.service.getBlacklistByMobile(dto, UserUtils.getUser(),true);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据手机号码查询是否被拉黑出错：{}", e, e);
            return R.error("根据手机号码查询是否被拉黑出错" + e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          导入内控用户
     * @date                 2021/8/4 16:01
     * @param                file
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Object>
     */
    @PostMapping("/import")
    public DataResult<String> doBlacklistExternalImport(MultipartFile file){
        try {
            String id = this.service.doBlacklistExternalImport(file, UserUtils.getUser());
            return R.success(id);
        } catch (DefaultException e) {
            log.error("导入内控用户出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("导入内控用户出错：{}", e, e);
            return R.error("导入内控用户出错" + e.getMessage());
        }
    }


    /**
     * BOSS查看页面
     * 统计内控名单-按企业
     * 2023-09-15修改为纵向权限
     * @author  DaBai
     * @date  2022/10/10 15:12
     */
    @GetMapping("/statistics-by-company")
    public DataResult<List<BlackListCount>> getBlacklistCount(){
        try {
            UserVo user = UserUtils.getUser();
            List<BlackListCount> result = this.service.getBlacklistCount();
            boolean isCeo = user.isCEO();
            boolean isBack = user.isBack();
            if(isCeo||isBack){
                return R.success(result);
            }else {
                String companyId = user.getCompanyId();
                if(DataUtil.isNotEmpty(companyId)){
                    List<BlackListCount> collect = result.stream().filter(s -> s.getCompanyId().equals(companyId)).collect(Collectors.toList());
                    return R.success(collect);
                }
            }
            return R.success(result);
        } catch (DefaultException e) {
            log.error("统计黑名单企业人数出错：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("统计黑名单企业人数出错：{}", e, e);
            return R.error("统计黑名单企业人数出错" + e.getMessage());
        }
    }



    /**
     * 修改伙伴黑名单所属企业
     * @author  DaBai
     * @date  2022/10/10 16:26
     */
    @PutMapping("/update")
    public DataResult<Object> updateStaffBlacklist(@RequestBody StaffBlacklist param){
        try {
           this.service.updateStaffBlacklist(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改伙伴黑名单所属企业错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改伙伴黑名单所属企业错误：{}", e, e);
            return R.error("修改伙伴黑名单所属企业错误" + e.getMessage());
        }
    }

    /**
     * 根据身份证或者手机号码查询是否存在
     * @author  luolujin
     * @date  2022/12/30
     */
    @GetMapping("/internal/control")
    public DataResult<InternalControlVo> updateInternalControl(InternalControlDto internalControlDto) {
        try {
            UserVo userVo = UserUtils.getUser();
            internalControlDto.setCompanyId(userVo.getCompanyId());
            InternalControlVo internalControlVo = this.service.updateInternalControl(internalControlDto);
            return R.success(internalControlVo);
        } catch (DefaultException e) {
            log.error("根据身份证或者手机号码查询是否存在：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据身份证或者手机号码查询是否存在错误：{}", e, e);
            return R.error("根据身份证或者手机号码查询是否存在错误" + e.getMessage());
        }
    }
}
