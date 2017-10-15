package com.zlb.ecp.ptmanager.service;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zlb.ecp.helper.DataConvertHelper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.EntityResult;
import com.zlb.ecp.pojo.ListResult;
import com.zlb.ecp.ptmanager.ibiz.IPtMgCtrlBiz;

@RestController
@RequestMapping("zlbapp/ptmanager/PtMgCtrlService")
public class PtMgCtrlService {

	private static final Logger log = LoggerFactory.getLogger(PtMgCtrlService.class);
	
	@Autowired
	private IPtMgCtrlBiz ptMgCtrlBiz;
	
	/**
	 * <p>Description: 查询平台管控表数据<p>
	 * @author Jane.Luo
	 * @param map
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年9月26日 下午2:44:00
	 */
	@RequestMapping(value = "queryPtMg.do", method = RequestMethod.GET)
	public ListResult<Map<String, Object>> queryPtMg(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		ListResult<Map<String, Object>> result = ptMgCtrlBiz.queryPtMg(where);
		return result;
	}
	
	/**
	 * <p>Description: 保存平台管控表数据<p>
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	@RequestMapping(value = "saveServerDetail.do", method = RequestMethod.GET)
	public EntityResult<?> saveServerDetail(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		EntityResult<?> result = new EntityResult<>();
		try {
			result = ptMgCtrlBiz.saveServerDetail(where);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setResult("0");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * <p>Description: 授权<p>
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	@RequestMapping(value = "updateOauth.do", method = RequestMethod.GET)
	public EntityResult<?> updateOauth(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		EntityResult<String> result = new EntityResult<>();
		try {
			result = ptMgCtrlBiz.updateOauth(where);
			Object guid = result.getRows();
			String newPath = "oauthcode";
			String realPath = request.getSession().getServletContext().getRealPath("") + newPath;
			File dir = new File(realPath);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			String fileName = realPath + File.separator + guid + ".locl";
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			String downloadUrl = "/" + newPath.replace(File.separator, "/") + "/" + guid + ".locl";
			System.out.println(realPath);
			result.setRows("generate local file path:" + downloadUrl);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setResult("0");
			result.setMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * <p>Description: 测试定时任务的方法<p>
	 * @author Jane.Luo void
	 * @date 2017年9月27日 上午11:03:41
	 */
	@RequestMapping(value = "queryIsOauthScheduled.do", method = RequestMethod.GET)
	public void queryIsOauthScheduled(){
		ptMgCtrlBiz.queryIsOauthScheduled();
	}
	
	/**
	 * <p>Description: 验证远程服务器的mac地址<p>
	 * @author Jane.Luo
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年9月27日 上午10:50:52
	 */
	@RequestMapping(value = "validServerMac.do", method = RequestMethod.GET)
	public EntityResult<?> validServerMac(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> where = DataConvertHelper.getRequestParams(request);
		EntityResult<?> result = new EntityResult<>();
		try {
			result = ptMgCtrlBiz.validServerMac(where);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setResult("0");
			result.setMsg("操作失败");
		}
		return result;
	}
}
