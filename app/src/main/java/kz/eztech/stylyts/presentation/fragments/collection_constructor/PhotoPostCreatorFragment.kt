package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_photo_post_creator.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.common.PhotoLibraryModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.PhotoLibraryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.ViewUtils
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.io.File

class PhotoPostCreatorFragment(
    private val inPager: Boolean = false
) : BaseFragment<MainActivity>(), EmptyContract.View,
    LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, UniversalViewClickListener {

    private lateinit var photoAdapter: PhotoLibraryAdapter
    private lateinit var galleryResultLaunch: ActivityResultLauncher<Intent>

    private val listOfAllImages = ArrayList<PhotoLibraryModel>()
    private val listOfChosenImages = ArrayList<PhotoLibraryModel>()

    private var photoUri: Uri? = null
    private var mode = -1
    private var isMultipleChoice: Boolean = false

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun getLayoutId(): Int = R.layout.fragment_photo_post_creator

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_fragment_photo_post_creator) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PhotoPostCreatorFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = context.getString(R.string.post_creator_title)
            toolbar_title_text_view.show()

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            )
            toolbar_right_text_text_view.setOnClickListener(this@PhotoPostCreatorFragment)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("uri")) {
                photoUri = it.getParcelable("uri")
            }
            if (it.containsKey("mode")) {
                mode = it.getInt("mode")
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Uri>(CameraFragment.URI_FROM_CAMERA)
            ?.observe(viewLifecycleOwner, {
                photoUri = it
                onNextClick()
            })
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentActivity.hideBottomNavigationView()

        when (mode) {
            0 -> switchToCameraPicture()
            else -> {
                checkWritePermission()
                switchFromCameraPicture()
            }
        }
    }

    private fun switchToCameraPicture() {
        recycler_view_fragment_photo_post_creator_list.hide()

        photoUri?.path?.loadImage(target = image_view_fragment_photo_post_creator)
    }

    private fun switchFromCameraPicture() {
        photoAdapter = PhotoLibraryAdapter()
        photoAdapter.setOnClickListener(this)

        val numbers = ViewUtils.calculateNoOfColumns(currentActivity, 100f)

        recycler_view_fragment_photo_post_creator_list.layoutManager =
            GridLayoutManager(currentActivity, numbers)
        recycler_view_fragment_photo_post_creator_list.adapter = photoAdapter

        photoAdapter.updateList(listOfAllImages)
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is PhotoLibraryModel -> onPhotoClicked(photoLibraryModel = item, position)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media._ID
        )
        val selection: String? = null     //Selection criteria
        val selectionArgs = arrayOf<String>()  //Selection criteria
        val sortOrder = MediaStore.Images.Media._ID;

        return CursorLoader(
            currentActivity,
            uri,
            projection,
            null,
            null,
            sortOrder
        )

        Log.wtf("onCreateLoader", "Loaded")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val listOfImages = ArrayList<PhotoLibraryModel>()
        var counter = 1

        data?.let {

            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (it.moveToNext()) {
                listOfImages.add(
                    PhotoLibraryModel(
                        id = counter,
                        photo = it.getString(columnIndexData)
                    ))

                counter++
            }
        }

        listOfImages.reverse()

        if (listOfImages.isNotEmpty()) {
            listOfAllImages.clear()
            listOfAllImages.addAll(listOfImages)
        }

        if (listOfAllImages.isNotEmpty()) {
            photoUri = Uri.fromFile(File(listOfAllImages[0].photo))

            listOfAllImages[0].photo
                .loadImage(target = image_view_fragment_photo_post_creator)

            photoAdapter.updateList(listOfAllImages)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

    override fun initializeListeners() {
        frame_layout_fragment_photo_post_creator_camera.setOnClickListener(this)
        frame_layout_fragment_photo_post_creator_chooser.setOnClickListener(this)
        text_view_fragment_photo_post_creator_album_chooser.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        galleryResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    setImage(uri = result.data?.data)
                }
            }
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_fragment_photo_post_creator_camera -> onCameraClick()
            R.id.toolbar_right_text_text_view -> onNextClick()
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.frame_layout_fragment_photo_post_creator_chooser -> onMultipleButtonClick()
            R.id.text_view_fragment_photo_post_creator_album_chooser -> openGallery()
        }
    }

    private fun checkWritePermission() {
        val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionGranted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(
                currentActivity,
                writeExternalStoragePermission
            )
            != permissionGranted || ContextCompat.checkSelfPermission(
                currentActivity,
                readExternalStoragePermission
            )
            != permissionGranted
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    writeExternalStoragePermission
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    writeExternalStoragePermission
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    currentActivity,
                    arrayOf(writeExternalStoragePermission, readExternalStoragePermission),
                    2
                )
            }
        } else {
            LoaderManager.getInstance(this).initLoader(21, null, this)
        }
    }

    private fun onPhotoClicked(
        photoLibraryModel: PhotoLibraryModel,
        position: Int
    ) {
        if (!isMultipleChoice) {
            listOfChosenImages.clear()
            photoUri = Uri.fromFile(
                File(photoLibraryModel.photo)
            )
        } else {
            if (photoLibraryModel.isChosen) {
                photoAdapter.removeNumber(position)

                listOfChosenImages.clear()
                listOfChosenImages.addAll(photoAdapter.getChosenPhotos())
            } else {
                if (listOfChosenImages.size == 5) {
                    return
                }

                listOfChosenImages.add(photoLibraryModel)
                photoAdapter.setNumber(position, listOfChosenImages.count())
            }

            if (listOfChosenImages.isNotEmpty()) {
                photoUri = Uri.fromFile(File(listOfChosenImages[0].photo))
            }
        }

        photoUri?.loadImage(target = image_view_fragment_photo_post_creator)
    }

    private fun onCameraClick() {
        val bundle = Bundle()
        bundle.putInt(CameraFragment.MODE_KEY, CameraFragment.GET_PHOTO_MODE)

        if (inPager) {
            findNavController().navigate(
                R.id.action_createCollectionFragment_to_cameraFragment,
                bundle
            )
        } else {
            findNavController().navigate(
                R.id.action_photoPostCreatorFragment_to_cameraFragment,
                bundle
            )
        }
    }

    private fun onNextClick() {
        if (photoUri != null) {
            val bundle = Bundle()

            if (listOfChosenImages.isNotEmpty()) {
                listOfChosenImages.removeFirst()
            }

            val preparedImagesList = ArrayList<String>()

            listOfChosenImages.map {
                preparedImagesList.add(it.photo)
            }

            bundle.putInt(
                CreateCollectionAcceptFragment.MODE_KEY,
                CreateCollectionAcceptFragment.POST_MODE
            )
            bundle.putParcelable(CreateCollectionAcceptFragment.PHOTO_URI_KEY, photoUri)
            bundle.putStringArrayList(CreateCollectionAcceptFragment.CHOSEN_PHOTOS_KEY, preparedImagesList)

            if (inPager) {
                bundle.putBoolean(CreateCollectionAcceptFragment.IS_CHOOSER_KEY, true)

                findNavController().navigate(
                    R.id.action_createCollectionFragment_to_createCollectionAcceptFragment,
                    bundle
                )
            } else {
                findNavController().navigate(R.id.createCollectionAcceptFragment, bundle)
            }
        } else {
            displayMessage(msg = getString(R.string.error_choose_photo))
        }
    }

    private fun onMultipleButtonClick() {
        when (isMultipleChoice) {
            true -> {
                photoAdapter.disableMultipleChoice()
                listOfChosenImages.clear()
            }
            false -> photoAdapter.enableMultipleChoice()
        }

        isMultipleChoice = !isMultipleChoice
    }

    private fun openGallery() {
        galleryResultLaunch.launch(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        )
    }

    private fun setImage(uri: Uri?) {
        photoUri = uri

        onNextClick()
    }
}