package com.zlb.ecp.data.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zlb.ecp.data.ibiz.IWebAccessDataBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 页面访问数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午3:32:06 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/WebAccessDataService")
public class WebAccessDataService {

	private static final Logger log = LoggerFactory.getLogger(WebAccessDataService.class);

	@Autowired
	private IWebAccessDataBiz webAccessDataBiz;

	/**
	 * <p>Description: 页面访问顶部4个数据<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午12:03:17
	 */
	@RequestMapping(value = "queryWebTopNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryWebTopNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = webAccessDataBiz.queryWebTopNum(where);
		return result;
	}
	
	/**
	 * <p>Description: 页面访问趋势分析<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 下午4:20:55
	 */
	@RequestMapping(value = "queryWebLoginTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryWebLoginTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = webAccessDataBiz.queryWebLoginTrend(where);
		return result;
	}

}
