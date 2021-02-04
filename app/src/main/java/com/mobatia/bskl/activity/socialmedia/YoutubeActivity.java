package com.mobatia.bskl.activity.socialmedia;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.socialmedia.adapter.YoutubeRecyclerAdapter;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.socialmedia.model.YoutubeModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mobatia on 04/09/18.
 */

public class YoutubeActivity extends Activity implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants {
    private Context mContext;
    String nextPageToken = "";
    ArrayList<YoutubeModel> mYoutubeModelArrayList;
    RecyclerView mYoutubeRecyclerList;
    Bundle extras;
    private boolean loading = true;
    private boolean isBottomReached = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private int previousTotal = 0;
//    private int visibleThreshold = 5;
//    int firstVisibleItem, visibleItemCount, totalItemCount;
YoutubeRecyclerAdapter mYoutubeRecyclerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_recycler);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            nextPageToken = extras.getString("nextPageToken", "");
            mYoutubeModelArrayList=new ArrayList<>();
            mYoutubeModelArrayList = (ArrayList<YoutubeModel>) extras
                    .getSerializable("youtubeArray");
        }
        mYoutubeRecyclerList = findViewById(R.id.mYoutubeRecyclerList);
        mYoutubeRecyclerList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mYoutubeRecyclerList.setLayoutManager(mLayoutManager);
         mYoutubeRecyclerAdapter = new YoutubeRecyclerAdapter(mContext, mYoutubeModelArrayList);
        mYoutubeRecyclerList.setAdapter(mYoutubeRecyclerAdapter);




        mYoutubeRecyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                isBottomReached = !mYoutubeRecyclerList.canScrollVertically(1);

                if (isBottomReached) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!(nextPageToken.equalsIgnoreCase(""))) {

                            if (AppUtils.isNetworkConnected(mContext)) {
                                getYouTubeList(URL_GET_YOUTUBE_LIST_WITH_PAGINATION+nextPageToken);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }
                        //Do pagination.. i.e. fetch new data
                    }
//                    if (loading) {
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                            loading = false;
//                            if (!(nextPageToken.equalsIgnoreCase(""))) {
//
//                                if (AppUtils.isNetworkConnected(mContext)) {
//                                    getYouTubeList(URL_GET_YOUTUBE_LIST_WITH_PAGINATION+nextPageToken);
//                                } else {
//                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//                                }
//
//                            }
//                            //Do pagination.. i.e. fetch new data
//                        }
//                    }
                   /* visibleItemCount = mYoutubeRecyclerList.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached


                        // Do something

                        loading = true;
                        if (!(nextPageToken.equalsIgnoreCase(""))) {

                            if (AppUtils.isNetworkConnected(mContext)) {
                                getYouTubeList(URL_GET_YOUTUBE_LIST_WITH_PAGINATION);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }
                    }*/

                }
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
                System.out.println("The response is youtube" + successResponse);
                try {
                    JSONObject youtubeJsonObj = new JSONObject(successResponse);
                    if (youtubeJsonObj.has("nextPageToken")) {
                        nextPageToken = youtubeJsonObj.optString("nextPageToken");
                    }
                    else
                    {
                        nextPageToken="";
                    }
                    JSONArray youtubeListArray = youtubeJsonObj.optJSONArray("items");
                    int length=youtubeListArray.length();
                    if (nextPageToken.equalsIgnoreCase(""))
                    {
                        length=youtubeListArray.length()-1;
                    }
                    else

                    {
                        length=youtubeListArray.length();

                    }
                    for (int i=0;i<length;i++)
                    {
                        JSONObject youtubeListItem=youtubeListArray.optJSONObject(i);
                        JSONObject snippetYoutube=youtubeListItem.optJSONObject("snippet");
                        JSONObject idYoutube=youtubeListItem.optJSONObject("id");
                        JSONObject thumbnailsJsonObject=snippetYoutube.optJSONObject("thumbnails");
                        JSONObject defaultUrlJsonObject=thumbnailsJsonObject.optJSONObject("default");
                        JSONObject mediumUrlJsonObject=thumbnailsJsonObject.optJSONObject("medium");
                        JSONObject highUrlJsonObject=thumbnailsJsonObject.optJSONObject("high");


                        YoutubeModel mYoutubeModel=new YoutubeModel();
                        mYoutubeModel.setVideoId(idYoutube.optString("videoId"));
                        mYoutubeModel.setTitle(snippetYoutube.optString("title"));
                        mYoutubeModel.setPublishedAt(snippetYoutube.optString("publishedAt"));
                        mYoutubeModel.setChannelId(snippetYoutube.optString("channelId"));
                        mYoutubeModel.setThumbnailsDefault(defaultUrlJsonObject.optString("url"));
                        mYoutubeModel.setThumbnailsMedium(mediumUrlJsonObject.optString("url"));
                        mYoutubeModel.setThumbnailsHigh(highUrlJsonObject.optString("url"));
                        mYoutubeModelArrayList.add(mYoutubeModel);
                    }

                    mYoutubeRecyclerAdapter.notifyItemRangeChanged(0, mYoutubeRecyclerAdapter.getItemCount());


                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

}
