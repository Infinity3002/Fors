package proglife.fora.bank.ui.services.map.google

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import proglife.fora.bank.R

class MyClusterRenderer(var context: Context?, map: GoogleMap?, clusterManager: ClusterManager<MyItem>?) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {

    override fun onBeforeClusterRendered(cluster: Cluster<MyItem>?, markerOptions: MarkerOptions?) {
        super.onBeforeClusterRendered(cluster, markerOptions)

        //markerOptions!!.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
    }

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {


        when {
            markerOptions.snippet == "Банкомат" -> markerOptions.icon(bitmapDescriptorFromVector(context!!, R.drawable.ic_marker_fora))
            markerOptions.snippet == "Платёжный терминал" -> markerOptions.icon(bitmapDescriptorFromVector(context!!, R.drawable.ic_marker_device))
            else -> markerOptions.icon(bitmapDescriptorFromVector(context!!, R.drawable.ic_marker_fora))
        }

        super.onBeforeClusterItemRendered(item, markerOptions)
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
}