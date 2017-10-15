package com.zlb.ecp.data.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.entity.datacentermo.ResourceStatisticsMo;
import com.zlb.api.IAppUsersStaticsService;
import com.zlb.ecp.data.ibiz.IAppUseAnalyBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用分析
 * @author Jane.Luo
 * @date 2017年8月17日 下午2:16:06
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/AppUseAnalyService")
public class AppUseAnalyService {

	private static final Logger log = LoggerFactory.getLogger(AppUseAnalyService.class);

	@Autowired
	private IAppUseAnalyBiz appUseAnalyBiz;
	
	/**
	 * <p>Description: 各应用使用人数趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@RequestMapping(value = "queryAppUsePeoNumTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAppUsePeoNumTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseAnalyBiz.queryAppUsePeoNumTrend(where);
		return result;
	}
	
	/**
	 * <p>Description: 各应用使用次数趋势分析<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@RequestMapping(value = "queryAppUseNumTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAppUseNumTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseAnalyBiz.queryAppUseNumTrend(where);
		return result;
	}
	
}
