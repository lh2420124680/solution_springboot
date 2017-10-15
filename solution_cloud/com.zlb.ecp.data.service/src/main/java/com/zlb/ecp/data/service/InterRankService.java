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
import com.zlb.ecp.data.ibiz.IInterRankBiz;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.ExcelHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.ListResult;

/**
 * @Description: 积分排行榜 
 * @author Jane.Luo
 * @date 2017年8月18日 下午2:29:19 
 * @version V1.0
 */
@RestController
@RequestMapping("zlbapp/datacenter/InterRankService")
public class InterRankService {

	private static final Logger log = LoggerFactory.getLogger(AppUseAnalyService.class);

	@Autowired
	private IInterRankBiz interRankBiz;
	
	/**
	 * <p>Description: 积分排行榜 <p>
	 * @param where
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年8月18日 下午2:30:16
	 */
	@RequestMapping(value = "queryInterRank.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryInterRank(HttpServletRequest request) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = interRankBiz.queryInterRank(where);
		return result;
	}
	
	/**
	 * <p>Description: 应用使用明细导出<p>
	 * @param request
	 * @return EntityResult<T>
	 * @throws Exception 
	 * @date 2017年8月17日 下午4:28:48
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "queryInterRankExport.do", method = RequestMethod.POST)
	public List<Map<String, Object>> queryInterRankExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Map<String, Object>> entityResult = new ArrayList<>();
		Map<String, Object> downMap = new HashMap<>();
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = interRankBiz.queryInterRankExport(where);
		List<Map<String, Object>> list = result.getRows();
		String newPath = "exportFile/dataCenter/interRank" + File.separator + StringHelper.GetGUID();
		String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
		String[] columnNames = { "userRelName", "scNum", "exNum", "teNum" };
		String[] titleNames = { "姓名", "积分", "经验值", "教学币" };
		// 判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if (!dir.isDirectory())
			dir.mkdirs();
		String sheetName = "积分排行榜";
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
