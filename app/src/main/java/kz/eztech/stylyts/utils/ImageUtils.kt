package kz.eztech.stylyts.utils

import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.math.roundToInt

class ImageUtils(
    private val context: Context
) {

    fun compressImage(imageUri: String): String {
        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options: BitmapFactory.Options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        var bmp: Bitmap = BitmapFactory.decodeFile(filePath, options)

        var actualHeight: Int = options.outHeight
        var actualWidth: Int = options.outWidth

        val maxHeight = 816.0f
        val maxWidth = 612.0f
        var imgRatio: Float = (actualWidth / actualHeight) as Float
        val maxRatio: Float = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth) as Int
                actualHeight = maxHeight as Int
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight) as Int
                actualWidth = maxWidth as Int
            } else {
                actualHeight = maxHeight as Int
                actualWidth = maxWidth as Int
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace();
        }

        val ratioX: Float = actualWidth / options.outWidth as Float
        val ratioY: Float = actualHeight / options.outHeight as Float
        val middleX: Float = actualWidth / 2.0f
        val middleY: Float = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas()
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        var exif: ExifInterface? = null

        try {
            filePath?.let {
                exif = ExifInterface(it)
            }

            val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")

            val matrix = Matrix()

            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }

            scaledBitmap?.let {
                scaledBitmap = Bitmap.createBitmap(
                    it,
                    0,
                    0,
                    it.width,
                    it.height,
                    matrix,
                    true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename: String = getFilename()

        try {
            out = FileOutputStream(filename)

            scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    fun getFilename(): String {
        val file: File = File(Environment.getExternalStorageDirectory().path, "MyFolder/Images")

        if (!file.exists()) {
            file.mkdirs()
        }

        return (file.absolutePath + "/" + System.currentTimeMillis() + ".jpg")
    }

    fun getRealPathFromURI(contentUri: String): String? {
        val imageUri: Uri = Uri.parse(contentUri)
        val cursor: Cursor? = context.contentResolver.query(imageUri, null, null, null, null)

        return run {
            cursor?.moveToFirst()
            val index: Int = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA) ?: 0

            cursor?.getString(index)
        }
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height: Int = options.outHeight
        val width: Int = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio: Int = (height as Float / reqHeight as Float).roundToInt()
            val widthRatio: Int = (width as Float / reqWidth as Float).roundToInt()
            inSampleSize = when (heightRatio < widthRatio) {
                true -> heightRatio
                false -> widthRatio
            }
        }

        val totalPixels: Float = (width * height) as Float
        val totalReqPixelsCap: Float = (reqWidth * reqHeight * 2) as Float

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }
}