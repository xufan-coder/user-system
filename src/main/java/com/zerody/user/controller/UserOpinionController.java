package com.zerody.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.vo.UserVo;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.service.UserOpinionService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author kuang
 * 意见箱
 */
@Slf4j
@RestController
@RequestMapping("/user-opinion")
public class UserOpinionController {

    @Resource
    private UserOpinionService userOpinionService;

    @Autowired
    private CheckUtil checkUtil;

    /**
     * @description 添加意见(董事长信箱)
     * @author kuang
     * @date 2022/4/14
     */
    @PostMapping(value = "/add")
    public DataResult<Object> addUserOpinion(@RequestBody @Validated UserOpinionDto param) {
        try {
            UserVo userVo = UserUtils.getUser();
            if (Objects.nonNull(userVo)) {
                param.setUserId(userVo.getUserId());
                param.setUserName(userVo.getUserName());
            }
            this.userOpinionService.addUserOpinion(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加意见反馈异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加意见反馈出错:{}", e, e);
            return R.error("添加意见反馈出错" + e);
        }
    }

    /**
     * @description 意见回复(意见箱)
     * @author kuang
     * @date 2022/4/14
     */
    @PostMapping(value = "/reply/add")
    public DataResult<Object> addUserReply(@Validated @RequestBody UserReplyDto param) {
        try {
            UserVo userVo = UserUtils.getUser();
            if (Objects.nonNull(userVo)) {
                param.setUserId(userVo.getUserId());
                param.setUserName(userVo.getUserName());
            }
            this.userOpinionService.addUserReply(param);
            return R.success();
        } catch (DefaultException e) {
            log.error("添加回复意见反馈异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("添加回复意见反馈出错:{}", e, e);
            return R.error("添加回复意见反馈出错" + e);
        }
    }


    /***
     * @description 查询详情
     * @author kuang
     * @date 2022/4/14
     */
    @GetMapping("/details/{id}")
    public DataResult<UserOpinionDetailVo> getOpinionDetail(@PathVariable("id") String id) {
        try {
            UserOpinionDetailVo problemFeedbackVo = this.userOpinionService.getOpinionDetail(id);
            return R.success(problemFeedbackVo);
        } catch (DefaultException e) {
            log.error("查询详情意见反馈异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询详情意见反馈出错:{}", e, e);
            return R.error("查询详情意见反馈出错" + e);
        }
    }


    /***
     * @description 根据用户查询(董事长信箱)
     * @author kuang
     * @date 2022/4/14
     */
    @GetMapping("/query-user")
    public DataResult<IPage<UserOpinionVo>> queryUserOpinionUser(UserOpinionQueryDto queryDto) {
        try {
            queryDto.setUserId(UserUtils.getUserId());
            this.checkUtil.setFiltrateTime(queryDto);
            IPage<UserOpinionVo> iPage = this.userOpinionService.queryUserOpinionUser(queryDto);
            return R.success(iPage);
        } catch (DefaultException e) {
            log.error("查询用户反馈异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询用户反馈出错:{}", e, e);
            return R.error("查询用户反馈出错" + e);
        }
    }

    /***
     * @description 分页查询(意见箱)
     * @author kuang
     * @date 2022/4/14
     */
    @GetMapping("/page")
    public DataResult<IPage<UserOpinionPageVo>> queryUserReplyUser(UserOpinionQueryDto dto) {
        try {
            if(!UserUtils.getUser().isCEO()){
                dto.setUserId(UserUtils.getUserId());
            }
            checkUtil.setFiltrateTime(dto);
            IPage<UserOpinionPageVo> iPage = this.userOpinionService.queryUserOpinionPage(dto);
            return R.success(iPage);
        } catch (DefaultException e) {
            log.error("分页查询意见箱异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("分页查询意见箱出错:{}", e, e);
            return R.error("分页查询意见箱出错" + e);
        }
    }

    /**
     * @author kuang
     * @description 获取意见总数
     **/
    @GetMapping("/get/summary")
    public DataResult<Object> getOpinionAndReplySum(UserOpinionQueryDto dto){
        try {
            dto.setUserId(UserUtils.getUserId());
            dto.setCEO(UserUtils.getUser().isCEO());
            Object summary = this.userOpinionService.getOpinionAndReplySum(dto);
            return R.success(summary);
        } catch (DefaultException e) {
            log.error("获取意见总数异常:{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取意见总数出错:{}", e, e);
            return R.error("获取意见总数出错" + e);
        }
    }

    /**
    * @Description:         修改意见反馈状态为已完成
    * @Author:              xufan
    * @Date:                2024/2/29 10:37
    */
    @PutMapping("/modify/state/{id}")
    public DataResult<Object> modifyOpinionStateById(@PathVariable String id){
        try {
            this.userOpinionService.modifyOpinionStateById(id);
            return R.success();
        } catch (DefaultException e) {
            log.error("修改意见反馈状态错误：{}", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改意见反馈状态错误：{}", e, e);
            return R.error("修改意见反馈状态错误" + e.getMessage());
        }
    }

}
