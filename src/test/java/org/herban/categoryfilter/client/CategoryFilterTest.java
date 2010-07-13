package org.herban.categoryfilter.client;

 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class CategoryFilterTest extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "org.herban.categoryfilter.CategoryFilterJUnit";
  }

  /**
   * Tests the category union.
   * Not sure if this test makes sense, because it is more sophisticated than the implementation itself
   */
  /**
 * 
 */
public void testCategoryUnion() {
	  List<String> categories=new ArrayList<String>();
	  categories.add("Europe");
	  categories.add("Switzerland");
	  categories.add("Bern");
	  
	  Set<String> allItems= new HashSet<String>();
	  allItems.add("My Chalet");
	  allItems.add("Uncle Tom's cabin");
	  
	  Map<String, Set<String>> itemsByCategory= new HashMap<String, Set<String>>();
	  Set<String> itemSet1=new HashSet<String>();
	  itemSet1.add("Uncle Tom's cabin");
	  itemsByCategory.put("USA", itemSet1);
	  
	  
	  assertTrue(SetAlgebra.getUnionOfItemsByCategories(categories, allItems, itemsByCategory).isEmpty());
	  Set<String> itemSet2=new HashSet<String>();
	  itemSet2.add("My Chalet");
	  itemsByCategory.put("Bern", itemSet2);
	  
	  assertFalse(SetAlgebra.getUnionOfItemsByCategories(categories, allItems, itemsByCategory).isEmpty());
	  
	  
   
  }
  
  /**
 *  Not sure if this test makes sense, because it is more sophisticated than the implementation itself
 */
public void testCategoryIntersection() {
	  List<String> categories1=new ArrayList<String>();
	  categories1.add("Europe");
	  categories1.add("Switzerland");
	  categories1.add("Bern");
	  
	  List<String> categories2=new ArrayList<String>();
	  categories1.add("Chalet");
	  
	  Set<List<String>> filterSet=new HashSet<List<String>>();
	  filterSet.add(categories1);
	  filterSet.add(categories2);
	  
	  Set<String> allItems= new HashSet<String>();
	  allItems.add("My Chalet");
	  allItems.add("Uncle Tom's cabin");
	  
	  Set<String> itemSet2=new HashSet<String>();
	  Map<String, Set<String>> itemsByCategory= new HashMap<String, Set<String>>();
	  itemSet2.add("My Chalet");
	  itemsByCategory.put("Bern", itemSet2);
	  itemsByCategory.put("Chalet", itemSet2);
	  
	  
	  assertFalse(SetAlgebra.getIntersectionOfItemsByFilter(filterSet, allItems, itemsByCategory).isEmpty());
		 
  }

  


}
