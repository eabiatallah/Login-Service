package com.eaa.login.registration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.eaa.login.appuser.validation.PasswordsEqualConstraint;
import com.eaa.login.constants.EAConstants;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@PasswordsEqualConstraint(message = "passwords are not equal")
public class RegistrationRequest {
	
	@NotNull
	@Size(min = 2, max = 30,  message = "First Name shoud be between 2 and 30 characters")
    private  String firstName;
    
    @NotNull
	@Length(max = 30, message = "Last Name should be less than 31 characters")
    private  String lastName;
    
     
    @Pattern(regexp = EAConstants.REGEX_EMAIL_PATTERN,message = "Invalid Email Address")
    private  String email;
    
    @NotEmpty
    private  String password;
    
    @NotEmpty
    private String confirmPassword;
}
