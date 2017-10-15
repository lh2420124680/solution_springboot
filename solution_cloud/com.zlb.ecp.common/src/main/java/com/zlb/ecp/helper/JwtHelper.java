package com.zlb.ecp.helper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtHelper {

	private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";

	public static String sign(Map<String, Object> map, long maxAge) {
		String token = "";
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String subject = JSON.toJSONString(map);
			String jwtId = map.get("userId").toString();
			token = JWT.create()
					.withJWTId(jwtId)
					.withSubject(subject)
					.sign(algorithm);
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (JWTCreationException e){
			e.printStackTrace();
		}
		return token;
	}
	
	public static void main(String[] args) {
		
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJ1c2VyTmFtZVwiOlwiamFuZVwiLFwicHdkXCI6XCJxd2VyXCIsXCJ1c2VySWRcIjpcIjEyMzQ1NlwifSIsImp0aSI6IjEyMzQ1NiJ9.9pHt98sIykgUKDWfOMAxEmAaKS_QOOq07rAlO-lcPxs";
		try {
		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    JWTVerifier verifier = JWT.require(algorithm)
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		    System.out.println("getId"+jwt.getId());
		    System.out.println("getKeyId"+jwt.getKeyId());
		    System.out.println("getSubject"+jwt.getSubject());
		} catch (UnsupportedEncodingException exception){
		    //UTF-8 encoding not supported
		} catch (JWTVerificationException exception){
		    //Invalid signature/claims
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", "123456");
		map.put("userName", "jane");
		map.put("pwd", "qwer");
		map.put("pwd1", "qwer");
		String sign = sign(map,1);
		System.out.println(sign);
	}
}
