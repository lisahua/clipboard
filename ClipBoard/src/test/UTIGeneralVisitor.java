package test;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;

public abstract class UTIGeneralVisitor<T> extends ASTVisitor {
	public abstract List<T> getResults();
}