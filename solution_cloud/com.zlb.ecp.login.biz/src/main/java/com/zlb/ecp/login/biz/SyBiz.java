package com.zlb.ecp.login.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlb.ecp.conf.EcpDataSource;
import com.zlb.ecp.conf.ReadWriteDataSourceDecision;
import com.zlb.ecp.login.ibiz.ISyBiz;
import com.zlb.ecp.login.ibiz.IUserBiz;
import com.zlb.ecp.login.mapper.SyMapper;
import com.zlb.ecp.login.mapper.UserMapper;

@Service
@Transactional
public class SyBiz implements ISyBiz {

	public static final String DEFAULT_LINK = "default";
	public static final String SWITCH_LINK = "b";
	
	@Autowired
	private SyMapper syMapper;
	
	@Autowired
	private UserMapper userMapper;

	/*@Override
	public Integer updateUser() {
		ReadWriteDataSourceDecision.markWrite("b");
		Integer updateUser = syMapper.updateUser();
		System.out.println(updateUser);
		ReadWriteDataSourceDecision.markWrite("default");
		List<Map<String, Object>> queryMsg = syMapper.queryMsg();
		System.out.println(queryMsg);
		return 1;
	}*/
	
	@Override
	@EcpDataSource("b")
	@Transactional(readOnly=true)
	public Integer updateUser() {
		List<Map<String, Object>> queryMsg = syMapper.queryMsg();
		System.out.println(queryMsg);
		return 1;
	}
	
}
