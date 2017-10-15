package com.zlb.ecp.login.service;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zlb.ecp.entity.EntityResult;
import com.zlb.ecp.helper.CookieHelper;
import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.login.ibiz.IUserBiz;

/**
 * @Description: sso
 * @author Jane.Luo
 * @date 2017年8月2日 上午10:52:40
 * @version V1.0
 */
@RestController
@RequestMapping(value = "ecp/login/service")
public class LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private IUserBiz userBiz;

	/**
	 * <p>
	 * Description: sso
	 * <p>
	 * 
	 * @param request
	 * @param response
	 * @return EntityResult<?>
	 * @date 2017年8月2日 上午10:52:34
	 */
	@RequestMapping(value = "loginApp.do", method = RequestMethod.GET)
	public EntityResult<?> loginApp(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		String token = StringHelper.GetGUID();
		where.put("TOKEN", token);
		// 添加写cookie的逻辑，cookie的有效期是关闭浏览器就失效。
		CookieHelper.setCookie(request, response, "TT_TOKEN", token);
		EntityResult<?> result = new EntityResult<>();
		try {
			result = userBiz.loginApp(where);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResult("0");
			result.setMsg("操作失败");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 校验方法
	 * <p>
	 * 
	 * @param request
	 * @param response
	 * @return EntityResult<?>
	 * @date 2017年8月2日 下午1:41:57
	 */
	@RequestMapping(value = "checkData.do", method = {RequestMethod.GET,RequestMethod.POST})
	public EntityResult<?> checkData(HttpServletRequest request, HttpServletResponse response) {
		EntityResult<?> result = new EntityResult<>();
		response.setHeader("Access-Control-Allow-Origin", "http://www.pt.com");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//response.setHeader("XDomainRequestAllowed","1");
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		Cookie[] cookies = request.getCookies();
		if (!StringHelper.IsEmptyOrNull(cookies)) {
			for (int i = 0; i < cookies.length; i++) {
				if ("TT_TOKEN".equals(cookies[i].getName())) {
					where.put("TOKEN", cookies[i].getValue());
					break;
				}
			}
			try {
				result = userBiz.checkData(where);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.setResult("0");
				result.setMsg("操作失败");
			}
		} else {
			result.setResult("0");
			result.setMsg("操作失败");
		}
		return result;
	}
}
