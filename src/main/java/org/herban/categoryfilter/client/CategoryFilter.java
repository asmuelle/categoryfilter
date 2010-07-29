package org.herban.categoryfilter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
 

/**
 * Entry point classes define <code>onModuleLoad()</code>. This class is
 * compiled to javascript code an runs in the browser, not on the server
 */
public class CategoryFilter implements EntryPoint {
	 
	/**
	 * This is the entry point method. It parses the HTML response of the
	 * webserver. The elements inside the <div id=catlist> are interpreted as
	 * categories The rows belonging to <tbody id=sitetable> are interpreted as
	 * item list to be filtered
	 */
    private static HashMap<String, CheckBox> checkBoxes = new HashMap<String, CheckBox>();
	
	public void onModuleLoad() {

		final Set<Tree> categoryTrees = buildCategoryTrees();
		;
		final Map<String, Set<String>> sitesByCategory = new HashMap<String, Set<String>>();
		final Set<String> allSites = new TreeSet<String>();
		final Set<String> selectedSites = new HashSet<String>();
		final Set<String> visibleSites = new HashSet<String>();
			
			
		final TabbedImageView detailsView = new TabbedImageView();
        final Button compareButton=new Button("compare", new ClickHandler(){

			public void onClick(ClickEvent event) {
				final PieChartView pieChartView= new PieChartView(selectedSites);
				pieChartView.show();
				
			}});
        compareButton.setVisible(false);
        RootPanel.get("piechart").add(compareButton);
       
		NodeList<Element> rowList = RootPanel.get("sitetable").getElement()
				.getElementsByTagName("tr");
		for (int i = 0; i < rowList.getLength(); i++) {
			final TableRowElement tr = rowList.getItem(i).cast();
			allSites.add(tr.getId());
			NodeList<TableCellElement> cells = tr.getCells();
            
			if (cells != null && cells.getLength() > 1) {
				Anchor a = new Anchor(cells.getItem(2).getInnerHTML(), "#");
				RootPanel.get().add(a);
				a.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent arg0) {
						detailsView.show(tr.getId(), tr.getCells().getItem(2).getInnerText() );

					}
				});
               
				cells.getItem(2).setInnerHTML("");
				
				cells.getItem(2).appendChild(a.getElement());

				String[] categories = cells.getItem(0).getInnerText()
						.split(" ");
				CheckBox cb=new CheckBox();
			    checkBoxes.put(tr.getId(), cb);
				cb.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue()) {
							selectedSites.add(tr.getId());
						} else {
							selectedSites.remove(tr.getId());
						}
						    //selectedSites.retainAll(visibleSites);
							compareButton.setVisible(selectedSites.size()>1);
						
					}

					});
				cb.setTitle(tr.getId());
				cells.getItem(0).setInnerHTML(" ");
				cells.getItem(0).setAttribute("style", "");
				RootPanel.get().add(cb);
				cells.getItem(0).appendChild(cb.getElement());
				
				for (String cat : categories) {
					Set<String> categorySet = sitesByCategory.get(cat);
					if (categorySet == null) {
						categorySet = new TreeSet<String>();
						sitesByCategory.put(cat, categorySet);
					}
					categorySet.add(tr.getId());

				}
			}
		}
		 
		SelectionHandler<TreeItem> handler = new SelectionHandler<TreeItem>() {

			/**
			 * This method is invoked when the user selects a category in any
			 * category tree
			 * 
			 * @param event
			 *            this parameter is ignored, because on every selection
			 *            the state of all category trees must be evaluated
			 */
			public void onSelection(SelectionEvent<TreeItem> event) {

				Set<List<String>> filters = new HashSet<List<String>>();
				for (Tree tree : categoryTrees) {
					TreeItem item = tree.getSelectedItem();
					if (item != null && item.getParentItem() != null) {
						List<String> filter = getItemCategoriesRecursivly(item);
						if (filter != null)

							filters.add(filter);
					}
				}
				Set<String> visibleSites=SetAlgebra.getIntersectionOfItemsByFilter(filters,
						allSites, sitesByCategory);
				filterTable(visibleSites, allSites);
			    selectedSites.retainAll(visibleSites);
				compareButton.setVisible(selectedSites.size()>1);
				 

			}
		};

		for (Tree tree : categoryTrees) {
			tree.addSelectionHandler(handler);
		}
		 

	 

	}

	/**
	 * Interpret elements below <div id=catlist> as hierarchy. The class
	 * attribute points to the parent id Place selection trees inside <div
	 * id=menu> element
	 * 
	 * @return A set of Trees
	 */
	public static Set<Tree> buildCategoryTrees() {
		Set<Tree> categoryTrees = new HashSet<Tree>();
		NodeList<Element> nodeList = Document.get().getElementById("catlist")
				.getElementsByTagName("div");

		HashMap<String, TreeItem> itemSet = new HashMap<String, TreeItem>();
		for (int i = 0; i < nodeList.getLength(); i++) {

			Element div = nodeList.getItem(i);
			if (!div.getInnerText().equals("ROOT")) {
				TreeItem treeItem = new TreeItem(div.getInnerText());
				treeItem.setUserObject(div.getClassName());
				itemSet.put(div.getId(), treeItem);
			}

		}
		RootPanel.get("menu").getElement().setInnerHTML("");
		for (String itemId : itemSet.keySet()) {
			TreeItem item = itemSet.get(itemId);
			if (item.getUserObject().equals("i1")) {
				Tree tree = new Tree();

				categoryTrees.add(tree);
				RootPanel.get("menu").add(tree);

				tree.addItem(item);

				item.setTitle("click here to remove Filter:" + item.getText());
				item.setSelected(true);
			} else {
				item.setTitle(itemId);
				itemSet.get(item.getUserObject()).addItem(item);

			}

		}
		return categoryTrees;

	}

	/**
	 * @param categoryTreeItem
	 *            A category tree item
	 * @return a list of category identifiers that refers to the category of the
	 *         tree item and all categories of descendants
	 */
	public static List<String> getItemCategoriesRecursivly(
			TreeItem categoryTreeItem) {
		List<String> categories = new ArrayList<String>();
		categories.add(categoryTreeItem.getTitle());

		for (int i = 0; i < categoryTreeItem.getChildCount(); i++) {
			categories.addAll(getItemCategoriesRecursivly(categoryTreeItem
					.getChild(i)));
		}
		return categories;
	}

	/**
	 * Hide items that should be invisible, show items that should be visible
	 * 
	 * @param visibleItems
	 * @param allItems
	 */
	public static void filterTable(Set<String> visibleItems,
			Set<String> allItems) {
		int i = 0;
		for (String siteId : allItems) {
			Element row = Document.get().getElementById(siteId);
			if (visibleItems.contains(row.getId())) {
				i++;
				if (i % 2 == 0)
					row.setAttribute("style", "background-color:#EFEFEF;");
				else
					row.setAttribute("style", "background-color:#FFF;");

			} else {
				row.setAttribute("style", "display:none");
				CheckBox cb=checkBoxes.get(row.getId());
				cb.setValue(false);
			}
		}
	}
	 
	 

}
