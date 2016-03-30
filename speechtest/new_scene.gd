
extends Node2D

var singleton=null
var button_node=null
var text_node=null

func _ready():
	
	button_node=get_node("Button")
	text_node=get_node("TextButton")
	singleton=Globals.get_singleton("GodotCustomSpeech")
	
	set_process(true)
		
func _process(delta):
	var speech = singleton.getWords()
	text_node.set_text(speech)	


func _on_Button_pressed():
	singleton.doListen()
