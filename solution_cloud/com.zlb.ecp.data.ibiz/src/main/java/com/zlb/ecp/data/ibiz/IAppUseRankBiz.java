package com.zlb.ecp.data.ibiz;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用排行 
 * @author Jane.Luo
 * @date 2017年8月17日 下午3:16:08 
 * @version V1.0
 */
public interface IAppUseRankBiz {

	/**
	 * <p>Description: 应用使用排行 <p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午3:17:26
	 */
	public ListResult<Map<String, Object>> queryAppUseRank(Map<String, Object> where);
}
