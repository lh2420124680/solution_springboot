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
import com.zlb.api.ISpaceStaticsService;
import com.zlb.ecp.data.api.service.ZoneOpenNumService;
import com.zlb.ecp.data.ibiz.IZoneUseAnalyBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.DataCommDto;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 空间明细数据
 * @author Jane.Luo
 * @date 2017年8月16日 下午4:48:06
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/ZoneDetailService")
public class ZoneDetailService {

	private static final Logger log = LoggerFactory.getLogger(ZoneDetailService.class);

	@Autowired
	private ISpaceStaticsService spaceStaticsService;

	@Autowired
	private IZoneUseAnalyBiz zoneUseAnalyBiz;

	/**
	 * <p>
	 * Description: 空间明细数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceStaticsByTown.do", method = RequestMethod.GET)
	public List<?> findSpaceStaticsByTown(HttpServletRequest request) {
		Map<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		Map<String, Object> dataMap = zoneUseAnalyBiz.queryAnyZone(where);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceStaticsByTown(whereStr);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			String name1 = result.get(i).getName1();
			Map<String, Object> map = new HashMap<>();
			if (dataMap.containsKey(name1)) {
				DataCommDto dcd = (DataCommDto) dataMap.get(name1);
				map.put("name", name1);
				// 开通数
				map.put("teaOpen", result.get(i).getValue1());
				map.put("stuOpen", result.get(i).getValue2());
				map.put("jzOpen", result.get(i).getValue3());
				map.put("clsOpen", result.get(i).getValue4());
				map.put("schOpen", result.get(i).getValue5());
				map.put("mechOpen", result.get(i).getValue6());
				map.put("msOpen", result.get(i).getValue7());
				// 使用数据
				map.put("teaUse", dcd.getValueOne());
				map.put("stuUse", dcd.getValueTwo());
				map.put("jzUse", 0);
				map.put("clsUse", dcd.getValueThr());
				map.put("schUse", dcd.getValueFou());
				map.put("mechUse", dcd.getValueFiv());
				map.put("msUse", 0);
				list.add(map);
			} else {
				map.put("name", name1);
				// 开通数
				map.put("teaOpen", result.get(i).getValue1());
				map.put("stuOpen", result.get(i).getValue2());
				map.put("jzOpen", result.get(i).getValue3());
				map.put("clsOpen", result.get(i).getValue4());
				map.put("schOpen", result.get(i).getValue5());
				map.put("mechOpen", result.get(i).getValue6());
				map.put("msOpen", result.get(i).getValue7());
				// 使用数据
				map.put("teaUse", 0);
				map.put("stuUse", 0);
				map.put("jzUse", 0);
				map.put("clsUse", 0);
				map.put("schUse", 0);
				map.put("mechUse", 0);
				map.put("msUse", 0);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * <p>
	 * Description: 空间明细数据导出
	 * <p>
	 * 
	 * @param request
	 * @return EntityResult<T>
	 * @throws Exception
	 * @date 2017年8月17日 下午4:28:48
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "exportSpaceDetail.do", method = { RequestMethod.POST, RequestMethod.GET })
	public List<Map<String, Object>> exportSpaceDetail(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> downMap = new HashMap<>();
		Map<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		Map<String, Object> dataMap = zoneUseAnalyBiz.queryAnyZone(where);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceStaticsByTown(whereStr);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			String name1 = result.get(i).getName1();
			Map<String, Object> map = new HashMap<>();
			if (dataMap.containsKey(name1)) {
				DataCommDto dcd = (DataCommDto) dataMap.get(name1);
				map.put("name", name1);
				map.put("teaOpen", result.get(i).getValue1() + "|" + dcd.getValueOne());
				map.put("stuOpen", result.get(i).getValue2() + "|" + dcd.getValueTwo());
				map.put("jzOpen", result.get(i).getValue3() + "|" + 0);
				map.put("clsOpen", result.get(i).getValue4() + "|" + dcd.getValueThr());
				map.put("schOpen", result.get(i).getValue5() + "|" + dcd.getValueFou());
				map.put("mechOpen", result.get(i).getValue6() + "|" + dcd.getValueFiv());
				map.put("msOpen", result.get(i).getValue7() + "|" + 0);
				list.add(map);
			} else {
				map.put("name", name1);
				map.put("teaOpen", result.get(i).getValue1() + "|" + 0);
				map.put("stuOpen", result.get(i).getValue2() + "|" + 0);
				map.put("jzOpen", result.get(i).getValue3() + "|" + 0);
				map.put("clsOpen", result.get(i).getValue4() + "|" + 0);
				map.put("schOpen", result.get(i).getValue5() + "|" + 0);
				map.put("mechOpen", result.get(i).getValue6() + "|" + 0);
				map.put("msOpen", result.get(i).getValue7() + "|" + 0);
				list.add(map);
			}
		}
		String newPath = "exportFile/dataCenter/zoneDetail" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "name", "teaOpen", "stuOpen", "jzOpen", "clsOpen", "schOpen", "mechOpen", "msOpen" };
		String[] titleNames = { "区域", "教师空间(开通数|使用数)", "学生空间(开通数|使用数)", "家长空间(开通数|使用数)", "班级空间(开通数|使用数)",
				"学校空间(开通数|使用数)", "机构空间(开通数|使用数)", "名师工作室(开通数|使用数)" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "空间明细";
		String fileName = realPath + File.separator + sheetName + ".xlsx";
		new ExcelHelper().writeDataToExcel(fileName, sheetName, titleNames, columnNames, list);
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
