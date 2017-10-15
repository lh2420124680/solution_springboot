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
import com.zlb.ecp.data.ibiz.IUserDataStatBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.JsonHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;
import com.zlb.ecp.redis.impl.JedisClientSingle;

/**
 * @Description: 用户使用数据模块
 * @author Jane.Luo
 * @date 2017年8月8日 上午9:31:03 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/UserDataStatService")
public class UserDataStatService {
	
	private static final Logger log = LoggerFactory.getLogger(UserDataStatService.class);
	
	@Autowired
	private IUserDataStatBiz userDataStatBiz;
	
	@Autowired
	private JedisClientSingle jedisClient;

	/**
	 * <p>Description: 用户使用数据顶部统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午9:30:26
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryUseStatNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryUseStatNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		String whereParam = JsonHelper.object2json(where);
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		String cachResult = jedisClient.get("queryUseStatNum:"+whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			list = jsonHelper.fromJson(cachResult,List.class);
			result.setRows(list);
			return result;
		} else {
			result = userDataStatBiz.queryUseStatNum(where);
			cachResult = JSON.toJSONString(result.getRows());
			jedisClient.set("queryUseStatNum:"+whereParam,cachResult);
			jedisClient.expire("queryUseStatNum:"+whereParam, 3600);
		}
		return result;
	}
	
	/**
	 * <p>Description: 各区域使用统计<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月8日 上午11:34:32
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryAreaUseStat.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaUseStat(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		String whereParam = JsonHelper.object2json(where);
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		String cachResult = jedisClient.get("queryAreaUseStat:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			list = jsonHelper.fromJson(cachResult,List.class);
			result.setRows(list);
			return result;
		} else {
			result = userDataStatBiz.queryAreaUseStat(where);
			cachResult = JSON.toJSONString(result.getRows());
			jedisClient.set("queryAreaUseStat:" + whereParam,cachResult);
			jedisClient.expire("queryAreaUseStat:" + whereParam, 3600);
		}
		return result;
	}
	
	/**
	 * <p>Description: 各区域活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:07:59
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryAreaActUser.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaActUser(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		String whereParam = JsonHelper.object2json(where);
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		String cachResult = jedisClient.get("queryAreaActUser:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			list = jsonHelper.fromJson(cachResult,List.class);
			result.setRows(list);
			return result;
		} else {
			result = userDataStatBiz.queryAreaActUser(where);
			cachResult = JSON.toJSONString(result.getRows());
			jedisClient.set("queryAreaActUser:" + whereParam,cachResult);
			jedisClient.expire("queryAreaActUser:" + whereParam, 3600);
		}
		return result;
	}
	
	/**
	 * <p>Description: 各类型活跃用户分布<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月9日 上午9:36:47
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryTypeActUser.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryTypeActUser(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		String whereParam = JsonHelper.object2json(where);
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		String cachResult = jedisClient.get("queryTypeActUser:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			list = jsonHelper.fromJson(cachResult,List.class);
			result.setRows(list);
			return result;
		} else {
			result = userDataStatBiz.queryTypeActUser(where);
			cachResult = JSON.toJSONString(result.getRows());
			jedisClient.set("queryTypeActUser:" + whereParam,cachResult);
			jedisClient.expire("queryTypeActUser:" + whereParam, 3600);
		}
		return result;
	}
	
	/**
	 * <p>Description: 用户使用趋势分析<p>
	 * @param request
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月10日 上午10:00:00
	 */
	@RequestMapping(value = "queryUserUseTrend.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryUserUseTrend(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = userDataStatBiz.queryUserUseTrend(where);
		return result;
	}
	
	/**
	 * <p>Description: 测试定时方法<p>
	 * @param request void
	 * @date 2017年8月10日 上午9:24:26
	 */
	@RequestMapping(value = "insertMongoTimer.do", method = RequestMethod.GET)
	public void insertMongoTimer(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		userDataStatBiz.insertMongoTimer();
	}
}
