package com.zlb.ecp.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zlb.ecp.login.ibiz.ISyBiz;

@RestController
@RequestMapping("SyService")
public class SyService {

	private static final Logger log = LoggerFactory.getLogger(SyService.class);
	
	@Autowired
	private ISyBiz syBiz;
	
	@RequestMapping(value="heo.do",method=RequestMethod.GET)
	public Integer heo(){
		return syBiz.updateUser();
	}
}
