package kz.eztech.stylyts.presentation.base

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.item_hide_keyboard.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import kotlin.math.roundToInt


/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
abstract class BaseFragment<T : BaseActivity> : Fragment() {
    val currentActivity: T
        get() = activity as T

    val currentView: BaseView
        get() = getContractView()

    var hasInitializedRootView = false

    private var rootView: View? = null
    private var keyboardHideView: View? = null

    private val keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        if (isKeyboardOpen()) {
            keyboardHideView?.show()
        } else {
            keyboardHideView?.hide()
        }
    }

    fun displayToast(msg: String) {
        rootView?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(getColor(context, R.color.app_dark_blue_gray))
                view.setPadding(0, 0, 0, 0)
            }.show()
        }

    }

    fun getPersistentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false)
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentView.initializeDependency()
        currentView.initializePresenter()
        return getPersistentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            currentView.initializeViewsData()
            currentView.initializeArguments()
            currentView.customizeActionBar()
            currentView.initializeViews()
            currentView.initializeListeners()
            currentView.processPostInitialization()
        }
    }

    override fun onStop() {
        super.onStop()
        currentView.disposeRequests()
    }

    override fun onDestroy() {
        super.onDestroy()
        getRootView()?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardListener)
    }

    fun hideSoftWareKeyboard() {
        val imm =
            currentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentActivity.currentFocus
        if (view == null) {
            view = View(currentActivity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    abstract fun getLayoutId(): Int

    abstract fun getContractView(): BaseView

    fun customizeActionToolBar(toolbar: View? = null, title: String? = null) {
        toolbar?.let {
            it.apply {
                title?.let { text ->
                    toolbar_title_text_view.text = text
                }
                toolbar_left_corner_action_image_button.setOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun initializeHideKeyboardView(view: View? = null) {
        keyboardHideView = view

        view?.let {
            it.hide_keyboard_text_view.setOnClickListener {
                val inputMethodManager =
                    activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }

            getRootView()?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardListener)
        }
    }

    private fun getRootView(): View? = activity?.findViewById(android.R.id.content)

    private fun isKeyboardOpen(): Boolean {
        val visibleBounds = Rect()

        getRootView()?.getWindowVisibleDisplayFrame(visibleBounds)

        val heightDiff = (getRootView()?.height ?: 0) - visibleBounds.height()
        val marginOfError = this.convertDpToPx(dp = 50F).roundToInt()

        return heightDiff > marginOfError
    }

    private fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics
        )
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    @CallSuper
    private fun onLifecyclePause() {
        getRootView()?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardListener)
    }
}