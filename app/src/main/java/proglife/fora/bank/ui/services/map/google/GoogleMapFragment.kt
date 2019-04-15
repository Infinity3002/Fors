package proglife.fora.bank.ui.services.map.google

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.AvoidType
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.directions.route.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.bs_map_layout.*
import kotlinx.android.synthetic.main.fragment_map_google.*
import org.json.JSONArray
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import java.io.IOException
import java.util.ArrayList


class GoogleMapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        RoutingListener, ClusterManager.OnClusterItemClickListener<MyItem>, MapView {


    val REQUEST_CODE_FINE_GPS : Int = 9999

    private lateinit var mClusterManager : ClusterManager<MyItem>

    var map: GoogleMap? = null
    var myLocation : LatLng? = null
    var currentSelectMarker : LatLng? = null

    @InjectPresenter
    lateinit var mPresenter: MapPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_google, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = this.childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        btnList.setOnClickListener { mPresenter.back() }
        btnMyLocation.setOnClickListener{ moveToMyLocation() }
        btnRoute.setOnClickListener{ route()}
        btnClose.setOnClickListener{ layoutBottomSheet!!.visibility = View.GONE }
        btnPanelRoute.setOnClickListener{ route() }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

        map = googleMap
        map!!.setOnMarkerClickListener(this)
        map!!.uiSettings.isMyLocationButtonEnabled = false

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map!!.isMyLocationEnabled = true
            moveToMyLocation()
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_FINE_GPS)
        }


        mClusterManager = ClusterManager(context!!, map)

        var myClusterRenderer = MyClusterRenderer(context!!, map,  mClusterManager)
        mClusterManager.renderer = myClusterRenderer
        map!!.setOnCameraIdleListener(mClusterManager)
        map!!.setOnMarkerClickListener(mClusterManager)

        showObject()
    }

    fun showObject() {



        var array = JSONArray(getBankBranches())
        for (x in 0 until array.length()) {
            var o = array.getJSONObject(x)
            var offsetItem : MyItem? = null
            var geoPoint = LatLng(o.getDouble("latitude"), o.getDouble("longitude"))

            var title = o.getString("name") + "\n" + o.getString("city")  + "\n" + o.getString("address")  +  "\n" + o.getString("schedule")

            var snippet = ""

            if (o.has("type")) {

                snippet = o.getString("type")
            }

            var markerOptions = MarkerOptions()
                    .position(geoPoint)
                    .title(title)
                    .snippet(snippet)

            offsetItem = MyItem(markerOptions)
            mClusterManager.setOnClusterItemClickListener(this)
            mClusterManager.addItem(offsetItem)


        }
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId :  Int) : BitmapDescriptor {
        var background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight);
        var vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable!!.setBounds(40, 40, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight);
        var bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888);
        var canvas = Canvas(bitmap);
        background.draw(canvas);
        //vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun getBankBranches(): String {
        val jsonString = getAssetsJSON("bank_branches.json")
        return jsonString
    }


    fun getAssetsJSON(fileName: String): String {
        var json: String? = null
        try {
            val inputStream = context!!.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json!!
    }


    override fun onMarkerClick(marker: Marker?): Boolean {



        Log.d("32322", "3232 = " + marker!!.position  )

        return true
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_FINE_GPS) {
            if (permissions.size == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map!!.isMyLocationEnabled = true
                moveToMyLocation()

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun moveToMyLocation() {

        var locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager


        var criteria = Criteria()

        var provider = locationManager.getBestProvider(criteria, true)


        var location = locationManager.getLastKnownLocation(provider)

        if (location != null) {

            var latitude = location.latitude;

            var longitude = location.longitude;

            myLocation = LatLng(latitude, longitude)

            map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 8f));
        }



    }


    fun route() {

        if(myLocation == null) {
            Toast.makeText(context!!, "Неудалось определить местоположение", Toast.LENGTH_SHORT).show();
        } else if(currentSelectMarker == null) {
            Toast.makeText(context!!, "Выберите метку ", Toast.LENGTH_SHORT).show();
        } else {

            GoogleDirection.withServerKey("AIzaSyBlMoPgaZHgePRixRGUMLcTkpVF4NbW97g")
                    .from(myLocation)
                    .to(currentSelectMarker)
                    .avoid(AvoidType.FERRIES)
                    .avoid(AvoidType.HIGHWAYS)
                    .optimizeWaypoints(true)
                    .execute(object : DirectionCallback {

                        override fun onDirectionSuccess(direction: Direction, rawBody: String) {
                            //mMap.addMarker(new MarkerOptions().position(origin).icon(null));
                            //mMap.addMarker(new MarkerOptions().position(currentSelectMarker));

                            val directionPositionList = direction.routeList[0].legList[0].directionPoint
                            map!!.addPolyline(DirectionConverter.createPolyline(context!!, directionPositionList, 5, Color.parseColor("#d42e2c")))
                            //String info = marker.getTitle();


                            Log.d("32323112", "22222 " + direction.routeList[0].legList[0].distance.text)
                        }

                        override fun onDirectionFailure(t: Throwable) {
                            // Do something
                        }
                    })


           /*var routing = Routing.Builder()
                   .travelMode(AbstractRouting.TravelMode.WALKING)
                   .withListener(this)
                   .alternativeRoutes(true)
                    .waypoints(myLocation, currentSelectMarker)
                    .key("AIzaSyBlMoPgaZHgePRixRGUMLcTkpVF4NbW97g")
                    .build()
            routing.execute()*/

        }
        /*DirectionCallback {
                    override fun onDirectionSuccess(direction : Direction, rawBody : String) {

                        var directionPositionList = direction.routeList[0].legList[0].directionPoint;
                        map!!.addPolyline(DirectionConverter.createPolyline(activity, directionPositionList, 5, Color.parseColor("#47ac0b")));
                    }
                    override
                    fun onDirectionFailure( t : Throwable) {
                        // Do something
                    }
                }*/
    }


    override fun onClusterItemClick(item: MyItem?): Boolean {
        currentSelectMarker = item!!.position

        tvName.text = item.mTitle

        layoutBottomSheet!!.visibility = View.VISIBLE

        return true
    }


    override fun onRoutingCancelled() {
        Log.d("22222", "32322 Cancelled" )
    }

    override fun onRoutingStart() {
        Log.d("22222", "32322 RoutingStart" )
    }

    override fun onRoutingFailure(p0: RouteException?) {
        Log.d("22222", "32322 onRoutingFailure" + p0.toString() )
    }

    override fun onRoutingSuccess(routes: ArrayList<Route>?, p1: Int) {

        for(x in 0 until routes!!.size) {
           // map.addPolyline(createPolyline(context!!, routes, 5, Color.parseColor("#47ac0b")))
        }

        Log.d("22222", "32322 RoutingSuccess" )
    }


    fun createPolyline(context: Context, locationList: ArrayList<LatLng>, width: Int, color: Int): PolylineOptions {
        val rectLine = PolylineOptions().width(dpToPx(context, width).toFloat()).color(color).geodesic(true)
        for (location in locationList) {
            rectLine.add(location)
        }
        return rectLine
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

}