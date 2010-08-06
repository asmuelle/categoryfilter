package org.herban.categoryfilter.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateRangePreselectionPanel extends Composite{

	public DateRangePreselectionPanel() {
		super();
		HorizontalPanel timeFilterPanel=new HorizontalPanel();
   	 
    	timeFilterPanel.setSpacing(10);
    	final DateBox b1=new DateBox();
    	b1.setValue(new Date(new Date().getTime()-1000*60*60*24));
    	final DateBox b2=new DateBox();
    	b2.setValue(new Date());	
    	final ListBox periodFilter= new ListBox();
    	periodFilter.addItem("-- please select --", "0");
    	periodFilter.addItem("Last Rolling Year", "1");
    	periodFilter.addItem("Last YTD", "2");
    	periodFilter.addItem("Last Full Quarter", "3");
    	periodFilter.addItem("Last Full Month", "4");
    	periodFilter.addItem("Last Full Year", "5");
    	periodFilter.addChangeHandler(new ChangeHandler (){

			public void onChange(ChangeEvent arg0) {
				b1.setValue(getStartDate(periodFilter.getSelectedIndex()));
				b2.setValue(getStopDate(periodFilter.getSelectedIndex()));
				
				
			}
        });
    	timeFilterPanel.add(periodFilter);
    	timeFilterPanel.add(new Label("from"));
    	timeFilterPanel.add(b1);
    	timeFilterPanel.add(new Label("to"));
    	timeFilterPanel.add(b2);
        timeFilterPanel.add(new WeekDaySelector());
        initWidget(timeFilterPanel);
	}
	

	private Date getStopDate(int selectedIndex) {
		Date today=new Date();
		if (selectedIndex==1) return today;
		else if (selectedIndex==4) return new Date(today.getYear() , today.getMonth()-1,31,0,0);
		else if (selectedIndex==5) return new Date(today.getYear()-1 , 11,31,0,0);
		return today;
	}

	private Date getStartDate(int selectedIndex) {
		Date today=new Date();
		 
		if (selectedIndex==1) return new Date(today.getYear()-1 , today.getMonth(), today.getDay(),0,0);
		else if (selectedIndex==4) return new Date(today.getYear() , today.getMonth()-1,1,0,0);
		else if (selectedIndex==5) return new Date(today.getYear()-1 , 0,1,0,0);
		return today;
	}

}
