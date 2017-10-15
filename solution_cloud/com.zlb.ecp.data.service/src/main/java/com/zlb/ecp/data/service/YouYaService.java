package com.zlb.ecp.data.service;

import java.io.IOException;
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

import com.alibaba.fastjson.JSONObject;
import com.zlb.api.IYoyaService;
import com.zlb.ecp.entity.EntityResult;
import com.zlb.ecp.helper.DataConvertHelper;

@RestController
@RequestMapping("zlbapp/yoya/YouYaService")
public class YouYaService {

	private static final Logger log = LoggerFactory.getLogger(YouYaService.class);

	@Autowired
	private IYoyaService yoyaService;

	/**
	 * <p>
	 * Description: 优芽
	 * <p>
	 * 
	 * @param request
	 * @return List<?>
	 * @date 2017年8月18日 下午5:25:20
	 */
	@RequestMapping(value = "queryLink.do", method = RequestMethod.GET)
	public void findMoviePage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		String result = yoyaService.queryLink(whereStr);
		JSONObject json = new JSONObject();
		json.put("name", result);
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print("successCallback(" + json.toJSONString() + ")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return result;
	}

	/**
	 * <p>
	 * Description: 获取连接
	 * <p>
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * @date 2017年9月12日 下午6:26:36
	 */
	@RequestMapping(value = "getDataByLink.do", method = RequestMethod.GET)
	public void getDataByLink(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> whereStr = DataConvertHelper.getRequestParamString(request);
		String result = yoyaService.getDataByLink(whereStr);
		JSONObject json = new JSONObject();
		json.put("name", result);
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print("successCallback(" + json.toJSONString() + ")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
