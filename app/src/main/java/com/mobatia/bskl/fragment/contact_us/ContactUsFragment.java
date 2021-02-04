package com.mobatia.bskl.fragment.contact_us;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.web_view.LoadUrlWebViewActivityNew;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.contact_us.adapter.ContactUsAdapter;
import com.mobatia.bskl.fragment.contact_us.model.ContactUsModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 27/06/18.
 */

public class ContactUsFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {
    private WebSettings mwebSettings;
    private View mRootView;
    private Context mContext;
    private WebView web;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    TextView mTitleTextView, desc;
    RotateAnimation anim;
    private boolean loadingFlag = true;
    String mLoadUrl;
    private boolean mErrorFlag = false;
    String latitude, longitude, description, c_latitude, c_longitude;
    ArrayList<ContactUsModel> contactUsModelsArrayList = new ArrayList<>();
    RecyclerView contactList;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private LocationManager lm;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    Double lat, lng;
    private static View view;

    public ContactUsFragment() {

    }

    public ContactUsFragment(String title, String tabId) {
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
        Log.d("Called onCreateView", "asass");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            mRootView = inflater.inflate(R.layout.fragment_contact_us, container,
                    false);
        } catch (InflateException e) {
        }
        mContext = getActivity();
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getlatlong();
            callcontactUsApi(URL_GET_CONTACTUS);

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
        contactList = mRootView.findViewById(R.id.mnewsLetterListView);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText("Contact us");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        desc = mRootView.findViewById(R.id.description);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        contactList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contactList.setLayoutManager(llm);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        contactList.addItemDecoration(itemDecoration);
        contactList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }


    private void callcontactUsApi(String URL) {
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);

        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token"};
        String[] value = {PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject data = secobj.getJSONObject(JTAG_RESPONSE_DATA_ARRAY);
                            latitude = data.getString(JTAG_EVENT_LATITUDE);
                            longitude = data.getString(JTAG_EVENT_LONGITUDE);
                            description = data.getString(JTAG_DESCRIPTION);
                            desc.setText(description);
                            JSONArray contact = data.getJSONArray(JTAG_CONTACTS);
                            if (contact.length() > 0) {
                                for (int i = 0; i < contact.length(); i++) {
                                    JSONObject cObj = contact.getJSONObject(i);
                                    ContactUsModel contactUsModel = new ContactUsModel();
                                    contactUsModel.setContact_email(cObj.getString(JTAG_EMAIL));
                                    contactUsModel.setContact_phone(cObj.getString(JTAG_EVENT_PHONE));
                                    contactUsModel.setContact_name(cObj.getString(JTAG_TAB_NAME));
                                    contactUsModelsArrayList.add(contactUsModel);
                                }
                                System.out.println("sw344" + latitude + "--" + longitude);
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        Log.d("Map Ready", "Bam.");
                                        mMap = googleMap;
                                        mMap.getUiSettings().setMapToolbarEnabled(false);
                                        mMap.getUiSettings().setZoomControlsEnabled(false);

                                        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                        mMap.addMarker(new MarkerOptions()
                                                .position(latLng) //setting position
                                                .draggable(true) //Making the marker draggable
                                                .title("BSKL"));


                                        //Moving the camera
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                        //Animating the camera
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            public void onInfoWindowClick(Marker marker) {
                                                if (AppUtils.isNetworkConnected(mContext)) {
                                                    if (!isGPSEnabled) {
                                                        Intent callGPSSettingIntent = new Intent(
                                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                        startActivity(callGPSSettingIntent);
                                                    } else {
                                                        Intent intent = new Intent(mContext, LoadUrlWebViewActivityNew.class);
                                                        intent.putExtra("url", "http://maps.google.com/maps?saddr=" + c_latitude + "," + c_longitude + "&daddr=British International School Kulalalumpur - Kulalalumpur ");
                                                        intent.putExtra("tab_type", "Contact us");
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                }


                                            }
                                        });
                                    }
                                });

                                System.out.println("656" + contactUsModelsArrayList.size());

                                ContactUsAdapter contactUsAdapter = new ContactUsAdapter(mContext, contactUsModelsArrayList);
                                contactList.setAdapter(contactUsAdapter);
                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                    else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callcontactUsApi(URL_GET_CONTACTUS);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callcontactUsApi(URL_GET_CONTACTUS);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callcontactUsApi(URL_GET_CONTACTUS);

                    }
                    else {
                        CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                , getResources().getString(R.string.ok));
                        dialog.show();
                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    private void getlatlong() {
        Location location;
        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    else {
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,
                                0.0F, this);
                        location = lm
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,
                            0.0F, this);
                    location = lm
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }

                }


            }

        }
        c_latitude = String.valueOf(lat);
        c_longitude = String.valueOf(lng);
        System.out.println("lat---"+c_latitude);
        System.out.println("lat---"+c_longitude);

    }

    @Override
    public void onResume() {
        super.onResume();
        getlatlong();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Contact us Screen" + " " + PreferenceManager.getUserEmail(mContext) + Calendar.getInstance().getTime());
        }
    }

}
