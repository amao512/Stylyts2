package kz.eztech.stylyts.common.presentation.utils.stick;

import android.view.MotionEvent;

/**
 * Created by Ruslan Erdenoff on 16.01.2021.
 */
public interface StickerIconEvent {
    void onActionDown(MotionView stickerView, MotionEvent event);

    void onActionMove(MotionView stickerView, MotionEvent event);

    void onActionUp(MotionView stickerView, MotionEvent event);
}
