package com.zlb.ecp.data.ibiz;

import java.util.Map;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用明细 
 * @author Jane.Luo
 * @date 2017年8月17日 下午4:04:32 
 * @version V1.0
 */
public interface IAppDetailBiz {
	
	/**
	 * <p>
	 * Description: 应用使用人数
	 * <p>
	 * 
	 * @param where
	 * @return Map<String, Object>
	 * @date 2017年8月17日 下午4:05:32
	 */
	public Map<String, Object> queryAppUsePeoDetail(Map<String, Object> where);

	/**
	 * <p>Description: 应用使用明细<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午4:05:32
	 */
	public ListResult<Map<String, Object>> queryAppDetail(Map<String, Object> where);
}
