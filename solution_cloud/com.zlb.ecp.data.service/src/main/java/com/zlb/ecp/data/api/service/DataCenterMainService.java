package com.zlb.ecp.data.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.entity.datacentermo.ResourceStatisticsMo;
import com.zlb.api.IDataCenterService;
import com.zlb.ecp.data.ibiz.IUserDataStatBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.JsonHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;
import com.zlb.ecp.redis.impl.JedisClientSingle;

/**
 * @Description: 数据服务中心首页
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/DataCenterMainService")
public class DataCenterMainService {

	private static final Logger log = LoggerFactory.getLogger(DataCenterMainService.class);

	@Autowired
	private IDataCenterService dataCenterService;
	
	@Autowired
	private IUserDataStatBiz userDataStatBiz;
	
	@Autowired
	private JedisClientSingle jedisClient;
	
	/**
	 * <p>Description: 资源数饼状图<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findResourceStatistics.do", method = RequestMethod.GET)
	public List<?> findResourceStatistics(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findResourceStatistics(where);
		return result;
	}
	
	/**
	 * <p>Description: 资源贡献榜<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findResourceContribution.do", method = RequestMethod.GET)
	public List<?> findResourceContribution(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findResourceContribution(where);
		return result;
	}
	
	/**
	 * <p>Description: 地图查询学校数 用户数<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "findUsersAndSchoolStatics.do", method = RequestMethod.GET)
	public List<?> findUsersAndSchoolStatics(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		Map<String, Object> requestParams = DataConvertHelper.getRequestParams(request);
		String whereParam = JsonHelper.object2json(requestParams);
		List<?> result = new ArrayList<>();
		List<Map<String, Object>> list = new ArrayList<>();
		String cachResult = jedisClient.get("findUsersAndSchoolStatics:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			result = jsonHelper.fromJson(cachResult,List.class);
			return result;
		} else {
			result = dataCenterService.findUsersAndSchoolStatics(where);
			ListResult<Map<String, Object>> queryUseStatNum = userDataStatBiz.queryUseStatNum(requestParams);
			Map<String, Object> map = new HashMap<>();
			map.put("resourceStatisticsMo", result);
			map.put("userUseNum", queryUseStatNum.getRows().get(0).get("ptUserUseAllNum"));
			list.add(map);
			cachResult = JSON.toJSONString(list);
			jedisClient.set("findUsersAndSchoolStatics:" + whereParam,cachResult);
			jedisClient.expire("findUsersAndSchoolStatics:" + whereParam, 3600);
		}
		return list;
	}
	
	/**
	 * <p>Description:地图下面，用户类型分布<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserType.do", method = RequestMethod.GET)
	public List<?> findUserType(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findUserType(where);
		return result;
	}
	
	/**
	 * <p>Description:机构学段分布<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserStage.do", method = RequestMethod.GET)
	public List<?> findUserStage(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findUserStage(where);
		return result;
	}
	
	/**
	 * <p>Description:教师年龄分布<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserAge.do", method = RequestMethod.GET)
	public List<?> findUserAge(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findUserAge(where);
		return result;
	}
	
	/**
	 * <p>Description: 应用数统计<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserAPP.do", method = RequestMethod.GET)
	public List<?> findUserAPP(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findUserAPP(where);
		return result;
	}
	
	/**
	 * <p>Description: 空间数统计<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserSpaceNum.do", method = RequestMethod.GET)
	public List<?> findUserSpaceNum(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = dataCenterService.findUserSpaceNum(where);
		return result;
	}
	
	/**
	 * <p>Description: 查询用户和学校统计<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月13日 下午12:02:02
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "findUsersAndSchoolStaticsByMap.do", method = RequestMethod.GET)
	public List<?> findUsersAndSchoolStaticsByMap(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		String whereParam = JsonHelper.object2json(where);
		List<ResourceStatisticsMo> result = new ArrayList<>();
		String cachResult = jedisClient.get("findUsersAndSchoolStaticsByMap:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			result = jsonHelper.fromJson(cachResult,List.class);
			return result;
		} else {
			result = dataCenterService.findUsersAndSchoolStaticsByMap(where);
			Map<String, Object> map = userDataStatBiz.queryPtUserUseAllNumByGid(where);
			for(int i = 0; i < result.size(); i++) {
				String name = result.get(i).getName1();
				if (map.containsKey(name)) {
					result.get(i).setValue3(map.get(name).toString());
				}
			}
			cachResult = JSON.toJSONString(result);
			jedisClient.set("findUsersAndSchoolStaticsByMap:" + whereParam,cachResult);
			jedisClient.expire("findUsersAndSchoolStaticsByMap:" + whereParam, 3600);
		}
		return result;
	}
	
}
