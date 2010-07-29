package org.herban.categoryfilter.client;

 
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.Table;

public class PieChartView {
    private DialogBox dialogBox;
    
    public PieChartView(final Set<String> selection) {
     	
    	this.dialogBox= new DialogBox(true, true);
    	dialogBox.setText("Pie Chart");
    	dialogBox.setTitle("Pie Chart");
    	  VisualizationUtils.loadVisualizationApi(new Runnable() {
    	      public void run() {
    	        dialogBox.add(createPieChart(selection));
    	       
    	       
    	      }}, PieChart.PACKAGE, Table.PACKAGE);

    
    }
    
    public void show( ){
    	dialogBox.center();
    
    	dialogBox.show();
    }
    /**
     * Creates a pie chart visualization.
     * 
     * @return panel with pie chart.
     */
    private Widget createPieChart(Set<String> selection) {
      /* create a datatable */
      NodeList<Element> rowList = RootPanel.get("sitetable").getElement().getElementsByTagName("tr");
      DataTable data = DataTable.create();
      data.addColumn(ColumnType.STRING, "Meter");
      data.addColumn(ColumnType.NUMBER, "kwHr");
      data.addRows(rowList.getLength());
      for (int i = 0; i < rowList.getLength(); i++) {
	    final TableRowElement tr = rowList.getItem(i).cast();
	    if (selection.contains(tr.getId())) {
	      data.setValue(i, 0, tr.getCells().getItem(2).getInnerText());
	      data.setValue(i, 1, Integer.parseInt(tr.getCells().getItem(3).getInnerText()));
	    }
      }
    
      
     
    

      /* create pie chart */

      PieChart.Options options = PieChart.Options.create();
      options.setWidth(400);
      options.setHeight(240);
      options.set3D(true);
      options.setTitle("Energy Consumption in kwHr per year");
      return new PieChart(data, options);
    }

   
}
