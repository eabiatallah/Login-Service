package com.eaa.login.registration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.eaa.login.appuser.validation.PasswordsEqualConstraintValidator;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={PasswordsEqualConstraintValidator.class,RegistrationRequest.class})
public class RegistrationRequestTest {
	
	@Mock
	ConstraintValidatorContext constraintValidatorContext;
	
//	@Autowired
//	private PasswordsEqualConstraintValidator passwordsEqualConstraintValidator;
	
	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	
	@BeforeClass
	public static void createValidator(){
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		
	}
	
	@AfterClass
	public static void close(){
		validatorFactory.close();
	}
	
	@Test
	public void testShouldHaveNoViolations(){
		
		//Given:
		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("elias");
		req.setLastName("Abi Atallah");
		req.setEmail("eabiatallah@gmail.com");
		req.setPassword("pass");
		req.setConfirmPassword("pass");
		
		//when:
		Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(req);
		
		//then:
		assertTrue(violations.isEmpty());
		assertEquals(violations.size(), 0);
		
	}
	
	@Test
	public void testInvalidInputs(){
		
		//Given:
		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("e");
		//req.setLastName("Abi Atallah");
		req.setEmail("eabiatallah");
		req.setPassword("pass");
		req.setConfirmPassword("pass12");
		
		//when:
		Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(req);
		
		//then:
		assertTrue(!violations.isEmpty());
		assertEquals(violations.size(), 3);
		
	}
	
	@Test
	public void testInvalidPasswords(){
		
		//Given:
		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("elias");
		req.setLastName("Abi Atallah");
		req.setEmail("eabiatallah@gmail.com");
		req.setPassword("pass");
		req.setConfirmPassword("pass1");
		
		//when:
		Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(req);
		
		//then:
		assertTrue(!violations.isEmpty());
		assertEquals(violations.size(), 1);
		
	}


}
