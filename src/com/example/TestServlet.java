package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;

import com.example.bean.HelloBean;
import com.example.bean.ResultBean;
import com.example.constants.RequestType;
import com.example.controller.Controller;
import com.example.resolver.GetResolver;
import com.example.resolver.RequestResolver;
import com.example.servletAnotation.ApiController;
import com.example.servletAnotation.GetAnnotation;
import com.example.util.SingletonFactory;


/**
 * Servlet implementation class TestServlet
 */
@WebServlet(name = "TestServlet", displayName = "FilterTestServlet", urlPatterns ="/*")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1000000065464L;
	
	private final String controllerPackage = "com.example.controller";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		analyzeReq(request, response);
//		response.getWriter().append("Served at: ").append(request.getContextPath()).append("//" + request.getAttribute("filter"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		analyzeReq(request, response);
	}
	
	
	private void analyzeReq(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 리퀘스트 메소드 구분, 일단 여기서는 Post는 받지 않는다.
		if (request.getMethod() == RequestType.POST.toString()) {
			response.getWriter().append("do not accepted post request now");
			return;
		}
		
		RequestResolver reqResolver = new GetResolver(controllerPackage, request.getRequestURI());
		
		try {
			boolean isSuccess = reqResolver.execute();
			if (!isSuccess) {
				System.out.println("Log대신:Url분석 실패");
				return;
			}

			Method method = reqResolver.getTargetMethod();
			
			// 타겟 클래스의 인스턴스를 생성
			Controller controllClass = (Controller) SingletonFactory.getInstance(reqResolver.getTargetClass());
			
			// 리퀘스트 파라메터는 우선 간단한 방식으로 전달 
			HelloBean bean = new HelloBean();
			bean.setName(request.getParameter("name"));
			
			// 호출
			ResultBean result = (ResultBean) method.invoke(controllClass, bean);
			
			response.getWriter().append(result.getResult());
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
