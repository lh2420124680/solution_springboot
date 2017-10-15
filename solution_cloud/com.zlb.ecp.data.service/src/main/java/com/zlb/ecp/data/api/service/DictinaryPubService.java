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

import com.cloud.entity.bo.DictionaryBo;
import com.zlb.api.IDictinaryService;
import com.zlb.ecp.data.service.WebAccessDataService;
import com.zlb.ecp.helper.DataConvertHelper;

/**
 * @Description: 下拉框地区选择
 * @author Jane.Luo
 * @date 2017年8月10日 下午2:39:07 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/DictinaryPubService")
public class DictinaryPubService {

	private static final Logger log = LoggerFactory.getLogger(WebAccessDataService.class);

	@Autowired
	private IDictinaryService dictinaryService;
	
	/**
	 * <p>Description: 查询所有地区信息<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "listTownTree.do", method = RequestMethod.GET)
	public List<?> listTownTree(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<DictionaryBo> result = dictinaryService.listTownTree();
		return result;
	}
	
	/**
	 * <p>Description: 查询地区下的学校<p>
	 * @param request
	 * @return List<?>
	 * @date 2017年8月10日 下午4:52:08
	 */
	@RequestMapping(value = "findSchoolName.do", method = RequestMethod.GET)
	public List<?> findSchoolName(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		List<DictionaryBo> result = dictinaryService.findSchoolName(where.get("gid"));
		return result;
	}
	
	/**
	 * <p>Description: 查询资源文件，已确认平台<p>
	 * @param request
	 * @return String
	 * @date 2017年8月28日 下午6:03:54
	 */
	@RequestMapping(value = "findArea.do", method = RequestMethod.GET)
	public String findArea(HttpServletRequest request) {
		Map<String, String> where = DataConvertHelper.getRequestParamString(request);
		String findArea = dictinaryService.findArea();
		return findArea;
	}
}
