package com.lcass.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Encapsulated_method {
	public Method method;
	public Object instance;
	public Object[] parameters;

	public Encapsulated_method(Method method, Object[] parameters,
			Object instance) {
		this.method = method;
		this.parameters = parameters;
		this.instance = instance;
	}

	public void call() {
		if(instance == null){
			return;
		}
		if(method == null){
			return;
		}
		if (parameters != null) {
			try {
				method.invoke(instance, parameters);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			try {
				method.invoke(instance);
			}
			catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	public Object call_back(){
		Object temp;
		if(instance == null){
			System.out.println("no instance");
			return null;
		}
		if(method == null){
			System.out.println("no method");
			return null;
		}
		if (parameters != null) {
			try {
				temp = method.invoke(instance, parameters);
				return temp;
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			try {
				temp = method.invoke(instance);
				return temp;
			}
			catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
