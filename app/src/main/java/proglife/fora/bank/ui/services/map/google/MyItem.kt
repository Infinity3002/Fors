package proglife.fora.bank.ui.services.map.google

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.android.gms.maps.model.MarkerOptions



class MyItem : ClusterItem {

    //private val mPosition: LatLng
    lateinit var mTitle: String
    lateinit var mSnippet: String

    lateinit var latLng: LatLng
    private var icon: BitmapDescriptor? = null


    /*constructor(lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)
    }

    constructor(lat: Double, lng: Double, title: String, snippet: String) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
    }*/



    constructor(markerOptions: MarkerOptions) {
        latLng = markerOptions.position
        mTitle = markerOptions.title
        mSnippet = markerOptions.snippet
        icon = markerOptions.icon
    }

    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    fun setIcon() {

    }

     fun getIcon(): BitmapDescriptor? {
        return icon
    }
}