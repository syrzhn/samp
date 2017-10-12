package ru.syrzhn.samples.mvc.tree_view1.model;

import java.util.Stack;

public abstract class ANode {
	public String mID;
	
	public Stack<ANode> mChildren;
	public Stack<ANode> mAncestors;
	
	public ANode() {
		mChildren = new Stack<ANode>();
		mAncestors = new Stack<ANode>();
	}
	
	@Override
	public String toString() {
		return mID;
	}
}
