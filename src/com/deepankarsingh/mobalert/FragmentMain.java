package com.deepankarsingh.mobalert;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deepankarsingh.mobalert.helper.DbConnect;

public class FragmentMain extends Fragment implements OnClickListener {

	private SpeechRecognizer sr;
	private Button cancel;
	private Button alertb;
	private Dialog cust;
	private int n;
	public static int flag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		TextView tvsayhelp = (TextView) rootView.findViewById(R.id.SayHelp);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/headliner.ttf");
		tvsayhelp.setTypeface(typeFace);
		alertb = (Button) rootView.findViewById(R.id.b_alert);
		alertb.setOnClickListener(this);
		flag = 0;
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (flag == 0) {
			sr = SpeechRecognizer.createSpeechRecognizer(getActivity());
			sr.setRecognitionListener(new listener());
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
					"voice.recognition.test");
			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
			sr.startListening(intent);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		sr.destroy();
	}

	public void onClick(View v) {
		sr.destroy(); // when clicked, stop listening.
		// database connectivity need to check if there is name in the emergency
		// contacts or not.
		final DbConnect db = new DbConnect(getActivity());
		n = db.getInfo().getCount();
		db.close();
		if (v.getId() == R.id.b_alert) {
			if (n != 0) {
				// creating a custom dialog box for animation
				cust = new Dialog(getActivity());
				cust.requestWindowFeature(Window.FEATURE_NO_TITLE);
				cust.setContentView(R.layout.custom_dialogbox);
				Window window = cust.getWindow();
				window.setLayout(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				ImageView img = (ImageView) cust.findViewById(R.id.ivdialog);
				cancel = (Button) cust.findViewById(R.id.bcancel);
				img.setBackgroundResource(R.drawable.animdialog);
				AnimationDrawable frameAnimation = (AnimationDrawable) img
						.getBackground();
				frameAnimation.start();
				cust.show();
				checkIfAnimationDone(frameAnimation);

			} else {
				Toast.makeText(getActivity(),
						"No Emergency Contacts, add Emergency Contacts",
						Toast.LENGTH_SHORT).show();
				flag = 1;

				// to start fragment people activity

				// Fragment frag = new FragmentPeople();
				// FragmentManager fragmentManager = getFragmentManager();
				// FragmentTransaction fragmentTransaction = fragmentManager
				// .beginTransaction();
				// fragmentTransaction.setCustomAnimations(R.anim.frag_slide_in,
				// R.anim.frag_slide_out);
				// fragmentTransaction.replace(R.id.main_container, frag);
				// fragmentTransaction.commit();
				// getActivity().getActionBar().setTitle("People");
				// ListView mDrawerList = (ListView) getActivity().findViewById(
				// R.id.left_drawer);
				// mDrawerList.setItemChecked(1, true);
			}
		}
	}

	// for animated notification, handling completion of task using android
	// handler class
	private void checkIfAnimationDone(AnimationDrawable anim) {
		final AnimationDrawable a = anim;
		int timeBetweenChecks = 350;
		final Handler h = new Handler();
		final Runnable run = new Runnable() {
			public void run() {
				if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
					checkIfAnimationDone(a);
				} else {
					cust.cancel();
					Intent i = new Intent(getActivity(), Alert.class);
					startActivity(i);
				}
			}
		};
		h.postDelayed(run, timeBetweenChecks);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cust.cancel();
				sr = SpeechRecognizer.createSpeechRecognizer(getActivity());
				sr.setRecognitionListener(new listener());
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
						"voice.recognition.test");
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
				sr.startListening(intent);
				h.removeCallbacks(run);
			}
		});
	}

	class listener implements RecognitionListener {

		public void onReadyForSpeech(Bundle params) {
		}

		public void onBeginningOfSpeech() {
		}

		public void onRmsChanged(float rmsdB) {
		}

		public void onBufferReceived(byte[] buffer) {
		}

		public void onEndOfSpeech() {
		}

		// if error , start listening again
		public void onError(int error) {
			Log.d("CHECK", "error" + error);
			Toast.makeText(getActivity(), "Not able to recognise 'Help'!",
					Toast.LENGTH_SHORT).show();
			sr.destroy();
			if (flag == 0) {
				sr = SpeechRecognizer.createSpeechRecognizer(getActivity());
				sr.setRecognitionListener(new listener());
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
						"voice.recognition.test");
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
				sr.startListening(intent);
			}
		}

		// results after speech recognintion
		public void onResults(Bundle results) {
			ArrayList<String> data = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			StringTokenizer st = new StringTokenizer(data.get(0));
			while (st.hasMoreElements()) {
				String str = st.nextToken();
				// due to wrong recognition of help by speechrocgniser.
				// help is recognised as hello, home, hell sometimes by the
				// android speech recogniser
				if (str.equals("help") || str.equals("hello")
						|| str.equals("home") || str.equals("hell")) {
					onClick(alertb);
					flag = 1;
					break;
				}
			}
			sr.destroy();
			if (flag == 0) {
				Toast.makeText(getActivity(), "Not able to recognise 'Help'!",
						Toast.LENGTH_SHORT).show();
				sr = SpeechRecognizer.createSpeechRecognizer(getActivity());
				sr.setRecognitionListener(new listener());
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
						"voice.recognition.test");
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
				sr.startListening(intent);
			}
		}

		@Override
		public void onPartialResults(Bundle partialResults) {
		}

		@Override
		public void onEvent(int eventType, Bundle params) {
		}
	}
}
