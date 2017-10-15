package com.zlb.ecp.data.service;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cloud.entity.datacentermo.ResourceStatisticsMo;
import com.zlb.api.IAppUsersStaticsService;
import com.zlb.ecp.data.ibiz.IAppDetailBiz;
import com.zlb.ecp.data.ibiz.IAppUseStatBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 应用使用明细 
 * @author Jane.Luo
 * @date 2017年8月17日 下午4:04:32 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/AppDetailService")
public class AppDetailService {

	private static final Logger log = LoggerFactory.getLogger(AppUseAnalyService.class);

	@Autowired
	private IAppDetailBiz appDetailBiz;
	
	@Autowired
	private IAppUsersStaticsService appUsersStaticsService;
	
	/**
	 * <p>Description: 应用使用明细<p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月17日 下午2:18:12
	 */
	@RequestMapping(value = "queryAppDetail.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryAppDetail(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		HashMap<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		ListResult<Map<String, Object>> result = appDetailBiz.queryAppDetail(where);
		List<Map<String, Object>> rows = result.getRows();
		Map<String, Object> dataMap = new HashMap<>();
		for (int i = 0; i < rows.size(); i++) {
			Map<String, Object> map = rows.get(i);
			dataMap.put(map.get("appName").toString(), map.get("appTypeName").toString()+":"+map.get("actNum"));
		}
		Map<String, Object> queryAppUsePeoDetail = appDetailBiz.queryAppUsePeoDetail(where);
		for(Map.Entry<String, Object> entry : queryAppUsePeoDetail.entrySet()) {
			String key = entry.getKey();
			if (dataMap.containsKey(key)) {
				dataMap.put(key, dataMap.get(key) + ":" + entry.getValue());
			}
		}
		List<ResourceStatisticsMo> list = appUsersStaticsService.findAppUserStatics(whereStr);
		List<Map<String, Object>> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String name1 = list.get(i).getName1();
			Map<String, Object> map = new HashMap<>();
			if (dataMap.containsKey(name1)) {
				String[] split = dataMap.get(name1).toString().split(":");
				map.put("appName", name1);
				map.put("appTypeName", split[0]);
				map.put("actNum", split[1]);
				map.put("usePeople", split[2]);
				newList.add(map);
			} else {
				map.put("appName", list.get(i).getName1());
				map.put("appTypeName", list.get(i).getValue1());
				map.put("actNum", 0);
				map.put("usePeople", 0);
				newList.add(map);
			}
		}
		ListResult<Map<String, Object>> newResult = new ListResult<>();
		newResult.setRows(newList);
		return newResult;
	}
	
	/**
	 * <p>Description: 应用使用明细导出<p>
	 * @param request
	 * @return EntityResult<T>
	 * @throws Exception 
	 * @date 2017年8月17日 下午4:28:48
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "exportAppDetail.do", method = {RequestMethod.POST,RequestMethod.GET})
	public List<Map<String, Object>> exportAppDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> downMap = new HashMap<>();
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		HashMap<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		ListResult<Map<String, Object>> result = appDetailBiz.queryAppDetail(where);
		List<Map<String, Object>> rows = result.getRows();
		Map<String, Object> dataMap = new HashMap<>();
		for (int i = 0; i < rows.size(); i++) {
			Map<String, Object> map = rows.get(i);
			dataMap.put(map.get("appName").toString(), map.get("appTypeName").toString()+":"+map.get("actNum"));
		}
		Map<String, Object> queryAppUsePeoDetail = appDetailBiz.queryAppUsePeoDetail(where);
		for(Map.Entry<String, Object> entry : queryAppUsePeoDetail.entrySet()) {
			String key = entry.getKey();
			if (dataMap.containsKey(key)) {
				dataMap.put(key, dataMap.get(key) + ":" + entry.getValue());
			}
		}
		List<ResourceStatisticsMo> list = appUsersStaticsService.findAppUserStatics(whereStr);
		List<Map<String, Object>> newList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String name1 = list.get(i).getName1();
			Map<String, Object> map = new HashMap<>();
			if (dataMap.containsKey(name1)) {
				String[] split = dataMap.get(name1).toString().split(":");
				map.put("appName", name1);
				map.put("appTypeName", split[0]);
				map.put("actNum", split[1]);
				map.put("usePeople", split[2]);
				newList.add(map);
			} else {
				map.put("appName", list.get(i).getName1());
				map.put("appTypeName", list.get(i).getValue1());
				map.put("actNum", 0);
				map.put("usePeople", 0);
				newList.add(map);
			}
		}
		String newPath = "exportFile/dataCenter/appDetail" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "appTypeName", "appName", "usePeople", "actNum" };
		String[] titleNames = { "应用类型", "应用名称", "使用人数", "使用次数" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "应用使用明细";
		String fileName = realPath + File.separator + sheetName + ".xlsx";
		new ExcelHelper().writeDataToExcel(fileName, sheetName, titleNames, columnNames, newList);
		String downloadUrl = "/" + newPath.replace(File.separator, "/") + "/" + sheetName + ".xlsx";
		downMap.put("downloadUrl", downloadUrl);
		entityResult.add(downMap);
		returnHtml(entityResult, response);
		return entityResult;
	}
	
	/**
	 * <返回html格式结果>
	 *
	 * @param uploadResult
	 * @param response
	 * @author 罗浩 2017年1月5日
	 */
	private void returnHtml(List<Map<String, Object>> uploadResult, HttpServletResponse response) {
		try {
			String jsonStr = JSON.toJSONString(uploadResult);

			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			OutputStream os = response.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
