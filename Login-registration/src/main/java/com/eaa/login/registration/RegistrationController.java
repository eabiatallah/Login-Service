package com.eaa.login.registration;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/registration")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@PostMapping("/register")
	public RegistrationResponse register(@Valid @RequestBody RegistrationRequest request) {
		RegistrationResponse response = registrationService.register(request);
		return new RegistrationResponse("token: " + response.getMessage(), response.isStatus());
	}

	@GetMapping("/confirm/{token}")
	public RegistrationResponse confirm(@PathVariable("token") String token) {
		RegistrationResponse response =  registrationService.confirmToken(token);
        return new RegistrationResponse(response.getMessage(), response.isStatus());
	}

}
