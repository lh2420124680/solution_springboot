package com.zlb.ecp.data.idao;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用统计 
 * @author Jane.Luo
 * @date 2017年8月17日 上午11:39:28 
 * @version V1.0
 */
public interface IAppUseStatDao {
	
	/**
	 * <p>Description: 应用使用人数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:46
	 */
	public Integer queryAppUsePeoNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 应用使用次数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:36
	 */
	public Integer queryAppUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 应用浏览次数<p>
	 * @param where
	 * @return Integer
	 * @date 2017年8月17日 上午11:40:46
	 */
	public Integer queryAppBrowNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域应用使用人数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	public ListResult<Map<String, Object>> queryAreaAppUseNum(Map<String, Object> where);
	
	/**
	 * <p>Description: 各区域应用使用次数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	public ListResult<Map<String, Object>> queryAreaAppUse(Map<String, Object> where);

}
