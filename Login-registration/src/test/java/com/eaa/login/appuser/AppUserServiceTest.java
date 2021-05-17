package com.eaa.login.appuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.eaa.login.registration.token.ConfirmationToken;
import com.eaa.login.registration.token.ConfirmationTokenService;

@RunWith(SpringRunner.class) 
@SpringBootTest
public class AppUserServiceTest {

	@Autowired
	private AppUserService appUserService;

	@MockBean
	private AppUserRepository appUserRepository;

	@MockBean
	private ConfirmationTokenService confirmationTokenService;

	@Test
	public void testLoadUserByUsername() {

		AppUser appUser = new AppUser();
		appUser.setFirstName("Elias");
		appUser.setLastName("Abi Atallah");
		appUser.setEmail("eabiatallah@gmail.com");
		appUser.setPassword("pass");
		appUser.setEnabled(true);
		appUser.setLocked(false);
		appUser.isAccountNonExpired();
		appUser.isCredentialsNonExpired();

		Mockito.when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));

		assertThat(appUserService.loadUserByUsername(appUser.getEmail()).isEnabled()).isEqualTo(Boolean.TRUE);
		assertThat(appUserService.loadUserByUsername(appUser.getEmail()).isAccountNonLocked()).isEqualTo(Boolean.TRUE);
		assertThat(appUserService.loadUserByUsername(appUser.getEmail()).getUsername())
				.isEqualTo("eabiatallah@gmail.com");

	}

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameException() {

		String email = "invalidEmail";
		doThrow(new UsernameNotFoundException("user with email invalidEmail not found")).when(appUserRepository)
				.findByEmail(email);
	  appUserService.loadUserByUsername(email);
		
	}

	@Test(expected = IllegalStateException.class)
	public void testSignUpUserEmailAlreadyExists() {

		AppUser appUser = new AppUser();
		appUser.setFirstName("Elias");
		appUser.setLastName("Abi Atallah");
		appUser.setEmail("eabiatallah@gmail.com");
		appUser.setPassword("pass");

		doThrow(new IllegalStateException("email already taken")).when(appUserRepository).findByEmail(appUser.getEmail());

		appUserService.signUpUser(appUser);
	}
	
	@Test
	public void testSignUpUser(){
		
		AppUser appUser = new AppUser();
		appUser.setFirstName("Elias");
		appUser.setLastName("Abi Atallah");
		appUser.setEmail("eabiatallah@gmail.com");
		appUser.setPassword("pass");
		
		ConfirmationToken confirmationToken = new ConfirmationToken("tokenValue", LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15), appUser);

		
		Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);
		Mockito.when(confirmationTokenService.saveConfirmationToken(confirmationToken)).thenReturn(confirmationToken);
		
		String s = appUserService.signUpUser(appUser);
		assertTrue(s.contains("tokenValue"));
	}
	
	@Test
	public void testEnableAppUser(){
		
		Mockito.when(appUserRepository.enableAppUser("email@gmail.com")).thenReturn(200);
		assertEquals(200, appUserService.enableAppUser("email@gmail.com"));
	}
	
	@Test
	public void testEnableAppUserInvalid(){
		
		Mockito.when(appUserRepository.enableAppUser("email@gmail.com")).thenReturn(-200);
		assertTrue(appUserService.enableAppUser("email@gmail.com") == -200);
	}
}
