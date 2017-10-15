package com.zlb.ecp.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zlb.ecp.data.ibiz.IPtLoginDataBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.JsonHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;
import com.zlb.ecp.redis.impl.JedisClientSingle;

/**
 * @Description: 平台登录数据 
 * @author Jane.Luo
 * @date 2017年8月9日 下午2:32:04 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/PtLoginDataService")
public class PtLoginDataService {

	private static final Logger log = LoggerFactory.getLogger(UserDataStatService.class);

	@Autowired
	private IPtLoginDataBiz ptLoginDataBiz;
	
	@Autowired
	private JedisClientSingle jedisClient;
	
	/**
	 * <p>Description: 获取顶部的4个总数据<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:25:13
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryTopNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryTopNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> listResult = new ListResult<>();
		List<Map<String, Object>> result = new ArrayList<>();
		String whereParam = JsonHelper.object2json(where);
		String cachResult = jedisClient.get("queryTopNum:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			result = jsonHelper.fromJson(cachResult,List.class);
			listResult.setRows(result);
			return listResult;
		} else {
			listResult = ptLoginDataBiz.queryTopNum(where);
			cachResult = JSON.toJSONString(listResult.getRows());
			jedisClient.set("queryTopNum:" + whereParam,cachResult);
			jedisClient.expire("queryTopNum:" + whereParam, 3600);
		}
		return listResult;
	}
	
	/**
	 * <p>Description: 平台登录用户分布<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 下午2:25:13
	 */
	@RequestMapping(value = "queryLoginUserDist.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryLoginUserDist(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = ptLoginDataBiz.queryLoginUserDist(where);
		return result;
	}
	
	/**
	 * <p>Description: 定时任务测试<p>
	 * @param request void
	 * @date 2017年8月10日 上午11:35:45
	 */
	@RequestMapping(value = "insertLoginMongoTimer.do", method = RequestMethod.GET)
	public void insertLoginMongoTimer(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ptLoginDataBiz.insertLoginMongoTimer();
	}
	
	/**
	 * <p>Description: 平台登录趋势分析<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 上午11:36:22
	 */
	@RequestMapping(value = "queryLoginTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryLoginTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = ptLoginDataBiz.queryLoginTrend(where);
		return result;
	}
	
}
