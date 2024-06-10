package com.harsh_kumar.whereami.screens

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Mainscreen(location: MutableState<Location?>,retryLocation: ()->Unit){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
       if(location.value == null){
           Column(horizontalAlignment = Alignment.CenterHorizontally ) {
               Text("Accessing Location 📍")
               Button( modifier = Modifier.padding(20.dp),  onClick = { retryLocation() }) {
                   Text("Retry")
               }
           }

       }else{
           val loc = LatLng((location.value)!!.latitude,(location.value)!!.longitude)
           val cameraPositionState = rememberCameraPositionState {
               position = CameraPosition.fromLatLngZoom(loc, 70f)
           }
           GoogleMap(
               modifier = Modifier.fillMaxSize(),
               cameraPositionState = cameraPositionState,
               properties = MapProperties(mapType = MapType.SATELLITE)
           ) {
               Marker(
                   state = MarkerState(position = loc),
                   title = "Location",
                   snippet = "Current Location"
               )
           }
       }
    }
}