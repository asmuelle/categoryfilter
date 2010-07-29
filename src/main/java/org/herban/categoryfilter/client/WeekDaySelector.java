package org.herban.categoryfilter.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class WeekDaySelector extends Composite {

	public WeekDaySelector() {
		super();
		DayLabel mo=new DayLabel("M");
		Label tu=new DayLabel("T");
		Label we=new DayLabel("W");
		Label th=new DayLabel("T");
		Label fr=new DayLabel("F");
		Label sa=new DayLabel("S");
		Label su=new DayLabel("S");
		HorizontalPanel panel=new HorizontalPanel();
		panel.add(mo);
		panel.add(tu);
		panel.add(we);
		panel.add(th);
		panel.add(fr);
		panel.add(sa);
		panel.add(su);
		initWidget(panel);
		
		
		
	}

}
