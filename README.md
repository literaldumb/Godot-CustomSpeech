#Godot-CustomSpeech
- Custom Speech-to-text support on Godot engine for Android, similar to [Godot-SpeechToText](https://github.com/literaldumb/Godot-SpeechToText), but without the Google's default pop-up UI and beeps.
- Godot version 2.0 stable.
- Copyright [TeamKrishna](http://teamkrishna.in)
- Demonstrates usage of runOnUiThread()
- Works perfectly on lollipop. Leaves a small beep trace after listening is done on kitkat (#TODO)

##How to use
- Drop the customspeech folder inside godot/modules

- Move the customspeech/GodotCustomSpeech.java to godot/platform/android/java/src/org/godotengine/godot

**Note:** The speechtotext/android.jar is taken from  *android-sdk-linux/platforms/android-22*. You may choose to use any other api version.

###Compile
1. #> scons platform=android
2. cd godot/platform/android/java
3. #> ./gradlew build
4. The resulting apk will be available at godot/platform/android/java/build/outputs/apk
 
###Configure
Add the following in the engine.cfg file:

> [android]

> modules="org/godotengine/godot/GodotCustomSpeech"

**Use them in the script:**

> var singleton = Globals.get_singleton("GodotCustomSpeech")

> singleton.doListen() # opens up the mic ui
> singleton.getWords() # gets the strings of words 

###Build the game apk
From the settings of the godot engine UI:

> Export->Target->Android


Custom Package (Debug/Release):
> Point to the newly built apk

> Permission check: RecordAudio,Modify Audio Settings

####License
MIT


Similar plugings: [GodotVibrate](https://github.com/literaldumb/GodotVibrate)
[Godot-SpeechToText](https://github.com/literaldumb/Godot-SpeechToText)
[Godot-TextToSpeech](https://github.com/literaldumb/Godot-TextToSpeech)

