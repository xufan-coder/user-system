package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2023/10/7 11:33
 */

@Data
public class ExpireTimeNoticeDto {

    private Date thirty;

    private Date ninety;
}
