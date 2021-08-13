package com.zerody.user.dto;

import com.zerody.user.domain.StaffBlacklist;
import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName StaffBlacklistAddDto
 * @DateTime 2021/8/4_9:46
 * @Deacription TODO
 */
@Data
public class StaffBlacklistAddDto {

    /** 员工黑名单 */
    private StaffBlacklist blacklist;

    private List<String> images;
}
