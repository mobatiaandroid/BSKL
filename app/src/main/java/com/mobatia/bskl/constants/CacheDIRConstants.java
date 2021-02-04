/**
 * 
 */
package com.mobatia.bskl.constants;


import com.mobatia.bskl.R;
import com.mobatia.bskl.exception.ExceptionHandler;

/**
 * @author Rijo K Jose
 * 
 */
public interface CacheDIRConstants {
	String APP_DIR = "."
			+ ExceptionHandler.getContext().getString(R.string.app_name) + "/";

	String HTMLFILE = "file://APP_DIR";

	String WEB_CONTENT = APP_DIR + "WEBCONTENT/";

	String WEB_URL = APP_DIR + "WEBURL/";

	String VIDEOS_DIR = APP_DIR + "VIDEOS";

	String PHOTOS_LIST_DIR = APP_DIR + "PHOTOS_LIST";

	String PHOTOS_CAT_LIST_DIR = APP_DIR + "PHOTOS_CAT_LIST";

	String PHOTOS_GRID_DIR = APP_DIR + "PHOTOS_GRID";

	String NEWS_LIST_DIR = APP_DIR + "NEWS_LIST";

	String MENU_LIST_DIR = APP_DIR + "MENU_LIST";

	String MENU_ITEM_LIST_DIR = APP_DIR + "MENU_ITEM_LIST";

	String MENU_ITEM_CHECKOUT_DIR = APP_DIR + "MENU_ITEM_CHECKOUT";

	String LOC_LIST_DIR = APP_DIR + "LOC_LIST";

	String FOOD_LIST_DIR = APP_DIR + "FOOD_LIST";

	String FOOD_ITEM_LIST_DIR = APP_DIR + "FOOD_ITEM_LIST";

	String SOCIAL_LIST_DIR = APP_DIR + "SOCIAL_LIST";

	String DIRECTORY_CAT_LIST_DIR = APP_DIR + "DIRECTORY_CAT_LIST";

	String DIRECTORY_DEPT_LIST_DIR = APP_DIR + "DIRECTORY_DEPT_LIST";

	String DIRECTORY_STAFF_PROFILE_DIR = APP_DIR
			+ "DIRECTORY_STAFF_PROFILE";

	String GALLERY_DIR = APP_DIR + "GALLERY";

	String ABOUT_US_DIR = APP_DIR + "ABOUTUS";

	String FACILITIES_DIR = APP_DIR + "FACILITIES";

	String CALENDAR_CAT_DIR = APP_DIR + "CALENDAR_CAT";

	String CALENDAR_DATA_DIR = APP_DIR + "CALENDAR_DATA_DIR";

	String WISS_UP_DIR = APP_DIR + "WISS_UP";

	String SETTINGS_CACHE = APP_DIR + "SETTINGS/";

	String ACADAMIES_DIR = APP_DIR + "ACADAMIES";

}
