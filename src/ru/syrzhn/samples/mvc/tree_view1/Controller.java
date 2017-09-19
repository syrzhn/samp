package ru.syrzhn.samples.mvc.tree_view1;

import ru.syrzhn.samples.mvc.tree_view1.model.MNode;
import ru.syrzhn.samples.mvc.tree_view1.model.Model;

public class Controller {
	
	private Viewer mViewer;
	
	private Model mModel;
	
	public Controller(Viewer viewer) {
		mModel = new Model(3, 3);
		mViewer = viewer;
	}
	
	public void setViewer(Viewer viewer) {
		mViewer = viewer;
	}
	
	public ISource setData() {
		String str = mViewer.mCurrentItem.toString();
		mViewer.mForm.printMessage("Adding new node to ".concat(str));
		mModel.mTree.generation++;
		MNode parent = (MNode)mViewer.mCurrentItem.getData();
		if (parent == null) throw new RuntimeException("Empty data in the item ".concat(str));
		MNode node = mModel.mTree.addNode(parent); 
		mViewer.mForm.printMessage(Model.messBuff.toArray( new String[ Model.messBuff.size() ] )); Model.messBuff.clear();
		mViewer.mForm.updateState(new Viewer.IForm.State( new String[] {String.valueOf(mModel.mTree.mAllNodesCount).concat(" nodes in the tree")} ));
		return new TreeSource(node);
	}

	public void disposeData() {
		String str = mViewer.mCurrentItem.toString();
		mViewer.mForm.printMessage("Disposing the node ".concat(str));
		MNode node = (MNode)mViewer.mCurrentItem.getData();
		if (node == null) throw new RuntimeException("Empty data in the item ".concat(str));
		mViewer.mForm.printMessage(mModel.mTree.disposeChild(node)); Model.messBuff.clear();
		mViewer.mForm.updateState(new Viewer.IForm.State(new String[] {String.valueOf(mModel.mTree.mAllNodesCount).concat(" nodes in the tree")}));
	}

	public String[] parseDataToItemColumns(Object data) {
		return new String[] { data.toString(), ((MNode)data).mPath } ;
	}

	public void setDataOnCollapse() {
		String str = mViewer.mCurrentItem.toString() + " was collapsed";
		mViewer.mForm.printMessage(str);
	}

	public void setDataOnExpand() {
		String str = mViewer.mCurrentItem.toString() + " was expanded";
		mViewer.mForm.printMessage(str);
	}

	public void setDataOnSelection() {
		String str = mViewer.mCurrentItem.toString() + " was selected";
		mViewer.mForm.printMessage(str);
	}

	public void setDataOnCheck() {
		String str = mViewer.mCurrentItem.toString() + (mViewer.mCurrentItem.getChecked() ? " was checked" : " was unchecked");
		mViewer.mForm.printMessage(str);
	}
	
	public interface ISource {
		ISource[] getChildren(ISource parent);
		Object getData();
	}
	
	public ISource[] getSource() {
		mViewer.mForm.updateState(new Viewer.IForm.State(new String[] {String.valueOf(mModel.mTree.mAllNodesCount).concat(" nodes in the tree")}));
		return new TreeSource().getBeginDataSet();
	}

	public ISource[] getSource(Object node) {
		mViewer.mForm.updateState(new Viewer.IForm.State(new String[] {String.valueOf(mModel.mTree.mAllNodesCount).concat(" nodes in the tree")}));
		return new TreeSource((MNode)node).getBeginDataSet();
	}
	
	public class TreeSource implements ISource {
		private MNode mChildren[];
		private MNode mSource;
		
		public TreeSource() {
			mChildren = mModel.getTreeData();
		}
		
		public TreeSource(MNode node) {
			if (node == null) throw new RuntimeException("Empty node!");
			mSource = node;
			mChildren = mModel.getTreeData(mSource);
		}
		
		public MNode getData() {return mSource;}
		
		public ISource[] getBeginDataSet() {
			TreeSource arg[] = new TreeSource[mChildren.length];
			for (int i = 0; i < mChildren.length; i++) {
				arg[i] = new TreeSource(mChildren[i]);
			}
			return arg;
		}
		
		@Override
		public ISource[] getChildren(ISource parent) {
			MNode node = (MNode)parent.getData();
			if (node == null) return null;
			mChildren = mModel.getTreeData(node);
			TreeSource ret[] = new TreeSource[mChildren.length];
			for (int i = 0; i < mChildren.length; i++) {
				ret[i] = new TreeSource(mChildren[i]);
			}
			return ret;
		}
		
		@Override
		public String toString() {
			String s = mSource.toString();
			if (mChildren == null || mChildren.length == 0) return s;
			s = s.concat(" and children: ");
			for (int i = 0; i < mChildren.length; i++) {
				MNode child = mChildren[i];
				if (i == 0)
					s.concat(child.toString());
				else
					s.concat(",").concat(child.toString());
			}
			return s;
		}
	}
}


