package com.zlb.ecp.data.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zlb.ecp.data.ibiz.IZoneUseAnalyBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 空间使用分析
 * @author Jane.Luo
 * @date 2017年8月16日 下午4:48:06
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/ZoneUseAnalyService")
public class ZoneUseAnalyService {

	private static final Logger log = LoggerFactory.getLogger(ZoneUseAnalyService.class);

	@Autowired
	private IZoneUseAnalyBiz zoneUseAnalyBiz;
	
	/**
	 * <p>Description: 查询空间使用分析顶部的几条总数据<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:05:48
	 */
	@RequestMapping(value = "queryZoneUseAnyTopNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryZoneUseAnyTopNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = zoneUseAnalyBiz.queryZoneUseAnyTopNum(where);
		return result;
	}
	
	/**
	 * <p>Description: 各区域空间使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:26:35
	 */
	@RequestMapping(value = "queryAreaZoneUseStat.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaZoneUseStat(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = zoneUseAnalyBiz.queryAreaZoneUseStat(where);
		return result;
	}
	
	/**
	 * <p>Description: 各区域活跃空间分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月16日 下午5:26:35
	 */
	@RequestMapping(value = "queryAreaZoneDistUser.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaZoneDistUser(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = zoneUseAnalyBiz.queryAreaZoneDistUser(where);
		return result;
	}
	
	/**
	 * <p>Description: 各类型活跃空间分布<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:41:54
	 */
	@RequestMapping(value = "queryTypeZoneUser.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryTypeZoneUser(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = zoneUseAnalyBiz.queryTypeZoneUser(where);
		return result;
	}
	
	/**
	 * <p>Description: 定时去存每一天的活跃数据(测试方法)<p>
	 * void
	 * @date 2017年8月17日 上午10:08:43
	 */
	@RequestMapping(value = "insertMongoTimer.do", method = RequestMethod.GET)
	public void insertMongoTimer(HttpServletRequest request) {
		zoneUseAnalyBiz.insertMongoTimer();
	}
	
	/**
	 * <p>Description: 空间使用趋势分析<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午9:41:54
	 */
	@RequestMapping(value = "queryZoneUseTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryZoneUseTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = zoneUseAnalyBiz.queryZoneUseTrend(where);
		return result;
	}
}
