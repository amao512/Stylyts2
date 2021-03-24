package kz.eztech.stylyts.common.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import kz.eztech.stylyts.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat

/**
 * Created by Ruslan Erdenoff on 11.01.2020.
 */
object FileUtils {
    
    private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    private const val PHOTO_EXTENSION = ".jpg"
    
    fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format)
                    .format(System.currentTimeMillis()) + extension)
    
    fun createPngFileFromBitmap(context:Context,bitmap:Bitmap):File?{
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        val file = if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
        val photoFile = File(file, SimpleDateFormat(FILENAME)
                .format(System.currentTimeMillis()) + PHOTO_EXTENSION
        )
        try {
            val out = FileOutputStream(photoFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            return photoFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }
    fun getUriFromString(string: String?): Uri? {
        return if(string!=null){
            try {
                val file = File(string)
                file.toUri()
            }catch (e:Exception){
                return null
            }
        }else{
            null
        }
    }

    fun deleteFileFromPath(path:String?){
        path?.let {
            val file = File(path)
            if(file.exists())
                file.delete()
        }
    }
    
   
}