package com.zlb.ecp.data.idao;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用分析
 * @author Jane.Luo
 * @date 2017年8月17日 下午2:16:06 
 * @version V1.0
 */
public interface IAppUseAnalyDao {
	
	/**
	 * <p>
	 * Description: 各应用使用人数趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	public ListResult<Map<String, Object>> queryAppUsePeoNumTrend(Map<String, Object> where);

	/**
	 * <p>Description: 各应用使用次数趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	public ListResult<Map<String, Object>> queryAppUseNumTrend(Map<String, Object> where);
}
