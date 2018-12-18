package com.synergy.keimed_ordergenie.utils;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by 1132 on 10-02-2017.
 */

public abstract class OnInfoWindowElemTouchListener implements View.OnTouchListener {
    private final View view;
    String client_id;
    String client_name;
    String response;

    private final Handler handler = new Handler();

    private Marker marker;
    private Context ctx;
    private boolean pressed = false;

    public OnInfoWindowElemTouchListener(Context ctx,View view, String client_id, String client_name, String response) {
        this.view = view;
        this. client_id=client_id;
        this. client_name=client_name;
        this. response=response;
        this.ctx=ctx;
    }
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public boolean onTouch(View vv, MotionEvent event) {
        if (0 <= event.getX() && event.getX() <= view.getWidth() &&
                0 <= event.getY() && event.getY() <= view.getHeight())
        {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: startPress(); break;

                // We need to delay releasing of the view a little so it shows the pressed state on the screen
                case MotionEvent.ACTION_UP: handler.postDelayed(confirmClickRunnable, 150); break;

                case MotionEvent.ACTION_CANCEL: endPress(); break;
                default: break;
            }
        }
        else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress();
        }
        return false;
    }

    private void startPress() {
        if (!pressed) {
            pressed = true;
            handler.removeCallbacks(confirmClickRunnable);
            if (marker != null)
                marker.showInfoWindow();
        }
    }

    private boolean endPress() {
        if (pressed) {
            this.pressed = false;
            handler.removeCallbacks(confirmClickRunnable);



            if (marker != null)
                marker.showInfoWindow();
            return true;
        }
        else
            return false;
    }

    private final Runnable confirmClickRunnable = new Runnable() {
        public void run() {
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };

    /**
     * This is called after a successful click
     */
    protected abstract void onClickConfirmed(View v, Marker marker);
}
