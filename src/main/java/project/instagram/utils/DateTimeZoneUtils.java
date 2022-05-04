package project.instagram.utils;

import java.sql.Timestamp;
import java.time.Instant;
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

}
