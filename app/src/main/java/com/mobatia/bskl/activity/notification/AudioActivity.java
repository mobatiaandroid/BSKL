/**
 * 
 */
package com.mobatia.bskl.activity.notification;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.adapter.StudentUnReadRecyclerAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Rijo
 * 
 */
public class AudioActivity extends AppCompatActivity implements OnSeekBarChangeListener,
		Callback, OnPreparedListener, OnCompletionListener, OnClickListener,
		OnSeekCompleteListener ,IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {
	private SeekBar seekBarProgress;
	private LinearLayout linearLayoutMediaController;
	TextView btnplay;
	String action = "play";
	int position;
	private boolean isReset = false;
	Context mcontext = this;
	MediaPlayer player;
	private TextView textViewPlayed;
	private TextView textViewLength;
	private TextView textcontent;
	private Timer updateTimer;
	ImageView playerIamge;
	private String title = null;
	private AnimationDrawable anim;
	Bundle extras;
	ArrayList<PushNotificationModel> alertlist;
	private ProgressBar progressBarWait;
	private static final String TAG = "androidEx2 = VideoSample";
	private String url = "";
	private boolean isplayclicked = false;
	ImageView backImg;
	ImageView home;
	Context mContext;

	Activity mActivity;
	TextView msgTitle;
	RecyclerView studentRecycleUnread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.audio_push);
		mContext=this;
		mActivity=this;
		extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt(POSITION);
			title = extras.getString("title");

			alertlist = (ArrayList<PushNotificationModel>) extras
					.getSerializable(PASS_ARRAY_LIST);

		}
		initialiseUI();

	}

	/**
	 * Method Name : initialiseUI Description : for initializing views Params :
	 * nil Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
	 * */
	private void initialiseUI() {

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
		getSupportActionBar().setElevation(0);

		View view =getSupportActionBar().getCustomView();
		Toolbar toolbar=(Toolbar)view.getParent();
		toolbar.setContentInsetsAbsolute(0,0);

		TextView headerTitle = view.findViewById(R.id.headerTitle);
		ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
		ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
		ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
		action_bar_back.setImageResource(R.drawable.back_new);
		headerTitle.setText("Messages");
		headerTitle.setVisibility(View.VISIBLE);
		logoClickImgView.setVisibility(View.INVISIBLE);
		action_bar_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		msgTitle= findViewById(R.id.msgTitle);
		studentRecycleUnread= findViewById(R.id.studentRecycle);
		msgTitle.setText(title);
		studentRecycleUnread.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(mContext);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		studentRecycleUnread.setLayoutManager(llm);
		StudentUnReadRecyclerAdapter mStudentRecyclerAdapter= new StudentUnReadRecyclerAdapter(mContext,alertlist.get(position).getStudentArray());
		studentRecycleUnread.setAdapter(mStudentRecyclerAdapter);


		linearLayoutMediaController = findViewById(R.id.linearLayoutMediaController);
		btnplay = findViewById(R.id.btn_play);
		textViewPlayed = findViewById(R.id.textViewPlayed);
		textViewLength = findViewById(R.id.textViewLength);
		textcontent = findViewById(R.id.txt);
		seekBarProgress = findViewById(R.id.seekBarProgress);
		playerIamge = findViewById(R.id.imageViewPauseIndicator);
		url = alertlist.get(position).getUrl();
		textcontent.setText(alertlist.get(position).getTitle());
		// System.out.println("check url" + url);
		seekBarProgress.setProgress(0);
		seekBarProgress.setOnSeekBarChangeListener(this);
		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnSeekCompleteListener(this);
		progressBarWait = findViewById(R.id.progressBarWait);
		if (AppUtils.isNetworkConnected(mcontext)) {
			playMp3();

		} else {
			Toast.makeText(mcontext,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
		}
		btnplay.setOnClickListener(this);
	}

	/**
	 * Method Name : setListeners Description : initiates listeners to view
	 * Params : nil Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
	 * */

	/**
	 * Method Name : playMp3 Description : Play the audio from the link Params :
	 * link Return type : nil Date : 21/06/2013 Author : Jiju Induchoodan
	 * */
	public void playMp3() {
		if (url.equals(" ")) {
		} else {

			try {

				player.setDataSource(url);
				player.prepareAsync();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				showToast(getResources().getString(R.string.no_internet));
				e.printStackTrace();
			}

		}
	}

	/**
	 * Method Name : showToast Description :for showing Toast Params : String
	 * Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
	 * */
	private void showToast(final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(mcontext, string, Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		progressBarWait.setVisibility(View.GONE);
		btnplay.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.stop();
		btnplay.setText(getResources().getString(R.string.play));


		if (updateTimer != null) {
			updateTimer.cancel();
		}
		player.reset();
		isReset = true;

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		int duration = mp.getDuration() / 1000; // duration in seconds

		seekBarProgress.setMax(duration);
		textViewLength.setText(AppUtils.durationInSecondsToString(duration));
		progressBarWait.setVisibility(View.GONE);

		if (!mp.isPlaying()) {
			playerIamge.setBackgroundResource(R.drawable.mic);
			btnplay.setVisibility(View.VISIBLE);
			btnplay.setText(getResources().getString(R.string.pause));
			mp.start();
			updateMediaProgress();
			linearLayoutMediaController.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (!fromUser) {
			textViewPlayed.setText(AppUtils.durationInSecondsToString(progress));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if (player.isPlaying()) {
			progressBarWait.setVisibility(View.GONE);
			player.seekTo(seekBar.getProgress() * 1000);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnplay) {

			if (!isplayclicked) {

				if (player.isPlaying()) {
					System.out.println("is come click second");
					player.pause();
					playerIamge.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.mic));
					btnplay.setText(getResources().getString(R.string.play));


				}

				isplayclicked = true;
			} else {

				if (player == null || isReset) {
					if (AppUtils.isNetworkConnected(mcontext)) {
						playMp3();
						playerIamge
								.setBackgroundResource(R.drawable.michover);
						isReset = false;
					} else {
						Toast.makeText(mcontext,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();

					}

				} else {
					if (AppUtils.isNetworkConnected(mcontext)) {
						player.start();
						playerIamge
								.setBackgroundResource(R.drawable.michover);
						btnplay.setText(getResources()
								.getString(R.string.pause));

					} else {
						Toast.makeText(mcontext,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();

					}
				}
				isplayclicked = false;
			}

		} else if (v == backImg) {
			finish();
		}
	}

	private void updateMediaProgress() {
		updateTimer = new Timer("progress Updater");
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (player != null) {
							seekBarProgress.setProgress(player
									.getCurrentPosition() / 1000);
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player != null) {
			/*
			 * if(player.isPlaying()) {
			 */
			player.stop();
			player.release();
			player = null;

		}

	}

	@Override
	protected void onResume() {
		if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
		{
			AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
			AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
			AppController.getInstance().trackScreenView("Audio Notification."+"("+PreferenceManager.getUserEmail(mContext)+ ")"+" "+ "("+ Calendar.getInstance().getTime()+ ")");
		}


		super.onResume();
	}
}
