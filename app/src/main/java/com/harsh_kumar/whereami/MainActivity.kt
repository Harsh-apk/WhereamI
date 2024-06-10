package com.harsh_kumar.whereami

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.harsh_kumar.whereami.screens.Mainscreen
import com.harsh_kumar.whereami.services.LocationService
import com.harsh_kumar.whereami.ui.theme.WhereAmITheme

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val location :MutableState<Location?> = mutableStateOf(null)
    private fun retryLocation(){
        when {
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED )->{

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }).addOnSuccessListener {
                    if(it!=null){
                        location.value = it
                        Toast.makeText(this,"Lat: ${it.latitude} | Long: ${it.longitude} ",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       when{
           ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_DENIED )->{
               val locationPermissionRequest = registerForActivityResult(
                   ActivityResultContracts.RequestMultiplePermissions()
               ) { permissions ->
                   when {
                       permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                           // Precise location access granted.
                           Toast.makeText(this,"Fine Location Granted",Toast.LENGTH_SHORT).show()
                       }
                       permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                           // Only approximate location access granted.
                           Toast.makeText(this,"Coarse Location Granted",Toast.LENGTH_SHORT).show()
                       } else -> {
                       // No location access granted.
                       Toast.makeText(this,"Please grant the permission first",Toast.LENGTH_SHORT).show()

                   }
                   }
               }

               locationPermissionRequest.launch(arrayOf(
                   android.Manifest.permission.ACCESS_FINE_LOCATION,
                   android.Manifest.permission.ACCESS_COARSE_LOCATION))
           }


       }
        retryLocation()


        setContent {
            WhereAmITheme {
                Mainscreen(location = location,::retryLocation)
            }
        }
    }
}
