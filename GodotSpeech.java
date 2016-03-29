//Kaushik Mazumdar

package org.godotengine.godot;

//imports
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Locale;

public class GodotSpeech extends Godot.SingletonBase implements RecognitionListener{

	private static final int REQUEST_OK = 1;
	private static final int RESULT_OK = -1;
	Activity m_pActivity;
	Intent recognizerIntent;
	SpeechRecognizer speech = null;
	Context localContext = null;
	String m_pWords ="";
	String LOG_TAB = "GodotSpeech";

	public String getWords()
	{
		return m_pWords;
	}

	/*
	protected void onMainCreate(Bundle icicle)
	{
		super.onMainCreate(icicle);

		Log.i(LOG_TAB,"onMainCreate");
		speech = SpeechRecognizer.createSpeechRecognizer(localContext);
		
		if(speech!=null)
		{
			Log.i(LOG_TAB,"speech initialized");
			speech.setRecognitionListener(this);
		}
		else
			Log.i(LOG_TAB,"speech is null");

		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"en"});
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,localContext.getPackageName());
	}
	*/

	public void doListen()
	{
		Log.i(LOG_TAB,"startListening");
		
		speech = SpeechRecognizer.createSpeechRecognizer(localContext);
		if(speech!=null)
		{
			Log.i(LOG_TAB,"speech initialized");
			speech.setRecognitionListener(this);
		}
		else
			Log.i(LOG_TAB,"speech is null");

		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"en"});
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,localContext.getPackageName());
		
		speech.startListening(recognizerIntent);
		
		//m_pActivity.startActivityForResult(recognizerIntent, REQUEST_OK);
	}

    	static public Godot.SingletonBase initialize(Activity p_activity) {
		//Log.i(LOG_TAB,"Godot.SingletonBase initialize");
        	return new GodotSpeech(p_activity);
    	}

    	public GodotSpeech(Activity p_activity) {
		Log.i(LOG_TAB,"GodotSpeech constructor");
		m_pActivity = p_activity;
		localContext = m_pActivity.getApplicationContext();

        	//register class name and functions to bind
        	registerClass("GodotSpeech", new String[]{"doListen","getWords"});
    	}

    	// forwarded callbacks
	/*	
    	protected void onMainActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onMainActivityResult(requestCode,resultCode,data);
		if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) 
		{
			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			m_pWords = thingsYouSaid.get(0);
		}
	}
	*/
	
	protected void onMainPause()
	{
		super.onMainPause();
		if(speech != null)
		{
			speech.destroy();
			speech = null;
		}
	}
	
	// RecognitionListener abstract methods override
	public void onBeginningOfSpeech() {  Log.i(LOG_TAB,"onBeginningOfSpeech"); }
	public void onBufferReceived(byte[] buffer) {Log.i(LOG_TAB,"onBufferReceived");}
	public void onEndOfSpeech() {Log.i(LOG_TAB,"onEndOfSpeech");}
	public void onError(int error) {Log.i(LOG_TAB,"onError");}
	public void onEvent(int eventType,Bundle params) {Log.i(LOG_TAB,"onEvent");}
	public void onPartialResults(Bundle partialResults) {Log.i(LOG_TAB,"onPartialResults");}
	public void onReadyForSpeech(Bundle params) {Log.i(LOG_TAB,"onReadyForSpeech");}
	public void onRmsChanged(float rmsdB) {Log.i(LOG_TAB,"onRmsChanged");}
	
	public void onResults(Bundle results)
	{
		Log.i(LOG_TAB,"onResults");
		ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		m_pWords = matches.get(0);
		
		speech.stopListening();
	}
	
}
