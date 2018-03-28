package ru.syrzhn.samples.mvc.tree_view1;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainForm extends Dialog implements Viewer.IForm {
	
	private Object mTarget;
	public MainForm(Shell parent, int style, Object target) {
		super(parent, style);
		setText("������ ����������� �� ������ �����");
		mTarget = target;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		
		viewer = new Viewer(this); 

		createContents();
		
		viewer.getItemsFromMTree(tree);
		
		shlMainForm.open();
		shlMainForm.layout();
		while (!shlMainForm.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public Shell shlMainForm;
	public Tree tree;
	
	private Viewer viewer;
	private Display display;

	@Override
	public Object getData() {
		return mTarget;
	}
	
	@Override
	public void updateState(States state, Object o) {
		switch (state) {
		case CAPTION :
			display.asyncExec(() -> shlMainForm.setText("Tree sample ".concat(o.toString())) );
			break;
		case TREE_ITEM :
			break;
		}
	}

	@Override
	public void showMessage(String msg) {
		display.asyncExec(() -> {
				MessageBox msgBox = new MessageBox(shlMainForm, SWT.ICON_INFORMATION);
				msgBox.setText("Test application for tree �1");
				msgBox.setMessage(msg);
				msgBox.open();	
			}
		);
	}

	@Override
	public void printMessage(String[] msgs) {
		for (String msg : msgs)
			System.out.println(msg);
	}

	@Override
	public void printMessage(List<String> msgs) {
		for (String msg : msgs)
			System.out.println(msg);
		msgs.clear();
	}

	@Override
	public void printMessage(String msg) {
		System.out.println(msg);
	}

	@Override
	public Display getDisplay() {
		return display;
	}
	
	@Override
	public String getSearch() {
		return comboSearch.getText().trim();
	}

	private Combo comboSearch;

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlMainForm = new Shell();
		shlMainForm.setImage(SWTResourceManager.getImage(MainForm.class, "/com/LANIT/reports/Specification/images/specific.png"));
		shlMainForm.setSize(1280, 720);
		shlMainForm.setText("Tree sample");
		shlMainForm.setLayout(new GridLayout(3, false));
		
		ToolBar toolBar = new ToolBar(shlMainForm, SWT.FLAT | SWT.RIGHT);
		toolBar.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.addSelectionListener(viewer.getNewItemSelectionAdapter());
		tltmNewItem.setImage(SWTResourceManager.getImage(MainForm.class, "/com/LANIT/reports/Specification/images/new1.png"));
		
		ToolItem tltmDeleteItem = new ToolItem(toolBar, SWT.NONE);
		tltmDeleteItem.setImage(SWTResourceManager.getImage(MainForm.class, "/com/LANIT/reports/Specification/images/delete1.png"));
		tltmDeleteItem.addSelectionListener(viewer.getDeleteItemSelectionAdapter());
		
		comboSearch = new Combo(shlMainForm, SWT.NONE);
		comboSearch.addKeyListener(viewer.comboSearchHandler.getKeyAdapter());
		comboSearch.addSelectionListener(viewer.comboSearchHandler.getSelectionAdapter());
		comboSearch.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_comboSearch = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboSearch.heightHint = 35;
		comboSearch.setLayoutData(gd_comboSearch);
		
		ToolBar toolBarSearch = new ToolBar(shlMainForm, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmGo = new ToolItem(toolBarSearch, SWT.NONE);
		tltmGo.addSelectionListener(viewer.getSearchSelectionAdapter());
		tltmGo.setImage(SWTResourceManager.getImage(MainForm.class, "/com/LANIT/reports/Specification/images/search1.png"));
		
		TabFolder tabFolder = new TabFolder(shlMainForm, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		TabItem tbtmTree = new TabItem(tabFolder, SWT.NONE);
		tbtmTree.setText("Tree");
		
		tree = new Tree(tabFolder, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.MULTI);
		tbtmTree.setControl(tree);
		tree.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		tree.setHeaderVisible(true);
		tree.addListener(SWT.Collapse, viewer.getTableEventListener(SWT.Collapse));
		tree.addListener(SWT.Expand, viewer.getTableEventListener(SWT.Expand));
		//tree.addListener(SWT.CHECK, viewer.getTableEventListener(SWT.CHECK));
		tree.addListener(SWT.Selection, viewer.getTableEventListener(SWT.Selection));
		
		TreeColumn trclmnId = new TreeColumn(tree, SWT.NONE);
		trclmnId.setMoveable(true);
		trclmnId.setWidth(300);
		trclmnId.setText("ID");
		
		TreeColumn trclmnPath = new TreeColumn(tree, SWT.NONE);
		trclmnPath.setMoveable(true);
		trclmnPath.setWidth(300);
		trclmnPath.setText("Path");
		
		TreeColumn trclmnData = new TreeColumn(tree, SWT.NONE);
		trclmnData.setMoveable(true);
		trclmnData.setWidth(200);
		trclmnData.setText("Data");
		
		TreeColumn trclmnType = new TreeColumn(tree, SWT.NONE);
		trclmnType.setMoveable(true);
		trclmnType.setWidth(100);
		trclmnType.setText("Type");		
		
				TreeColumn trclmnAncestors = new TreeColumn(tree, SWT.NONE);
				trclmnAncestors.setMoveable(true);
				trclmnAncestors.setWidth(300);
				trclmnAncestors.setText("Ancestors");		
		
		TabItem tbtmHtml = new TabItem(tabFolder, SWT.NONE);
		tbtmHtml.setText("HTML");
		
		Browser browser = new Browser(tabFolder, SWT.NONE);
		browser.setText("<html>"
				+ "<head>" 
				+ "<base href=\"http://www.eclipse.org/swt/\" >"
				+ "<title>HTML Test</title>"
				+ "</head>"
				+ "<body>"
				+ "<a href=\"faq.php\">local link</a>"
				+ "</body>"
				+ "</html>");
		tbtmHtml.setControl(browser);
	}
}
