package clipboard.contentassist;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;

public class SynditProposalComputer implements IJavaCompletionProposalComputer {
	private final SynditProposalProcessor fProcessor = new SynditProposalProcessor();

	@Override
	public List computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor arg1) {
		// TODO Auto-generated method stub

		return Arrays.asList(fProcessor.computeCompletionProposals(
				context.getViewer(), context.getInvocationOffset()));
	}

	@Override
	public List computeContextInformation(
			ContentAssistInvocationContext context, IProgressMonitor arg1) {
		// TODO Auto-generated method stub
		return Arrays.asList(fProcessor.computeContextInformation(
				context.getViewer(), context.getInvocationOffset()));
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return fProcessor.getErrorMessage();
	}

	@Override
	public void sessionEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionStarted() {
		// TODO Auto-generated method stub

	}

	// IContentAssistProcessor assistant = new NodeContentAssistant();
	//
	// @Override
	// public List<ICompletionProposal>
	// computeCompletionProposals(ContentAssistInvocationContext context,
	// IProgressMonitor monitor) {
	// return
	// Arrays.asList(assistant.computeCompletionProposals(context.getViewer(),
	// context.getInvocationOffset()));
	// }
	//
	// @Override
	// public List<IContextInformation>
	// computeContextInformation(ContentAssistInvocationContext context,
	// IProgressMonitor monitor) {
	// return
	// Arrays.asList(assistant.computeContextInformation(context.getViewer(),
	// context.getInvocationOffset()));
	// }
}