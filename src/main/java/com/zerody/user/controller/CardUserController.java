package com.zerody.user.controller;

import com.zerody.user.service.CardUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名片账户管理
 * @author  DaBai
 * @date  2021/1/14 19:11
 */

@Slf4j
@RestController
@RequestMapping("/card-user")
public class CardUserController {
	@Autowired
	private CardUserService service;



}
