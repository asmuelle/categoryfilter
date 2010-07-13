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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CategoryFilter implements EntryPoint {

	private Set<Tree> categoryTrees= new HashSet<Tree>();
	private Map<String, Set<String>> sitesByCategory = new HashMap<String, Set<String>>();
	private Set<String> allSites = new TreeSet<String>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		 buildCategoryTrees();

		NodeList<Element> rowList = RootPanel.get("sitetable").getElement().getElementsByTagName("tr");
		for (int i = 0; i < rowList.getLength(); i++) {
			TableRowElement tr = rowList.getItem(i).cast();
			allSites.add(tr.getId());
			NodeList<TableCellElement> cells = tr.getCells();
			if (cells != null && cells.getLength() > 1) {
				String[] categories = cells.getItem(0).getInnerText()
						.split(" ");
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

			public void onSelection(SelectionEvent<TreeItem> event) {

				Set<List<String>> filters = new HashSet<List<String>>();
				for (Tree tree : categoryTrees) {
					TreeItem item = tree.getSelectedItem();
					if (item != null && item.getParentItem()!=null) {
						List<String> filter = getChildCategories(item);
						if (filter != null)

							filters.add(filter);
					}
				}
				filterTable(getIntersectionOfSitesByFilter(filters));

			}
		};

		for (Tree tree : categoryTrees) {
			tree.addSelectionHandler(handler);
		}

	}

	private void buildCategoryTrees() {
	 
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
        
	}

	private static List<String> getChildCategories(TreeItem parent) {
		List<String> categories = new ArrayList<String>();
		categories.add(parent.getTitle());

		for (int i = 0; i < parent.getChildCount(); i++) {
			categories.addAll(getChildCategories(parent.getChild(i)));
		}
		return categories;
	}

	public Set<String> getUnionOfSitesByCategories(List<String> categories) {
		if (categories.isEmpty())
			return allSites;
		Set<String> visibleSites = new TreeSet<String>();
		for (String cat : categories) {
			Set<String> categorySites = sitesByCategory.get(cat);
			if (categorySites != null)
				visibleSites.addAll(categorySites);
		}
		return visibleSites;

	}
    /*
     * determine set of ids of sites that fulfill the filter criteria
     */
	private Set<String>  getIntersectionOfSitesByFilter(Set<List<String>> filterSet) {
		Set<String> visibleSites;
		if (filterSet.isEmpty()){
			visibleSites= allSites;
		} else {
		  visibleSites = new TreeSet<String>();
		
		  visibleSites.addAll(getUnionOfSitesByCategories(filterSet.iterator().next()));
		  for (List<String> group : filterSet) {
			visibleSites.retainAll(getUnionOfSitesByCategories(group));
		  }
		  
		}
		return visibleSites;
	}
    /*
     * hide sites that should not be visible
     */
	private void filterTable(Set<String> visibleSites) {
		for (String siteId:allSites) {
			Element row = Document.get().getElementById(siteId);
			if (visibleSites.contains(row.getId())) {
				row.setAttribute("style", " ");
			} else {
				row.setAttribute("style", "display:none");
			}
		}
	}
}
