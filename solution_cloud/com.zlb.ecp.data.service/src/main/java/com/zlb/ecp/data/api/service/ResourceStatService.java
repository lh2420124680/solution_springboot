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
import com.zlb.api.IResourceStaticsInfoService;
import com.zlb.ecp.data.service.WebAccessDataService;
import com.zlb.ecp.entity.EntityResult;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 资源数据
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/ResourceStatService")
public class ResourceStatService {

	private static final Logger log = LoggerFactory.getLogger(WebAccessDataService.class);

	@Autowired
	private IResourceStaticsInfoService resourceStaticsInfoService;

	/**
	 * <p>
	 * Description: 导出表单
	 * <p>
	 * 
	 * @param request
	 * @return EntityResult<?>
	 * @throws Exception
	 * @date 2017年8月10日 下午4:52:03
	 */
	@RequestMapping(value = "exportResourceAnalysis.do", method = RequestMethod.POST)
	public List<Map<String, Object>> exportResourceAnalysis(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> downMap = new HashMap<>();
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> exportResourceAnalysis = resourceStaticsInfoService.exportResourceAnalysis(where);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < exportResourceAnalysis.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("name1", exportResourceAnalysis.get(i).getName1());
			map.put("value1", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue1()) == false
					? exportResourceAnalysis.get(i).getValue1() : 0);
			map.put("value2", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue2()) == false
					? exportResourceAnalysis.get(i).getValue2() : 0);
			map.put("value3", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue3()) == false
					? exportResourceAnalysis.get(i).getValue3() : 0);
			map.put("value4", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue4()) == false
					? exportResourceAnalysis.get(i).getValue4() : 0);
			map.put("value5", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue5()) == false
					? exportResourceAnalysis.get(i).getValue5() : 0);
			map.put("value6", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue6()) == false
					? exportResourceAnalysis.get(i).getValue6() : 0);
			map.put("value7", StringHelper.IsEmptyOrNull(exportResourceAnalysis.get(i).getValue7()) == false
					? exportResourceAnalysis.get(i).getValue7() : 0);
			list.add(map);
		}
		String newPath = "exportFile/dataCenter/resDetail" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "name1", "value1", "value2", "value3", "value4", "value5", "value6", "value7" };
		String[] titleNames = { "区域", "资源总数", "资源上传量", "微课上传量", "资源阅读量", "资源下载量", "资源收藏量", "资源评论量" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "资源明细数据";
		String fileName = realPath + File.separator + sheetName + ".xlsx";
		new ExcelHelper().writeDataToExcel(fileName, sheetName, titleNames, columnNames, list);
		String downloadUrl = "/" + newPath.replace(File.separator, "/") + "/" + sheetName + ".xlsx";
		downMap.put("downloadUrl", downloadUrl);
		entityResult.add(downMap);
		returnHtml(entityResult, response);
		return entityResult;
	}

	/**
	 * <p>
	 * Description: 各年级资源分布饼状图
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findGradeResource.do", method = RequestMethod.GET)
	public List<?> findGradeResource(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findGradeResource(where);
		return result;
	}

	/**
	 * <p>
	 * Description:
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:37
	 */
	@RequestMapping(value = "findResourceDetail.do", method = RequestMethod.GET)
	public List<?> findResourceDetail(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findResourceDetail(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 资源明细数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:50
	 */
	@RequestMapping(value = "findResourceTrends.do", method = RequestMethod.GET)
	public List<?> findResourceTrends(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findResourceTrends(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各区域资源使用统计
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:53:12
	 */
	@RequestMapping(value = "findServiceResource.do", method = RequestMethod.GET)
	public List<?> findServiceResource(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findServiceResource(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各学科资源分布饼状图
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:53:26
	 */
	@RequestMapping(value = "findSubjectResource.do", method = RequestMethod.GET)
	public List<?> findSubjectResource(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findSubjectResource(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 获取区资源
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月28日 下午12:10:25
	 */
	@RequestMapping(value = "findTownResource.do", method = RequestMethod.GET)
	public List<?> findTownResource(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findTownResource(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各区域本地资源类型分布
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:53:40
	 */
	@RequestMapping(value = "findTypeResource.do", method = RequestMethod.GET)
	public List<?> findTypeResource(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findTypeResource(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各类型用户使用分布
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:53:58
	 */
	@RequestMapping(value = "findUserTypeStatistics.do", method = RequestMethod.GET)
	public List<?> findUserTypeStatistics(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findUserTypeStatistics(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 查询资源类型统计
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月13日 下午12:01:05
	 */
	@RequestMapping(value = "findTypeResourceStatis.do", method = RequestMethod.GET)
	public List<?> findTypeResourceStatis(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = resourceStaticsInfoService.findTypeResourceStatis(where);
		return result;
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
