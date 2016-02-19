package clipboard.model.parser;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.ui.text.correction.AssistContext;
import org.eclipse.jdt.internal.ui.text.correction.ProblemLocation;
import org.eclipse.jdt.internal.ui.text.correction.QuickFixProcessor;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;

import clipboard.model.CheckItem;

/**
 * @Author Lisa
 * @Date: May 7, 2014
 */
public class QuickFixPerformer {

	public static IJavaCompletionProposal[] actionPerform(CheckItem item) {
		IProblem[] problems = item.getErrorList();
		ProblemLocation[] pLocList = new ProblemLocation[problems.length];
		for (int i = 0; i < pLocList.length; i++) {
			pLocList[i] = new ProblemLocation(problems[i]);
		}

		AssistContext context = new AssistContext(item.getCu(),
				item.getStartPos(), item.getPreview().length());

		QuickFixProcessor quickFixProcessor = new QuickFixProcessor();
		try {
			IJavaCompletionProposal[] proposals = quickFixProcessor
					.getCorrections(context, pLocList);
//			item.setProposals(proposals);
			return proposals;

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
public static void applyProposals(CheckItem item) {
	IDocument doc = item.getDoc();
	
	IJavaCompletionProposal[] proposals = item.getProposals();
	for (IJavaCompletionProposal proposal: proposals) {
		
	}
}
}
