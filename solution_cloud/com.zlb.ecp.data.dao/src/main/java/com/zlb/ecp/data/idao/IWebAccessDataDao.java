package com.zlb.ecp.data.idao;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 页面访问数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午3:32:06 
 * @version V1.0
 */
public interface IWebAccessDataDao {

	/**
	 * <p>Description: 今日浏览次数<p>
	 * @return Integer
	 * @date 2017年8月9日 下午3:33:18
	 */
	public Integer currBrowseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 今日独立访客<p>
	 * @return Integer
	 * @date 2017年8月9日 下午3:33:18
	 */
	public Integer currIndepCustNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 今日访问Ip数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午3:35:17
	 */
	public Integer currIpNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 今日访问次数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月9日 下午3:35:53
	 */
	public Integer currAccessNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 页面访问趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午1:49:21
	 */
	public ListResult<Map<String, Object>> queryWebLoginTrend(Map<String, Object> where);
}
