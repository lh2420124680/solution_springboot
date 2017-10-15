package com.zlb.ecp.ptmanager.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.EntityResult;
import com.zlb.ecp.pojo.ListResult;

public interface IPtMgCtrlBiz {
	
	/**
	 * <p>Description: 查询平台管控表数据<p>
	 * @author Jane.Luo
	 * @param map
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年9月26日 下午2:44:00
	 */
	public ListResult<Map<String, Object>> queryPtMg(Map<String, Object> where);
	
	/**
	 * <p>Description: 保存平台管控表数据<p>
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	public EntityResult<?> saveServerDetail(Map<String, Object> where);
	
	/**
	 * <p>Description: 授权<p>
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	public EntityResult<String> updateOauth(Map<String, Object> where);
	
	/**
	 * <p>Description: 验证远程服务器的mac地址<p>
	 * @author Jane.Luo
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年9月27日 上午10:50:52
	 */
	public EntityResult<?> validServerMac(Map<String, Object> where);
	
	public void queryIsOauthScheduled();

}
