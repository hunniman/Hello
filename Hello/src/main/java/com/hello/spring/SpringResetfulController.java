package com.hello.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringResetfulController {

	@RequestMapping(path = "/user/test", method = RequestMethod.GET)
	public @ResponseBody  UserModel  hellWOrld() {
		UserModel user=new UserModel();
		user.setId(1);
		user.setName("dflsdf");
		return user;
	}
}
