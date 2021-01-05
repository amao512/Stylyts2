package kz.eztech.stylyts.presentation.utils

import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.lang.Exception

/**
 * Created by Ruslan Erdenoff on 11.01.2020.
 */
object FileUtils {
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