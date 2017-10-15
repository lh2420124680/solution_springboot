package com.zlb.ecp.data.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IUserDataStatBiz;
import com.zlb.ecp.data.idao.IUserUseStatDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 用户使用数据模块
 * @author Jane.Luo
 * @date 2017年8月8日 上午9:31:03 
 * @version V1.0
 */
@Service
public class UserDataStatBiz implements IUserDataStatBiz {
	
	@Autowired
	private IUserUseStatDao userUseStatDao;

	/**
	 * <p>Description: 用户使用数据顶部统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午9:30:26
	 */
	@Override
	public ListResult<Map<String, Object>> queryUseStatNum(Map<String, Object> where) {
		/*ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Integer queryPtUserUseAllNum = userUseStatDao.queryPtUserUseAllNum(where);
		Integer queryPreActiveNum = userUseStatDao.queryPreActiveNum(where);
		Integer queryWeekActiveNum = userUseStatDao.queryWeekActiveNum(where);
		Integer queryMonthActiveNum = userUseStatDao.queryMonthActiveNum(where);
		map.put("ptUserUseAllNum", queryPtUserUseAllNum);
		map.put("preActiveNum", queryPreActiveNum);
		map.put("weekActiveNum", queryWeekActiveNum);
		map.put("monthActiveNum", queryMonthActiveNum);
		list.add(map);
		result.setRows(list);*/
		ListResult<Map<String, Object>> result = userUseStatDao.queryPtUserUseAllNum(where);
		return result;
	}
	
	/**
	 * <p>Description: 平台使用用户根据gid查询<p>
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月7日 下午4:57:30
	 */
	public Map<String, Object> queryPtUserUseAllNumByGid(Map<String, String> where) {
		return userUseStatDao.queryPtUserUseAllNumByGid(where);
	}
	
	/**
	 * <p>Description: 各区域使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午11:34:32
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaUseStat(Map<String, Object> where) {
		return userUseStatDao.queryAreaUseStat(where);
	}
	
	/**
	 * <p>Description: 各区域活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:07:59
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaActUser(Map<String, Object> where) {
		return userUseStatDao.queryAreaActUser(where);
	}
	
	/**
	 * <p>Description: 各类型活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:36:47
	 */
	@Override
	public ListResult<Map<String, Object>> queryTypeActUser(Map<String, Object> where) {
		return userUseStatDao.queryTypeActUser(where);
	}
	
	/**
	 * <p>Description:定时任务
	 * @date 2017年8月9日 下午5:02:12
	 */
	@Override
	public void insertMongoTimer() {
		userUseStatDao.insertMongoTimer();
	}
	
	/**
	 * <p>Description: 用户使用趋势<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午11:05:38
	 */
	public ListResult<Map<String, Object>> queryUserUseTrend(Map<String, Object> where) {
		return userUseStatDao.queryUserUseTrend(where);
	}

}
