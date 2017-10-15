package com.zlb.ecp.data.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IInterRankBiz;
import com.zlb.ecp.data.idao.IInterRankDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 积分排行榜 
 * @author Jane.Luo
 * @date 2017年8月18日 下午2:29:19 
 * @version V1.0
 */
@Service
public class InterRankBiz implements IInterRankBiz {

	@Autowired
	private IInterRankDao interRankDao;
	
	/**
	 * <p>Description: 积分排行榜 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	@Override
	public ListResult<Map<String, Object>> queryInterRank(Map<String, Object> where) {
		return interRankDao.queryInterRank(where);
	}
	
	/**
	 * <p>Description: 积分排行榜导出 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	@Override
	public ListResult<Map<String, Object>> queryInterRankExport(Map<String, Object> where) {
		return interRankDao.queryInterRankExport(where);
	}

}
