package com.zlb.ecp.data.idao;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

public interface IUserUseStatDao {

	/**
	 * <p>Description: 平台使用用户<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午4:57:30
	 */
	public ListResult<Map<String, Object>> queryPtUserUseAllNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 平台使用用户根据gid查询<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午4:57:30
	 */
	public Map<String, Object> queryPtUserUseAllNumByGid(Map<String, String> where);
	
	/**
	 * <p>Description: 日活跃用户<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午4:58:01
	 */
	public Integer queryPreActiveNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 周活跃用户<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午5:00:59
	 */
	public Integer queryWeekActiveNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 月活跃用户<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午5:01:29
	 */
	public Integer queryMonthActiveNum(Map<String, Object> where);
	
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
	 * <p>Description: 用户使用趋势<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午11:05:38
	 */
	public ListResult<Map<String, Object>> queryUserUseTrend(Map<String, Object> where);
	
	/**
	 * <p>Description:定时任务
	 * @date 2017年8月9日 下午5:02:12
	 */
	public void insertMongoTimer();
}
