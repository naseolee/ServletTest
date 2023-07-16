package com.example.resolver;

import java.lang.reflect.Method;

import com.example.controller.Controller;

public interface RequestResolver {
	boolean execute();
	
	Method getTargetMethod() throws Exception;

	Class<Controller> getTargetClass();
}
