<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
html { height: 100% }
body { height: 100%; margin: 0; padding: 0 }
#map_canvas { height: 100% }
</style>
 <!--<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>-->
<script type="text/javascript"
src="http://maps.googleapis.com/maps/api/js">
</script>
 
<script type="text/javascript">
 jQuery(function($) {
    // Asynchronously Load the map API 
    var script = document.createElement('script');
    script.src = "http://maps.googleapis.com/maps/api/js?sensor=false&callback=initialize";
    document.body.appendChild(script);
});

function initialize(url) {
	    var mark=url.split('?');
	    lon=[];
	    col=[];
	    markers=[];
	    lat=[];
	    chId=[];
	    infoWindowContent=[]
	    for(i=1;i<mark.length;i++)
	    {
	    	lat[i-1]=mark[i].split(',')[0];
	    	lon[i-1]=mark[i].split(',')[1];
	    	col[i-1]=mark[i].split(',')[2];
	    	chId[i-1]=mark[i].split(',')[3];
	    }
	    var map;
	    var bounds = new google.maps.LatLngBounds();
	    var mapOptions = {
	        mapTypeId: 'roadmap'
	    };
	                    
	    // Display a map on the page
	    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
	    map.setTilt(45);
	     for (i=0;i<lon.length;i++)
	     {
	     	markers[i]=[lat[i],lon[i]];
	     	if(col[i]=="red")
	     	{
	     		infoWindowContent[i]=['<div class="info_content">' +
	        				'<h5> Vehicle No.'+chId[i]+' has met with an accident</h5>' +
	        	 			'</div>'];
	        	 }
	        	 else
	        	 {
	        	 	infoWindowContent[i]=['<div class="info_content">' +
	        				'<h5> Vehicle No.'+chId[i]+' faster than the speed-limit.</h5>' +
	        	 			'</div>'];
	        	 }
	     }   
	    // Multiple Markers
	    /*var markers = [
	        ['', 51.503454,-0.119562],
	        ['', 51.499633,-0.124755]
	    ];*/
	                        
	    // Info Window Content
	    /*var infoWindowContent = [
	        ['<div class="info_content">' +
	        '<h3>London Eye</h3>' +
	         '</div>'],
	        ['<div class="info_content">' +
	        '<h3>Palace of Westminster</h3>' +
	        '</div>']
	    ];*/
	        
	    // Display multiple markers on a map
	    var infoWindow = new google.maps.InfoWindow(), marker, i;
	    
	    // Loop through our array of markers & place each one on the map  
	    for( i = 0; i < markers.length; i++ ) {
	        var position = new google.maps.LatLng(markers[i][0], markers[i][1]);
	        bounds.extend(position);
	        icon = "http://maps.google.com/mapfiles/ms/icons/" + col[i] + ".png";
	        marker = new google.maps.Marker({
	            position: position,
	            map: map,
	            icon: new google.maps.MarkerImage(icon)
	        });
	        
	        // Allow each marker to have an info window    
	        google.maps.event.addListener(marker, 'click', (function(marker, i) {
	            return function() {
	                infoWindow.setContent(infoWindowContent[i][0]);
	                infoWindow.open(map, marker);
	            }
	        })(marker, i));

	        // Automatically center the map fitting all markers on the screen
	        map.fitBounds(bounds);
	    }

	    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
	    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
	        this.setZoom(14);
	        google.maps.event.removeListener(boundsListener);
	    });
	    
}
</script>
</head>
<?php
function curPageURL() {
 $pageURL = 'http';
 if ($_SERVER["HTTPS"] == "on") {$pageURL .= "s";}
 $pageURL .= "://";
 if ($_SERVER["SERVER_PORT"] != "80") {
  $pageURL .= $_SERVER["SERVER_NAME"].":".$_SERVER["SERVER_PORT"].$_SERVER["REQUEST_URI"];
 } else {
  $pageURL .= $_SERVER["SERVER_NAME"].$_SERVER["REQUEST_URI"];
 }
 return $pageURL;
}
?> 
<body onload=initialize("<?php echo curPageURL() ?>")>


<div id="map_canvas" class="mapping" style="width:100%; height:100%"></div>
</body>
</html>

