package clipboard.model.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;

import ut.seal.plugins.utils.change.UTChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.NodePair;
import clipboard.model.ClipBoardItem;

/**
 * @Author Lisa
 * @Date: Apr 12, 2014
 */
public class NewContextGenerator {

	// private HashMap<JavaEntityType, String> templateTypeValueMap,
	// methodTypeValueMap;
	HashSet<String> predictVariables = new HashSet<String>();

	public String getNewMethodString(Node method,
			ClipBoardItem template) {
		HashMap<String, String> templateMap = analyzeTemplate(template);
		HashMap<String, String> contextDiffMap = analyzeContextDifference(
				method, template);
		contextDiffMap = analyzeTypeValueDiff(contextDiffMap,
				analyzeNodes(template.getOldReg().getNodes().get(0)),
				analyzeNodes(method));
		HashMap<String, String> namePredictMap = predictName(contextDiffMap);
		if (template.getNewReg()==null) return "";
		return applyTemplate(template.getNewReg().getRegionText(),
				contextDiffMap, namePredictMap);

	}

	private HashMap<String, String> analyzeTemplate(ClipBoardItem template) {
		HashMap<String, String> templateChangeMap = new HashMap<String, String>();
		UTChangeDistiller distiller = new UTChangeDistiller();
	if (template.getNewReg()!=null) {
		List<SourceCodeChange> changeList = distiller.diffBlock(template
				.getOldReg().getNodes().get(0).copy(), template.getNewReg()
				.getNodes().get(0).copy());
		List<NodePair> templateMatches = distiller.getMatches();
//		System.out.println(templateMatches);
		for (NodePair pair : templateMatches) {
			if (pair.getLeft() != null) {
				String oldS = pair.getRight().getValue();
				String newS = pair.getLeft().getValue();
				if (!oldS.equals(newS))
					templateChangeMap.put(oldS, newS);
			}
		}
		

		// TODO get insert unique name
//		int insertVariableIndex = 0;
		for (SourceCodeChange change : changeList) {
			switch ((JavaEntityType) change.getChangedEntity().getType()) {
			case RETURN_STATEMENT:
				String name = change.getChangedEntity().getUniqueName();
				
				if (!name.contains(".")) {
					name = name.substring(0,name.length()-1);
					predictVariables.add(name);
				}
				break;
			default:
				break;
			}
			// System.out.println(change.getChangedEntity());
		}
	}
		return templateChangeMap;
	}

	private HashMap<String, String> analyzeContextDifference(Node method,
			ClipBoardItem template) {
		HashMap<String, String> contextDiffMap = new HashMap<String, String>();
		UTChangeDistiller distiller = new UTChangeDistiller();
		distiller.diffBlock(method.copy(),
				template.getOldReg().getNodes().get(0).copy());
		List<NodePair> contextDiffMatches = distiller.getMatches();
//		System.out.println(contextDiffMatches);
		for (NodePair pair : contextDiffMatches) {
			if (pair.getLeft() != null) {
				String contextS = pair.getRight().getValue();
				String templateS = pair.getLeft().getValue();
				if (!contextS.equals(templateS)) {
//					if (contextS.contains(";")) {
//						contextS = contextS.replace(";", "");
//						templateS = templateS.replace(";", "");
//					}
					contextDiffMap.put(contextS, templateS);
				}
			}

		}
		return contextDiffMap;
	}

	private HashMap<String, String> analyzeTypeValueDiff(
			HashMap<String, String> contextDiffMap,
			HashMap<JavaEntityType, String> templateTypeValueMap,
			HashMap<JavaEntityType, String> methodTypeValueMap) {

		// HashMap<String, String> namePredictMap = new HashMap<String,
		// String>();
		for (Entry<JavaEntityType, String> entry : methodTypeValueMap
				.entrySet()) {
			contextDiffMap.put(templateTypeValueMap.get(entry.getKey()),
					entry.getValue());
		}
		return contextDiffMap;
	}

	private HashMap<JavaEntityType, String> analyzeNodes(Node node) {
		HashMap<JavaEntityType, String> typeValueMap = new HashMap<JavaEntityType, String>();
		for (Node child : node.getAllNodes()) {
			switch ((JavaEntityType) child.getLabel()) {
			case SINGLE_TYPE:
				typeValueMap.put(JavaEntityType.SINGLE_TYPE, child.getValue()
						.split(":")[1].trim());
				break;
			case METHOD:
				typeValueMap.put(JavaEntityType.METHOD, child.getValue());
				break;

			// TODO: parameter, other class attributes

			default:
				break;

			}
		}
		return typeValueMap;
	}

	private HashMap<String, String> predictName(HashMap<String, String> nameMap) {
		// Pattern p = Pattern.compile("[^a-zA-Z]");
		HashMap<String, String> namePredictMap = new HashMap<String, String>();
		// extract common diff between template and method
		for (Entry<String, String> entry : nameMap.entrySet()) {
			if (entry.getKey().contains("."))
				continue;
			char[] keyC = entry.getKey().toCharArray();
			char[] valueC = entry.getValue().toCharArray();
			for (int i = 0; i < keyC.length; i++) {
				// exact match
				if (valueC[i] == keyC[i])
					continue;
				else {
					for (int j1 = keyC.length - 1, j2 = valueC.length - 1; j1 > i
							&& j2 > i; j1--, j2--) {
						if (valueC[j2] == keyC[j1])
							continue;
						else {
							namePredictMap.put(
									entry.getKey().substring(i, j1 + 1), entry
											.getValue().substring(i, j2 + 1));
							break;
						}
					}
					break;
				}
			}
		}
		HashMap<String, String> predictNames = namePredictMap;
		// for the set of predict variables
		for (String predictVariable : predictVariables) {
			for (Entry<String, String> entry : namePredictMap.entrySet()) {
				int matchID = predictVariable.toLowerCase().indexOf(
						entry.getKey().toLowerCase());
				if (matchID < 0)
					continue;

				String key = entry.getKey();
				String value = entry.getValue();
				String predictName = predictVariable;
				// exact match?
				if (predictName.contains(key)) {
					predictName = predictName.replace(key, value);
					predictNames.put(predictVariable, predictName);
					break;
				}

				char[] temp = key.toCharArray();
				temp[0] = (char) (temp[0] - 'A' + 'a');
				String keyLower = new String(temp);
				if (predictVariable.contains(keyLower)) {
					temp = value.toCharArray();
					temp[0] = (char) (temp[0] - 'A' + 'a');
					String valueLower = new String(temp);
					predictName = predictName.replace(keyLower, valueLower);
					predictNames.put(predictVariable, predictName);
					break;
				}
				// otherwise, all lowercases
				predictNames.put(predictVariable, predictName.toLowerCase()
						.replace(key.toLowerCase(), value.toLowerCase()));
			}
		}

		return predictNames;
	}

	private String applyTemplate(String newTemplate,
			HashMap<String, String> contextDiffMap,
			HashMap<String, String> namePredictMap) {

		// step1: replace all contextDiff
		for (Entry<String, String> entry : contextDiffMap.entrySet()) {
			newTemplate = newTemplate.replace(entry.getKey(), entry.getValue());

		}
		for (Entry<String, String> entry : namePredictMap.entrySet()) {
			newTemplate = newTemplate.replace(entry.getKey(), entry.getValue());
		}
		return newTemplate;
	}
}
