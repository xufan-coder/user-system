package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName InviteStateVo
 * @DateTime 2021/12/22_9:41
 * @Deacription TODO
 */
@Data
public class InviteStateVo {

    /** 用户id */
    private String id;

    /** 邀约人数 */
    private Integer inviteNum;

    /** 上门人数 */
    private Integer signNum;

}
