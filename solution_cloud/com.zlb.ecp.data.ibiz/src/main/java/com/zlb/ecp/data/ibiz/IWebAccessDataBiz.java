package com.zlb.ecp.data.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 页面访问数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午3:32:06 
 * @version V1.0
 */
public interface IWebAccessDataBiz {

	/**
	 * <p>Description: 页面访问顶部数据<p>
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月10日 上午11:58:39
	 */
	public ListResult<Map<String, Object>> queryWebTopNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 页面访问趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午1:49:21
	 */
	public ListResult<Map<String, Object>> queryWebLoginTrend(Map<String, Object> where);
	
}
