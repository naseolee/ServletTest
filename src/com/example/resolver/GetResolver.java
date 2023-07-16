package com.example.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.example.controller.Controller;
import com.example.servletAnotation.ApiController;
import com.example.servletAnotation.GetAnnotation;
import com.example.util.ServletUtils;

public class GetResolver implements RequestResolver {
	
	private final List<Method> candidateMethodList = new ArrayList<>();
	
	private List<Class<Controller>> candidateClassList = new ArrayList<>();
	
	private final String controllerPackage;
	
	private final String requestUri;
	
	private final String CLASS_EXTENSION = ".class";
	
	public GetResolver(String controllerPackage, String requestUri) {
		this.controllerPackage = controllerPackage;
		this.requestUri = requestUri;
	}
	
	// FIXME: Exception을 어떻게 처리하는게 좋은가?
	@Override
	public boolean execute() {
		
		String packagePath = this.controllerPackage.replace('.', '/');
		URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
		
		if (url == null) {
			System.out.println("Log대신:패키지가 존재하지 않음");
			return false;
		}
		
		File dir = new File(url.getFile());
		if (!dir.exists()) {
			System.out.println("Log대신:패키지의 디렉터리가 존재하지 않음");
			return false;
		}
		
		List<Class<Controller>> controllerClassList = null;
		try {
			controllerClassList = searchControllerClasses(dir.list());
		} catch (ClassNotFoundException e) {
			controllerClassList = null;
		}
		if (CollectionUtils.isEmpty(controllerClassList)) {
			System.out.println("Log대신:대상 클래스가 존재하지 않음");
			return false;
		}
		
		searchMethods(controllerClassList);
		if (CollectionUtils.isEmpty(candidateMethodList)) {
			System.out.println("Log대신:대상 메소드가 존재하지 않음");
			return false;
		}
		
		if (1 < candidateMethodList.size()) {
			System.out.println("Log대신:대상 메소드가 2건 이상 중복");
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private List<Class<Controller>> searchControllerClasses(String[] list) throws ClassNotFoundException {
		List<Class<Controller>> controllerClassList = new ArrayList<>();
		for (String className : list) {
			if(className.endsWith(".class")) {
				// .class 제거
				String classPath = this.controllerPackage + '.' + ServletUtils.deleteExt(className, CLASS_EXTENSION.length());
				Class<Controller> clazz = (Class<Controller>) Class.forName(classPath);
				controllerClassList.add(clazz);
			}
		}
		return controllerClassList;
	}
	
	private void searchMethods(List<Class<Controller>> controllerClassList) {
		for (Class<Controller> clazz : controllerClassList) {
			Annotation classAnnotation = clazz.getAnnotation(ApiController.class);
			if (classAnnotation == null) {
				continue;
			}
			searchCandidateMethods(clazz, clazz.getMethods());
		}
	}

	private void searchCandidateMethods(Class<Controller> clazz, Method[] methods) {
		for (Method m : methods) {
			Annotation methodAnnotation = m.getAnnotation(GetAnnotation.class);
			if (methodAnnotation == null) {
				continue;
			}
			// url 패턴 추출
			GetAnnotation getAnnotation = (GetAnnotation) methodAnnotation;
			if (getAnnotation.urlPattern().equals(this.requestUri)) {
				candidateClassList.add(clazz);
				candidateMethodList.add(m);
			}
		}
	}
	
	@Override
	public Class<Controller> getTargetClass() {
		return candidateClassList.get(0);
	}
	
	@Override
	public Method getTargetMethod() throws Exception {
		return getTargetMethod(candidateClassList.get(0), candidateMethodList.get(0));
	}
	
	private Method getTargetMethod(Class<Controller> clazz, Method method) throws Exception {
		return clazz.getMethod(method.getName(), method.getParameterTypes()); 
	}

}
