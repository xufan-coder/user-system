package com.zerody.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.jpush.api.dto.AddJdPushDto;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.domain.UserReply;
import com.zerody.user.dto.UserOpinionDto;
import com.zerody.user.dto.UserOpinionQueryDto;
import com.zerody.user.dto.UserReplyDto;
import com.zerody.user.feign.JPushFeignService;
import com.zerody.user.mapper.UserOpinionMapper;
import com.zerody.user.mapper.UserReplyMapper;
import com.zerody.user.service.ImageService;
import com.zerody.user.service.UserOpinionService;
import com.zerody.user.vo.UserOpinionDetailVo;
import com.zerody.user.vo.UserOpinionPageVo;
import com.zerody.user.vo.UserOpinionVo;
import com.zerody.user.vo.UserReplyVo;
import org.springframework.beans.BeanUtils;
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
public class UserOpinionServiceImpl extends ServiceImpl<UserOpinionMapper, UserOpinion> implements UserOpinionService {

    @Resource
    private ImageService imageService;

    @Resource
    private UserReplyMapper userReplyMapper;

    @Resource
    private JPushFeignService jPushFeignService;

    @Value("${jpush.template.user-system.reply-warn:}")
    private String replyWarnTemplate;

    @Override
    public void addUserOpinion(UserOpinionDto param) {
        UserOpinion opinion = new UserOpinion();
        BeanUtils.copyProperties(param,opinion);
        opinion.setCreateTime(new Date());
        opinion.setDeleted(YesNo.YES);
        opinion.setId(UUIDutils.getUUID32());
        this.save(opinion);
        insertImage(param.getReplyImageList(),opinion.getId(),ImageTypeInfo.USER_OPINION,opinion.getUserId(),opinion.getUserName());
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

        AddJdPushDto push = new AddJdPushDto();
        push.setData(reply);
        push.setUserId(opinion.getUserId());
        push.setTemplateCode(this.replyWarnTemplate);
        push.setType(1);
        this.jPushFeignService.doAuroraPush(push);

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
    public List<UserOpinionVo> queryUserOpinionUser(String userId) {
        return this.baseMapper.queryUserOpinionUser(userId);
    }

    @Override
    public IPage<UserOpinionPageVo> queryUserOpinionPage(UserOpinionQueryDto dto) {
        IPage<UserOpinionPageVo> iPage = new Page<>(dto.getCurrent(),dto.getPageSize());
        return this.baseMapper.queryUserOpinionPage(dto, iPage);
    }

    @Override
    public UserOpinionDetailVo getOpinionDetail(String id) {
        UserOpinionDetailVo detailVo =this.baseMapper.getOpinionDetail(id);

        // 获取回复列表
        QueryWrapper<UserReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserReply:: getOpinionId,id);
        queryWrapper.lambda().eq(UserReply:: getDeleted,YesNo.YES);
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

}
