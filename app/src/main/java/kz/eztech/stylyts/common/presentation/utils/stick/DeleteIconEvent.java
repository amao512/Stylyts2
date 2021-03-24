package kz.eztech.stylyts.common.presentation.utils.stick;

import android.view.MotionEvent;

/**
 * Created by Ruslan Erdenoff on 16.01.2021.
 */
public class DeleteIconEvent implements StickerIconEvent{
    @Override public void onActionDown(MotionView stickerView, MotionEvent event) {

    }

    @Override public void onActionMove(MotionView stickerView, MotionEvent event) {

    }

    @Override public void onActionUp(MotionView stickerView, MotionEvent event) {
        stickerView.deletedSelectedEntity();
    }
}
