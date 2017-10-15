package com.zlb.ecp.login.ibiz;

import java.util.Map;

import com.zlb.ecp.entity.EntityResult;

/**
 * @Description: sso 
 * @author Jane.Luo
 * @date 2017年8月2日 上午9:39:22 
 * @version V1.0
 */
public interface IUserBiz {

	/**
	 * <p>Description: sso<p>
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月2日 上午9:35:18
	 */
	public EntityResult<?> loginApp(Map<String, Object> where);
	
	/**
	 * <p>Description: 用户验证<p>
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年8月2日 下午12:08:35
	 */
	public EntityResult<?> checkData(Map<String, Object> where);
	
	/**
	 * <p>Description: rpc调用测试<p>
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年8月2日 下午12:08:35
	 */
	public String remoteTest(Map<String, Object> where);
	
}
