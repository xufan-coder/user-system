package com.zerody.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.expression.Expression;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.util.UUIDutils;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.jpush.api.dto.AddJdPushDto;
import com.zerody.user.config.OpinionMsgConfig;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.domain.UserReply;
import com.zerody.user.dto.FlowMessageDto;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.feign.JPushFeignService;
import com.zerody.user.mapper.UserOpinionMapper;
import com.zerody.user.mapper.UserReplyMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.UserOpinionRefService;
import com.zerody.user.service.UserOpinionService;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;
import com.zerody.user.vo.UserReplyVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kuang
 */
@RefreshScope
@Service
@Slf4j
public class UserOpinionServiceImpl extends ServiceImpl<UserOpinionMapper, UserOpinion> implements UserOpinionService {

    @Resource
    private ImageService imageService;

    @Resource
    private UserReplyMapper userReplyMapper;

    @Resource
    private UserOpinionRefService userOpinionRefService;

    @Resource
    private JPushFeignService jPushFeignService;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Autowired
    private OpinionMsgConfig opinionMsgConfig;

    @Value("${jpush.template.user-system.reply-warn:}")
    private String replyWarnTemplate;
    @Value("${jpush.template.user-system.reply-warn2:}")
    private String replyWarnTemplate2;


    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;

    @Override
    public void addUserOpinion(UserOpinionDto param) {
        UserOpinion opinion = new UserOpinion();
        BeanUtils.copyProperties(param,opinion);
        opinion.setCreateTime(new Date());
        opinion.setDeleted(YesNo.YES);
        opinion.setId(UUIDutils.getUUID32());
        this.save(opinion);
        insertImage(param.getReplyImageList(),opinion.getId(),ImageTypeInfo.USER_OPINION,opinion.getUserId(),opinion.getUserName());
        userOpinionRefService.addOpinionRef(opinion.getId(),param.getSeeUserIds());
        log.info("数据参数-1{}",opinion);
        new Thread(() -> {
            for(String userId : param.getSeeUserIds()){
                log.info("数据参数-2{}",opinion);
                jdPush(this.replyWarnTemplate,userId,opinion);
                pushIm(opinionMsgConfig.getTitle(),opinion.getId(),userId,opinion.getUserName(),opinionMsgConfig.getContent());
            }
        }).start();

    }

    @Override
    public void addUserReply(UserReplyDto param) {

        UserOpinion opinion = this.getById(param.getOpinionId());
        if(Objects.isNull(opinion)){
            throw new DefaultException("未找到需要回复的意见信息");
        }
        UserReply reply = new UserReply();
        BeanUtils.copyProperties(param,reply);
        reply.setCreateTime(new Date());
        reply.setDeleted(YesNo.YES);
        reply.setId(UUIDutils.getUUID32());
        this.userReplyMapper.insert(reply);
        insertImage(param.getReplyImageList(),reply.getId(),ImageTypeInfo.USER_REPLY,reply.getUserId(),reply.getUserName());
        log.info("数据参数-1{}",opinion);
        new Thread(() -> {
            log.info("数据参数-1{}",opinion);
            jdPush(this.replyWarnTemplate2,opinion.getUserId(),reply);
            pushIm(opinionMsgConfig.getTitle2(),opinion.getId(),opinion.getUserId(),reply.getUserName(),opinionMsgConfig.getContent2());
        }).start();


    }

    private void jdPush(String code,String userId, Object obj){
        //极光推送
        AddJdPushDto push = new AddJdPushDto();
        push.setData(obj);
        push.setUserId(userId);
        push.setTemplateCode(code);
        push.setType(MESSAGE_TYPE_FLOW);
        this.jPushFeignService.doAuroraPush(push);
    }

    private void pushIm(String title,String id, String userId, String userName,String content){
        // 小藏推送

        FlowMessageDto dto = new FlowMessageDto();
        dto.setTitle(title);
        dto.setMessageSource("extend");
        dto.setUrl(opinionMsgConfig.getUrl());

        Map params = new HashMap();
        params.put("id", id);
        params.put("userName", userName);

        String query = Expression.parse(opinionMsgConfig.getQuery(), params);
        Object parse = JSONObject.parse(query);
        dto.setQuery(parse);
        String arguments = Expression.parse(opinionMsgConfig.getArguments(), params);
        Object argumentsParse = JSONObject.parse(arguments);
        dto.setQuery(parse);
        dto.setArguments(argumentsParse);


        SendRobotMessageDto data = new SendRobotMessageDto();
        String msg = Expression.parse(content, params);
        dto.setContent(msg);
        data.setContent(msg);
        data.setTarget(userId);
        data.setContentPush(msg);
        data.setContentExtra(com.zerody.flow.client.util.JsonUtils.toString(dto));
        data.setType(MESSAGE_TYPE_FLOW);
        log.info("小藏推送:{}",com.zerody.flow.client.util.JsonUtils.toString(data));
        com.zerody.common.api.bean.DataResult<Object> result = this.sendMsgFeignService.send(data);
        log.info("推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));

    }

    private void insertImage(List<String> replyImageList, String contentId, String imageType, String userId, String userName){
        if(Objects.nonNull(replyImageList)) {
            List<Image> images = new ArrayList<>();
            replyImageList.forEach(r -> {
                Image image = new Image();
                image.setConnectId(contentId);
                image.setCreateTime(new Date());
                image.setImageType(imageType);
                image.setImageUrl(r);
                image.setId(UUIDutils.getUUID32());
                image.setCreateBy(userId);
                image.setCreateUsername(userName);
                images.add(image);
            });
            if(images.size() > 0) {
                imageService.saveBatch(images);
            }
        }
    }



    @Override
    public IPage<UserOpinionVo> queryUserOpinionUser(UserOpinionQueryDto queryDto) {
        Page<UserOpinionVo> iPage = new Page<>(queryDto.getCurrent(),queryDto.getPageSize());
        return this.baseMapper.queryUserOpinionUser(queryDto,iPage);
    }

    @Override
    public IPage<UserOpinionPageVo> queryUserOpinionPage(UserOpinionQueryDto dto) {
        IPage<UserOpinionPageVo> iPage = new Page<>(dto.getCurrent(),dto.getPageSize());
        return this.baseMapper.queryUserOpinionPage(dto, iPage);
    }

    @Override
    public UserOpinionDetailVo getOpinionDetail(String id) {
        UserOpinionDetailVo detailVo =this.baseMapper.getOpinionDetail(id);
        if(Objects.isNull(detailVo)) {
            throw new DefaultException("问题信息错误");
        }
        // 获取回复列表
        QueryWrapper<UserReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserReply:: getOpinionId,id);
        queryWrapper.lambda().eq(UserReply:: getDeleted,YesNo.YES);
        queryWrapper.lambda().orderByDesc(UserReply :: getCreateTime);
        List<UserReply> replyList = this.userReplyMapper.selectList(queryWrapper);
        List<UserReplyVo> replyVos = new ArrayList<>();
        List<String> contentIds = replyList.stream().map(UserReply::getId).collect(Collectors.toList());
        contentIds.add(id);

        // 获取图片列表
        QueryWrapper<Image> imageQw = new QueryWrapper<>();
        imageQw.lambda().in(Image::getConnectId,contentIds);
        List<Image> images = this.imageService.list(imageQw);
        Map<String,List<String>> imageList = images.stream().collect(Collectors.groupingBy(Image::getConnectId,
                Collectors.mapping(Image::getImageUrl,Collectors.toList())));
        replyList.forEach(r -> {
            List<String> list = imageList.get(r.getId());
            UserReplyVo  replyVo = new UserReplyVo();
            BeanUtils.copyProperties(r,replyVo);
            replyVo.setReplyImageList(list);
            replyVos.add(replyVo);
        });
        detailVo.setReplyList(replyVos);
        List<String> list = imageList.get(id);
        detailVo.setReplyImageList(list);
        return detailVo;
    }

    @Override
    public Object getOpinionAndReplySum(UserOpinionQueryDto dto) {

        Integer opinionSum = (int)queryUserOpinionUser(dto).getTotal();
        if(dto.isCEO()){
            dto.setUserId(null);
        }
        Integer replySum = this.baseMapper.getOpinionReplyTotal(dto);
        Map<String,Object> orderStatusMap = new HashedMap<>();
        orderStatusMap.put("replySum",replySum);
        orderStatusMap.put("opinionSum",opinionSum);

        return orderStatusMap;
    }

}
