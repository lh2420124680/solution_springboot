package com.zlb.ecp.login.mapper;

import java.util.List;
import java.util.Map;

public interface SyMapper {

	public Integer updateUser();
	
	public List<Map<String, Object>> queryMsg();
}
