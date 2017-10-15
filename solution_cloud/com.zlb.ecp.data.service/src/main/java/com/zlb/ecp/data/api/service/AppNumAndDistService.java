package com.zlb.ecp.data.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.entity.datacentermo.ResourceStatisticsMo;
import com.zlb.api.IAppUsersStaticsService;
import com.zlb.ecp.data.service.AppUseAnalyService;

/**
 * @Description: 应用使用明细
 * @author Jane.Luo
 * @date 2017年8月17日 下午4:04:32
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/AppNumAndDistService")
public class AppNumAndDistService {

	private static final Logger log = LoggerFactory.getLogger(AppNumAndDistService.class);

	@Autowired
	private IAppUsersStaticsService appUsersStaticsService;

	/**
	 * <p>Description: 各类型应用统计<p>
	 * @param request
	 * @return List<ResourceStatisticsMo>
	 * @date 2017年8月18日 下午5:12:07
	 */
	@RequestMapping(value = "findAppStatics.do", method = RequestMethod.GET)
	public List<ResourceStatisticsMo> queryAppDetail(HttpServletRequest request) {
		List<ResourceStatisticsMo> result = appUsersStaticsService.findAppStatics();
		return result;
	}
	
	/**
	 * <p>Description: 各用户应用分布<p>
	 * @param request
	 * @return List<ResourceStatisticsMo>
	 * @date 2017年8月18日 下午5:12:07
	 */
	@RequestMapping(value = "findAppByUsers.do", method = RequestMethod.GET)
	public List<ResourceStatisticsMo> findAppByUsers(HttpServletRequest request) {
		List<ResourceStatisticsMo> result = appUsersStaticsService.findAppByUsers();
		return result;
	}
	
}
