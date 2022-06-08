package project.instagram.common.enums.constants;

import org.springframework.stereotype.Component;

@Component
public class Validation {

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String CANNOT_SEARCH = "You have run out of quantity search or you did not have buy any packages";

	public static final String WAITING_A_FEW_MINUTES = "Waiting a few minutes for the crawl system to crawl hashtag data for you";
	
	public static final String DATA_IS_EXISTS = "Data crawl is exists";

}
