package com.zlb.ecp.data.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 平台登录数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午2:32:04 
 * @version V1.0
 */
public interface IPtLoginDataBiz {

	/**
	 * <p>Description: 获取顶部的总数据<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:19:46
	 */
	public ListResult<Map<String, Object>> queryTopNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 平台登录用户分布<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:42:25
	 */
	public ListResult<Map<String, Object>> queryLoginUserDist(Map<String, Object> where);
	
	/**
	 * <p>Description: 平台登录趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 上午10:51:32
	 */
	public ListResult<Map<String, Object>> queryLoginTrend(Map<String, Object> where);
	
	/**
	 * <p>Description:平台登录趋势定时任务<p>
	 * @date 2017年8月9日 下午5:02:12
	 */
	public void insertLoginMongoTimer();
}
