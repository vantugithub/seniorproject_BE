package project.instagram.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateTimeZoneUtils {
	
	private Date dateTimeZoneGMT;
	
	public Date getDateTimeZoneGMT() {
		Instant zulu = Instant.now();
		ZonedDateTime zdt = zulu.atZone(ZoneId.of("Europe/London"));
		dateTimeZoneGMT = Timestamp.valueOf(zdt.toLocalDateTime());
		
		return dateTimeZoneGMT;
	}
	
	public Date getDateZoneGMT() {
		Instant zulu = Instant.now();
		ZonedDateTime zdt = zulu.atZone(ZoneId.of("Europe/London"));
		dateTimeZoneGMT = Timestamp.valueOf(zdt.toLocalDateTime());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		try {
			dateFormat = df.parse(dateTimeZoneGMT.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateFormat;
	}
	
	public Date getLocalDateTime() {
		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		
		return out;
	}
	
	public Date formatDateTime(String dateStr) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		try {
			dateFormat = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateFormat;
	}
	
	public int getCurrentMonth() {
		Date date = getLocalDateTime();
		@SuppressWarnings("deprecation")
		int month = date.getMonth() + 1;
		
		return month;
	}

}
