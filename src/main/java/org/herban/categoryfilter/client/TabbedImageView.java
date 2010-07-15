package org.herban.categoryfilter.client;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
 

public class TabbedImageView {
    private DialogBox dialogBox;
    private Set<String> siteIdentifiers;
    private Image  ddaChart;
    public TabbedImageView() {
    	ddaChart= new Image("/ddachart/1",0,0,800,600);
    	this.dialogBox= new DialogBox(true, true);
    	dialogBox.setText("Details");
    	dialogBox.setTitle("Details");
    	
    	VerticalPanel boxLayout=new VerticalPanel();
    	HorizontalPanel timeFilterPanel=new HorizontalPanel();
    	 
    	timeFilterPanel.setSpacing(10);
    	
    	timeFilterPanel.add(new Label("filter from"));
    	timeFilterPanel.add(new DateBox());
    	timeFilterPanel.add(new Label("to"));
    	timeFilterPanel.add(new DateBox());
    	boxLayout.add(timeFilterPanel);
    	
    	TabPanel tabPanel=new TabPanel();
    	boxLayout.add(tabPanel);
    	VerticalPanel ddaChartPanel=new VerticalPanel();
    	ddaChartPanel.add(ddaChart);
    	
    	tabPanel.add( ddaChartPanel, "DDAChart");
    	HTMLPanel otherChartPanel=new HTMLPanel("<h1>coming soon ...</h1>");
    	 
    	
    	tabPanel.add( otherChartPanel, "some Interactive Chart");
    	dialogBox.add(boxLayout);
        tabPanel.selectTab(0);
        
    	 
    }
    
    public void show(String siteId){
    	dialogBox.center();
    	ddaChart.setUrl("/userscope/ddachart/"+siteId);
    	dialogBox.show();
    }
    
   
}
