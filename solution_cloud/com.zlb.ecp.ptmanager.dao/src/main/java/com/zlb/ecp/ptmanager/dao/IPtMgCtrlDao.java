package com.zlb.ecp.ptmanager.dao;

import java.util.List;
import java.util.Map;

/**
 * 平台管控
 * @Description: TODO 
 * @author Jane.Luo
 * @date 2017年9月26日 下午2:31:08 
 * @version V1.0
 */
public interface IPtMgCtrlDao {

	/**
	 * <p>Description: 管控表中添加数据<p>
	 * @author Jane.Luo
	 * @param where
	 * @return Integer
	 * @date 2017年9月26日 下午2:32:18
	 */
	public Integer addPtMg(Map<String, Object> where);
	
	/**
	 * <p>Description: 平台管控表更新数据<p>
	 * @author Jane.Luo
	 * @return Integer
	 * @date 2017年9月26日 下午2:33:00
	 */
	public Integer updatePtMg(Map<String, Object> where);
	
	/**
	 * <p>Description: 授权<p>
	 * @author Jane.Luo
	 * @return Integer
	 * @date 2017年9月26日 下午2:33:00
	 */
	public Integer updateOauth(Map<String, Object> where);
	
	/**
	 * <p>Description: 更新警告状态<p>
	 * @author Jane.Luo
	 * @return Integer
	 * @date 2017年9月26日 下午2:33:00
	 */
	public Integer updateIsWarn(Map<String, Object> where);
	
	/**
	 * <p>Description: 根据服务器ip查询平台管控表数据<p>
	 * @author Jane.Luo
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年9月26日 下午2:34:16
	 */
	public List<Map<String, Object>> queryPtMgByIp(Map<String, Object> where);
	
	/**
	 * <p>Description: 查询平台管控表数据<p>
	 * @author Jane.Luo
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年9月26日 下午2:34:16
	 */
	public List<Map<String, Object>> queryPtMg(Map<String, Object> where);
}
