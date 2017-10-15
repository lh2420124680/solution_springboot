package com.zlb.ecp.data.service;

import java.util.ArrayList;
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

import com.zlb.ecp.data.ibiz.IAppUseStatBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;
import com.cloud.entity.bo.DictionaryBo;
import com.zlb.api.IDictinaryService;

/**
 * @Description: 应用使用统计
 * @author Jane.Luo
 * @date 2017年8月17日 上午11:39:28
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/AppUseStatService")
public class AppUseStatService {

	private static final Logger log = LoggerFactory.getLogger(AppUseStatService.class);

	@Autowired
	private IAppUseStatBiz appUseStatBiz;

	@Autowired
	private IDictinaryService dictinaryService;

	/**
	 * <p>
	 * Description: 查询应用使用统计的顶部数据
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 上午11:53:42
	 */
	@RequestMapping(value = "queryAppUseTopNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAppUseTopNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseStatBiz.queryAppUseTopNum(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各区域应用使用人数
	 * <p>
	 * 
	 * @param request
	 * @return List<ResourceStatisticsMo>
	 * @date 2017年8月18日 下午4:09:35
	 */
	@RequestMapping(value = "queryAreaAppUseNum.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaAppUseNum(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseStatBiz.queryAreaAppUseNum(where);
		List<Map<String, Object>> rows = result.getRows();
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, Object> newMap = new HashMap<>();
		List<Map<String, Object>> newList = new ArrayList<>();
		for (int i = 0; i < rows.size(); i++) {
			dataMap.put(rows.get(i).get("name").toString(), rows.get(i).get("value"));
		}
		Map<String, String> whereString = DataConvertHelper.getRequestParamString(request);
		String schoolGid = whereString.get("schoolGid");
		if (StringHelper.IsEmptyOrNull(schoolGid)) { 
			List<DictionaryBo> menuList = dictinaryService.listTownTree();
			String layerLevel = where.get("layerLevel").toString();
			if ("3".equals(layerLevel)) { // 区级
				List<DictionaryBo> list = dictinaryService.findSchoolName(whereString.get("gid"));
				
				for (int i = 0; i < list.size(); i++) {
					String dicName = list.get(i).getDicName();
					if (!dataMap.containsKey(dicName)) {
						newMap = new HashMap<>();
						newMap.put("name", dicName);
						newMap.put("value", 0);
						newList.add(newMap);
					} else {
						newMap = new HashMap<>();
						newMap.put("name", dicName);
						newMap.put("value", dataMap.get(dicName));
						newList.add(newMap);
					}
				}
			} else if ("2".equals(layerLevel)) { // 市级
				for(int i = 0; i < menuList.size(); i++){
					String level = menuList.get(i).getLayerLevel().toString();
					String parentGid = menuList.get(i).getParentGid();
					if ("3".equals(level) && "460200".equals(parentGid) ) {
						String dicName = menuList.get(i).getDicName();
						if (!dataMap.containsKey(dicName)) {
							newMap = new HashMap<>();
							newMap.put("name", dicName);
							newMap.put("value", 0);
							newList.add(newMap);
						} else {
							newMap = new HashMap<>();
							newMap.put("name", dicName);
							newMap.put("value", dataMap.get(dicName));
							newList.add(newMap);
						}
					}
				}
			} else if ("1".equals(layerLevel)) { // 省级
				for(int i = 0; i < menuList.size(); i++){
					String level = menuList.get(i).getLayerLevel().toString();
					if ("2".equals(level)) {
						String dicName = menuList.get(i).getDicName();
						if (!dataMap.containsKey(dicName)) {
							newMap = new HashMap<>();
							newMap.put("name", dicName);
							newMap.put("value", 0);
							newList.add(newMap);
						} else {
							newMap = new HashMap<>();
							newMap.put("name", dicName);
							newMap.put("value", dataMap.get(dicName));
							newList.add(newMap);
						}
					}
				}
			} 
		} else {
			if (dataMap.size() == 0) {
				dataMap.put("name", where.get("schoolName"));
				dataMap.put("value", 0);
				newList.add(dataMap);
			} 
		}
		ListResult<Map<String, Object>> newResult = new ListResult<>();
		newResult.setRows(newList);
		return newResult;
	}

	/**
	 * <p>
	 * Description: 各区域应用使用次数
	 * <p>
	 * 
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午12:02:03
	 */
	@RequestMapping(value = "queryAreaAppUse.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAreaAppUse(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = appUseStatBiz.queryAreaAppUse(where);
		return result;
	}

}
