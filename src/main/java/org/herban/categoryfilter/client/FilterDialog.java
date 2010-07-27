package org.herban.categoryfilter.client;

import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
 

public class FilterDialog {
    private DialogBox dialogBox;
    private Set<String> siteIdentifiers;
   
    public FilterDialog() {
    	 
    	this.dialogBox= new DialogBox(true, true);
    	dialogBox.setText("Filter");
    	dialogBox.setTitle("Filter");
    	
    	VerticalPanel boxLayout=new VerticalPanel();
    	 
    	
    	TabPanel tabPanel=new TabPanel();
    	boxLayout.add(tabPanel);
    	FlexTable ddaChartPanel=new FlexTable();
    
    	
    	
    	VerticalPanel otherChartPanel=new VerticalPanel();
    	otherChartPanel.add(new CheckBox("Monday")); 
    	otherChartPanel.add(new CheckBox("Tuesday")); 
    	otherChartPanel.add(new CheckBox("Wednesday")); 
    	otherChartPanel.add(new CheckBox("Thursday")); 
    	otherChartPanel.add(new CheckBox("Friday")); 
    	otherChartPanel.add(new CheckBox("Saturday")); 
    	otherChartPanel.add(new CheckBox("Sunday")); 
    	VerticalPanel minutePanel=new VerticalPanel();
    	minutePanel.add(new CheckBox("00")); 
    	minutePanel.add(new CheckBox("15")); 
    	minutePanel.add(new CheckBox("30")); 
    	minutePanel.add(new CheckBox("45")); 
    	VerticalPanel yearPanel=new VerticalPanel();
    	yearPanel.add(new CheckBox("2005")); 
    	yearPanel.add(new CheckBox("2006")); 
    	yearPanel.add(new CheckBox("2007")); 
    	yearPanel.add(new CheckBox("2008")); 
    	yearPanel.add(new CheckBox("2009")); 
    	yearPanel.add(new CheckBox("2010")); 
    	
     
    	
    	ddaChartPanel.setWidget(0, 0, new CheckBox("00:00")); 
    	ddaChartPanel.setWidget(1, 0, new CheckBox("01:00")); 
    	ddaChartPanel.setWidget(2, 0, new CheckBox("02:00")); 
    	ddaChartPanel.setWidget(3, 0, new CheckBox("03:00")); 
    	ddaChartPanel.setWidget(4, 0, new CheckBox("04:00")); 
    	ddaChartPanel.setWidget(5, 0, new CheckBox("05:00")); 
    	ddaChartPanel.setWidget(6, 0, new CheckBox("06:00")); 
    	ddaChartPanel.setWidget(7, 0, new CheckBox("07:00")); 
    	ddaChartPanel.setWidget(8, 0, new CheckBox("08:00")); 
    	ddaChartPanel.setWidget(9, 0, new CheckBox("09:00")); 
    	ddaChartPanel.setWidget(10, 0, new CheckBox("10:00")); 
    	ddaChartPanel.setWidget(11, 0, new CheckBox("11:00")); 
    	ddaChartPanel.setWidget(0, 1, new CheckBox("12:00")); 
    	ddaChartPanel.setWidget(1, 1, new CheckBox("13:00")); 
    	ddaChartPanel.setWidget(2, 1, new CheckBox("14:00")); 
    	ddaChartPanel.setWidget(3, 1, new CheckBox("15:00")); 
    	ddaChartPanel.setWidget(4, 1, new CheckBox("16:00")); 
    	ddaChartPanel.setWidget(5, 1, new CheckBox("17:00")); 
    	ddaChartPanel.setWidget(6, 1, new CheckBox("18:00")); 
    	ddaChartPanel.setWidget(7, 1, new CheckBox("19:00")); 
    	ddaChartPanel.setWidget(8, 1, new CheckBox("20:00")); 
    	ddaChartPanel.setWidget(9, 1, new CheckBox("21:00")); 
    	ddaChartPanel.setWidget(10, 1, new CheckBox("22:00"));
    	ddaChartPanel.setWidget(11, 1, new CheckBox("23:00"));
        	
    	
    	
    	tabPanel.add( otherChartPanel, "Day");
    	tabPanel.add( ddaChartPanel, "Hour");
    	tabPanel.add( minutePanel, "Minute");
    	tabPanel.add( yearPanel, "Year");
    	
    	dialogBox.add(boxLayout);
    	Button closeButton =new Button("close", new ClickHandler(){

			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}});
    	boxLayout.add(closeButton);
        tabPanel.selectTab(0);
        
    	 
    }
    
    public void show(String siteId){
    	dialogBox.center();
    
    	dialogBox.show();
    }
    
   
}
