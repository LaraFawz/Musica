package com.example.lara.sing;

/**
 * Created by Lara on 5/11/2018.
 */

public class Setting {

    /** String resource ID for the default translation of the word */
    private int mDefaultTranslationId;


    /** Image resource ID for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;


    public Setting(int defaultTranslationId, int imageResourceId) {
        mDefaultTranslationId = defaultTranslationId;
        mImageResourceId = imageResourceId;
    }



    /**
     * Get the string resource ID for the default translation of the word.
     */
    public int getDefaultTranslationId() {
        return mDefaultTranslationId;
    }


    /**
     * Return the image resource ID of the word.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }


}

