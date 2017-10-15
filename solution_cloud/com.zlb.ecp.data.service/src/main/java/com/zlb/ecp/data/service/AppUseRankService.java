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
import com.zlb.ecp.data.ibiz.IAppUseRankBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用排行
 * @author Jane.Luo
 * @date 2017年8月17日 下午3:16:08
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/AppUseRankService")
public class AppUseRankService {

	private static final Logger log = LoggerFactory.getLogger(AppUseAnalyService.class);

	@Autowired
	private IAppUseRankBiz appUseRankBiz;

	@Autowired
	private IAppUsersStaticsService appUsersStaticsService;

	/**
	 * <p>
	 * Description: 应用使用排行
	 * <p>
	 * 
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午3:17:26
	 */
	@RequestMapping(value = "queryAppUseRank.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAppUseRank(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseRankBiz.queryAppUseRank(where);
		List<Map<String, Object>> rows = result.getRows();
		Map<String, Object> dataMap = new HashMap<>();
		for (int i = 0; i < rows.size(); i++) {
			Map<String, Object> map = rows.get(i);
			dataMap.put(map.get("name").toString(), map.get("value"));
		}
		List<ResourceStatisticsMo> list = appUsersStaticsService.findAllApps();
		for (int i = 0; i < list.size(); i++) {
			String name1 = list.get(i).getName1();
			if (!dataMap.containsKey(name1)) {
				Map<String, Object> newMap = new HashMap<>();
				newMap.put("name", name1);
				newMap.put("value", 0);
				rows.add(newMap);
			}
		}
		return result;
	}
	
	/**
	 * <p>Description: 下拉应用查询<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月21日 上午10:18:58
	 */
	@RequestMapping(value = "findAllApps.do", method = RequestMethod.GET)
	public List<?> findAllApps(HttpServletRequest request) {
		List<ResourceStatisticsMo> result = appUsersStaticsService.findAllApps();
		return result;
	}
}
