package org.herban.categoryfilter.client;

import java.util.*;

public class SetAlgebra {

	/**
	 * @param categories A set of category identifiers
	 * @param allSites A set of site identifiers 
	 * @param sitesByCategory Maps category identifiers to sets of site identifiers
	 * @return A set of site identifiers. All corresponding sites belong at least one to one of the specified categories. If the set of category identifiers is empty, all site identifiers are returned
	 *          
	 */
	public static Set<String> getUnionOfItemsByCategories(
			List<String> categories, Set<String> allItems,
			Map<String, Set<String>> itemsByCategory) {
		if (categories.isEmpty())
			return allItems;
		Set<String> visibleItems = new TreeSet<String>();
		for (String cat : categories) {
			Set<String> categorySites = itemsByCategory.get(cat);
			if (categorySites != null)
				visibleItems.addAll(categorySites);
		}
		return visibleItems;

	}

	 
	/**
	 * @param filterSet
	 * @param allItems
	 * @param itemsByCategory
	 * @return A set of 
	 */
	public static Set<String> getIntersectionOfItemsByFilter(
			Set<List<String>> filterSet, Set<String> allItems,
			Map<String, Set<String>> itemsByCategory) {
		Set<String> items;
		if (filterSet.isEmpty()) {
			items = allItems;
		} else {
			items = new TreeSet<String>();

			items.addAll(getUnionOfItemsByCategories(filterSet
					.iterator().next(), allItems, itemsByCategory));
			for (List<String> group : filterSet) {
				items.retainAll(getUnionOfItemsByCategories(group,
						allItems, itemsByCategory));
			}

		}
		return items;
	}

}
