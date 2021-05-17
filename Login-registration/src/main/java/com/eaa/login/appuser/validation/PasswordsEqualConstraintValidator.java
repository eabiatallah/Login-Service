package com.eaa.login.appuser.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.eaa.login.registration.RegistrationRequest;

public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, RegistrationRequest>{
	
//	private final static Logger LOGGER = LoggerFactory.getLogger(PasswordsEqualConstraintValidator.class);


	@Override
	public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
		
		Optional<String> pass = Optional.ofNullable(request.getPassword());
		Optional<String> confPass = Optional.ofNullable(request.getConfirmPassword());
		
		if(!pass.equals(confPass))
			return false;
		
		// return request.getPasword().equals(request.getConfirmPassword());
		
		return true;
	}

}
