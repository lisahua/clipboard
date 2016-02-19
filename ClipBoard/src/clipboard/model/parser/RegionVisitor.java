package clipboard.model.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * @Author Lisa
 * @Date: Apr 7, 2014
 */
public class RegionVisitor extends ASTVisitor {
	
	List<SimpleName> names = new ArrayList<SimpleName>();
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

	  @Override
	  public boolean visit(MethodDeclaration node) {
	    methods.add(node);
	    return super.visit(node);
	  }

	  public List<MethodDeclaration> getMethods() {
	    return methods;
	  }
	public boolean visit(VariableDeclarationFragment node) {
		SimpleName name = node.getName();
		this.names.add(name);
		System.out.println("Declaration of '" + name );
		return false; // do not continue 
	}
	public List<SimpleName> getSimpleName() {
		return names;
	}

	public boolean visit(SimpleName node) {
		
			System.out.println("simple name '" + node.getIdentifier() );
		
		return true;
	}
	
	
}

