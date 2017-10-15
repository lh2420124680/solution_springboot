package com.zlb.ecp.data.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IAppUseRankBiz;
import com.zlb.ecp.data.idao.IAppUseRankDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用排行 
 * @author Jane.Luo
 * @date 2017年8月17日 下午3:16:08 
 * @version V1.0
 */
@Service
public class AppUseRankBiz implements IAppUseRankBiz {

	@Autowired
	private IAppUseRankDao appUseRankDao;

	/**
	 * <p>Description: 应用使用排行 <p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午3:17:26
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUseRank(Map<String, Object> where) {
		return appUseRankDao.queryAppUseRank(where);
	}
	
	
}
