package de.thm.arsnova.connector.services;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;

import de.thm.arsnova.connector.core.NotFoundException;
import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

/** This service class provides access to ilias repository
 *
 * @author Paul-Christian Volkmer
 * @since 0.20.0
 */
public interface UniRepService {
	/** Returns a dump of the Ilias repository tree.
	 *
	 * @param refId The root nodes ID as reference ID
	 * @return A list of category nodes
	 * @throws ServiceUnavailableException
	 * @throws NotFoundException
	 */
	@PostAuthorize("isAuthenticated() and (hasRole('ADMIN') or hasPermission(#refId,'uniRepTree','read'))")
	IliasCategoryNode getTreeObjects(int refId) throws ServiceUnavailableException, NotFoundException;

	/** Returns a list of questions identified by the question pool or test reference ID
	 *
	 * @param refId The reference id of the question pool containing this question
	 * @return A list of questions containing the question, possible answers and feedback.
	 * @throws ServiceUnavailableException
	 */
	@PostAuthorize("isAuthenticated() and (hasRole('ADMIN') or hasPermission(#refId,'uniRepQuestion','read'))")
	List<IliasQuestion> getQuestions(int refId, boolean noRandomQuestions) throws ServiceUnavailableException;
	
	/** Returns a list of random questions identified by the test reference ID
	 *
	 * @param refId The reference id of the test containing this question
	 * @return A random list of questions containing the question, possible answers and feedback.
	 * @throws ServiceUnavailableException
	 */
	@PostAuthorize("isAuthenticated() and (hasRole('ADMIN') or hasPermission(#refId,'uniRepQuestion','read'))")
	List<IliasQuestion> getRandomQuestions(int refId);
}
