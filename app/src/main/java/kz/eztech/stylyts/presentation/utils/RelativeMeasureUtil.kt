package kz.eztech.stylyts.presentation.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
object RelativeMeasureUtil {

    const val rcX = 65f
    const val rcY = 100f

    fun measureView(view: View,container:ViewGroup):RelativeImageMeasurements{
        view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        container.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        Log.wtf("ResultAbsolute ","x:${view.x},y:${view.y},w:${view.width},x:${view.height}")
        Log.wtf("ResultAbsoluteContainer ","x:${container.x},y:${container.y},w:${container.width},x:${container.height}")
        val xAp = (100*view.x)/container.width
        val xR = (rcX * xAp)/100

        val yAp = (100*view.y)/container.height
        val yR = (rcY * yAp)/100

        val wAp = (100*view.width)/container.width
        val wR = (rcX * wAp)/100

        val hAp = (100*view.height)/container.height
        val hR = (rcY * hAp)/100

        return RelativeImageMeasurements(xR,yR,wR,hR)
    }
    
    fun measureEntity(view: ImageEntity,container:ViewGroup):RelativeImageMeasurements{
        Log.wtf("ResultAbsolute ","x:${view.absoluteCenterX()},y:${view.absoluteCenterY()},w:${view.getScaledWidth()},x:${view.getScaledHeight()}")
        Log.wtf("ResultAbsoluteContainer ","x:${container.x},y:${container.y},w:${container.width},x:${container.height}")
        val xAp = (100*view.absoluteCenterX())/container.width
        val xR = (rcX * xAp)/100
        
        val yAp = (100*view.absoluteCenterY())/container.height
        val yR = (rcY * yAp)/100
        
        val wAp = (100*view.getScaledWidth())/container.width
        val wR = (rcX * wAp)/100
        
        val hAp = (100*view.getScaledHeight())/container.height
        val hR = (rcY * hAp)/100
        
        Log.wtf("ResultRelative ","x:${xR},y:${yR},w:${wR},x:${hR}")
        return RelativeImageMeasurements(xR,yR,wR,hR,view.layer.rotationInDegrees)
    }

    fun reMeasureView(relativeImageReMeasurements:RelativeImageMeasurements,container:View): RelativeImageMeasurements{
        Log.wtf("ResultRelative","i x:${relativeImageReMeasurements.point_x},y:${relativeImageReMeasurements.point_y},w:${relativeImageReMeasurements.width},x:${relativeImageReMeasurements.height}")
        container.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        val xAp = (relativeImageReMeasurements.point_x*100)/rcX
        val x = (container.width*xAp)/100

        val yAp = (relativeImageReMeasurements.point_y*100)/ rcY
        val y = (container.height*yAp)/100

        val wAp = (relativeImageReMeasurements.width*100)/rcX
        val width = (container.width*wAp)/100

        val hAp = (relativeImageReMeasurements.height*100)/rcY
        val height = (container.height*hAp)/100

        return RelativeImageMeasurements(x,y,width,height)
    }
    
    fun reMeasureEntity(relativeImageReMeasurements:RelativeImageMeasurements,container:View):RelativeImageMeasurements{
        Log.wtf("ResultRelative ","x:${relativeImageReMeasurements.point_x},y:${relativeImageReMeasurements.point_y},w:${relativeImageReMeasurements.width},x:${relativeImageReMeasurements.height}")
        Log.wtf("ResultAbsoluteContainer ","x:${container.x},y:${container.y},w:${container.width},x:${container.height}")
        val xAp = (relativeImageReMeasurements.point_x*100)/rcX
        val x = (container.width*xAp)/100
    
        val yAp = (relativeImageReMeasurements.point_y*100)/ rcY
        val y = (container.height*yAp)/100
    
        val wAp = (relativeImageReMeasurements.width*100)/rcX
        val width = (container.width*wAp)/100
    
        val hAp = (relativeImageReMeasurements.height*100)/rcY
        val height = (container.height*hAp)/100
        Log.wtf("ResultAbsolute ","x:${x},y:${y},w:${width},x:${height}")
        return RelativeImageMeasurements(x,y,width,height,relativeImageReMeasurements.degree)
    }
}

data class RelativeImageMeasurements(
    var point_x:Float = 0f,
    var point_y:Float = 0f,
    var width:Float = 0f,
    var height:Float = 0f,
    var degree:Float = 0f,
)