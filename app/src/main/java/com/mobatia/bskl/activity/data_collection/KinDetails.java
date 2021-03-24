package com.mobatia.bskl.activity.data_collection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.fragment.app.Fragment;
import androidx.core.view.GestureDetectorCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class KinDetails extends Fragment {

    Activity mContext;

    public KinDetails() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private KinAdapter mAdapter;
    private ArrayList<KinModel> modelList;
    private ArrayList<KinModel> tempList;
    private ImageView closeBtn, AddImageFragment;
    private LinearLayout place_holder;
    String closeMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_kin_details, container, false);
        mContext = getActivity();
        findViews(v);
        setData();

        return v;
    }

    public void showDialogCloseButton(final Activity activity, String msg, String msgHead, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msgHead);
        textHead.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                editor.putString("data_collection_flag", "1");
                editor.putString("kinmodel", " ");
                editor.putString("emergencymodel", " ");
                editor.apply();
                getActivity().finish();

            }
        });

        dialog.show();

    }


    public void showAlertOKButton(final Activity activity, String msg, String msgHead, String button, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msgHead);
        textHead.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText(button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void findViews(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        AddImageFragment = v.findViewById(R.id.addImgfragment);
        place_holder = v.findViewById(R.id.empty_placeholder);
        closeBtn = v.findViewById(R.id.closeImg);
        closeMsg = "Please update this information next time";
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                editor.putString("data_collection_flag", "1");
                editor.apply();
                showDialogCloseButton(mContext, "Alert", closeMsg, R.drawable.exclamationicon, R.drawable.round);

            }
        });

    }


    private void setData() {
        SharedPreferences prefs = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE);
        String data = prefs.getString("DATA_COLLECTION", null);
        modelList = new ArrayList<>();
        tempList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray arrayObj = jsonObj.getJSONArray("kin_details");
            Log.e("json: ", String.valueOf(arrayObj));
            for (int i = 0; i < arrayObj.length(); i++) {
                JSONObject dataObject = arrayObj.getJSONObject(i);
                String id = dataObject.getString("id");
                String user_id = dataObject.getString("user_id");
                String kin_id = dataObject.getString("kin_id");
                String name = dataObject.getString("name");
                String email = dataObject.getString("email");
                String phone = dataObject.getString("phone");
                String code = dataObject.getString("code");
                String user_mobile = dataObject.getString("user_mobile");
                String student_id = dataObject.getString("student_id");
                String kin_status = "5";
                String kin_request = "0";
                String pid = String.valueOf(i + 1);
                Log.e("pid: ", pid);
                modelList.add(new KinModel(id, user_id, kin_id, name, email, phone, code, student_id, user_mobile, kin_status, kin_request, "0", pid));
                tempList.add(new KinModel(id, user_id, kin_id, name, email, phone, code, student_id, user_mobile, kin_status, kin_request, "0", pid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter = new KinAdapter(getContext(), tempList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        AddImageFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tempList.add(new KinModel("", "", "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                modelList.add(new KinModel("", "", "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                mAdapter = new KinAdapter(getContext(), tempList);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(tempList.size() - 1);

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
         //   Log.e("setUserVisibleHint: ", "visibe");
            mAdapter = new KinAdapter(getContext(), tempList);
            recyclerView.setAdapter(mAdapter);
        } else {

        }
    }

//============================RecyclerView Adapter Class===================================================


    public class KinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        GestureDetectorCompat mDetector;
        private Context mContext;
        private ArrayList<KinModel> listData;


        public KinAdapter(Context context, ArrayList<KinModel> modelList) {
            this.mContext = context;
            this.listData = modelList;
        }

        public void updateList(ArrayList<KinModel> modelList) {
            // this.listData = tempList;

            SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(modelList);
            editor.putString("kinmodel", json);
            editor.apply();
            // notifyDataSetChanged();

        }

        public void updateData(ArrayList<KinModel> modelList) {
            // this.listData = modelList;
            SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(modelList);
            editor.putString("validationkin", "0");
            editor.putString("kinmodel", json);
            editor.apply();

//            notifyDataSetChanged();

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kin_item_recycler_list, viewGroup, false);
            SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(modelList);
            editor.putString("kinmodel", json);
            editor.apply();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //Here you can fill your row view
            if (holder instanceof ViewHolder) {
                final KinModel model = getItem(position);
                final ViewHolder genericViewHolder = (ViewHolder) holder;
                if (position > 0) {
                    if (position == (tempList.size() - 1)) {
                        genericViewHolder.AddItem.setVisibility(View.VISIBLE);
                        genericViewHolder.AddImage_last.setVisibility(View.GONE);
                    } else {
                        genericViewHolder.AddItem.setVisibility(View.GONE);
                        genericViewHolder.AddImage_last.setVisibility(View.GONE);
                    }
                } else if (tempList.size() == 1) {
                    genericViewHolder.AddItem.setVisibility(View.GONE);
                    genericViewHolder.AddImage_last.setVisibility(View.VISIBLE);
                    genericViewHolder.DeleteItem.setVisibility(View.GONE);

                } else {
                    genericViewHolder.AddItem.setVisibility(View.GONE);
                    genericViewHolder.DeleteItem.setVisibility(View.VISIBLE);
                    genericViewHolder.AddImage_last.setVisibility(View.GONE);
                }
                if (position == tempList.size() - 1) {
                    genericViewHolder.space.setVisibility(View.VISIBLE);
                } else {
                    genericViewHolder.space.setVisibility(View.GONE);
                }
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-zA-Z0-9._-]+";
            /*    if (model.getName().equals("")) {
                    genericViewHolder.Name.setBackgroundResource(R.drawable.rec_red);
                } else {
                    genericViewHolder.Name.setBackgroundResource(R.drawable.edit_text_login);
                }
              *//*  if (model.getEmail().equals("")) {
                    genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);
                } else {
                    genericViewHolder.Email.setBackgroundResource(R.drawable.edit_text_login);
                }
                if (!model.getEmail().matches(emailPattern)) {
                    genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);
                } else {
                    genericViewHolder.Email.setBackgroundResource(R.drawable.edit_text_login);
                }*//*
                if (model.getUser_mobile().equals("")) {
                    genericViewHolder.Contact.setBackgroundResource(R.drawable.rec_red);
                } else {
                    genericViewHolder.Contact.setBackgroundResource(R.drawable.edit_text_login);
                }*/
                SharedPreferences prefs = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE);
                String noti = prefs.getString("validationkin", " ");
                if (noti.equals("1")) {
                    if (model.getName().equals("")) {
                        genericViewHolder.Name.setBackgroundResource(R.drawable.rec_red);
                    } else {
                        genericViewHolder.Name.setBackgroundResource(R.drawable.edit_text_login);
                    }
                    if (!model.getEmail().equals("")) {
                        if (!model.getEmail().matches(emailPattern)) {
                            genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);
                        } else {
                            genericViewHolder.Email.setBackgroundResource(R.drawable.edit_text_login);
                        }
                    }
                    if (model.getUser_mobile().equals("")) {
                        genericViewHolder.Contact.setBackgroundResource(R.drawable.rec_red);
                    } else {
                        genericViewHolder.Contact.setBackgroundResource(R.drawable.edit_text_login);
                    }
                }
                String NamegrayPart = "Name";
                String NameredPart = "*";
                SpannableStringBuilder Namebuilder = new SpannableStringBuilder();
                SpannableString NameredColoredString = new SpannableString(NamegrayPart);
                NameredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, NamegrayPart.length(), 0);
                Namebuilder.append(NameredColoredString);
                SpannableString NameblueColoredString = new SpannableString(NameredPart);
                NameblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, NameredPart.length(), 0);
                Namebuilder.append(NameblueColoredString);
                genericViewHolder.Name.setHint(Namebuilder);

             /*   String EmailgrayPart = "Email";
                String EmailredPart = "*";
                SpannableStringBuilder Emailbuilder = new SpannableStringBuilder();
                SpannableString EmailredColoredString = new SpannableString(EmailgrayPart);
                EmailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, EmailgrayPart.length(), 0);
                Emailbuilder.append(EmailredColoredString);
                SpannableString EmailblueColoredString = new SpannableString(EmailredPart);
                EmailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, EmailredPart.length(), 0);
                Emailbuilder.append(EmailblueColoredString);
                genericViewHolder.Email.setHint(Emailbuilder);
*/
                String ContactgrayPart = "Contact Number";
                String ContactredPart = "*";
                SpannableStringBuilder Contactbuilder = new SpannableStringBuilder();
                SpannableString ContactredColoredString = new SpannableString(ContactgrayPart);
                ContactredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, ContactgrayPart.length(), 0);
                Contactbuilder.append(ContactredColoredString);
                SpannableString ContactblueColoredString = new SpannableString(ContactredPart);
                ContactblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, ContactredPart.length(), 0);
                Contactbuilder.append(ContactblueColoredString);
                genericViewHolder.Contact.setHint(Contactbuilder);
                // }
                //  Log.e("onBindViewHolder: ", String.valueOf(j));
                genericViewHolder.Name.setText(model.getName());
                genericViewHolder.Name.setSelection(genericViewHolder.Name.getText().length());
                genericViewHolder.Name.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub

                        modelList.get(position).setName(genericViewHolder.Name.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                        if (genericViewHolder.Name.getText().toString().equals("")) {
                            genericViewHolder.Name.setBackgroundResource(R.drawable.rec_red);
                            modelList.get(pos).setName(genericViewHolder.Name.getText().toString());
                            tempList.get(position).setName(genericViewHolder.Name.getText().toString());
                            if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                                modelList.get(pos).setStatus("0");
                                modelList.get(pos).setRequest("1");
                                tempList.get(position).setStatus("0");
                                tempList.get(position).setRequest("1");
                                // System.out.println("request value name empty" + modelList.get(pos).getRequest());

                            } else {
                                modelList.get(pos).setStatus("1");
                                modelList.get(pos).setRequest("0");
                                tempList.get(position).setStatus("1");
                                tempList.get(position).setRequest("0");
                                // System.out.println("request value name empty else" + modelList.get(pos).getRequest());
                            }
                            updateData(modelList);
                            //    showAlertButton(getActivity(), "Alert", "Please enter Name.", R.drawable.exclamationicon, R.drawable.round);
                        } else {
                            genericViewHolder.Name.setBackgroundResource(R.drawable.edit_text_login);
                            modelList.get(pos).setName(genericViewHolder.Name.getText().toString());
                            tempList.get(position).setName(genericViewHolder.Name.getText().toString());
                            if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                                modelList.get(pos).setStatus("0");
                                modelList.get(pos).setRequest("1");
                                tempList.get(position).setStatus("0");
                                tempList.get(position).setRequest("1");
                                // System.out.println("request value name" + modelList.get(pos).getRequest());

                            } else {
                                modelList.get(pos).setStatus("1");
                                modelList.get(pos).setRequest("0");
                                tempList.get(position).setStatus("1");
                                tempList.get(position).setRequest("0");
                                // System.out.println("request value name else" + modelList.get(pos).getRequest());


                            }
                            updateData(modelList);

                        }

                        // TODO Auto-generated method stub
                    }

                });
                genericViewHolder.Email.setText(model.getEmail());
                genericViewHolder.Email.setSelection(genericViewHolder.Email.getText().length());
                genericViewHolder.Email.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        modelList.get(position).setEmail(genericViewHolder.Email.getText().toString());
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                  /*      if (genericViewHolder.Email.getText().toString().equals("")) {
                            genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);*/
                        modelList.get(pos).setEmail(genericViewHolder.Email.getText().toString());
                        tempList.get(position).setEmail(genericViewHolder.Email.getText().toString());
                        if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                            modelList.get(pos).setStatus("0");
                            modelList.get(pos).setRequest("1");
                            tempList.get(position).setStatus("0");
                            tempList.get(position).setRequest("1");
                            // System.out.println("request value email empty" + modelList.get(pos).getRequest());

                        } else {
                            modelList.get(pos).setStatus("1");
                            modelList.get(pos).setRequest("0");
                            tempList.get(position).setStatus("1");
                            tempList.get(position).setRequest("0");
                            // System.out.println("request value email empty else" + modelList.get(pos).getRequest());


                        }
                        updateData(modelList);
                        //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
                    }/* else {
                            genericViewHolder.Email.setBackgroundResource(R.drawable.edit_text_login);
                            modelList.get(pos).setEmail(genericViewHolder.Email.getText().toString());
                            tempList.get(position).setEmail(genericViewHolder.Email.getText().toString());
                            if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                                modelList.get(pos).setStatus("0");
                                modelList.get(pos).setRequest("1");
                                tempList.get(position).setStatus("0");
                                tempList.get(position).setRequest("1");
                                // System.out.println("request value email empty" + modelList.get(pos).getRequest());

                            } else {
                                modelList.get(pos).setStatus("1");
                                modelList.get(pos).setRequest("0");
                                tempList.get(position).setStatus("1");
                                tempList.get(position).setRequest("0");
                                // System.out.println("request value email empty else" + modelList.get(pos).getRequest());


                            }
                            updateData(modelList);
                        }

                        // TODO Auto-generated method stub
                    }*/
                });
                String PID = tempList.get(position).getPid();
                int pos = Integer.parseInt(PID) - 1;
                if (genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus().equals("")) {
                    modelList.get(pos).setCode(genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus());
                    tempList.get(position).setCode(genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus());
                } else {
                    genericViewHolder.spinnerCode.setCountryForNameCode("+60");
                    modelList.get(pos).setCode("+60");
                    tempList.get(position).setCode("+60");
                }
                if (!model.getCode().equals("")) {
                    genericViewHolder.spinnerCode.setCountryForPhoneCode(Integer.parseInt(model.getCode()));
                } else {
                    genericViewHolder.spinnerCode.setCountryForNameCode("+60");
                    modelList.get(pos).setCode("+60");
                    tempList.get(position).setCode("+60");
                }
                genericViewHolder.spinnerCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                    @Override
                    public void onCountrySelected() {
                        Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                        String code = genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus();
                        modelList.get(pos).setCode(genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus());
                        modelList.get(pos).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                        tempList.get(position).setCode(genericViewHolder.spinnerCode.getSelectedCountryCodeWithPlus());
                        tempList.get(position).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                        if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                            modelList.get(pos).setStatus("0");
                            modelList.get(pos).setRequest("1");
                            tempList.get(position).setStatus("0");
                            tempList.get(position).setRequest("1");
                            // System.out.println("request value phone empty" + modelList.get(pos).getRequest());

                        } else {
                            modelList.get(pos).setStatus("1");
                            modelList.get(pos).setRequest("0");
                            tempList.get(position).setStatus("1");
                            tempList.get(position).setRequest("0");
                            // System.out.println("request value phone empty else" + modelList.get(pos).getRequest());


                        }
                        updateData(modelList);


                    }
                });
                genericViewHolder.Contact.setText(model.getUser_mobile());
                genericViewHolder.Contact.setSelection(genericViewHolder.Contact.getText().length());
                genericViewHolder.Contact.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        modelList.get(position).setUser_mobile(genericViewHolder.Contact.getText().toString());
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                        if (genericViewHolder.Contact.getText().toString().equals("")) {
                            genericViewHolder.Contact.setBackgroundResource(R.drawable.rec_red);
                            modelList.get(pos).setUser_mobile(genericViewHolder.Contact.getText().toString());
                            modelList.get(pos).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                            tempList.get(position).setUser_mobile(genericViewHolder.Contact.getText().toString());
                            tempList.get(position).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                            if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                                modelList.get(pos).setStatus("0");
                                modelList.get(pos).setRequest("1");
                                tempList.get(position).setStatus("0");
                                tempList.get(position).setRequest("1");
                                // System.out.println("request value phone" + modelList.get(pos).getRequest());

                            } else {
                                modelList.get(pos).setStatus("1");
                                modelList.get(pos).setRequest("0");
                                tempList.get(position).setStatus("1");
                                tempList.get(position).setRequest("0");

                            }
                            updateData(modelList);
                            //   showAlertButton(getActivity(), "Alert", "Please enter Contact number.", R.drawable.exclamationicon, R.drawable.round);
                        } else {
                            genericViewHolder.Contact.setBackgroundResource(R.drawable.edit_text_login);
                            modelList.get(pos).setUser_mobile(genericViewHolder.Contact.getText().toString());
                            modelList.get(pos).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                            tempList.get(position).setUser_mobile(genericViewHolder.Contact.getText().toString());
                            tempList.get(position).setPhone(String.valueOf(genericViewHolder.spinnerCode.getSelectedCountryCodeAsInt()) + genericViewHolder.Contact.getText().toString());
                            if (modelList.get(pos).getRequest().equals("1") && tempList.get(position).getRequest().equals("1")) {
                                modelList.get(pos).setStatus("0");
                                modelList.get(pos).setRequest("1");
                                tempList.get(position).setStatus("0");
                                tempList.get(position).setRequest("1");
                                // System.out.println("request value name ++" + modelList.get(pos).getRequest());
                            } else {
                                modelList.get(pos).setStatus("1");
                                modelList.get(pos).setRequest("0");
                                tempList.get(position).setStatus("1");
                                tempList.get(position).setRequest("0");
                                // System.out.println("request value name else ++" + modelList.get(pos).getRequest());


                            }
                            updateData(modelList);
                        }

                        //   }

                        // TODO Auto-generated method stub
                    }
                });

                genericViewHolder.DeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
                        dialog.setContentView(R.layout.custom_alert_dialog);
                        dialog.setCancelable(false);
                        ImageView icon = dialog.findViewById(R.id.iconImageView);
                        icon.setBackgroundResource(R.drawable.round);
                        icon.setImageResource(R.drawable.questionmark_icon);
                        // set the custom dialog components - text, image, button
                        TextView text = dialog.findViewById(R.id.text);
                        text.setText("Do you want to delete this details?");
                        Button dialogCancelButton = dialog
                                .findViewById(R.id.dialogButtonCancel);
                        dialogCancelButton.setText("Cancel");
                        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        Button dialogOkButton = dialog.findViewById(R.id.dialogButtonOK);
                        dialogOkButton.setVisibility(View.GONE);
                        dialogOkButton.setText("Ok");

                        dialogOkButton.setVisibility(View.VISIBLE);
                        dialogOkButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                                String PID = tempList.get(position).getPid();
                                int pos = Integer.parseInt(PID) - 1;
                                Log.e("pos ", String.valueOf(pos));
                                modelList.get(pos).setDeleted("1");
                                modelList.get(pos).setStatus("2");
                                modelList.get(pos).setRequest("0");
                                // System.out.println("request value name **" + modelList.get(pos).getRequest());
                                tempList.remove(position);
                                updateList(modelList);

                                mAdapter = new KinAdapter(getContext(), tempList);
                                recyclerView.setAdapter(mAdapter);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                genericViewHolder.AddItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                        if (!tempList.get(position).getName().equals("") /*&& !tempList.get(position).getEmail().equals("")*/ && !tempList.get(position).getUser_mobile().equals("")) {
                            tempList.add(new KinModel("", tempList.get(position).getUser_id(), "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                            modelList.add(new KinModel("", modelList.get(position).getUser_id(), "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                            updateList(modelList);
                            mAdapter = new KinAdapter(getContext(), tempList);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.scrollToPosition(tempList.size() - 1);
                            Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                            //notifyDataSetChanged();
                        } else {
                            showAlertOKButton(getActivity(), "Alert", "Please fill all the fields marked in red color.", "Ok", R.drawable.exclamationicon, R.drawable.round);
                            if (tempList.get(position).getName().equals("")) {
                                genericViewHolder.Name.setBackgroundResource(R.drawable.rec_red);
                            }
                           /* if (tempList.get(position).getEmail().equals("")) {
                                genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);
                            }*/
                            if (tempList.get(position).getUser_mobile().equals("")) {
                                genericViewHolder.Contact.setBackgroundResource(R.drawable.rec_red);
                            }
                        }
                    }
                });
                genericViewHolder.AddImage_last.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String PID = tempList.get(position).getPid();
                        int pos = Integer.parseInt(PID) - 1;
                        Log.e("pos ", String.valueOf(pos));
                        if (!tempList.get(position).getName().equals("") /*&& !tempList.get(position).getEmail().equals("")*/ && !tempList.get(position).getUser_mobile().equals("")) {
                            tempList.add(new KinModel("", tempList.get(position).getUser_id(), "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                            modelList.add(new KinModel("", modelList.get(position).getUser_id(), "", "", "", "", "", "", "", "0", "1", "0", String.valueOf(modelList.size() + 1)));
                            updateList(modelList);
                            mAdapter = new KinAdapter(getContext(), tempList);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.scrollToPosition(tempList.size() - 1);
                            Log.e("afterTextChanged: ", modelList.get(position).getPid() + tempList.get(position).getPid());
                            //notifyDataSetChanged();
                        } else {
                            showAlertOKButton(getActivity(), "Alert", "Please fill all the fields marked in red color.", "Ok", R.drawable.exclamationicon, R.drawable.round);
                            if (tempList.get(position).getName().equals("")) {
                                genericViewHolder.Name.setBackgroundResource(R.drawable.rec_red);
                            }
                           /* if (tempList.get(position).getEmail().equals("")) {
                                genericViewHolder.Email.setBackgroundResource(R.drawable.rec_red);
                            }*/
                            if (tempList.get(position).getUser_mobile().equals("")) {
                                genericViewHolder.Contact.setBackgroundResource(R.drawable.rec_red);
                            }
                        }
                    }
                });

            }

        }


        @Override
        public int getItemCount() {
            if (modelList.size() == 0) {
                AddImageFragment.setVisibility(View.VISIBLE);
                place_holder.setVisibility(View.VISIBLE);
            } else {
                AddImageFragment.setVisibility(View.GONE);
                place_holder.setVisibility(View.GONE);
            }
            return tempList.size();
        }


        private KinModel getItem(int position) {
            return listData.get(position);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView DeleteItem, AddItem, AddImage_last;
            private EditText Name, Email, Contact;
            private CountryCodePicker spinnerCode;
            private LinearLayout linearLayout;
            private RelativeLayout parent;
            private TextView space;

            public ViewHolder(final View itemView) {
                super(itemView);

                this.DeleteItem = itemView.findViewById(R.id.deleteImg);
                this.AddItem = itemView.findViewById(R.id.addImg);
                this.AddImage_last = itemView.findViewById(R.id.addImglast);
                this.Name = itemView.findViewById(R.id.fullname);
                this.Email = itemView.findViewById(R.id.email);
                this.Contact = itemView.findViewById(R.id.contactnumber);
                this.spinnerCode = itemView.findViewById(R.id.spinnerCode);
                this.linearLayout = itemView.findViewById(R.id.linearLayout);
                this.parent = itemView.findViewById(R.id.parent);
                this.space = itemView.findViewById(R.id.space);
            }

        }

    }


    //============================RecyclerView Modal Class===============================================================


    public class KinModel {

        private String id;
        private String user_id;
        private String kin_id;
        private String name;
        private String email;
        private String phone;
        private String code;
        private String student_id;
        private String user_mobile;
        private String status;
        private String request;
        private String deleted;
        private String pid;


        public KinModel(String id, String user_id, String kin_id, String name, String email, String phone, String code, String student_id, String user_mobile, String status, String request, String deleted, String pid) {
            this.id = id;
            this.user_id = user_id;
            this.kin_id = kin_id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.code = code;
            this.student_id = student_id;
            this.user_mobile = user_mobile;
            this.status = status;
            this.request = request;
            this.deleted = deleted;
            this.pid = pid;
        }

        public KinModel() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getKin_id() {
            return kin_id;
        }

        public void setKin_id(String kin_id) {
            this.kin_id = kin_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }
    }


}
