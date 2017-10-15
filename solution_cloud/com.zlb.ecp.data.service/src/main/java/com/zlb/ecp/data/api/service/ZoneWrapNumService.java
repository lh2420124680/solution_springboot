package com.zlb.ecp.data.api.service;

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
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.StringHelper;

/**
 * @Description: 空间内容数
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/ZoneWrapNumService")
public class ZoneWrapNumService {

	private static final Logger log = LoggerFactory.getLogger(ZoneWrapNumService.class);

	@Autowired
	private ISpaceStaticsService spaceStaticsService;

	/**
	 * <p>
	 * Description: 各类型空间内容统计
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceContent.do", method = RequestMethod.GET)
	public List<?> findSpaceContent(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceContent(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 空间内容明细数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceContentDetail.do", method = RequestMethod.GET)
	public List<?> findSpaceContentDetail(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceContentDetail(where);
		return result;
	}
	
	/**
	 * <p>
	 * Description: 空间内容顶部数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "findSpaceCount.do", method = RequestMethod.GET)
	public List<?> findSpaceCount(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = spaceStaticsService.findSpaceCount(where);
		return result;
	}
	
	/**
	 * <p>Description: 导出表单<p>
	 * @param request
	 * @return EntityResult<?>
	 * @throws Exception 
	 * @date 2017年8月10日 下午4:52:03
	 */
	@RequestMapping(value = "exportZoneWrapDetailStat.do", method = RequestMethod.POST)
	public List<Map<String, Object>> exportResourceAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> downMap = new HashMap<>();
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> exportSpaceDetail = spaceStaticsService.exportSpaceDetail(where);
		List<Map<String, Object>> list = new ArrayList<>();
		for(int i = 0; i< exportSpaceDetail.size();i++){
			Map<String, Object> map = new HashMap<>();
			map.put("name1", exportSpaceDetail.get(i).getName1());
			map.put("value1", exportSpaceDetail.get(i).getValue1());
			map.put("value2", exportSpaceDetail.get(i).getValue2());
			map.put("value3", exportSpaceDetail.get(i).getValue3());
			map.put("value4", exportSpaceDetail.get(i).getValue4());
			map.put("value5", exportSpaceDetail.get(i).getValue5());
			map.put("value6", exportSpaceDetail.get(i).getValue6());
			map.put("value7", exportSpaceDetail.get(i).getValue7());
			list.add(map);
		}
		String newPath = "exportFile/dataCenter/zoneWrapDetail" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "name1", "value1", "value2", "value3", "value4", "value5", "value6", "value7" };
		String[] titleNames = { "区域", "教师空间(资源|文章|照片)", "学生空间(资源|文章|照片)", "家长空间(资源|文章|照片)", "班级空间(资源|文章|照片)", 
				"学校空间(资源|文章|照片)", "机构空间(资源|文章|照片)", "名师工作室(资源|文章|照片)" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "空间内容明细统计";
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
	 * @author Jane.Luo 2017年1月5日
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
