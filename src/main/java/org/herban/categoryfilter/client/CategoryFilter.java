package org.herban.categoryfilter.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;

/**
 * Entry point classes define <code>onModuleLoad()</code>. This class is
 * compiled to javascript code an runs in the browser, not on the server
 */
public class CategoryFilter implements EntryPoint {
	 private PieChart  pie;
	/**
	 * This is the entry point method. It parses the HTML response of the
	 * webserver. The elements inside the <div id=catlist> are interpreted as
	 * categories The rows belonging to <tbody id=sitetable> are interpreted as
	 * item list to be filtered
	 */
	public void onModuleLoad() {

		final Set<Tree> categoryTrees = buildCategoryTrees();
		;
		final Map<String, Set<String>> sitesByCategory = new HashMap<String, Set<String>>();
		final Set<String> allSites = new TreeSet<String>();
		final TabbedImageView detailsView = new TabbedImageView();
       

		NodeList<Element> rowList = RootPanel.get("sitetable").getElement()
				.getElementsByTagName("tr");
		for (int i = 0; i < rowList.getLength(); i++) {
			final TableRowElement tr = rowList.getItem(i).cast();
			allSites.add(tr.getId());
			NodeList<TableCellElement> cells = tr.getCells();

			if (cells != null && cells.getLength() > 1) {
				Anchor a = new Anchor("details", "#");
				RootPanel.get().add(a);
				a.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent arg0) {
						detailsView.show(tr.getId() );

					}
				});

				cells.getItem(2).setInnerHTML("");
				cells.getItem(2).appendChild(a.getElement());

				String[] categories = cells.getItem(0).getInnerText()
						.split(" ");
				cells.getItem(0).removeFromParent();
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
				pie.draw(createTable(visibleSites), createOptions());
				if (visibleSites.size()<2) {
					pie.setVisible(false);
				} else {
					pie.setVisible(true);
				}

			}
		};

		for (Tree tree : categoryTrees) {
			tree.addSelectionHandler(handler);
		}
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				Panel panel = RootPanel.get("piechart");

				 pie = new PieChart(createTable(allSites), createOptions());
				                 
				pie.addSelectHandler(createSelectHandler(pie));
			  
				panel.add(pie);
			}
		};

		// Load the visualization api, passing the onLoadCallback to be called
		// when loading is done.
		VisualizationUtils.loadVisualizationApi(onLoadCallback,
				PieChart.PACKAGE);

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
			}
		}
	}

	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("Energy Consumption by Sites");
		return options;
	}

	private SelectHandler createSelectHandler(final PieChart chart) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String message = "";

				// May be multiple selections.
				JsArray<Selection> selections = chart.getSelections();

				for (int i = 0; i < selections.length(); i++) {
					// add a new line for each selection
					message += i == 0 ? "" : "\n";

					Selection selection = selections.get(i);

					if (selection.isCell()) {
						// isCell() returns true if a cell has been selected.

						// getRow() returns the row number of the selected cell.
						int row = selection.getRow();
						// getColumn() returns the column number of the selected
						// cell.
						int column = selection.getColumn();
						message += "cell " + row + ":" + column + " selected";
					} else if (selection.isRow()) {
						// isRow() returns true if an entire row has been
						// selected.

						// getRow() returns the row number of the selected row.
						int row = selection.getRow();
						message += "row " + row + " selected";
					} else {
						// unreachable
						message += "Pie chart selections should be either row selections or cell selections.";
						message += "  Other visualizations support column selections as well.";
					}
				}

				Window.alert(message);
			}
		};
	}

	private AbstractDataTable createTable(Set <String> visibleSites) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Site");
		data.addColumn(ColumnType.NUMBER, "Consumption in kWh");
	 
		data.addRows(visibleSites.size());
		 int i=0;
		for (String site:visibleSites) {
		data.setValue(i, 0, Document.get().getElementById(site).getInnerText());
		data.setValue(i++, 1, 14);
		} 
		return data;
	}

}
