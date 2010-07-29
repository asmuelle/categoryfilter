package org.herban.categoryfilter.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class DayLabel extends Label {
    private boolean excluded;
	public DayLabel(String text) {
		super(text);
		excluded=false;
		this.setStyleName("daylabel");
		 
		this.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				excluded=!excluded;
				 
				setStyleName(excluded?"excluded":"daylabel");
				setTitle(new StringBuffer("click to ").toString()+new StringBuffer(excluded?"include":"exclude"));
			}});
	}

}
