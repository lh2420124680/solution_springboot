package com.zlb.ecp.data.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用统计
 * @author Jane.Luo
 * @date 2017年8月17日 上午11:39:28
 * @version V1.0
 */
public interface IAppUseStatBiz {

	/**
	 * <p>Description: 查询应用使用统计的顶部数据<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午11:53:42
	 */
	public ListResult<Map<String, Object>> queryAppUseTopNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域应用使用次数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	public ListResult<Map<String, Object>> queryAreaAppUse(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域应用使用人数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	public ListResult<Map<String, Object>> queryAreaAppUseNum(Map<String, Object> where);
}
