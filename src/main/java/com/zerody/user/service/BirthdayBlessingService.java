package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.BirthdayBlessing;

import java.util.List;

/**
 * @author kuang
 */
public interface BirthdayBlessingService extends IService<BirthdayBlessing> {
    List<BirthdayBlessing> getBlessingList(Integer type);
}
