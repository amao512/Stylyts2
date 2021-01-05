package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.fragment_camera.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class CameraFragment : BaseFragment<MainActivity>(),BaseView {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format)
                .format(System.currentTimeMillis()) + extension)
    }


    inner class ImageAnalyzer : ImageAnalysis.Analyzer {

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanBarcodes(image)
            }
        }
    }
    private val executor = Executors.newSingleThreadExecutor()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun getLayoutId(): Int {
        return R.layout.fragment_camera
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {

    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                currentActivity,permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun initializeViews() {

    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {

    }

    override fun hideProgress() {

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(currentActivity)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor, ImageAnalyzer())
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageAnalysis,preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(currentActivity))
    }

    private fun scanBarcodes(image: InputImage) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()

        val scanner = BarcodeScanning.getClient()
        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_barcodes]
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints

                    val rawValue = barcode.rawValue
                    Log.wtf("BARCODE","bounds = $bounds ,corners = $corners")
                    Log.wtf("BARCODE","rawValue = $rawValue")
                    val valueType = barcode.valueType

                    if(rawValue.isNotEmpty()){
                        findNavController().navigate(R.id.action_cameraFragment_to_itemDetailFragment)
                    }
                    // See API reference for complete list of supported types
                    when (valueType) {
                        Barcode.TYPE_WIFI -> {
                            val ssid = barcode.wifi!!.ssid
                            val password = barcode.wifi!!.password
                            val type = barcode.wifi!!.encryptionType
                            Log.wtf("BARCODE - WIFI","ssid = $ssid ,password = $password,type = $type ")
                        }
                        Barcode.TYPE_URL -> {
                            val title = barcode.url!!.title
                            val url = barcode.url!!.url
                            Log.wtf("BARCODE - TYPE_URL","title = $title ,url = $url")

                        }
                    }

                }
            }
            .addOnFailureListener {
                displayMessage("Упс, что то пошло не так :(,Попробуйте еще раз")
            }
    }

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            currentActivity, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

}