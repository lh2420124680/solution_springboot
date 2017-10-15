package com.zlb.ecp.data.api.service;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
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
import com.zlb.api.IUserStaticsInfoService;
import com.zlb.ecp.data.ibiz.IUserDataStatBiz;
import com.zlb.ecp.entity.EntityResult;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.JsonHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.redis.impl.JedisClientSingle;

/**
 * @Description: 用户模块
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/UserModuleStatService")
public class UserModuleStatService {

	private static final Logger log = LoggerFactory.getLogger(UserModuleStatService.class);

	@Autowired
	private IUserStaticsInfoService userStaticsInfoService;

	@Autowired
	private IUserDataStatBiz userDataStatBiz;
	
	@Autowired
	private JedisClientSingle jedisClient;

	/**
	 * <p>
	 * Description: 各区域用户统计
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUsersStaticByTown.do", method = RequestMethod.GET)
	public List<?> findUsersStaticByTown(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.findUsersStaticByTown(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 用户明细数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "findUsersStaticDetail.do", method = RequestMethod.GET)
	public List<?> findUsersStaticDetail(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		String whereParam = JsonHelper.object2json(where);
		List<ResourceStatisticsMo> result = new ArrayList<>();
		String cachResult = jedisClient.get("findUsersStaticDetail:" + whereParam);
		if (!StringHelper.IsEmptyOrNull(cachResult)) {
			JsonHelper jsonHelper = new JsonHelper();
			result = jsonHelper.fromJson(cachResult,List.class);
			return result;
		} else {
			result = userStaticsInfoService.findUsersStaticByTown(where);
			Map<String, Object> map = userDataStatBiz.queryPtUserUseAllNumByGid(where);
			for (int i = 0; i < result.size(); i++) {
				String name = result.get(i).getName1().toString();
				if (map.containsKey(name)) {
					result.get(i).setValue7(map.get(name).toString());
				} else {
					result.get(i).setValue7("0");
				}
			}
			cachResult = JSON.toJSONString(result);
			jedisClient.set("findUsersStaticDetail:" + whereParam,cachResult);
			jedisClient.expire("findUsersStaticDetail:" + whereParam, 3600);
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 导出用户明细数据
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @throws Exception
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "exportUserDtailData.do", method = RequestMethod.POST)
	public List<Map<String, Object>> exportUserDtailData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> downMap = new HashMap<>();
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.findUsersStaticByTown(where);
		Map<String, Object> map = userDataStatBiz.queryPtUserUseAllNumByGid(where);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> newMap = new HashMap<>();
			String name = result.get(i).getName1().toString();
			if (map.containsKey(name)) {
				result.get(i).setValue7(map.get(name).toString());
			} else {
				result.get(i).setValue7("0");
			}
			newMap.put("name1", result.get(i).getName1());
			newMap.put("value1", result.get(i).getValue1());
			newMap.put("value2", result.get(i).getValue2());
			newMap.put("value3", result.get(i).getValue3());
			newMap.put("value4", result.get(i).getValue4());
			newMap.put("value5", result.get(i).getValue5());
			newMap.put("value6", result.get(i).getValue6());
			newMap.put("value7", result.get(i).getValue7());
			list.add(newMap);
		}
		String newPath = "exportFile/dataCenter/userDetail" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "name1", "value1", "value2", "value7", "value3", "value4", "value5", "value6" };
		String[] titleNames = { "区域", "学校数", "注册用户数", "使用用户数", "教师用户数", "学生用户数", "家长用户数", "其他用户数" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "用户明细数据";
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
	 * Description: 各区域用户分布
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUsersDistribution.do", method = RequestMethod.GET)
	public List<?> findUsersDistribution(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.findUsersDistribution(where);
		return result;
	}

	/**
	 * <p>
	 * Description: 各类型用户分布
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findUserType.do", method = RequestMethod.GET)
	public List<?> findUserType(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.findUserType(where);
		return result;
	}
	
	/**
	 * <p>Description: 教师性别比例<p>
	 * @author Jane.Luo
	 * @param request
	 * @return List<?>
	 * @date 2017年9月8日 下午4:09:11
	 */
	@RequestMapping(value = "teacherSexScale.do", method = RequestMethod.GET)
	public List<?> teacherSexScale(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.teacherSexScale(where);
		return result;
	}
	
	/**
	 * <p>Description: 教师职称<p>
	 * @author Jane.Luo
	 * @param request
	 * @return List<?>
	 * @date 2017年9月8日 下午4:09:21
	 */
	@RequestMapping(value = "teachTitle.do", method = RequestMethod.GET)
	public List<?> teachTitle(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.teachTitle(where);
		return result;
	}
	
	/**
	 * <p>Description: 教师学段<p>
	 * @author Jane.Luo
	 * @param request
	 * @return List<?>
	 * @date 2017年9月8日 下午4:09:34
	 */
	@RequestMapping(value = "teachStage.do", method = RequestMethod.GET)
	public List<?> teachStage(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.teachStage(where);
		return result;
	}
	
	/**
	 * <p>Description: 教师学科<p>
	 * @author Jane.Luo
	 * @param request
	 * @return List<?>
	 * @date 2017年9月8日 下午4:09:48
	 */
	@RequestMapping(value = "teachSubject.do", method = RequestMethod.GET)
	public List<?> teachSubject(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<ResourceStatisticsMo> result = userStaticsInfoService.teachSubject(where);
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
