package project.instagram.entity;

import java.util.Calendar;
import java.util.Date;

public class DateRange {
	private Date startDate;
	private Date endDate;
	
	private Date createDate(int month, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);

		return calendar.getTime();
	}
	
	public void correspondingDateRange(Date currentDate, Date pointOfTime) {
		if (pointOfTime.after(currentDate)) {
			setStartDate(createDate(-1, pointOfTime));
			setEndDate(pointOfTime);
		} else {
			setStartDate(pointOfTime);
			setEndDate(createDate(1, pointOfTime));
		}
	}
	
	public DateRange() {
		super();
	}
	public DateRange(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
