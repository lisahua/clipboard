package clipboard.model.parser;

import java.util.ArrayList;
import java.util.List;

public class TestLCS {

	public static void main(String[] args) {
		LongestCommonSubsequence<Character> lcs = new LongestCommonSubsequence<Character>();
		List<Character> input1 = new ArrayList<Character>();
		input1.add('a');
		input1.add('b');
		input1.add('c');
		List<Character> input2 = new ArrayList<Character>();
		input2.add('b');
		input2.add('c');
		input2.add('d');
		System.out.println(lcs.getLCS(input1, input2));
		System.out.println(lcs.getLeftCSIndexes());
		System.out.println(lcs.getRightCSIndexes());
	}
}
