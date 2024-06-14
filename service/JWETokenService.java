package com.myapp.service;

public interface JWETokenService {
	  String encryptToken(String userId, String userType);

	  String decryptToken(String encryptJwtToken);
}
