package com.zlb.ecp.data.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IAppUseAnalyBiz;
import com.zlb.ecp.data.idao.IAppUseAnalyDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用分析
 * @author Jane.Luo
 * @date 2017年8月17日 下午2:16:06
 * @version V1.0
 */
@Service
public class AppUseAnalyBiz implements IAppUseAnalyBiz {

	@Autowired
	private IAppUseAnalyDao appUseAnalyDao;
	
	/**
	 * <p>
	 * Description: 各应用使用人数趋势分析
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUsePeoNumTrend(Map<String, Object> where) {
		return appUseAnalyDao.queryAppUsePeoNumTrend(where);
	}
	
	/**
	 * <p>Description: 各应用使用次数趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUseNumTrend(Map<String, Object> where) {
		return appUseAnalyDao.queryAppUseNumTrend(where);
	}
	
}
