package com.example.common.utils.weixinToken;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.common.utils.request.HttpRequest;
import com.example.common.utils.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class WeiXinToken {	
	
	public static void getWeixinToken(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		String callback = request.getParameter("jsoncallback");
		try {
			long nowTime = System.currentTimeMillis() / 1000;
			String APPID = "wx353451a485ed43ac";//
			String SECRET = "4c7591db16fc9b2c81bc48395642addf";
			HttpSession session = request.getSession();
			// 先判断是否有access_token的缓存 以及时间是否过期
			String access_token = (String) session.getAttribute("access_token");
			String expires_in_time = (String) session.getAttribute("expires_in_time");
			if (StringUtil.isEmpty(access_token) || StringUtil.isEmpty(expires_in_time)
					|| (nowTime - Long.parseLong(expires_in_time)) >= 7200) {
				String gettoken = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token",
						"grant_type=client_credential&appid=" + APPID + "&secret=" + SECRET);
				JSONObject jsonObject = JSONObject.fromObject(gettoken);
				if (null != jsonObject && null != jsonObject.get("access_token")) {
					access_token = jsonObject.get("access_token").toString();
					session.setAttribute("access_token", access_token);
					session.setAttribute("expires_in_time", String.valueOf(nowTime));

				}
			}
			System.out.println("access_token===" + access_token);
			// 通过access_token 获取 jsapi_ticket
			String jsapi_ticket = (String) session.getAttribute("jsapi_ticket");
			String jsapi_expires_in = (String) session.getAttribute("jsapi_expires_in");
			if (StringUtil.isEmpty(jsapi_ticket) || StringUtil.isEmpty(jsapi_expires_in)
					|| (nowTime - Long.parseLong(jsapi_expires_in)) >= 7200) {
				String ticket = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket",
						"access_token=" + access_token + "&type=jsapi");
				JSONObject jsonObject = JSONObject.fromObject(ticket);
				if (null != jsonObject && null != jsonObject.get("ticket")) {
					jsapi_ticket = jsonObject.get("ticket").toString();
					session.setAttribute("jsapi_ticket", jsapi_ticket);
					session.setAttribute("jsapi_expires_in", String.valueOf(nowTime));
				}
			}
			System.out.println("jsapi_ticket====" + jsapi_ticket);
			String strBackUrl = "http://" + request.getServerName() + "/*.html"; // 请求页面或其他地址
			Map<String, String> ret = Sign.sign(jsapi_ticket, strBackUrl);
			JSONObject jsonObj1 = new JSONObject();
			for (Map.Entry entry : ret.entrySet()) {
				jsonObj1.put(entry.getKey(), entry.getValue());
				System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			jsonObj1.put("appId", APPID);
			response.getWriter().write(callback + "(" + jsonObj1 + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}}
