package com.sqx.common.core.xss;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS过滤
 *
 */
@Slf4j
public class XssFilter implements Filter {



	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		String requestURI = hreq.getRequestURI();
		String method = hreq.getMethod();
		log.info("当前请求的方法是：{}，请求地址是：{}", method, requestURI);

		HttpServletResponse hresp = (HttpServletResponse) response;

		//跨域
		hresp.setHeader("Access-Control-Allow-Origin", "*");


		//跨域 Header

		hresp.setHeader("Access-Control-Allow-Methods", "*");

		hresp.setHeader("Access-Control-Allow-Headers", "*");

		// Filter 只是链式处理，请求依然转发到目的地址。

		chain.doFilter(request, response);



		/*XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);*/
	}

	@Override
	public void destroy() {
	}

}