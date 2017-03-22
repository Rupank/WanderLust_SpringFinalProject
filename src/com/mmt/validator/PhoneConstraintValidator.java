package com.mmt.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {

	
	@Override
	public void initialize(Phone Long) { }

	@Override
	public boolean isValid(String phoneField, ConstraintValidatorContext cxt) {
		if(phoneField == null || phoneField.length()!=10) {
			return false;
		}
		return phoneField.matches("[0-9()-]*");
	}

}
