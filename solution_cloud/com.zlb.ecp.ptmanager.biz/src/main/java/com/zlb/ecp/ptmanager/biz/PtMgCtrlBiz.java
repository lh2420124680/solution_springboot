package com.zlb.ecp.ptmanager.biz;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.pojo.EntityResult;
import com.zlb.ecp.pojo.ListResult;
import com.zlb.ecp.ptmanager.dao.IPtMgCtrlDao;
import com.zlb.ecp.ptmanager.ibiz.IPtMgCtrlBiz;

@Service
@Transactional
public class PtMgCtrlBiz implements IPtMgCtrlBiz {

	@Autowired
	private IPtMgCtrlDao ptMgCtrlDao;

	/**
	 * <p>
	 * Description: 查询平台管控表数据
	 * <p>
	 * 
	 * @author Jane.Luo
	 * @param map
	 * @return ListResult<Map<String,Object>>
	 * @date 2017年9月26日 下午2:44:00
	 */
	@Override
	public ListResult<Map<String, Object>> queryPtMg(Map<String, Object> where) {
		ListResult<Map<String, Object>> result = new ListResult<>();
		List<Map<String, Object>> list = ptMgCtrlDao.queryPtMg(where);
		result.setRows(list);
		return result;
	}

	/**
	 * <p>
	 * Description: 保存平台管控表数据
	 * <p>
	 * 
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	@Override
	public EntityResult<?> saveServerDetail(Map<String, Object> where) {
		EntityResult<?> result = new EntityResult<>();
		String all = where.get("ALL").toString();
		String[] allSplit = all.split("-");
		where.put("SERVER_IP", allSplit[0]);
		where.put("FROM_URL", allSplit[1]);
		where.put("PARAM", allSplit[2]);
		List<Map<String, Object>> list = ptMgCtrlDao.queryPtMgByIp(where);
		Integer flag = 0;
		if (list.isEmpty() && list.size() == 0 && where.get("SERVER_IP").toString().length() > 4) { // 执行添加
			flag = ptMgCtrlDao.addPtMg(where);
		} else { // 执行更新
			flag = ptMgCtrlDao.updatePtMg(where);
		}
		if (flag > 0) {
			result.setMsg("操作成功");
			result.setResult("1");
		} else {
			result.setMsg("操作失败");
			result.setResult("0");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 授权
	 * <p>
	 * 
	 * @author Jane.Luo
	 * @param map
	 * @return EntityResult<?>
	 * @date 2017年9月26日 下午2:44:39
	 */
	@Override
	public EntityResult<String> updateOauth(Map<String, Object> where) {
		EntityResult<String> result = new EntityResult<>();
		String guid = StringHelper.GetGUID();
		result.setRows(guid);
		String isOauth = where.get("IS_OAUTH").toString();
		if ("1".equals(isOauth)) {
			where.put("OAUTH_CODE", guid);
		} else {
			where.put("OAUTH_CODE", "");
		}
		Integer updateOauth = ptMgCtrlDao.updateOauth(where);
		if (updateOauth > 0) {
			result.setMsg("操作成功");
			result.setResult("1");
		} else {
			result.setMsg("操作失败");
			result.setResult("0");
		}
		return result;
	}

	/**
	 * <p>
	 * Description: 定时轮询服务器是否有授权
	 * <p>
	 * 
	 * @author Jane.Luo
	 * @return void
	 * @date 2017年9月26日 下午7:22:02
	 */
	// @Scheduled(cron = "0 12 15 * * ?")
	public void queryIsOauthScheduled() {
		try {
			Map<String, Object> where = new HashMap<>();
			List<Map<String, Object>> list = ptMgCtrlDao.queryPtMg(where);
			for (int i = 0, l = list.size(); i < l; i++) {
				String serverIp = list.get(i).get("SERVER_IP").toString();
				String fromUrl = list.get(i).get("FROM_URL").toString();
				Object oauthCode = list.get(i).get("OAUTH_CODE");
				if (StringHelper.IsEmptyOrNull(oauthCode)) { // 没有授权给予警告
					where.put("SERVER_IP", serverIp);
					where.put("IS_WARN", "0");
					ptMgCtrlDao.updateIsWarn(where);
					continue;
				}
				if (serverIp.length() > 4 && !StringHelper.IsEmptyOrNull(fromUrl)) {
					URL url = null;
					url = new URL(fromUrl);
					String host = "http://" + url.getHost() + ":8088/oauthcode/" + oauthCode + ".locl";
					url = new URL(host);
					URLConnection conn = url.openConnection();
					String str = conn.getHeaderField(0);
					if (str.indexOf("200") > 0) {
						// 可以访问到授权文件
						where.put("SERVER_IP", serverIp);
						where.put("IS_WARN", "1");
						ptMgCtrlDao.updateIsWarn(where);
					} else {
						// 无法访问到授权文件
						where.put("SERVER_IP", serverIp);
						where.put("IS_WARN", "1");
						ptMgCtrlDao.updateIsWarn(where);
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Description: 验证远程服务器的mac地址
	 * <p>
	 * 
	 * @author Jane.Luo
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年9月27日 上午10:50:52
	 */
	@Override
	public EntityResult<?> validServerMac(Map<String, Object> where) {
		EntityResult<String> result = new EntityResult<>();
		Object all = where.get("ALL");
		if (StringHelper.IsEmptyOrNull(all)) {
			result.setMsg("操作失败,传过来的参数为空");
			result.setResult("0");
			return result;
		}
		String[] allArr = all.toString().split(":");
		if (allArr.length < 2) {
			result.setMsg("操作失败,传过来的mac地址为空");
			result.setResult("0");
			return result;
		}
		where.put("SERVERIP", allArr[0]);
		List<Map<String, Object>> list = ptMgCtrlDao.queryPtMg(where);
		Object registMac = list.get(0).get("MAC");
		if ((list.isEmpty() && list.size() == 0) || StringHelper.IsEmptyOrNull(registMac) ) {
			result.setMsg("操作失败,没有次服务器的ip或者注册mac地址为空");
			result.setResult("0");
			return result;
		}
		for (int i = 0, l = allArr.length; i < l; i++) {
			if (registMac.equals(allArr[i])) {
				result.setMsg("操作成功");
				result.setResult("SDK:100962");
				return result;
			}
		}
		return result;
	}

}
