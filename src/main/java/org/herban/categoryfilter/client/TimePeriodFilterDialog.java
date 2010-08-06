package org.herban.categoryfilter.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class TimePeriodFilterDialog {
    private DialogBox dialogBox;
    
    public TimePeriodFilterDialog() {
    	final Map<String, String> colorMap=new HashMap<String,String>();
    	
    	this.dialogBox= new DialogBox(true, true);
    	dialogBox.setText("Filter");
    	dialogBox.setTitle("Filter");
    	
    	VerticalPanel boxLayout=new VerticalPanel();
    	HorizontalPanel labelChooser=new HorizontalPanel();
    	 MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() ;  
    	   oracle.add("Closed All Day");
    	   oracle.add("Before Opening");
    	   oracle.add("Open");
    	   oracle.add("Closed");
    	   oracle.add("Late Night Hours");
    	 
        colorMap.put("Closed All Day", "gray");
        colorMap.put("Open", "green");
        colorMap.put("Before Opening", "#b0a");
        colorMap.put("Closed", "#eee");
        colorMap.put("Late Night Hours", "red");
        
        
    	final SuggestBox labelBox=new SuggestBox(oracle);
    	labelChooser.add(labelBox);
    	labelBox.setText("Open");
         
         
    	 NumberFormat nf = NumberFormat.getFormat("00");
       
       
    	final FlexTable weekPanel=new FlexTable();
    	weekPanel.setStylePrimaryName("FlexTable");
    	ScrollPanel scrollPanel=new ScrollPanel(weekPanel);
    	scrollPanel.setSize("100%", "500px");
    	weekPanel.addClickHandler(new ClickHandler(){
     
			public void onClick(ClickEvent event) {
				Cell cell=weekPanel.getCellForEvent(event);
				Object color=colorMap.get(labelBox.getValue());
				if (color==null) {color="#aaa";}
				cell.getElement().setAttribute("style", "background-color:"+color);
				cell.getElement().setInnerHTML(labelBox.getValue());
				
			}});
        weekPanel.setText(0, 1,  "Monday");
       
        weekPanel.setText(0, 2,  "Tuesday");
        weekPanel.setText(0,3,"Wednesday");
        weekPanel.setText(0, 4,  "Thursday");
        weekPanel.setText(0, 5,  "Friday");
        weekPanel.setText(0, 6,  "Saturday");
        weekPanel.setText(0, 7, "Sunday");
     
        for (int i=0;i<48;i++) {
          weekPanel.setWidget(i+1, 0, new Label(nf.format(i*30/60)+":"+nf.format(i*30%60)));
          for (int j=1;j<8;j++) {
        	  weekPanel.setText(i+1, j,  "Closed");
        	  
        	  weekPanel.getCellFormatter().setStylePrimaryName(i+1, j, "FlexTable-ColumnLabelCell");
        		 
         	 
          }
        }
        
        
        
        boxLayout.add(labelChooser);
        boxLayout.add(scrollPanel);
    	dialogBox.add(boxLayout);
    	Button closeButton =new Button("close", new ClickHandler(){

			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}});
    	boxLayout.add(closeButton);
      
        
    	 
    }
    
    public void show(String siteId){
    	dialogBox.center();
    
    	dialogBox.show();
    }
    
   
}
