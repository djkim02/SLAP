package com.djkim.slap.login;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.login.widget.LoginButton;

/**
 * Created by dongjoonkim on 10/24/15.
 */
public class ParseButton extends LoginButton {
    public ParseButton(Context context) {
        super(context);
    }

    public ParseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParseButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void configureButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.configureButton(context, attrs, defStyleAttr, defStyleRes);
        this.setInternalOnClickListener(null);
    }
}
