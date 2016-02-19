package clipboard.model;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: May 4, 2014
 */
public class UndoEditManager  {
	// private static UndoManager manager = new UndoManager();
	HashMap<IFile, Integer> offSetMap = new HashMap<IFile, Integer>();

	// UndoManager undoManager = new UndoManager();

	public void undoChange(UndoSystematicEdit edit) {
		CheckItem item = edit.getItem();
		IFile file=  item.getFile();
		IDocument doc = RegionParser.convertFileToDoc(file);
		int offset = 0;
		if (offSetMap.containsKey(file)) {
			offset = offSetMap.get(file);
		}
		try {
			doc.replace(item.getStartPos(), item.getPreview().length(),
					item.getOldMethod());
			offSetMap.put(file, offset - edit.getOffset());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public boolean addEdit(UndoSystematicEdit systematicEdit) {
		
		IFile file = systematicEdit.getFile();
		int offset = 0;
		if (offSetMap.containsKey(file))
			offset = offSetMap.get(file);
		systematicEdit.applyChange(offset);
		offSetMap.put(file, offset + systematicEdit.getOffset());
		return true;
	}

	
	public void clearOffsetMap() {
		offSetMap = new HashMap<IFile, Integer>();
	}
}
