package com.zlb.ecp.login.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description: sso
 * @author Jane.Luo
 * @date 2017年8月2日 上午9:35:23 
 * @version V1.0
 */
public interface UserMapper {

	/**
	 * <p>Description: 查询用户的信息<p>
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月2日 上午9:35:18
	 */
	public List<Map<String, Object>> userLoginApp(Map<String, Object> where);
	
	/**
	 * <p>Description: 查找该登录邮箱是否存在<p>
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月2日 上午9:36:26
	 */
	public List<Map<String, Object>> findLoginMail(Map<String, Object> where);
	
	/**
	 * <p>Description: 查询某个用户在某个学校的职务<p>
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月2日 上午9:36:43
	 */
	public List<Map<String, Object>> selectUserPosition(Map<String, Object> where);
}
