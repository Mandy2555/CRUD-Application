package com.myapp.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.myapp.service.JWETokenService;
import com.myapp.utils.DateUtil;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;

@Service("jweTokenService")
@Repository
public class JWETokenServiceImpl implements JWETokenService {
	
	private static final Logger LOG= LoggerFactory.getLogger(JWETokenServiceImpl.class);

	String secretKey = "o00+LVwPqxt4jHbArk50b2yaMVDM9b2mYmWdj95SNbE=";
	
	@Override
    public String encryptToken(final String userId, final String userType) {
        LOG.info("Starting token encryption for userId: {}", userId);

        try {
            LOG.debug("Using secret key: {}", secretKey);

            // Create the header
            final JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
            LOG.debug("JWE Header created: {}", header);

            // Set the plain text
            final Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("userId", userId);
            jsonObject.put("requestedFor", userType);
            jsonObject.put("requestTime", DateUtil.currentTimeStamp());
            LOG.debug("Payload created: {}", jsonObject);

            final Payload payload = new Payload(jsonObject);

            // Create the JWE object and encrypt it
            final JWEObject jweObject = new JWEObject(header, payload);
            LOG.debug("JWE Object created: {}", jweObject);

            // Decode the base64 encoded secret key
            byte[] decodedSecretKey = Base64.getDecoder().decode(secretKey);
            LOG.debug("Decoded secret key: {}", decodedSecretKey);

            jweObject.encrypt(new DirectEncrypter(decodedSecretKey));
            LOG.info("JWE Object encrypted successfully");

            // Serialize to compact JOSE form...
            String token = jweObject.serialize();
            LOG.info("Token serialization successful: {}", token);

            return token;

        } catch (final Exception e) {
            LOG.error("Error occurred during token encryption for userId: {}", userId, e);
            return null;
        }
    }
		
		@Override
		public String decryptToken(final String encryptJwtToken) {

			try {

				// Parse into JWE object and decrypt
				final JWEObject jweObject = JWEObject.parse(encryptJwtToken);
				jweObject.decrypt(new DirectDecrypter(secretKey.getBytes()));
				//Get the plain text
				return jweObject.getPayload().toString();

			} catch (final Exception e) {
			  return null;
			}
		}
}
