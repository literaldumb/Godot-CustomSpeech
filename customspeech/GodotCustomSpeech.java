// Copyright: TeamKrishna(www.teamkrishna.in)
// Author: Kaushik Mazumdar(literaldumb@gmail.com)

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
import android.os.Handler ;
import android.media.AudioManager;
import java.util.ArrayList;
import java.util.Locale;
import java.lang.*;

public class GodotCustomSpeech extends Godot.SingletonBase implements RecognitionListener{

	private static final int REQUEST_OK = 1;
	private static final int RESULT_OK = -1;
	Activity m_pActivity;
	Intent recognizerIntent;
	SpeechRecognizer speech = null;
	Context localContext = null;
	AudioManager am = null;
	String m_pWords ="";
	String LOG_TAB = "GodotCustomSpeech";
	int ringerMode;

	public String getWords()
	{
		return m_pWords;
	}

	public void doListen()
	{
		m_pActivity.runOnUiThread(new Runnable(){

		public void run()
		{
			try{
				Log.i(LOG_TAB,"runOnUiThread");
				speech = SpeechRecognizer.createSpeechRecognizer(localContext);
				if(speech!=null)
				{
					Log.i(LOG_TAB,"speech initialized");
					speech.setRecognitionListener(GodotCustomSpeech.this);
				}
				else
					Log.i(LOG_TAB,"speech is null");

				recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"en"});
				recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,localContext.getPackageName());
			
				// turn all mute from whatever
				am.setStreamMute(AudioManager.STREAM_MUSIC,true);	
				am.setStreamMute(AudioManager.STREAM_SYSTEM,true);	
				am.setStreamMute(AudioManager.STREAM_NOTIFICATION,true);	
				am.setStreamMute(AudioManager.STREAM_RING,true);
				am.setStreamMute(AudioManager.STREAM_ALARM,true);	
				am.setStreamMute(AudioManager.STREAM_DTMF,true);	
		
				// start listening
				speech.startListening(recognizerIntent);
			}catch(Exception e) { Log.i(LOG_TAB,"godot exception "+e.getMessage()) ; }
		}
});
	}

    	static public Godot.SingletonBase initialize(Activity p_activity) {
        	return new GodotCustomSpeech(p_activity);
    	}

    	public GodotCustomSpeech(Activity p_activity) {
		m_pActivity = p_activity;
		localContext = m_pActivity.getApplicationContext();
		am = (AudioManager)localContext.getSystemService(localContext.AUDIO_SERVICE);

        	//register class name and functions to bind
        	registerClass("GodotCustomSpeech", new String[]{"doListen","getWords"});
    	}

    	// forwarded callbacks
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
	
		// stop listening	
		speech.stopListening();

		// set it back to whatever
		am.setStreamMute(AudioManager.STREAM_MUSIC,false);	
		am.setStreamMute(AudioManager.STREAM_SYSTEM,false);	
		am.setStreamMute(AudioManager.STREAM_NOTIFICATION,false);	
		am.setStreamMute(AudioManager.STREAM_RING,false);	
		am.setStreamMute(AudioManager.STREAM_ALARM,false);	
		am.setStreamMute(AudioManager.STREAM_DTMF,false);	
	}
	
}
