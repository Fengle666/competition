package com.yjq.programmer.interceptor;

import com.alibaba.fastjson.JSON;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录拦截器
 * @author 杨杨吖
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	private Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

	@Resource
	private IUserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setContentType("application/json; charset=utf-8");
		String method = request.getMethod();
		if("OPTIONS".equalsIgnoreCase(method)) {
			// 如果是OPTIONS测试请求，则直接返回测试成功
			try {
				//JSON.parseObject，是将Json字符串转化为相应的对象；JSON.toJSONString则是将对象转化为Json字符串。
				response.getWriter().print(JSON.toJSONString(ResponseDTO.success(true)));
				return false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String token = request.getHeader("token");
		log.info("接受到的token={}", token);
		UserDTO userDTO = new UserDTO();
		userDTO.setToken(token);
		ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
		if(responseDTO.getCode() != 0) {
			try {
				//JSON.parseObject，是将Json字符串转化为相应的对象；JSON.toJSONString则是将对象转化为Json字符串。
				response.getWriter().print(JSON.toJSONString(ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED)));
				return false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
