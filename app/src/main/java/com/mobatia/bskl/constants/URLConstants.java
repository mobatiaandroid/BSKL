/**
 *
 */
package com.mobatia.bskl.constants;

/**
 * @author RIJO K JOSE
 */
public interface URLConstants extends KeyConstants {


    //   ========================================LIVE API===========================================================
//        String POST_APITOKENURL = "https://bsklparentapp.com.my/bskl/oauth/access_token";
//        String URL_HEAD = "https://bsklparentapp.com.my/bskl/api/";
    //   ===================================================================================================
    //   ========================================LIVE API===========================================================
//        String POST_APITOKENURL = "http://alpha.mobatia.in:808/bskl/oauth/access_token";
//        String URL_HEAD = "http://alpha.mobatia.in:808/bskl/api/";
    //   ===================================================================================================

    //   ========================================DEV API===========================================================
//
    public String POST_APITOKENURL="http://alpha.mobatia.in:808/bskl/oauth/access_token";
    public String URL_HEAD = "http://alpha.mobatia.in:808/bskl/api/";
    //   ===================================================================================================

    String URL_DEVICE_REGISTRATION = URL_HEAD
            + "deviceregistration_V2";
    String URL_TERMS_OF_SERVICE = URL_HEAD
            + "terms_of_service";
    String URL_GET_OTP_VERIFICATION = URL_HEAD + "otpverfication_V2";
    String URL_GET_OTP_NEWPHONENUMBER = URL_HEAD + "newphone_number_request";
    String URL_LOGOUT = URL_HEAD + "logout_V2";
    String URL_GET_NOTICATIONS_LIST = URL_HEAD + "notifications";
    String URL_GET_NOTICATIONS_LIST_NEW = URL_HEAD + "notifications_new";
    String URL_GET_SAFE_GUARDING_LIST = URL_HEAD + "safeguarding";
    String URL_POST_SUBMIT_SAFE_GUARDING = URL_HEAD + "submitsafeguarding";
    String URL_GET_NOTICATIONS_STATUS_CHANGE = URL_HEAD + "notifactionstatuschanged";
    String URL_GET_NOTICATIONS_FAVOURITE_CHANGE = URL_HEAD + "favouritechanges";
    //	String URL_GET_ABOUTUS_LIST=URL_HEAD+"about_us";
    String URL_CLEAR_BADGE = URL_HEAD + "clear_calenderbadge";
    String URL_CALENDER_PUSH = URL_HEAD + "calenderpush";
    String URL_CALENDAR = URL_HEAD + "calender";
    String URL_CALENDAR_MONTH = URL_HEAD + "calender_month";
    String URL_USER_PROFILE = URL_HEAD + "userprofile";
    String URL_USER_PROFILE_STUDENT_DETAILS = URL_HEAD + "userprofile_studentdetails_new";
//    String URL_USER_PROFILE_STUDENT_DETAILS_V1 = URL_HEAD + "userprofile_studentdetails_new_v1";
    String URL_USER_PROFILE_STUDENT_DETAILS_V1 = URL_HEAD + "userprofile_studentdetails";
    String URL_GET_CONTACTUS = URL_HEAD + "contact_us";
    String URL_GET_DATA_COLLECTION_DETAILS = URL_HEAD + "data_collection_details";
    String URL_GET_DATA_COLLECTION_DETAILS_NEW = URL_HEAD + "data_collection_details_new";
    String URL_GET_STUDENT_LIST = URL_HEAD + "studentlist";
    String URL_GET_SETTINGS_USER_DETAIL = URL_HEAD + "settings_userdetails";
    String URL_GET_SAFEGUARD_RESET = URL_HEAD + "reset_safeguarding_notifaction";
    String URL_GET_STUDENT_REPORT = URL_HEAD + "progressreport";
    String URL_PARENT_SIGNUP = URL_HEAD + "parent_signup";
    String URL_LOGIN = URL_HEAD + "login_V2";
    String URL_SUBMIT_DATA_COLLECTION = URL_HEAD + "submit_datacollection";
    String URL_SUBMIT_DATA_COLLECTION_NEW = URL_HEAD + "submit_datacollection_new";
    String URL_GET_TIME_TABLE_LIST = URL_HEAD + "timetable_v2";
    String URL_CHANGEPSAAWORD = URL_HEAD + "changepassword";
    String URL_FORGOTPASSWORD = URL_HEAD + "forgotpassword";
    String URL_GET_LEAVEREQUESTSUBMISSION = URL_HEAD + "requestLeave";
    String URL_GET_LEAVEREQUEST_LIST = URL_HEAD + "leaveRequests";
    String URL_GET_ATTENDANCE = URL_HEAD + "attendance_record";
    String URL_EMAIL_PUSH = URL_HEAD + "emailpush";
    String URL_TRIGGER_USER = URL_HEAD + "trigger_user";
    String URL_GET_DATACOLLECTION_COUNTRIES = URL_HEAD + "countries";
    String URL_GET_INSTA_LIST = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + INSTA_TOKEN;
    String URL_GET_FB_LIST = "https://graph.facebook.com/v3.1/" + FB_PAGE_ID + "/feed?access_token=" + FB_TOKEN + "&fields=" + FB_FIELDS;
    String URL_FB_PAGE = "https://www.facebook.com/" + FB_PAGE_NAME_ID;
    String URL_INSTA_PAGE = "https://www.instagram.com/" + INSTA_PAGE_NAME_ID;
    String URL_GET_YOUTUBE_LIST = "https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=" + YOTUBE_CHANNEL_ID + "&maxResults=10" + YOTUBE_API_KEY;
    String URL_GET_YOUTUBE_LIST_WITH_PAGINATION = "https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=" + YOTUBE_CHANNEL_ID + "&maxResults=10" + YOTUBE_API_KEY + "&pageToken=";

}
