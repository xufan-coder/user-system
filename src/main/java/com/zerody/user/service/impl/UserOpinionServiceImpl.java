package com.zerody.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.config.OpinionMsgConfig;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.constant.OpinionStateType;
import com.zerody.user.domain.*;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.feign.JPushFeignService;
import com.zerody.user.mapper.UserOpinionMapper;
import com.zerody.user.mapper.UserReplyMapper;
import com.zerody.user.service.*;
import com.zerody.user.util.NoticeImUtil;
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
import java.util.stream.Stream;

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
    private CeoUserInfoService ceoUserInfoService;

    @Resource
    private JPushFeignService jPushFeignService;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private OpinionMsgConfig opinionMsgConfig;

    @Autowired
    private UserOpinionAutoAssignService autoAssignService;

    @Autowired
    private UserOpinionAssistantRefService assistantRefService;

    @Autowired
    private UserReplyService userReplyService;

    @Value("${jpush.template.user-system.reply-warn:}")
    private String replyWarnTemplate;
    @Value("${jpush.template.user-system.reply-warn2:}")
    private String replyWarnTemplate2;

    @Value("${receive.boss}")
    private List<String> receiveBoss;


    /**
     * 消息的类型：流程提醒
     */
    private static final int MESSAGE_TYPE_FLOW = 1010;

    /** 1 自动分配 0 手动分配 */
    private static final int SUGGESTION_BOX = 1;
    private static final int BOSS_MAIL = 0;

    @Override
    public void addUserOpinion(UserOpinionDto param) {
        UserOpinion opinion = new UserOpinion();
        BeanUtils.copyProperties(param,opinion);
        opinion.setCreateTime(new Date());
        opinion.setDeleted(YesNo.YES);
        opinion.setId(UUIDutils.getUUID32());
        opinion.setState(OpinionStateType.PENDING);
        opinion.setSource(param.getSource());
        //this.save(opinion);
        insertImage(param.getReplyImageList(),opinion.getId(),ImageTypeInfo.USER_OPINION,opinion.getUserId(),opinion.getUserName());

        JSONArray jsonArray = new JSONArray();
        if (DataUtil.isNotEmpty(param.getSeeUserIds()) && param.getSeeUserIds().size() > 0){
            // 设置意见来源 0 董事长信箱，1 意见箱
            opinion.setSource(YesNo.YES);
            // 添加接收人可查看关联
            userOpinionRefService.addOpinionRef(opinion.getId(),param.getSeeUserIds(),YesNo.YES);
            /*new Thread(() -> {for(String userId : param.getSeeUserIds()){
                jdPush(this.replyWarnTemplate,userId,opinion);
                pushIm(opinionMsgConfig.getTitle(),opinion.getId(),userId,opinion.getUserName(),opinionMsgConfig.getContent());
                }).start();*/

            List<String> assistantUserIdsTotal = new ArrayList<>();
            for(String userId : param.getSeeUserIds()){
                Long messageId = NoticeImUtil.pushOpinionToDirect(opinion.getId(),userId, param.getUserName(), param.getContent(), Boolean.FALSE);
                jsonArray.add(setMessageJson(messageId,userId));

                //如果开启了自动分配 , 且都配置了相同的协助人, 则消息只推送一次
                if (autoAssignService.isAutoAssign(userId)){
                    List<String> assistantUserIds = this.assistantRefService.getAssistantUserIds(userId);
                    assistantUserIdsTotal.addAll(assistantUserIds);
                }
                List<String> assistantUserIdsResult = assistantUserIdsTotal.stream().distinct().collect(Collectors.toList());
                // 添加协助人可查看关联
                userOpinionRefService.addOpinionRef(opinion.getId(),assistantUserIdsResult,YesNo.NO);
                // 推送给每个协助人
                for (String assistantUserId : assistantUserIdsResult){
                    Long assistantMessageId = NoticeImUtil.pushOpinionToAssistant(opinion.getId(),assistantUserId,opinion.getUserName(),param.getContent(), getReplyName(userId) ,opinion.getSource(),opinion.getState());
                    jsonArray.add(setMessageJson(assistantMessageId,assistantUserId));
                }
            }
        }else if (DataUtil.isEmpty(param.getSeeUserIds())){
            // 没有查看人说明是投递给boss，设置推送的boss账号
            // 设置意见来源 0 董事长信箱，1 意见箱
            opinion.setSource(YesNo.NO);
            param.setSeeUserIds(this.receiveBoss);
            userOpinionRefService.addOpinionRef(opinion.getId(),param.getSeeUserIds(),YesNo.YES);
            // 意见发起人信息
            String senderInfo = this.getSenderInfo(param.getUserId());
            List<String> assistantUserIdsTotal = new ArrayList<>();

            // 读取每个boss开启了自动配置的协助人
            for (String ceoUserId : param.getSeeUserIds()) {
                // 立即推送意见反馈给可查看的boss
                Long messageId = NoticeImUtil.pushOpinionToDirect(opinion.getId(),ceoUserId, senderInfo, param.getContent(),Boolean.TRUE);
                jsonArray.add(setMessageJson(messageId,ceoUserId));

                //如果boss开启了自动分配则同时推送给boss配置的协助人 , 如果boss都配置了相同的协助人, 则消息只推送一次
                if (autoAssignService.isAutoAssign(ceoUserId)){
                    List<String> assistantUserIds = this.assistantRefService.getAssistantUserIds(ceoUserId);
                    assistantUserIdsTotal.addAll(assistantUserIds);
                }
            }
            List<String> assistantUserIdsResult = assistantUserIdsTotal.stream().distinct().collect(Collectors.toList());
            // 添加可查看关联
            userOpinionRefService.addOpinionRef(opinion.getId(),assistantUserIdsResult,YesNo.NO);

            // 推送给每个协助人
            for (String assistantUserId : assistantUserIdsResult){
                Long assistantMessageId = NoticeImUtil.pushOpinionToAssistant(opinion.getId(),assistantUserId, senderInfo, param.getContent(),null,opinion.getSource(),opinion.getState());
                jsonArray.add(setMessageJson(assistantMessageId,assistantUserId));
            }
        }

        // 保存
        String messageJsonStr = JSONObject.toJSONString(jsonArray);
        opinion.setMessageJson(messageJsonStr);
        this.save(opinion);
    }

    private JSONObject setMessageJson(Long messageId, String userId){
        JSONObject json = new JSONObject();
        json.put("messageId",messageId);
        json.put("userId",userId);
        return json;
    }

    @Override
    public String getSenderInfo(String userId){
        CeoUserInfo ceoUserInfo = ceoUserInfoService.getById(userId);
        if (DataUtil.isNotEmpty(ceoUserInfo)){
            return ceoUserInfo.getUserName();
        }
        StaffInfoVo staffInfo = sysStaffInfoService.getStaffInfo(userId);
        if (DataUtil.isNotEmpty(staffInfo)){
            return staffInfo.getCompanyName() + staffInfo.getDepartmentName() + staffInfo.getUserName();
        }
        return null;
    }

    @Override
    public void updateOpinionMessageJson(String opinionId, JSONObject messageJson) {
        UserOpinion userOpinion = this.getById(opinionId);
        JSONArray jsonArray = JSONObject.parseArray(userOpinion.getMessageJson());
        jsonArray.add(messageJson);

        String messageJsonStr = JSONObject.toJSONString(jsonArray);
        UpdateWrapper<UserOpinion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(UserOpinion::getMessageJson,messageJsonStr);
        updateWrapper.lambda().eq(UserOpinion::getId,userOpinion.getId());
        this.update(updateWrapper);
    }

    private String getReplyName(String userId){
        //获取意见直接接收人信息
        CeoUserInfo ceoUserInfo = ceoUserInfoService.getById(userId);
        if (DataUtil.isNotEmpty(ceoUserInfo)){
            return ceoUserInfo.getUserName();
        }
        StaffInfoVo staffInfo = sysStaffInfoService.getStaffInfo(userId);
        if (DataUtil.isNotEmpty(staffInfo)){
            return staffInfo.getUserName();
        }
        return null;
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

        // 判断是 其他人回复信息 0  还是 提交人补充回复信息 1
        if (param.getUserId().equals(opinion.getUserId())){
            reply.setSource(YesNo.YES);
        }else {
            reply.setSource(YesNo.NO);
        }

        this.userReplyMapper.insert(reply);
        insertImage(param.getReplyImageList(),reply.getId(),ImageTypeInfo.USER_REPLY,reply.getUserId(),reply.getUserName());
        /*new Thread(() -> {
            opinion.setUserName(param.getUserName());
            jdPush(this.replyWarnTemplate2,opinion.getUserId(),opinion);
            pushIm(opinionMsgConfig.getTitle2(),opinion.getId(),opinion.getUserId(),reply.getUserName(),opinionMsgConfig.getContent2());
        }).start();*/

        // 判断是否是自己添加的回复=(补充提问) , 否变更处理进度，发起消息回复通知
        if (!opinion.getUserId().equals(param.getUserId())){
            // 有回复变更处理进度为处理中
            UpdateWrapper<UserOpinion> up = new UpdateWrapper<>();
            up.lambda().eq(UserOpinion::getId,opinion.getId());
            up.lambda().set(UserOpinion::getState,OpinionStateType.UNDERWAY);
            this.update(up);

            // 通知意见发起人有回复信息
            Long replyMessageId = NoticeImUtil.pushReplyToInitiator(opinion.getId(), opinion.getUserId(), reply.getUserName(), param.getContent(), opinion.getSource());
            // 变更意见json信息
            this.updateOpinionMessageJson(opinion.getId(),setMessageJson(replyMessageId,opinion.getUserId()));

            if (DataUtil.isNotEmpty(opinion.getMessageJson())){
                // 转换消息messageJson对象
                String messageId = "";
                JSONArray jsonArray = JSONObject.parseArray(opinion.getMessageJson());

                for (int i = 0; i<jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    messageId = String.valueOf(jsonObject.get("messageId"));
                    String userId = String.valueOf(jsonObject.get("userId"));

                    if (!userId.equals(opinion.getUserId())){
                        // 推送消息变更意见状态
                        NoticeImUtil.sendOpinionStateChange(opinion,OpinionStateType.UNDERWAY,userId,param.getUserId(),messageId,param.getSource(),Boolean.FALSE);
                    }
                }
            }


        }else if (opinion.getUserId().equals(param.getUserId())){
            /*// 获取意见收件人和协助人
            List<String> seeUserIds = this.userOpinionRefService.getSeeUserIds(opinion.getId());*/

            // 总的最新的协助人汇总
            List<String> newAssistantUserIdsTotal = new ArrayList<>();

            // 获取意见接收人 (直接查看人)
            List<String> directUserIds = userOpinionRefService.getReplyUserIds(opinion.getId(),YesNo.YES);

            // 获取意见协助人
            List<String> assistantIds = userOpinionRefService.getReplyUserIds(opinion.getId(),YesNo.NO);

            for (String directUserId : directUserIds) {
                // 推送补充意见
                Long additionalMessageId = NoticeImUtil.pushAdditionalOpinionToHandler(opinion, directUserId, param.getUserName(), param.getContent(), Boolean.TRUE);

                //TODO 变更意见json信息 ,此处变更操作较为频繁，后续可优化
                this.updateOpinionMessageJson(opinion.getId(),setMessageJson(additionalMessageId,directUserId));


                // 获取意见接收人最新的协助人
                List<String> assistantUserIds = this.assistantRefService.getAssistantUserIds(directUserId);
                newAssistantUserIdsTotal.addAll(assistantUserIds);
            }

            List<String> newAssistantUserIdsResult = newAssistantUserIdsTotal.stream().distinct().collect(Collectors.toList());


            List<String> resultList = Stream.concat(assistantIds.stream(), newAssistantUserIdsResult.stream())
                    .distinct()
                    .collect(Collectors.toList());

            List<String> newAssistantUserIds = newAssistantUserIdsResult.stream().filter(r -> !assistantIds.contains(r)).collect(Collectors.toList());

            // 添加新配置的协助人关联
            this.userOpinionRefService.addOpinionRef(opinion.getId(),newAssistantUserIds,YesNo.NO);

            log.info("推送包括新协助人入参:{}", JSON.toJSONString(resultList));
            for (String userId : resultList) {
                Long additionalMessageId = NoticeImUtil.pushAdditionalOpinionToHandler(opinion, userId, param.getUserName(), param.getContent(), Boolean.FALSE);

                //TODO 变更意见json信息 ,此处变更操作较为频繁，后续可优化
                this.updateOpinionMessageJson(opinion.getId(),setMessageJson(additionalMessageId,userId));
            }
        }

    }

/*    private void jdPush(String code,String userId, Object obj){
        //极光推送
        AddJdPushDto push = new AddJdPushDto();
        push.setData(obj);
        push.setUserId(userId);
        push.setTemplateCode(code);
        push.setType(MESSAGE_TYPE_FLOW);

        log.info("推送极光参数---:{}", com.zerody.flow.client.util.JsonUtils.toString(push));
        DataResult<Object> result = this.jPushFeignService.doAuroraPush(push);
        log.info("推送极光结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));
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
        DataResult<Long> result = this.sendMsgFeignService.send(data);
        log.info("推送IM结果:{}", com.zerody.flow.client.util.JsonUtils.toString(result));

    }*/

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
        dto.setReplyType(YesNo.YES);
        Integer replySum = this.baseMapper.getOpinionReplyTotal(dto);
        dto.setReplyType(YesNo.NO);
        Integer assistantSum = this.baseMapper.getOpinionReplyTotal(dto);
        Map<String,Object> orderStatusMap = new HashedMap<>();
        orderStatusMap.put("replySum",replySum);
        orderStatusMap.put("opinionSum",opinionSum);
        orderStatusMap.put("assistantSum",assistantSum);

        return orderStatusMap;
    }

    @Override
    public void modifyOpinionStateById(String id , String userId) {
        UpdateWrapper<UserOpinion> up = new UpdateWrapper<>();
        up.lambda().eq(UserOpinion::getId,id);
        up.lambda().set(UserOpinion::getState, OpinionStateType.ACCOMPLISH);
        this.update(up);

        // 变更通知消息状态
        UserOpinion byId = this.getById(id);

        // 获取意见直接查看人
        List<String> replyUserIds = this.userOpinionRefService.getReplyUserIds(id, YesNo.YES);

        if (DataUtil.isNotEmpty(byId.getMessageJson())){
            JSONArray jsonArray = JSONObject.parseArray(byId.getMessageJson());
            for (int i = 0; i<jsonArray.size();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String messageId = String.valueOf(jsonObject.get("messageId"));
                String messageUserId = String.valueOf(jsonObject.get("userId"));

                if (replyUserIds.contains(messageUserId)){
                    // 推送消息变更意见状态
                    NoticeImUtil.sendOpinionStateChange(byId,OpinionStateType.ACCOMPLISH,messageUserId,userId,messageId,byId.getSource(),Boolean.TRUE);
                }else {
                    // 推送消息变更意见状态
                    NoticeImUtil.sendOpinionStateChange(byId,OpinionStateType.ACCOMPLISH,messageUserId,userId,messageId,byId.getSource(),Boolean.FALSE);
                }
            }
        }

        // 意见已被处理完毕，除点击处理完毕操作人之外其他关联该意见的人都收到该信息
        List<String> allOpinionRefUserIds = this.userOpinionRefService.getReplyUserIds(id, null);
        List<String> receiveUserIds = allOpinionRefUserIds.stream().filter(r -> !r.equals(userId)).collect(Collectors.toList());
        // 获取最新的回复消息内容
        String latestReplyContent = userReplyService.getLatestReplyContent(id);

        for (String receiveCompleteUserId : receiveUserIds) {
            if (replyUserIds.contains(receiveCompleteUserId)){
                NoticeImUtil.pushOpinionCompleteToOther(byId,receiveCompleteUserId,null,latestReplyContent,Boolean.TRUE);
            }else {
                NoticeImUtil.pushOpinionCompleteToOther(byId,receiveCompleteUserId,null,latestReplyContent,Boolean.FALSE);
            }

        }
    }

}
