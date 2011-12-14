/***************************/
//@Author: Adrian "yEnS" Mato Gondelle
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!					
/***************************/

//SETTING UP OUR POPUP
//0 means disabled; 1 means enabled;
var popupStatus = 0;

//loading popup with jQuery magic!
function loadPopup(popupName){
	centerPopup(popupName);
	//loads popup only if it is disabled
	if(popupStatus==0){
		$("#backgroundPopup").css({
			"opacity": "0.7"
		});
		$("#backgroundPopup").fadeIn("slow");
		$(popupName).fadeIn("slow");
		popupStatus = 1;
	}
}

//disabling popup with jQuery magic!
function disablePopup(){
	//disables popup only if it is enabled
	if(popupStatus==1){
		$("#backgroundPopup").fadeOut("slow");
		$(".popupEntry").each(function(){$(this).fadeOut("slow")});
		popupStatus = 0;
	}
}

//centering popup
function centerPopup(popupName){
	//request data for centering
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $(popupName).height();
	var popupWidth = $(popupName).width();
	//centering
	$(popupName).css({
		"position": "absolute",
		"top": windowHeight/2-popupHeight/2,
		"left": windowWidth/2-popupWidth/2
	});
	//only need force for IE6
	
	$("#backgroundPopup").css({
		"height": windowHeight
	});
	
}


//CONTROLLING EVENTS IN jQuery
$(document).ready(function()
{
	//LOADING POPUP
	//Click the button event!
	$("#openNewsEntry1").click(function(){loadPopup("#popupNewsEntry1");});
	$("#openNewsEntry2").click(function(){loadPopup("#popupNewsEntry2");});
	$("#openNewsEntry3").click(function(){loadPopup("#popupNewsEntry3");});
	$("#openNewsEntry4").click(function(){loadPopup("#popupNewsEntry4");});
	$("#openNewsEntry5").click(function(){loadPopup("#popupNewsEntry5");});
	$("#openNewsEntry6").click(function(){loadPopup("#popupNewsEntry6");});
	$("#openLearnMoreEntry").click(function(){loadPopup("#learnMoreEntry");});
	$("#openJoinWineClubEntry").click(function(){loadPopup("#joinWineClubEntry");});
	//CLOSING POPUP
	//Click the x event!
	$("#openEventsEntry5").click(function(){loadPopup("#popupEventsEntry5");});
	$("a.popupCloseX").each(function(index){$(this).click(function(){
		disablePopup();
	})});
	//Click out event!
	$("#backgroundPopup").click(function(){
		disablePopup();
	});
	//Press Escape event!
	$(document).keypress(function(e){
		if(e.keyCode==27 && popupStatus==1){
			disablePopup();
		}
	});

});