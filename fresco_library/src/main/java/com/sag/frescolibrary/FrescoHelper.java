package com.sag.frescolibrary;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sag.foundationlibrary.base.image.ImageHelper;
import com.sag.foundationlibrary.base.image.ImageStamp;
import com.sag.foundationlibrary.base.util.ContextUtil;

/**
 * Created by SAG on 2016/10/25 0025.
 */

public class FrescoHelper implements ImageStamp<SimpleDraweeView> {

    public FrescoHelper() {
        Context context = ContextUtil.getContext();
        DiskCacheConfig cacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(context.getExternalCacheDir()).build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setMainDiskCacheConfig(cacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(context, config);
    }

    @BindingAdapter({"assets"})
    public static void showAssetsResource(SimpleDraweeView draweeView, String name) {
        Uri uri = Uri.parse("asset:///" + name);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    @BindingAdapter("drawable")
    public static void setImageResourceID(SimpleDraweeView simpleDraweeView, int resourceID) {
        simpleDraweeView.setBackgroundResource(resourceID);
    }

    @BindingAdapter({"url", "ratio"})
    public static void showImage(SimpleDraweeView view, String url, float ratio) {
        ImageHelper.loadImage(view, url, ratio);
    }

    public void loadImage(SimpleDraweeView view, String url, float aspectRatio) {
        if (url == null) {
            return;
        } else {
            Uri uri = Uri.parse(url);
            int width = view.getWidth();
            int height = view.getHeight();
            ResizeOptions resizeOptions;
            if (width <= 0 || height <= 0) {
                resizeOptions = null;
            } else {
                resizeOptions = new ResizeOptions(width, height);
            }
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(resizeOptions)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(view.getController())
                    .build();
            view.setAspectRatio(aspectRatio);
            view.setController(controller);
        }
    }

}
