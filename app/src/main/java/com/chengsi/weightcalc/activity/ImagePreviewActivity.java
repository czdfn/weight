package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.ImagePreviewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImagePreviewActivity extends BaseActivity {

    public static final String KEY_IMAGE_CURRENT_URL = "KEY_IMAGE_CURRENT_URL";
    public static final String KEY_IMAGE_URLLIST = "KEY_IMAGE_URLLIST";

    private List<String> imageList = new ArrayList<>();
    private String currentUrl = "";

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.inject(this);

        imageList = getIntent().getStringArrayListExtra(KEY_IMAGE_URLLIST);
        currentUrl = getIntent().getStringExtra(KEY_IMAGE_CURRENT_URL);
        ImagePreviewController controller = new ImagePreviewController(this, viewPager);
        controller.setImageSource(imageList);
        controller.setCurrentImage(currentUrl);
        controller.setEnableDoubleTap(true);
        controller.setEnableSingleTap(false);
    }
}
