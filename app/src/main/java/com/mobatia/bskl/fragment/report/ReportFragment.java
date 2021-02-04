package com.mobatia.bskl.fragment.report;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.report.adapter.RecyclerViewMainAdapter;
import com.mobatia.bskl.fragment.report.adapter.StudentSpinnerRport;
import com.mobatia.bskl.fragment.report.model.DataModel;
import com.mobatia.bskl.fragment.report.model.StudentInfoModel;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 27/06/18.
 */

public class ReportFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,
        IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {

    RelativeLayout mtitleRel;
    RelativeLayout alertTxtRelative;
    RelativeLayout CCAFRegisterRel;
    ArrayList<StudentModel> studentsModelArrayList;
    ListView studentDetail;
   TextView studentName;
    TextView textViewYear;
    String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img="";
    String progressReport="";
    String section="";
    String alumini="";
    RelativeLayout noDataRelative;
    TextView noDataTxt;
    ImageView noDataImg;
    LinearLayout mStudentSpinner;
    ImageView back;
    ImageView home;
    ImageView studImg;
    Bundle extras;
    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    private RecyclerView mRecycleView;
    private TextView alertText;

    public ReportFragment() {

    }

    public ReportFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.progress_report, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText("Reports");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
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
        if (extras != null) {
        }
//
        alertText = mRootView.findViewById(R.id.noDataTxt);
        mStudentSpinner = mRootView.findViewById(R.id.studentSpinner);
        studentName = mRootView.findViewById(R.id.studentName);
        studImg = mRootView.findViewById(R.id.imagicon);
        noDataImg = mRootView.findViewById(R.id.noDataImg);
        mRecycleView = mRootView.findViewById(R.id.recycler_view_list);
        noDataRelative= mRootView.findViewById(R.id.noDataRelative);
        relMain = mRootView.findViewById(R.id.relMain);
        alertTxtRelative = mRootView.findViewById(R.id.noDataRelative);
        textViewYear = mRootView.findViewById(R.id.textViewYear);
        noDataTxt = mRootView.findViewById(R.id.noDataTxt);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private StudentModel addStudentDetails(JSONObject dataObject) {
        StudentModel studentModel = new StudentModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        studentModel.setProgressReport(dataObject.optString("progressreport"));
        studentModel.setAlumini(dataObject.optString("alumi"));

        return studentModel;
    }
    private void getStudentsListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    studentsModelArrayList = new ArrayList<>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext).equalsIgnoreCase("")) {
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_id = studentsModelArrayList.get(0).getmId();
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    stud_img= studentsModelArrayList.get(0).getmPhoto();
                                    progressReport= studentsModelArrayList.get(0).getProgressReport();
                                    section=studentsModelArrayList.get(0).getmSection();
                                    alumini=studentsModelArrayList.get(0).getAlumini();
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");
                                }
                                else
                                {

                                    int studentSelectPosition=Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    progressReport= studentsModelArrayList.get(studentSelectPosition).getProgressReport();
                                    stud_img= studentsModelArrayList.get(studentSelectPosition).getmPhoto();
                                    alumini= studentsModelArrayList.get(studentSelectPosition).getAlumini();
                                    System.out.println("selected student image"+studentsModelArrayList.get(studentSelectPosition).getmPhoto());
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());

                                }
                                if(progressReport.equalsIgnoreCase("0")) {
                                    alertTxtRelative.setVisibility(View.GONE);
                                    alertText.setVisibility(View.GONE);
                                    noDataImg.setVisibility(View.GONE);
                                    mRecycleView.setVisibility(View.VISIBLE);
                                    if (AppUtils.isNetworkConnected(mContext)) {
                                        System.out.println("test working");
                                        getReportListAPI(URL_GET_STUDENT_REPORT);
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                    }
                                }
                                else
                                {
                                    alertTxtRelative.setVisibility(View.VISIBLE);
                                    alertText.setVisibility(View.VISIBLE);
                                    noDataImg.setVisibility(View.VISIBLE);
                                    alertText.setText(R.string.not_available_child);
                                    mRecycleView.setVisibility(View.GONE);
                                    System.out.println("testing........");
                                }
                            } else {
//                                mRecycleView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {


            }
        });


    }
    private void getReportListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "studid"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {
//                    studentsModelArrayList = new ArrayList<>();//wrong
                    ArrayList<StudentInfoModel> mStudentModel=new ArrayList<>();

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("responseArray");

                            if (data.length() > 0) {
                                noDataRelative.setVisibility(View.GONE);
                                mRecycleView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    StudentInfoModel model = new StudentInfoModel();
                                    model.setAcyear(dataObject.optString("Acyear"));
                                    JSONArray list =dataObject.getJSONArray("data");
                                    ArrayList<DataModel> mDatamodel=new ArrayList<DataModel>();
                                    if(list.length()>0)
                                    {
                                        for(int x=0;x<list.length();x++)
                                        {
                                            JSONObject listObject= list.getJSONObject(x);
                                            DataModel xmodel=new DataModel();
                                            xmodel.setAcademic_year(listObject.optString("academic_year"));
                                            xmodel.setClass_id(listObject.optString("class_id"));
                                            xmodel.setCreated_on(listObject.optString("created_on"));
                                            xmodel.setFile(listObject.optString("file"));
                                            xmodel.setReporting_cycle(listObject.optString("reporting_cycle"));
                                            xmodel.setStud_id(listObject.optString("stud_id"));
                                            xmodel.setUpdated_on(listObject.optString("updated_on"));
                                             mDatamodel.add(xmodel);


                                        }
                                    }
                                   /*else
                                    {
                                        mRecycleView.setVisibility(View.GONE);
                                        noDataRelative.setVisibility(View.VISIBLE);
                                        noDataTxt.setText("Currently you have no progress report");
                                    }
*/
                                    model.setmDataModel(mDatamodel);
                                    mStudentModel.add(model);
                                    mRecycleView.setVisibility(View.VISIBLE);
                                    RecyclerViewMainAdapter mRecyclerViewMainAdapter=new RecyclerViewMainAdapter(mContext,mStudentModel);
                                    mRecycleView.setAdapter(mRecyclerViewMainAdapter);
                                }


                            } else {
                                mRecycleView.setVisibility(View.GONE);
                                noDataRelative.setVisibility(View.VISIBLE);

                                alertTxtRelative.setVisibility(View.VISIBLE);
                                alertText.setVisibility(View.VISIBLE);
                                noDataImg.setVisibility(View.VISIBLE);
                                noDataTxt.setText("Currently you have no reports");
//                                Toast.makeText(getActivity(), "No progress reports available.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                       // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else {


                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_new));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button_new));

        }
        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        StudentSpinnerRport studentAdapter = new StudentSpinnerRport(mContext, mStudentArray);
        socialMediaList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        dialog.dismiss();
                        studentName.setText(mStudentArray.get(position).getmName());
                        stud_id = mStudentArray.get(position).getmId();
                        stud_name = mStudentArray.get(position).getmName();
                        stud_class = mStudentArray.get(position).getmClass();
                        stud_img=mStudentArray.get(position).getmPhoto();
                        section=mStudentArray.get(position).getmSection();
                        progressReport=mStudentArray.get(position).getProgressReport();
                        alumini=mStudentArray.get(position).getAlumini();
                        textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        }
                        else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        if(progressReport.equalsIgnoreCase("0")) {
                            alertTxtRelative.setVisibility(View.GONE);
                            alertText.setVisibility(View.GONE);
                            noDataImg.setVisibility(View.GONE);
                            mRecycleView.setVisibility(View.VISIBLE);
                            if (AppUtils.isNetworkConnected(mContext)) {
                                System.out.println("test working");
                                getReportListAPI(URL_GET_STUDENT_REPORT);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                        }
                        else
                        {
                            alertTxtRelative.setVisibility(View.VISIBLE);
                            alertText.setVisibility(View.VISIBLE);
                            noDataImg.setVisibility(View.VISIBLE);
                            alertText.setText(R.string.not_available_child);
                            mRecycleView.setVisibility(View.GONE);
                            System.out.println("testing........");
                        }


                       /* if (AppUtils.isNetworkConnected(mContext)) {
                            getReportListAPI(URL_GET_STUDENT_REPORT);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
*/
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


    }

    @Override
    public void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            //AppController.getInstance().trackScreenView("Report Screen Fragment"+" "+ PreferenceManager.getUserEmail(mContext)+ Calendar.getInstance().getTime());
            AppController.getInstance().trackScreenView("Report Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");
        }
    }
}
