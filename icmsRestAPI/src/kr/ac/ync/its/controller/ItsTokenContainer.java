package kr.ac.ync.its.controller;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class ItsTokenContainer {
	private static final String AUTHENTICATION_SCHEME = "Bearer";
	private static final String SECERET_SALT = "cjHerbSalt";
	private static final String SECERET_KEY = "itsAuthenticateKey";

	public String issueAuthenticate(String memberid, String permission) throws Exception {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECERET_SALT + SECERET_KEY);

			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.YEAR, 0);
			cal.add(Calendar.MONTH, 0);
			cal.add(Calendar.DATE, 0);
			cal.add(Calendar.HOUR, 0);
			cal.add(Calendar.MINUTE, 30);
			cal.add(Calendar.SECOND, 0);

			// System.out.println(cal.getTime());
			String token = JWT.create().withExpiresAt(cal.getTime()).withIssuer("its").withIssuedAt(new Date())
					.withClaim("memberid", memberid).withClaim("permission", permission).sign(algorithm);
			return token;

		} catch (JWTCreationException exception) {
			System.out.println(exception);
			return "TEMP_ERROR";
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		// Authenticate against a database, LDAP, file or whatever
		// Throw an Exception if the credentials are invalid
	}



	public boolean verifyAuthenticate(String _token) {
		Algorithm algorithm = Algorithm.HMAC256(SECERET_SALT + SECERET_KEY);
		try {
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier

			String token = _token.substring(AUTHENTICATION_SCHEME.length()).trim();
			DecodedJWT jwt = verifier.verify(token);

			System.out.println(token);
			System.out.println("Issuer: \t" + jwt.getIssuer());
			System.out.println("IssedAt: \t" + jwt.getIssuedAt());
			System.out.println("ExpiresAt: \t" + jwt.getExpiresAt());

			return true;
		} catch (Exception exception) {
			System.out.println(exception);
			return false;
		}
	}

	public String getUserid(String _token) {
		Algorithm algorithm = Algorithm.HMAC256(SECERET_SALT + SECERET_KEY);
		try {
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier

			String token = _token.substring(AUTHENTICATION_SCHEME.length()).trim();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim("memberid").asString();
		} catch (Exception exception) {
			System.out.println(exception);
			return "";
		}
	}

	public String getPermission(String _token) {
		Algorithm algorithm = Algorithm.HMAC256(SECERET_SALT + SECERET_KEY);
		try {
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier

			String token = _token.substring(AUTHENTICATION_SCHEME.length()).trim();
			DecodedJWT jwt = verifier.verify(token);

			return jwt.getClaim("permission").asString();
		} catch (Exception exception) {
			System.out.println(exception);
			return "";
		}
	}
}
