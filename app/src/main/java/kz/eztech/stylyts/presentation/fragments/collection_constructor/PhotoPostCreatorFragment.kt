package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_photo_post_creator.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.ViewUtils
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.adapters.collection_constructor.PhotoLibraryAdapter
import java.io.File

class PhotoPostCreatorFragment(
    private val inPager: Boolean = false
) : BaseFragment<MainActivity>(), EmptyContract.View,
    LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, UniversalViewClickListener {

    private lateinit var photoAdapter: PhotoLibraryAdapter

    private val listOfAllImages = ArrayList<String>()
    private val listOfChosenImages = ArrayList<String>()

    private var photoUri: Uri? = null
    private var mode = -1

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

        photoUri?.let {
            Glide.with(currentActivity)
                .load(it.path)
                .into(image_view_fragment_photo_post_creator)
        }
    }

    private fun switchFromCameraPicture() {
        photoAdapter = PhotoLibraryAdapter()
        photoAdapter.setOnClickListener(this)

        val numbers = ViewUtils.calculateNoOfColumns(currentActivity, 100f)

        recycler_view_fragment_photo_post_creator_list.layoutManager = GridLayoutManager(currentActivity, numbers)
        recycler_view_fragment_photo_post_creator_list.adapter = photoAdapter
        photoAdapter.updateList(listOfAllImages)
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        item as String

        photoUri = if (listOfChosenImages.contains(item)) {
            listOfChosenImages.remove(item)
            photoAdapter.notifyItemChanged(position, -1)

            null
        } else {
            if (listOfChosenImages.size == 4) {
                displayMessage(msg = "Только 4 фото!")

                return
            }

            listOfChosenImages.add(item)
            photoAdapter.notifyItemChanged(position, listOfChosenImages.count())

            Uri.fromFile(File(item))
        }

        Glide.with(currentActivity)
            .load(photoUri)
            .into(image_view_fragment_photo_post_creator)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    //resume tasks needing this permission
                    LoaderManager.getInstance(this).initLoader(21, null, this)
                }
            }
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
        val listOfImages = ArrayList<String>()
        data?.let {

            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (it.moveToNext()) {
                listOfImages.add(it.getString(columnIndexData));
            }
        }
        if (listOfImages.isNotEmpty()) {
            listOfAllImages.clear()
            listOfAllImages.addAll(listOfImages)
        }

        if (listOfAllImages.isNotEmpty()) {
            photoUri = Uri.fromFile(File(listOfAllImages[0]))
            Glide.with(currentActivity).load(listOfAllImages[0])
                .into(image_view_fragment_photo_post_creator)
            photoAdapter.updateList(listOfAllImages)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

    override fun initializeListeners() {
        frame_layout_fragment_photo_post_creator_camera.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

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
        }
    }

    private fun onCameraClick() {
        if (inPager) {
            findNavController().navigate(R.id.action_createCollectionFragment_to_cameraFragment)
        } else {
            findNavController().navigate(R.id.action_photoPostCreatorFragment_to_cameraFragment)
        }
    }

    private fun onNextClick() {
        if (photoUri != null) {
            val bundle = Bundle()

            if (listOfChosenImages.isNotEmpty()) {
                listOfChosenImages.removeLast()
            }

            bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, CreateCollectionAcceptFragment.POST_MODE)
            bundle.putParcelable(CreateCollectionAcceptFragment.PHOTO_URI_KEY, photoUri)
            bundle.putStringArrayList(CreateCollectionAcceptFragment.CHOSEN_PHOTOS_KEY, listOfChosenImages)

            if (inPager) {
                bundle.putBoolean(CreateCollectionAcceptFragment.IS_CHOOSER_KEY, true)

                findNavController().navigate(
                    R.id.action_createCollectionFragment_to_createCollectionAcceptDialog,
                    bundle
                )
            } else {
                findNavController().navigate(
                    R.id.createCollectionAcceptDialog,
                    bundle
                )
            }
        } else {
            displayMessage(msg = getString(R.string.error_choose_photo))
        }
    }
}