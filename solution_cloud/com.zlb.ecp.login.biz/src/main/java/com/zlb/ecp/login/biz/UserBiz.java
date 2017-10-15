package com.zlb.ecp.login.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlb.ecp.entity.EntityResult;
import com.zlb.ecp.helper.EncryptHelper;
import com.zlb.ecp.helper.EscapeHelper;
import com.zlb.ecp.helper.JsonHelper;
import com.zlb.ecp.helper.MD5Helper;
import com.zlb.ecp.helper.StringHelper;
import com.zlb.ecp.login.ibiz.IUserBiz;
import com.zlb.ecp.login.mapper.UserMapper;
import com.zlb.ecp.redis.JedisClient;

@Service
@Transactional
public class UserBiz implements IUserBiz {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	/**
	 * <p>
	 * Description: 查询用户的信息
	 * <p>
	 * 
	 * @param where
	 * @return List<Map<String,Object>>
	 * @date 2017年8月2日 上午9:35:18
	 */
	@Override
	public EntityResult<?> loginApp(Map<String, Object> where) {
		EntityResult<?> result = new EntityResult<>();
		if (StringHelper.IsEmptyOrNull(where.get("LOGIN_MAIL"))) {
			result.setMsg("用户登录帐号不能为空");
			return result;
		}
		if (StringHelper.IsEmptyOrNull(where.get("LOGIN_PSW"))) {
			result.setMsg("用户登录密码不能为空");
			return result;
		}
		String loginMail = where.get("LOGIN_MAIL").toString().trim();
		String loginPsw = where.get("LOGIN_PSW").toString().trim();
		List<Map<String, Object>> userLoginApp = null;
		// 学生家长登录 其为学生账号+j，密码为学生密码
		if ("j".equalsIgnoreCase(loginMail.substring(loginMail.length() - 1, loginMail.length()))) {
			String loginMail_stu = loginMail.substring(0, loginMail.length() - 1);
			HashMap<String, Object> loginMap = new HashMap<>();
			loginMap.put("LOGIN_MAIL", loginMail_stu);
			loginMap.put("IDCARD_NUM", EncryptHelper.setIdCard("IDCARD", loginMail.toLowerCase()));
			List<Map<String, Object>> findLoginMail = userMapper.findLoginMail(loginMap);
			if (Integer.valueOf(findLoginMail.get(0).get("COUNT_NUM").toString()) == 0) {
				result.setMsg("用户名或密码错误");
				return result;
			}
			// 家长账号
			HashMap<String, Object> userMap = new HashMap<>();
			userMap.put("LOGIN_MAIL", loginMail_stu.toLowerCase());
			userMap.put("LOGIN_PSW", MD5Helper.convertMD5(loginPsw));
			userMap.put("IDCARD_NUM", EncryptHelper.setIdCard("IDCARD", loginMail.toLowerCase()));
			userLoginApp = userMapper.userLoginApp(userMap);
			userLoginApp.get(0).put("LOGINMAIL", loginMail.toLowerCase());
			userLoginApp.get(0).put("ROLEFLAG", "003");
		} else {
			HashMap<String, Object> loginMap = new HashMap<>();
			loginMap.put("LOGIN_MAIL", loginMail.toLowerCase());
			loginMap.put("IDCARD_NUM", EncryptHelper.setIdCard("IDCARD", loginMail.toLowerCase()));
			List<Map<String, Object>> findLoginMail = userMapper.findLoginMail(loginMap);
			if (Integer.valueOf(findLoginMail.get(0).get("COUNT_NUM").toString()) == 0) {
				result.setMsg("用户名或密码错误");
				return result;
			}
			// 平台账号
			HashMap<String, Object> userMap = new HashMap<>();
			userMap.put("LOGIN_MAIL", loginMail.toLowerCase());
			userMap.put("LOGIN_PSW", MD5Helper.convertMD5(loginPsw));
			userMap.put("IDCARD_NUM", EncryptHelper.setIdCard("IDCARD", loginMail.toLowerCase()));
			userLoginApp = userMapper.userLoginApp(userMap);
		}

		if (userLoginApp.size() > 0) {
			Integer userState = Integer.valueOf(userLoginApp.get(0).get("USERSTATE").toString());
			String roleFlag = userLoginApp.get(0).get("ROLEFLAG").toString();
			// 判断用户是否冻结
			if (userState == 0) {
				result.setMsg("用户登录帐号已冻结");
				return result;
			}

			// 判断用户是否离职
			if (userState == 3) {
				result.setMsg("该老师已经离职");
				return result;
			}

			// 判断用户是否退学
			if (userState == 4) {
				result.setMsg("该用户已经退学");
				return result;
			}

			// 判断用户是否在转校中
			if (userState == 5) {
				result.setMsg("该用户正在转校中");
				return result;
			}

			// 判断用户是否毕业
			if (userState == 6 || userState == 7 || userState == 8) {
				result.setMsg("该用户已经毕业");
				return result;
			}

			if (!StringHelper.IsEmptyOrNull(roleFlag)) {
				// 判断用户的角色
				if (roleFlag.contains("002")) {
					// 说明是学生
					userLoginApp = userLoginApp;
				} else {
					// 说明是老师 看下他否是校外的角色
					if (roleFlag.contains("051") || roleFlag.contains("052") || roleFlag.contains("021")
							|| roleFlag.contains("031") || roleFlag.contains("041") || roleFlag.contains("042")) {
					} else {
						// 没有校外角色
						HashMap<String, Object> userMap = new HashMap<>();
						userMap.put("SCHOOL_GID", userLoginApp.get(0).get("SCHOOLGID"));
						userMap.put("USER_GID", userLoginApp.get(0).get("GID"));
						List<Map<String, Object>> selectUserPosition = userMapper.selectUserPosition(userMap);
						if (selectUserPosition.size() != 0) {
							if ("校长校长".equals(selectUserPosition.get(0).get("USERPOSITION"))) {
								userLoginApp.get(0).put("USERPOSITION", "校长");
							} else if ("平台管理处学校管理员".equals(selectUserPosition.get(0).get("USERPOSITION"))) {
								userLoginApp.get(0).put("USERPOSITION", "学校管理员");
							} else if ("教导（务）处主任".equals(selectUserPosition.get(0).get("USERPOSITION"))) {
								userLoginApp.get(0).put("USERPOSITION", "教导主任");
							} else if ("校长副校长".equals(selectUserPosition.get(0).get("USERPOSITION"))) {
								userLoginApp.get(0).put("USERPOSITION", "副校长");
							} else if ("教导（务）处副主任".equals(selectUserPosition.get(0).get("USERPOSITION"))) {
								userLoginApp.get(0).put("USERPOSITION", "教导副主任");
							} else {
								userLoginApp.get(0).put("USERPOSITION", selectUserPosition.get(0).get("USERPOSITION"));
							}

						}
					}
				}
			}

			String token = where.get("TOKEN").toString();
			userLoginApp.get(0).put("LOGINPSW", "");
			//数据转换
			List<Map<String, Object>> typeConv = typeConv(userLoginApp);
			String jsonString = JSON.toJSONString(typeConv.get(0));
			String escapeJson = EscapeHelper.escape(jsonString);
			jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, escapeJson);
			// 设置session的过期时间
			jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);

			result.setMsg("success");

			return result;
		} else {
			result.setMsg("用户名或密码错误");
			return result;
		}
	}

	/**
	 * <p>
	 * Description: 用户验证
	 * <p>
	 * 
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年8月2日 下午12:08:35
	 */
	public EntityResult<?> checkData(Map<String, Object> where) {
		EntityResult<Object> result = new EntityResult<>();
		Object token = where.get("TOKEN");
		// 根据token从redis中查询用户信息
		String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		// 判断是否为空
		if (StringHelper.IsEmptyOrNull(token)) {
			result.setMsg("此session已经过期，请重新登录");
			return result;
		}
		String userMsg = EscapeHelper.unescape(json);
		Object parse = JSON.parse(userMsg);
		result.setRows(parse);
		// 更新过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		result.setMsg("success");
		return result;
	}
	
	public static List<Map<String, Object>> typeConv(List<Map<String, Object>> map){
		map.get(0).put("cityName", map.get(0).get("CITYNAME"));
		map.get(0).put("userTitleName", map.get(0).get("USERTITLENAME"));
		map.get(0).put("schoolGid", map.get(0).get("SCHOOLGID"));
		map.get(0).put("userDeskCode", map.get(0).get("USERDESKCODE"));
		map.get(0).put("townName", map.get(0).get("TOWNNAME"));
		map.get(0).put("cityGid", map.get(0).get("CITYGID"));
		map.get(0).put("spaceStatus", map.get(0).get("SPACESTATUS"));
		map.get(0).put("relName", map.get(0).get("RELNAME"));
		map.get(0).put("userTel", map.get(0).get("USERTEL"));
		map.get(0).put("userType", map.get(0).get("USERTYPE"));
		map.get(0).put("townGid", map.get(0).get("TOWNGID"));
		map.get(0).put("portalGid", map.get(0).get("PORTALGID"));
		map.get(0).put("headerPic", map.get(0).get("HEADERPIC"));
		map.get(0).put("userSex", map.get(0).get("USERSEX"));
		map.get(0).put("provinceGid", map.get(0).get("PROVINCEGID"));
		map.get(0).put("isBang", map.get(0).get("ISBANG"));
		map.get(0).put("gid", map.get(0).get("GID"));
		map.get(0).put("roleFlag", map.get(0).get("ROLEFLAG"));
		map.get(0).put("userState", map.get(0).get("USERSTATE"));
		map.get(0).put("loginMail", map.get(0).get("LOGINMAIL"));
		map.get(0).put("brithDay", map.get(0).get("BRITHDAY"));
		map.get(0).put("userCode", map.get(0).get("USERCODE"));
		map.get(0).put("provinceName", map.get(0).get("PROVINCENAME"));
		map.get(0).put("idcardNum", map.get(0).get("IDCARDNUM"));
		map.get(0).put("userTitle", map.get(0).get("USERTITLE"));
		map.get(0).put("loginPsw", map.get(0).get("LOGINPSW"));
		map.get(0).put("totalScore", map.get(0).get("TOTALSCORE"));
		map.get(0).put("detailInfoGid", map.get(0).get("DETAILINFOGID"));
		return map;
	}
	
	/**
	 * <p>Description: rpc调用测试<p>
	 * @param where
	 * @return EntityResult<?>
	 * @date 2017年8月2日 下午12:08:35
	 */
	public String remoteTest(Map<String, Object> where) {
		return "调用" + where.get("key");
	}
}
