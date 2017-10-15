package com.zlb.ecp.data.idao;

import java.util.Map;

import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 积分排行榜 
 * @author Jane.Luo
 * @date 2017年8月18日 下午2:29:19 
 * @version V1.0
 */
public interface IInterRankDao {

	/**
	 * <p>Description: 积分排行榜 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	public ListResult<Map<String, Object>> queryInterRank(Map<String, Object> where);
	
	/**
	 * <p>Description: 积分排行榜导出 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	public ListResult<Map<String, Object>> queryInterRankExport(Map<String, Object> where);
}
