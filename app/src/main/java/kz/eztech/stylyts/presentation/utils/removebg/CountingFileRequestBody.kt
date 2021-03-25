package kz.eztech.stylyts.presentation.utils.removebg

import com.google.android.gms.common.util.IOUtils.closeQuietly
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.BufferedSink
import okhttp3.RequestBody
import okio.Source
import okio.source
import java.io.File
import java.io.IOException

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
class CountingFileRequestBody(
    private val file: File,
    private val contentType: String,
    private val listener: ProgressListener
) : RequestBody() {

    override fun contentLength(): Long {
        return file.length()
    }

    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        var source: Source? = null
        try {
            source = file.source()
            var total: Long = 0
            var read: Long = -1
            while ({ read = source!!.read(sink.buffer(),
                    SEGMENT_SIZE
                ); read }() != -1L) {
                total += read
                sink.flush()
                val perc = (total * 100f) / contentLength()
                this.listener.transferred(perc)
            }

        } finally {
           closeQuietly(source)
        }
    }

    interface ProgressListener {
        fun transferred(percentage: Float)
    }

    companion object {
        private const val SEGMENT_SIZE = 2048L
    }

}