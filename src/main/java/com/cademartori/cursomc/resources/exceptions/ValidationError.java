package com.cademartori.cursomc.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> list = new ArrayList<>();
	
	
	public ValidationError(Integer status, String msg, Long timeStramp) {
		super(status, msg, timeStramp);
		// TODO Auto-generated constructor stub
	}


	public List<FieldMessage> getErros() {
		return list;
	}


	public void addError(String fieldName, String Message) {
		list.add(new FieldMessage(fieldName,Message));
	}


}
