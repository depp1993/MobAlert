package com.deepankarsingh.mobalert;

import java.util.ArrayList;

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

	private final String TAG = "CHECK";
	private SpeechRecognizer sr;
	Button cancel;
	Button alertb;
	Dialog cust;
	int n;
	int flag = 0;

	public FragmentMain() {
	}

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
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
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

	public void onClick(View v) {
		sr.destroy();
		final DbConnect db = new DbConnect(getActivity());
		n = db.getname().getCount();
		db.close();
		if (v.getId() == R.id.b_alert) {
			if (n != 0) {
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
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cust.cancel();
					}
				});

			} else {
				Toast.makeText(getActivity(),
						"No Emergency Contacts, add Emergency Contacts",
						Toast.LENGTH_SHORT).show();
				flag = 1;

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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sr.destroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		sr.destroy();
	}

	class listener implements RecognitionListener {
		public void onReadyForSpeech(Bundle params) {
			Log.d(TAG, "onReadyForSpeech");
		}

		public void onBeginningOfSpeech() {
			Log.d(TAG, "onBeginningOfSpeech");

		}

		public void onRmsChanged(float rmsdB) {
			// Log.d(TAG, "onRmsChanged");
		}

		public void onBufferReceived(byte[] buffer) {
			// Log.d(TAG, "onBufferReceived");
		}

		public void onEndOfSpeech() {
			Log.d(TAG, "onEndofSpeech");
		}

		public void onError(int error) {
			Log.d(TAG, "error " + error);
			sr.destroy();
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

		public void onResults(Bundle results) {
			Log.d(TAG, "onResults " + results);
			ArrayList<String> data = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			@SuppressWarnings("unused")
			String str = "";
			for (int i = 0; i < data.size(); i++) {
				Log.d(TAG, "result " + data.get(i));
				str += data.get(i);
				if (data.get(i).equals("help") || data.get(i).equals("hello")
						|| data.get(i).equals("home")
						|| data.get(i).equals("hell")) {
					onClick(alertb);
					break;
				}
			}
			sr.destroy();
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

		@Override
		public void onPartialResults(Bundle partialResults) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onEvent(int eventType, Bundle params) {
			// TODO Auto-generated method stub

		}
	}

	public void onPartialResults(Bundle partialResults) {
		Log.d(TAG, "onPartialResults");
	}

	public void onEvent(int eventType, Bundle params) {
		Log.d(TAG, "onEvent " + eventType);
	}

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
				sr.destroy();
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
}
