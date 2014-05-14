package de.thm.arsnova.connector.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.dao.UniRepDao;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

@Service
public class UniRepServiceImpl implements UniRepService {

	@Autowired(required = false)
	private UniRepDao uniRepDao;

	@Override
	public IliasCategoryNode getTreeObjects(int refId)
			throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}

		List<IliasCategoryNode> result = uniRepDao.getTreeObjects(refId);
		while(removeBranchesWithoutQuestionPools(result));

		result = this.removeNotMarkedNodes(result, false);

		if (result.size() == 1) {
			return result.get(0);
		}
		throw new ServiceUnavailableException();
	}

	@Override
	public List<IliasQuestion> getQuestions(int refId)
			throws ServiceUnavailableException {
		if (uniRepDao == null) {
			throw new ServiceUnavailableException();
		}
		return uniRepDao.getQuestion(refId);
	}

	@Override
	public List<IliasQuestion> getRandomQuestions(int refId) {
		int amountOfQuestions = uniRepDao.getQuestionAmountPerTest(refId);
		List<IliasQuestion> allQuestions = uniRepDao.getRandomTestQuestions(refId);

		Set<Integer> randomIds = new HashSet<>();
		while (randomIds.size() < amountOfQuestions) {
			randomIds.add((int)Math.floor(Math.random() * allQuestions.size()));
		}

		List<IliasQuestion> result = new ArrayList<>();
		for (int id : randomIds) {
			result.add(allQuestions.get(id));
		}

		return result;
	}

	private boolean removeBranchesWithoutQuestionPools( List<IliasCategoryNode> nodes ) throws ServiceUnavailableException {

		if (nodes == null) {
			return false;
		}

		Iterator<IliasCategoryNode> it = nodes.iterator();

		boolean hasRemovedNodes = false;

		while (it.hasNext()) {
			IliasCategoryNode node = it.next();

			if (node == null) {
				continue;
			}

			if (node.getQuestionCount() == 0 && (node.getChildren() == null || node.getChildren().size() == 0)) {
				it.remove();
				hasRemovedNodes = true;
				continue;
			}

			if (removeBranchesWithoutQuestionPools(node.getChildren())) {
				hasRemovedNodes = true;
			}
		}

		return hasRemovedNodes;

	}

	private List<IliasCategoryNode> removeNotMarkedNodes(List<IliasCategoryNode> nodes, boolean isParentMarked) {
		Map<String, String> metaTagRefIds = uniRepDao.getReferenceIdsWithMetaDataFlag("UniRepApp");

		if (nodes == null) {
			return null;
		}

		Iterator<IliasCategoryNode> it = nodes.iterator();

		while (it.hasNext()) {
			IliasCategoryNode node = it.next();

			if (metaTagRefIds.containsKey(String.valueOf(node.getId()))) {
				if("no".equals(metaTagRefIds.get(String.valueOf(node.getId())))) {
					it.remove();
					continue;
				}

				else if("yes".equals(metaTagRefIds.get(String.valueOf(node.getId())))) {
					removeNotMarkedNodes(node.getChildren(), true);
				}
			}

			else {
				if(isParentMarked) {
					removeNotMarkedNodes(node.getChildren(), true);
				}
				else {
					removeNotMarkedNodes(node.getChildren(), false);
					if(node.getChildren() == null || node.getChildren().size() == 0) {
						it.remove();
					}
				}
			}
		}

		return nodes;
	}
}
