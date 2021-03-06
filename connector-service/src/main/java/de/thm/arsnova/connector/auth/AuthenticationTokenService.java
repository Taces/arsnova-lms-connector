package de.thm.arsnova.connector.auth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.persistence.domain.User;
import de.thm.arsnova.connector.persistence.repository.UserRepository;

@Service
public class AuthenticationTokenService {
	
	@Autowired
	private UserRepository userRep;
	
	public final String HEADER_SECURITY_TOKEN = "X-Auth-Token"; 
	
	/** Authenticates an user and writes the authentication token to
	 *  the response body.
	 *
	 * @param Authentication auth
	 * @param UserDetails ud
	 */
	@PostAuthorize("isAuthenticated()")
	public void authenticateUser(Authentication auth, HttpServletResponse response) throws IOException {
		UserDetails ud = (UserDetails) auth.getPrincipal();
		
		JSONObject obj = new JSONObject();
		obj.put(HEADER_SECURITY_TOKEN, createAndStoreToken(ud));
		
		response.getWriter().write(obj.toString());
	}
	
	
	/** Gets HEADER_SECURITY_TOKEN header from requests and tries to find
	 *  user associated with it.
	 * 
	 * @param HttpServletRequest request
	 * @return User user
	 */
	public User findUserByToken(HttpServletRequest request) {	
		String token = request.getHeader(HEADER_SECURITY_TOKEN);
		User user = null;
		
		if(token != null) {
			user = userRep.findByToken(token);
		}
		
		return user;
	}

	
	/** Generates an authentication token.
	 *
	 * @param ud UserDetails
	 * @return The authentication token string.
	 */
	private String generateToken(UserDetails ud) {
		Date now = new Date();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		
		return encoder.encodePassword(
				ud.getUsername() + now.toString() + String.valueOf(Math.random()),
				null
		);
	}
	
	
	/** Creates an authentication token from given UserDetails and
	 *  saves it in userRep.
	 *
	 * @param ud UserDetails
	 * @return The authentication token string.
	*/
	private String createAndStoreToken(UserDetails ud) {
		User user = userRep.findOne(ud.getUsername());
		String token = generateToken(ud);
		
		if(user == null) {
			user = new User();
			user.setUserId(ud.getUsername());
		}
		
		user.setAuthToken(token);
		userRep.save(user);
		
		return token;
	}
}
