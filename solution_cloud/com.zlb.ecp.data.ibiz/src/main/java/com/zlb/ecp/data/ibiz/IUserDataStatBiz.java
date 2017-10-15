package com.zlb.ecp.data.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 用户使用数据模块
 * @author Jane.Luo
 * @date 2017年8月8日 上午9:31:03 
 * @version V1.0
 */
public interface IUserDataStatBiz {

	/**
	 * <p>Description: 用户使用数据顶部统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午9:30:26
	 */
	public ListResult<Map<String, Object>> queryUseStatNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 平台使用用户根据gid查询<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午4:57:30
	 */
	public Map<String, Object> queryPtUserUseAllNumByGid(Map<String, String> where);
	
	/**
	 * <p>Description: 各区域使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午11:34:32
	 */
	public ListResult<Map<String, Object>> queryAreaUseStat(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:07:59
	 */
	public ListResult<Map<String, Object>> queryAreaActUser(Map<String, Object> where);
	
	/**
	 * <p>Description: 各类型活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:36:47
	 */
	public ListResult<Map<String, Object>> queryTypeActUser(Map<String, Object> where);
	
	/**
	 * <p>Description:定时任务
	 * @date 2017年8月9日 下午5:02:12
	 */
	public void insertMongoTimer();
	
	/**
	 * <p>Description: 用户使用趋势<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午11:05:38
	 */
	public ListResult<Map<String, Object>> queryUserUseTrend(Map<String, Object> where);
}
