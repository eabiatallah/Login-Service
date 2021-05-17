package com.eaa.login.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.eaa.login.appuser.AppUser;
import com.eaa.login.appuser.AppUserRole;
import com.eaa.login.appuser.AppUserService;
import com.eaa.login.registration.token.ConfirmationToken;
import com.eaa.login.registration.token.ConfirmationTokenService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationServiceTest {

	@Autowired
	private RegistrationService registrationService;

	@MockBean
	private AppUserService appUserService;

	@MockBean
	private ConfirmationTokenService confirmationTokenService;

	@Test
	public void testValidRegister() {

		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("Elias");
		req.setLastName("Abi atallah");
		req.setEmail("eabiatallah@gmail.com");
		req.setPassword("pass");

		Mockito.when(appUserService.signUpUser(Mockito.any(AppUser.class))).thenReturn("Token123");

		assertThat(registrationService.register(req).isStatus()).isEqualTo(Boolean.TRUE);

	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidRegister() {

		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("Elias");
		req.setLastName("Abi atallah");
		req.setEmail("eabiatallah@gmail.com");
		req.setPassword("pass");

		AppUser appUser = new AppUser(req.getFirstName(), req.getLastName(), req.getEmail(), req.getPassword(),
				AppUserRole.USER);

		doThrow(new IllegalStateException("email already taken")).when(appUserService).signUpUser(appUser);

		registrationService.register(req);
	}

	@Test(expected = IllegalStateException.class)
	public void testConfirmTokenException() {

		String token = "invalidToken";
		doThrow(new IllegalStateException("token not found")).when(confirmationTokenService).getToken(token);
		registrationService.confirmToken(token);

	}

	@Test
	public void testConfirmTokenValid() {

		ConfirmationToken confirmToken = new ConfirmationToken("tokenValue", LocalDateTime.now(),
				LocalDateTime.now().plus(1, ChronoUnit.DAYS), new AppUser());

		Mockito.when(confirmationTokenService.getToken(confirmToken.getToken())).thenReturn(Optional.of(confirmToken));

		assertThat(registrationService.confirmToken(confirmToken.getToken()).isStatus()).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void testConfirmTokenAlreadyConfirmed() {

		ConfirmationToken confirmToken = new ConfirmationToken();
		confirmToken.setConfirmedAt(LocalDateTime.now());
		confirmToken.setCreatedAt(LocalDateTime.now());
		confirmToken.setExpiresAt(LocalDateTime.now().plus(1, ChronoUnit.DAYS)); // tomorrow
		confirmToken.setToken("tokenValue");
		confirmToken.setAppUser(new AppUser());

		// ConfirmationToken confirmToken = new
		// ConfirmationToken("tokenValue",LocalDateTime.now(),LocalDateTime.now().plus(1,ChronoUnit.DAYS),new
		// AppUser());

		Mockito.when(confirmationTokenService.getToken(confirmToken.getToken())).thenReturn(Optional.of(confirmToken));

		assertThat(registrationService.confirmToken(confirmToken.getToken()).getMessage())
				.isEqualTo("email already confirmed");
	}

	@Test
	public void testConfirmTokenAlreadyExpired() {

		ConfirmationToken confirmToken = new ConfirmationToken();
		confirmToken.setConfirmedAt(null);
		confirmToken.setCreatedAt(LocalDateTime.now());
		confirmToken.setExpiresAt(LocalDateTime.now().minus(1, ChronoUnit.DAYS)); // yesterday
		confirmToken.setToken("tokenValue");
		confirmToken.setAppUser(new AppUser());

		Mockito.when(confirmationTokenService.getToken(confirmToken.getToken())).thenReturn(Optional.of(confirmToken));

		assertThat(registrationService.confirmToken(confirmToken.getToken()).getMessage())
				.isEqualTo("Token is expired");
	}

}
