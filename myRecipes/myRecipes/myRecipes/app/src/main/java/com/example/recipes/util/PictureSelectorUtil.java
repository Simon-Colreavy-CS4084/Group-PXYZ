package com.example.recipes.util;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.example.recipes.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;


   

public class PictureSelectorUtil {

       
    public static void openRoundImage(Activity fragment, int maxSelectNum) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(false)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(true)                  .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .withAspectRatio(1, 1)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(true)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(null)                  .isUseCustomCamera(true)
                                                        .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }


       
    public static void openImage(Activity activity, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(false)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(false)  
                .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                                                          .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }

       
    public static void openImage(Fragment activity, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(false)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(false)  
                .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                                                          .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }


       
    public static void openImageVideo(Activity activity, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(true)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(false)                  .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                  .isUseCustomCamera(true)
                                                        .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                  .isWithVideoImage(true)                  .maxVideoSelectNum(maxSelectNum)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }

       
    public static void openImageVideo(Fragment fragment, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofAll())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(true)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(false)                  .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                  .isUseCustomCamera(true)
                                                        .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                  .isWithVideoImage(true)                  .maxVideoSelectNum(maxSelectNum)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }

       
    public static void openVideo(Activity activity, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(true)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(false)                  .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                                                          .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }

       
    public static void openFaceImage(Activity activity, int maxSelectNum, List<LocalMedia> selectionMedia) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(false)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(true)                  .compress(true)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .hideBottomControls(false)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(false)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(selectionMedia)                                                          .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }


       
    public static void openRectangleImage(Activity activity, int maxSelectNum, int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())                  .loadImageEngine(  GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)                  .maxSelectNum(maxSelectNum)                  .minSelectNum(1)                  .imageSpanCount(4)                  .selectionMode(maxSelectNum > 1 ?
                        PictureConfig.MULTIPLE : PictureConfig.SINGLE)                  .previewImage(true)                  .previewVideo(false)                  .enablePreviewAudio(false)                   .isCamera(true)                  .isZoomAnim(true)                  .imageFormat(PictureMimeType.PNG)                                    .enableCrop(true)                  .compress(false)                  .synOrAsy(true)                                                      .glideOverride(160, 160)                  .withAspectRatio(aspect_ratio_x, aspect_ratio_y)                  .hideBottomControls(true)                  .isGif(false)                  .freeStyleCropEnabled(false)                  .circleDimmedLayer(false)                  .showCropFrame(true)                  .showCropGrid(false)                  .openClickSound(false)                  .selectionMedia(null)                                                          .minimumCompressSize(100)                                    .rotateEnabled(true)                   .scaleEnabled(true)                                                                        .forResult(PictureConfig.CHOOSE_REQUEST);      }
}
