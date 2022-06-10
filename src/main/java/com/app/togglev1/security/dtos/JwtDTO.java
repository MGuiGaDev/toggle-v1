package com.app.togglev1.security.dtos;

//import java.util.Collection;

//import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JwtDTO {

	@NonNull
	private String token;
}
