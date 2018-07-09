package com.logate.academy.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.logate.academy.security.components.AuthComponent;

@RunWith(MockitoJUnitRunner.class)
public class AuthComponentUnitTest {

	@InjectMocks
	private AuthComponent authComponent;
	
	@Before
	public void setup() 
	{
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(authComponent, "key", "Test123");
	}
	
	@Test
	public void hasHeaderPermissionRegularTest()
	{
		boolean result = authComponent.hasHeaderPermission("Test123");
		assertThat(result).isEqualTo(true);
	}
	
	@Test
	public void hasHeaderPermissionInvalidTest()
	{
		boolean result = authComponent.hasHeaderPermission("1234");
		assertThat(result).isEqualTo(false);
	}
}
