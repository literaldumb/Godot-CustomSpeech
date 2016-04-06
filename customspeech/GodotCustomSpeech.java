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
        String m_pWords = "" ;
        String LOG_TAB = "GodotCustomSpeech";
        int ringerMode;
        boolean bDetection = false;
        boolean bError = false;
        boolean bSpeechSetup = false;

        int errorState ;
        int ERROR=-1;
        int NO_ERROR=0;
        int DETECTED=1;

        /*
        public enum ERROR_STATES
        {
                ERROR(-1),NO_ERROR(0),DETECTED(1);
        }
        */

        public void resetDetectionFlag()
        {
                bDetection = false;
        }

        public void resetErrorFlag()
        {
                bError = false;
        }

        public String getWords()
        {
                return m_pWords;
        }

        public boolean isDetectDone()
        {
                return bDetection;
        }

        public boolean isError()
        {
                return bError;
        }
        public int getErrorState()
        {
                return errorState;
        }

        public void doListen()
        {
                bDetection = false;
                bError = false;
                errorState = NO_ERROR;

                m_pWords = "" ;
                m_pActivity.runOnUiThread(new Runnable(){

                public void run()
                {
                        try{
                                Log.i(LOG_TAB,"runOnUiThread");
                                if(speech==null)
                                {
                                        speech = SpeechRecognizer.createSpeechRecognizer(localContext);
                                        speech.setRecognitionListener(GodotCustomSpeech.this);

                                        Log.i(LOG_TAB,"speech initialized");
                                }

                                recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                recognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"en"});
                                recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,localContext.getPackageName());
                                // turn all mute from whatever
                                /*
                                am.setStreamMute(AudioManager.STREAM_MUSIC,true);       
                                am.setStreamMute(AudioManager.STREAM_SYSTEM,true);      
                                am.setStreamMute(AudioManager.STREAM_NOTIFICATION,true);        
                                am.setStreamMute(AudioManager.STREAM_RING,true);
                                am.setStreamMute(AudioManager.STREAM_ALARM,true);       
                                am.setStreamMute(AudioManager.STREAM_DTMF,true);        
                                */

                                // start listening
                                bSpeechSetup = true;
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
                registerClass("GodotCustomSpeech", new String[]{"doListen","getWords","isDetectDone","isError","resetDetectionFlag","resetErrorFlag","getErrorState"});
        }

        private void destroySRObject()
        {
                if(speech != null)
                {
                        speech.stopListening();
                        speech.destroy();
                        speech = null;
                }
        }

        // forwarded callbacks
        protected void onMainPause()
        {
                super.onMainPause();
                /*
                if(speech != null)
                {
                        speech.destroy();
                        speech = null;
                }
                */
                destroySRObject();
        }

        // RecognitionListener abstract methods override
        public void onBeginningOfSpeech() {  Log.i(LOG_TAB,"onBeginningOfSpeech"); }
        public void onBufferReceived(byte[] buffer) {Log.i(LOG_TAB,"onBufferReceived");}
        public void onEndOfSpeech() {Log.i(LOG_TAB,"onEndOfSpeech");}
        public void onError(int error)
        {
                Log.i(LOG_TAB,"onError: " + String.valueOf(error));

                errorState = ERROR;

                String message="";
                 switch (error)
                {
                         case SpeechRecognizer.ERROR_AUDIO:
                         message = "Audio recording error";
                         bError = true;
                         break;
                         case SpeechRecognizer.ERROR_CLIENT:
                         message = "Client side error";
                         break;
                         case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                         message = "Insufficient permissions";
                         bError = true;
                         break;
                         case SpeechRecognizer.ERROR_NETWORK:
                         message = "Network error";
                         break;
                         case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                         message = "Network timeout";
                         break;
                         case SpeechRecognizer.ERROR_NO_MATCH:
                         if(bSpeechSetup)
                         {
                                break;
                         }
                         else
                         {
                                bError = true;
                                message = "No match";
                                break;
                         }
                         case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                         bError = true;
                         message = "RecognitionService busy";
                         break;
                         case SpeechRecognizer.ERROR_SERVER:
                         message = "error from server";
                         break;
                         case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                         bError = true;
                         destroySRObject();
                         message = "No speech input";
                         break;
                         default:
                         message = "Didn't understand, please try again.";
                         break;
                 }
                 Log.i(LOG_TAB,"Error message: " + message);
                 // use this in another method
                 //return message;
        }
        public void onEvent(int eventType,Bundle params) {Log.i(LOG_TAB,"onEvent");}
        public void onPartialResults(Bundle partialResults) {Log.i(LOG_TAB,"onPartialResults");}
        public void onReadyForSpeech(Bundle params)
        {
                bSpeechSetup = false;
                Log.i(LOG_TAB,"onReadyForSpeech");
        }
        public void onRmsChanged(float rmsdB) {Log.i(LOG_TAB,"onRmsChanged");}
        public void onResults(Bundle results)
        {
                Log.i(LOG_TAB,"onResults");
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                m_pWords = "";
                m_pWords = matches.get(0);
                bDetection = true;
                errorState = DETECTED;

                // stop listening       
                speech.stopListening();

                // set it back to whatever
                /*
                am.setStreamMute(AudioManager.STREAM_MUSIC,false);      
                am.setStreamMute(AudioManager.STREAM_SYSTEM,false);     
                am.setStreamMute(AudioManager.STREAM_NOTIFICATION,false);       
                am.setStreamMute(AudioManager.STREAM_RING,false);       
                am.setStreamMute(AudioManager.STREAM_ALARM,false);      
                am.setStreamMute(AudioManager.STREAM_DTMF,false);       
                */
        }

}
