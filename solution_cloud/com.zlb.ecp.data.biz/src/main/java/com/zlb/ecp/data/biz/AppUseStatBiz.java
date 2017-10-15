package com.zlb.ecp.data.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IAppUseStatBiz;
import com.zlb.ecp.data.idao.IAppUseStatDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用统计
 * @author Jane.Luo
 * @date 2017年8月17日 上午11:39:28
 * @version V1.0
 */
@Service
public class AppUseStatBiz implements IAppUseStatBiz {

	@Autowired
	private IAppUseStatDao appUseStatDao;
	
	/**
	 * <p>Description: 查询应用使用统计的顶部数据<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午11:53:42
	 */
	@Override
	public ListResult<Map<String, Object>> queryAppUseTopNum(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Integer appUseNum = appUseStatDao.queryAppUseNum(where);
		Integer appBrowNum = appUseStatDao.queryAppBrowNum(where);
		Integer appUsePeoNum = appUseStatDao.queryAppUsePeoNum(where);
		map.put("appUsePeoNum", appUsePeoNum);
		map.put("appUseNum", appUseNum);
		map.put("appBrowNum", appBrowNum);
		list.add(map);
		result.setRows(list);
		return result;
	}
	
	/**
	 * <p>Description: 各区域应用使用人数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaAppUseNum(Map<String, Object> where) {
		return appUseStatDao.queryAreaAppUseNum(where);
	}
	
	/**
	 * <p>Description: 各区域应用使用次数<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaAppUse(Map<String, Object> where) {
		return appUseStatDao.queryAreaAppUse(where);
	}
}
