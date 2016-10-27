var req;
var isIE;

function init() {
    completeField = document.getElementById("complete");
    var timerID = setInterval("doCompletion()",300);
}

function doCompletion() {
        var url = "autocomplete";
        req = initRequest();
        
        req.open("GET", url, true);
        req.onreadystatechange = callback;
        req.send(null);
}

function callback() {
	if (req.readyState == 4) {
		if (req.status == 200) {
        	parseMessages(req.responseXML);
        }
    }
}

function parseMessages(responseXML) {
	
    // no matches returned
    if (responseXML == null) {
    	return false;
    } else {
    	var composers = responseXML.getElementsByTagName("feedback")[0].childNodes[0].nodeValue;
    	ReplaceContentInContainer(completeField,composers);
    }
}

function ReplaceContentInContainer(id, content)
{
	id.innerHTML = content;
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}