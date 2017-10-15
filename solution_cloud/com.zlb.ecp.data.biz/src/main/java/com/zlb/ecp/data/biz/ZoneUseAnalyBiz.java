package com.zlb.ecp.data.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlb.ecp.data.ibiz.IZoneUseAnalyBiz;
import com.zlb.ecp.data.idao.IZoneUseAnalyDao;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 空间使用分析
 * @author Jane.Luo
 * @date 2017年8月16日 下午4:48:06
 * @version V1.0
 */
@Service
public class ZoneUseAnalyBiz implements IZoneUseAnalyBiz {

	@Autowired
	private IZoneUseAnalyDao zoneUseAnalyDao;

	/**
	 * <p>Description: 查询空间使用分析顶部的几条总数据<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:05:48
	 */
	@Override
	public ListResult<Map<String, Object>> queryZoneUseAnyTopNum(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		Integer stuZoneUseNum = zoneUseAnalyDao.queryStuZoneUseNum(where);
		Integer teacherZoneUseNum = zoneUseAnalyDao.queryTeacherZoneUseNum(where);
		Integer classZoneUseNum = zoneUseAnalyDao.queryClassZoneUseNum(where);
		Integer schoolZoneUseNum = zoneUseAnalyDao.querySchoolZoneUseNum(where);
		Integer mechZoneUseNum = zoneUseAnalyDao.queryMechZoneUseNum(where);
		map.put("stuZoneUseNum", stuZoneUseNum);
		map.put("teacherZoneUseNum", teacherZoneUseNum);
		map.put("classZoneUseNum", classZoneUseNum);
		map.put("schoolZoneUseNum", schoolZoneUseNum);
		map.put("mechZoneUseNum", mechZoneUseNum);
		list.add(map);
		result.setRows(list);
		return result;
	}
	
	/**
	 * <p>Description: 各区域空间使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:26:35
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaZoneUseStat(Map<String, Object> where) {
		return zoneUseAnalyDao.queryAreaZoneUseStat(where);
	}
	
	/**
	 * <p>Description: 各区域活跃空间分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:16:01
	 */
	@Override
	public ListResult<Map<String, Object>> queryAreaZoneDistUser(Map<String, Object> where) {
		return zoneUseAnalyDao.queryAreaZoneDistUser(where);
	}
	
	/**
	 * <p>Description: 各类型活跃空间分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:40:10
	 */
	@Override
	public ListResult<Map<String, Object>> queryTypeZoneUser(Map<String, Object> where) {
		return zoneUseAnalyDao.queryTypeZoneUser(where);
	}
	
	/**
	 * <p>Description: 定时去存每一天的活跃数据(测试方法)<p>
	 * void
	 * @date 2017年8月17日 上午10:08:43
	 */
	@Override
	public void insertMongoTimer() {
		zoneUseAnalyDao.insertMongoTimer();
	}
	
	/**
	 * <p>Description: 空间使用趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午10:01:12
	 */
	@Override
	public ListResult<Map<String, Object>> queryZoneUseTrend(Map<String, Object> where) {
		return zoneUseAnalyDao.queryZoneUseTrend(where);
	}
	

	/**
	 * <p>Description: 查询各空间的使用数量<p>
	 * @param where
	 * @return Map<String,List<Map<String,Object>>>
	 * @date 2017年8月18日 下午3:45:51
	 */
	@Override
	public Map<String, Object> queryAnyZone(Map<String, Object> where) {
		return zoneUseAnalyDao.queryAnyZone(where);
	}
	
}
