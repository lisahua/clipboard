package test;

import java.util.ArrayList;
import java.util.List;

public class UTGeneralVisitor<T> extends UTIGeneralVisitor<T> {
	public List<T>	results;

	public UTGeneralVisitor() {
		results = new ArrayList<T>();
	}

	@Override
	public List<T> getResults() {
		return this.results;
	}
}