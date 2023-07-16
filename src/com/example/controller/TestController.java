package com.example.controller;

import com.example.bean.HelloBean;
import com.example.bean.ResultBean;
import com.example.servletAnotation.ApiController;
import com.example.servletAnotation.GetAnnotation;

@ApiController
public class TestController implements Controller {
	
	@GetAnnotation(urlPattern = "/servletTest/message", description = "getMessage")
	public ResultBean testService(HelloBean bean) {
		ResultBean result = new ResultBean();
		result.setResult("Hello " + bean.getName() + " Test Service!");
		return result;
	}
}
