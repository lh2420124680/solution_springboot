package com.zlb.ecp.data.api.service;

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
import com.zlb.api.ISpaceStaticsService;
import com.zlb.ecp.helper.DataConvertHelper;

/**
 * @Description: 空间开通数
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/ZoneOpenNumService")
public class ZoneOpenNumService {

	private static final Logger log = LoggerFactory.getLogger(ZoneOpenNumService.class);
	
	@Autowired
	private ISpaceStaticsService spaceStaticsService;
	
	/**
	 * <p>Description: 各区域空间统计<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceStaticsByTown.do", method = RequestMethod.GET)
	public List<?> findSpaceStaticsByTown(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceStaticsByTown(where);
		return result;
	}
	
	/**
	 * <p>Description: 各区域空间分布<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceDistribution.do", method = RequestMethod.GET)
	public List<?> findSpaceDistribution(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceDistribution(where);
		return result;
	}
	
	/**
	 * <p>Description: 各类型空间分布<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceTypeDis.do", method = RequestMethod.GET)
	public List<?> findSpaceTypeDis(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceTypeDis(where);
		return result;
	}
	
}
