package kz.eztech.stylyts.presentation.fragments.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CleanBackgroundFragment
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class CameraFragment: BaseFragment<MainActivity>(), BaseView, View.OnClickListener {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        const val MODE_KEY = "mode"
        const val BARCODE_MODE = 1
        const val PHOTO_MODE = 2
        const val GET_PHOTO_MODE = 3

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format)
                    .format(System.currentTimeMillis()) + extension
            )
    }

    private lateinit var imageCapture: ImageCapture
    private var mode = 0

    inner class ImageAnalyzer : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            Log.wtf("Analyze", "analyzing")
            val mediaImage = imageProxy.image

            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanBarcodes(image, imageProxy)
            }
        }
    }

    private val executor = Executors.newSingleThreadExecutor()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onStart() {
        super.onStart()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(currentActivity, permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun getLayoutId(): Int =  R.layout.fragment_camera

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_camera_toolbar) {
            setBackgroundColor(getColor(requireContext(), R.color.app_black_65))

            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.hide()

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setTextColor(getColor(requireContext(), R.color.app_light_orange))

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(MODE_KEY)) {
                mode = it.getInt(MODE_KEY)
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {
        frame_layout_fragment_camera_take_picture.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_fragment_camera_take_picture -> takePicture()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigateUp()
            }
        }
    }

    private fun takePicture() {
        val photoFile =
            createFile(FileUtils.getOutputDirectory(requireContext()), FILENAME, PHOTO_EXTENSION)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Log.wtf(TAG, "Photo capture failed: ${error.message}", error)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onImageSaved(photoFile)
                }
            })
    }

    private fun onImageSaved(photoFile: File) {
        val savedUri = photoFile.toUri()

        when (mode) {
            0 -> navigateToPhotoChooser(savedUri)
            1 -> {}
            2 -> navigateToCleanBackground(savedUri)
            3 -> {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("uri_from_camera", savedUri)
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToPhotoChooser(savedUri: Uri) {
        val bundle = Bundle()
        bundle.putInt("mode", 0)
        bundle.putParcelable("uri", savedUri)

        findNavController().navigate(
            R.id.action_cameraFragment_to_photoChooserFragment,
            bundle
        )
    }

    private fun navigateToCleanBackground(savedUri: Uri) {
        val bundle = Bundle()

        bundle.putParcelable(CleanBackgroundFragment.URI_KEY, savedUri)

        findNavController().navigate(R.id.action_cameraFragment_to_cleanBackgroundFragment, bundle)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(currentActivity)
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(requireView().display.rotation)
            .build()

        cameraProviderFuture.addListener(Runnable {
            provideCamera(cameraProviderFuture)
        }, ContextCompat.getMainExecutor(currentActivity))
    }

    private fun provideCamera(cameraProviderFuture: ListenableFuture<ProcessCameraProvider>) {
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Preview
        val preview = getPreview()

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        when (mode) {
            0 -> showCamera(
                cameraProvider = cameraProvider,
                cameraSelector = cameraSelector,
                preview = preview
            )
            1 -> showBarcodeScanner(
                cameraProvider = cameraProvider,
                cameraSelector = cameraSelector,
                preview = preview
            )
            2 -> try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
            3 -> showCamera(
                cameraProvider = cameraProvider,
                cameraSelector = cameraSelector,
                preview = preview
            )
        }
    }

    private fun getPreview(): Preview {
        return Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(viewFinder.surfaceProvider) }
    }

    private fun showCamera(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        preview: Preview
    ) {
        linear_layout_fragment_camera_scanner_holder.hide()
        frameLayout4.show()
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun showBarcodeScanner(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        preview: Preview
    ) {
        linear_layout_fragment_camera_scanner_holder.show()
        frameLayout4.hide()
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
                this, cameraSelector, imageAnalysis, preview
            )

        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun scanBarcodes(image: InputImage, imageProxy: ImageProxy) {
        val options = getBarcodeScannerOptions()
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener {
                onScannerSuccessListener(barcodes = it)
            }
            .addOnFailureListener {
                displayMessage("Упс, что то пошло не так :(,Попробуйте еще раз")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun getBarcodeScannerOptions(): BarcodeScannerOptions {
        return BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .build()
    }

    private fun onScannerSuccessListener(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val bounds = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue
            Log.wtf("BARCODE", "bounds = $bounds ,corners = $corners")
            Log.wtf("BARCODE", "rawValue = $rawValue")
            val valueType = barcode.valueType

            when (valueType) {
                Barcode.TYPE_WIFI -> {
                    val ssid = barcode.wifi!!.ssid
                    val password = barcode.wifi!!.password
                    val type = barcode.wifi!!.encryptionType
                    Log.wtf(
                        "BARCODE - WIFI",
                        "ssid = $ssid ,password = $password,type = $type "
                    )
                }
                Barcode.TYPE_URL -> {
                    val title = barcode.url!!.title
                    val url = barcode.url!!.url
                    Log.wtf("BARCODE - TYPE_URL", "title = $title ,url = $url")

                }
            }

            if (rawValue.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("barcode_code", rawValue)
                findNavController().navigate(R.id.clothesDetailFragment, bundle)
            }
        }
    }

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            currentActivity, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}