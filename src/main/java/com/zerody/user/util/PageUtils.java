package com.zerody.user.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zerody.common.api.bean.PageQueryDto;
import org.springframework.util.StringUtils;

/**
 * @author yumiaoxia
 * @since 2021-07-05
 */
public class PageUtils {

    public static <T> Page<T> getPageRequest(PageQueryDto pageParam, String defaultSortColumn, OrderType orderType){
        Page<T> pageRequest = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        if(!StringUtils.isEmpty(pageParam.getOrderProp())){
            if("asc".equals(pageParam.getOrderType())){
                pageRequest.setOrders(Lists.newArrayList(OrderItem.asc(pageParam.getOrderColumn())));
            }else{
                pageRequest.setOrders(Lists.newArrayList(OrderItem.desc(pageParam.getOrderColumn())));
            }
        }else{
            if(!StringUtils.isEmpty(defaultSortColumn) && orderType != null ){
                if(orderType == OrderType.ASC){
                    pageRequest.setOrders(Lists.newArrayList(OrderItem.asc(defaultSortColumn)));
                }else{
                    pageRequest.setOrders(Lists.newArrayList(OrderItem.desc(defaultSortColumn)));
                }
            }
        }
        return pageRequest;
    }


    public static enum OrderType{
        ASC,
        DESC;
    }
}
