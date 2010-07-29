package org.herban.categoryfilter.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
    	DateBox b1=new DateBox();
    	b1.setValue(new Date(new Date().getTime()-1000*60*60*24));
    	DateBox b2=new DateBox();
    	b2.setValue(new Date());	
    	ListBox periodFilter= new ListBox();
    	periodFilter.addItem("Last Full Year", "1");
    	periodFilter.addItem("Last Rolling Year", "1");
    	periodFilter.addItem("Last YTD", "1");
    	periodFilter.addItem("Last Full Quarter", "1");
    	periodFilter.addItem("Last Full Month", "1");
    	periodFilter.addItem("Last Full Year", "1");
    	timeFilterPanel.add(periodFilter);
    	timeFilterPanel.add(new Label("from"));
    	timeFilterPanel.add(b1);
    	timeFilterPanel.add(new Label("to"));
    	timeFilterPanel.add(b2);
        timeFilterPanel.add(new WeekDaySelector());
    	boxLayout.add(timeFilterPanel);
    	
    	TabPanel tabPanel=new TabPanel();
    	boxLayout.add(tabPanel);
    	VerticalPanel ddaChartPanel=new VerticalPanel();
    	ddaChartPanel.add(ddaChart);
    	
    	tabPanel.add( ddaChartPanel, "DDAChart");
    	HTMLPanel otherChartPanel=new HTMLPanel("<h1>coming soon ...</h1>");
    	 
    	
    	tabPanel.add( otherChartPanel, "some other Chart");
    	final TimePeriodFilterDialog filterDialog= new TimePeriodFilterDialog();
    
    	dialogBox.add(boxLayout);
    	Button closeButton =new Button("close", new ClickHandler(){

			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}});
    	  final Button filterButton=new Button("Period", new ClickHandler(){

  			public void onClick(ClickEvent event) {
  				filterDialog.show("");
  				
  			}});
    	HorizontalPanel buttonBar=new HorizontalPanel();
    	boxLayout.add(buttonBar);
    	buttonBar.add(filterButton);
    	buttonBar.add(closeButton);
    	
        tabPanel.selectTab(0);
        
    	 
    }
    
    public void show(String siteId, String meter){
    	dialogBox.center();
    	dialogBox.setText("Details for "+meter);
    	ddaChart.setUrl("/userscope/ddachart/"+siteId);
    	dialogBox.show();
    }
    
   
}
