package com.mobatia.bskl.fragment.socialmedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.web_view.LoadWebViewActivity;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.socialmedia.model.InstagramModel;
import com.mobatia.bskl.fragment.socialmedia.model.YoutubeModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 28/08/18.
 */

public class SocialMediaFragment extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants {

    private View mRootView;
    private static Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private RelativeLayout youtubeBtnRelative;
    private LinearLayout instagramButtonLinear;
    private LinearLayout facebookButtonLinear;
    String nextPageToken = "";
    ImageView youtubeImg;
    ImageView instagramImg;
    ImageView fbImg;
    TextView fbTextView;
    TextView fbTextViewOnly;
    TextView InstaTextViewOnly;
    TextView instaTextView;
    ArrayList<YoutubeModel> mYoutubeModelArrayList;
    ArrayList<InstagramModel> mInstagramModelArrayList;

    public SocialMediaFragment() {

    }

    public SocialMediaFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_social_media, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText(SOCIAL_MEDIA);
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        mYoutubeModelArrayList = new ArrayList<>();
        mInstagramModelArrayList = new ArrayList<>();
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getYouTubeList(URL_GET_YOUTUBE_LIST.replace(YOTUBE_CHANNEL_ID,PreferenceManager.getYouKey(mContext)));
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        relMain = mRootView.findViewById(R.id.relMain);
        youtubeImg = mRootView.findViewById(R.id.youtubeImg);
        instagramImg = mRootView.findViewById(R.id.instagramImg);
        fbImg = mRootView.findViewById(R.id.fbImg);
        fbTextView = mRootView.findViewById(R.id.fbTextView);
        fbTextViewOnly = mRootView.findViewById(R.id.fbTextViewOnly);
        InstaTextViewOnly = mRootView.findViewById(R.id.InstaTextViewOnly);
        instaTextView = mRootView.findViewById(R.id.instaTextView);
        youtubeBtnRelative = mRootView.findViewById(R.id.youtubeBtnRelative);
        instagramButtonLinear = mRootView.findViewById(R.id.instagramButton);
        facebookButtonLinear = mRootView.findViewById(R.id.facebookButton);

        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        youtubeBtnRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYoutubeModelArrayList.size() > 0) {
//                Intent mIntent=new Intent(mContext, YoutubeActivity.class);
//                mIntent.putExtra("nextPageToken",nextPageToken);
//                mIntent.putExtra("youtubeArray",mYoutubeModelArrayList);
//                mContext.startActivity(mIntent);

                    Intent mIntent = new Intent(mContext, LoadWebViewActivity.class);
                    mIntent.putExtra("url", "https://www.youtube.com/channel/" + mYoutubeModelArrayList.get(0).getChannelId() + "/videos");
                    mIntent.putExtra("tab_type", SOCIAL_MEDIA);
                    mContext.startActivity(mIntent);
                }

            }
        });

        instagramButtonLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, LoadWebViewActivity.class);
                mIntent.putExtra("url", URL_INSTA_PAGE);
                mIntent.putExtra("tab_type", SOCIAL_MEDIA);
                mContext.startActivity(mIntent);

            }
        });
        facebookButtonLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, LoadWebViewActivity.class);
                mIntent.putExtra("url", URL_FB_PAGE);
                mIntent.putExtra("tab_type", SOCIAL_MEDIA);
                mContext.startActivity(mIntent);

            }
        });


    }


    private void getYouTubeList(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {};
        String[] value = {};

        volleyWrapper.getResponseGET(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                // System.out.println("The response is youtube" + successResponse);
                try {
                    JSONObject youtubeJsonObj = new JSONObject(successResponse);
                    nextPageToken = youtubeJsonObj.optString("nextPageToken");
                    JSONArray youtubeListArray = youtubeJsonObj.optJSONArray("items");
                    for (int i = 0; i < youtubeListArray.length(); i++) {
                        JSONObject youtubeListItem = youtubeListArray.optJSONObject(i);
                        JSONObject snippetYoutube = youtubeListItem.optJSONObject("snippet");
                        JSONObject idYoutube = youtubeListItem.optJSONObject("id");
                        JSONObject thumbnailsJsonObject = snippetYoutube.optJSONObject("thumbnails");
                        JSONObject defaultUrlJsonObject = thumbnailsJsonObject.optJSONObject("default");
                        JSONObject mediumUrlJsonObject = thumbnailsJsonObject.optJSONObject("medium");
                        JSONObject highUrlJsonObject = thumbnailsJsonObject.optJSONObject("high");
                        YoutubeModel mYoutubeModel = new YoutubeModel();
                        mYoutubeModel.setVideoId(idYoutube.optString("videoId"));
                        mYoutubeModel.setTitle(snippetYoutube.optString("title"));
                        mYoutubeModel.setPublishedAt(snippetYoutube.optString("publishedAt"));
                        mYoutubeModel.setChannelId(snippetYoutube.optString("channelId"));
                        mYoutubeModel.setThumbnailsDefault(defaultUrlJsonObject.optString("url"));
                        mYoutubeModel.setThumbnailsMedium(mediumUrlJsonObject.optString("url"));
                        mYoutubeModel.setThumbnailsHigh(highUrlJsonObject.optString("url"));
                        mYoutubeModelArrayList.add(mYoutubeModel);
                    }
                    if (mYoutubeModelArrayList.size() > 0) {
                        Glide.with(mContext).load(AppUtils.replace(mYoutubeModelArrayList.get(0).getThumbnailsHigh())).centerCrop().into(youtubeImg);

                    }
                    getInstaList(URL_GET_INSTA_LIST.replace(INSTA_TOKEN,PreferenceManager.getInstaKey(mContext)));


                } catch (Exception ex) {
                    // System.out.println("The Exception in edit profile is" + ex.toString());
                    getInstaList(URL_GET_INSTA_LIST.replace(INSTA_TOKEN,PreferenceManager.getInstaKey(mContext)));

                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                getInstaList(URL_GET_INSTA_LIST.replace(INSTA_TOKEN,PreferenceManager.getInstaKey(mContext)));

            }
        });


    }


    private void getInstaList(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {};
        String[] value = {};

        volleyWrapper.getResponseGET(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                // System.out.println("The response is insta" + successResponse);
                try {
                    JSONObject instaJsonObj = new JSONObject(successResponse);
                    JSONArray instaListArray = instaJsonObj.optJSONArray("data");
                    for (int i = 0; i < instaListArray.length(); i++) {
                        InstagramModel mInstagramModel = new InstagramModel();

                        JSONObject instaListItem = instaListArray.optJSONObject(i);
                        JSONObject userJsonObject = instaListItem.optJSONObject("user");
                        if ( instaListItem.has("caption")) {
                            JSONObject captionJsonObject = instaListItem.optJSONObject("caption");
                            mInstagramModel.setText(captionJsonObject.optString("text"));
                        }else
                        {
                            mInstagramModel.setText("");

                        }

                        JSONObject thumbnailsinstaImagesJsonObject = instaListItem.optJSONObject("images");
                        JSONObject defaultUrlJsonObject = thumbnailsinstaImagesJsonObject.optJSONObject("thumbnail");
                        JSONObject lowresUrlJsonObject = thumbnailsinstaImagesJsonObject.optJSONObject("low_resolution");
                        JSONObject highresUrlJsonObject = thumbnailsinstaImagesJsonObject.optJSONObject("standard_resolution");


                        mInstagramModel.setUsername(userJsonObject.optString("username"));
                        mInstagramModel.setType(instaListItem.optString("type"));
                        mInstagramModel.setImagesthumbnail(defaultUrlJsonObject.optString("url"));
                        mInstagramModel.setImageslowres(lowresUrlJsonObject.optString("url"));
                        mInstagramModel.setImageshighres(highresUrlJsonObject.optString("url"));
                        mInstagramModelArrayList.add(mInstagramModel);
                    }
                    if (mInstagramModelArrayList.size() > 0) {
                        InstaTextViewOnly.setVisibility(View.GONE);
                        instaTextView.setVisibility(View.VISIBLE);
                        instaTextView.setText(mInstagramModelArrayList.get(0).getText());
                        Glide.with(mContext).load(AppUtils.replace(mInstagramModelArrayList.get(0).getImageshighres())).centerCrop().into(instagramImg);

                    }
                    else
                    {
                        InstaTextViewOnly.setVisibility(View.GONE);
                        instaTextView.setVisibility(View.GONE);
                        instagramImg.setVisibility(View.VISIBLE);
                        instagramImg.setImageResource(R.drawable.app_icon);

                    }

                    getFBList(URL_GET_FB_LIST.replace(FB_TOKEN,PreferenceManager.getFbKey(mContext)));


                } catch (Exception ex) {
                    // System.out.println("The Exception in edit profile is" + ex.toString());
                    getFBList(URL_GET_FB_LIST.replace(FB_TOKEN,PreferenceManager.getFbKey(mContext)));

                }
            }

            @Override
            public void responseFailure(String failureResponse) {
                InstaTextViewOnly.setVisibility(View.GONE);
                instaTextView.setVisibility(View.GONE);
                instagramImg.setVisibility(View.VISIBLE);
                instagramImg.setImageResource(R.drawable.app_icon);
                getFBList(URL_GET_FB_LIST.replace(FB_TOKEN,PreferenceManager.getFbKey(mContext)));

            }
        });


    }
    private void getFBList(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {};
        String[] value = {};

        volleyWrapper.getResponseGET(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                // System.out.println("The response is fb" + successResponse);
                try {
                    JSONObject fbJsonObj = new JSONObject(successResponse);
                    JSONArray fbJsonArray = fbJsonObj.optJSONArray("data");
                    if (fbJsonArray.length()>0)
                    {
                        JSONObject fbJson=fbJsonArray.optJSONObject(0);
                        if (fbJson.has("description"))
                        {
                            Glide.with(mContext).load(AppUtils.replace(fbJson.optString("full_picture"))).centerCrop().into(fbImg);
                            fbTextView.setText(fbJson.optString("description"));
                            fbImg.setVisibility(View.VISIBLE);
                            fbTextView.setVisibility(View.VISIBLE);
                            fbTextViewOnly.setVisibility(View.GONE);
                        }
                       else  if (fbJson.has("full_picture") && fbJson.has("message"))
                        {

                            Glide.with(mContext).load(AppUtils.replace(fbJson.optString("full_picture"))).centerCrop().into(fbImg);
                            fbTextView.setText(fbJson.optString("message"));
                            fbImg.setVisibility(View.VISIBLE);
                            fbTextView.setVisibility(View.VISIBLE);
                            fbTextViewOnly.setVisibility(View.GONE);
                        }
                        else  if (fbJson.has("full_picture") )
                        {
                            Glide.with(mContext).load(AppUtils.replace(fbJson.optString("full_picture"))).centerCrop().into(fbImg);
                            fbImg.setVisibility(View.VISIBLE);
                            fbTextViewOnly.setVisibility(View.GONE);
                            fbTextView.setVisibility(View.GONE);

                        }
                        else  if (fbJson.has("message") )
                        {
                            fbTextViewOnly.setText(fbJson.optString("message"));
                            fbTextViewOnly.setVisibility(View.VISIBLE);
                            fbImg.setVisibility(View.GONE);
                            fbTextView.setVisibility(View.GONE);

                        }

                    }
                    else
                    {
                        fbTextViewOnly.setVisibility(View.GONE);
                        //fbTextViewOnly.setText("No Data Available.");
                        fbTextViewOnly.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                        fbTextViewOnly.setVisibility(View.VISIBLE);
                        fbImg.setVisibility(View.VISIBLE);
                        fbImg.setImageResource(R.drawable.app_icon);
                        fbTextView.setVisibility(View.GONE);
                    }


                } catch (Exception ex) {
                    // System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                fbTextViewOnly.setVisibility(View.GONE);
                //fbTextViewOnly.setText("No Data Available.");
                fbTextViewOnly.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                fbTextViewOnly.setVisibility(View.VISIBLE);
                fbImg.setVisibility(View.VISIBLE);
                fbImg.setImageResource(R.drawable.app_icon);
                fbTextView.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {

            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            // AppController.getInstance().trackScreenView("Social Media Screen" +" "+ PreferenceManager.getUserEmail(mContext)+ Calendar.getInstance().getTime());
            AppController.getInstance().trackScreenView("Social Media. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");

        }
    }
}
