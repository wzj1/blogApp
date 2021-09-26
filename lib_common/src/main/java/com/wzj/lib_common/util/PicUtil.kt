package com.wzj.lib_common.util

import android.app.Activity
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wzj.lib_common.glide.GlideEngine

/**
 * @author wangzhijie
 * @time
 */
object PicUtil {
    //    /**
    //     * 获取图片选择的配置
    //     *
    //     * @param fragment
    //     * @return
    //     */
    //    public static PictureSelectionModel getPictureSelector(Fragment fragment) {
    //        return PictureSelector.create(fragment)
    //                .openGallery(PictureMimeType.ofImage())
    //                .maxSelectNum(8)
    //                .minSelectNum(1)
    //                .selectionMode(PictureConfig.MULTIPLE)
    //                .previewImage(true)
    //                .isCamera(true)
    //                .enableCrop(false)
    //                .compress(true)
    //                .previewEggs(true);
    //    }
    fun getPictureSelector(
        activity: Activity?,
        maxSelectNum: Int,
        minSelectNum: Int,
        previewImage: Boolean,
        isCamera: Boolean,
        enableCrop: Boolean,
        compress: Boolean,
        previewEggs: Boolean
    ): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .isWeChatStyle(true) // 是否开启微信图片选择风格
            .maxSelectNum(maxSelectNum) //最大选择
            .minSelectNum(minSelectNum) //最小选择
            .isNotPreviewDownload(false) //是否长按显示下载 设置为false
            .selectionMode(PictureConfig.MULTIPLE) // 多选
            .isPreviewImage(previewImage) //是否预览
            .setRequestedOrientation(1)
            .isCamera(isCamera) //是否显示相机
            .isCompress(compress) //是否压缩
            .isPreviewEggs(previewEggs) //是否预览大小
    }

    fun getPictureSelector(
        activity: Activity?,
        previewImage: Boolean,
        isCamera: Boolean,
        enableCrop: Boolean,
        compress: Boolean,
        circleDimmedLayer: Boolean,
        previewEggs: Boolean
    ): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .isWeChatStyle(true) // 是否开启微信图片选择风格
            .maxSelectNum(1) //最大选择
            .minSelectNum(1) //最小选择
            .isNotPreviewDownload(false) //是否长按显示下载 设置为false
            .selectionMode(PictureConfig.SINGLE) // 单选
            .isPreviewImage(previewImage) //是否预览
            .isCamera(isCamera) //是否显示相机
            .circleDimmedLayer(circleDimmedLayer) //是否开启圆形裁剪
            .isDragFrame(true) ////是否可拖动裁剪框(固定)
            //                .cropImageWideHigh(100,100)// 裁剪宽高比，设置如果大于图片本身宽高则无效
            .isEnableCrop(enableCrop) //是否开启裁剪
            .setRequestedOrientation(1)
            .scaleEnabled(true) //裁剪是否可放大缩小图片
            .cutOutQuality(100) // 裁剪输出质量 默认100
            .freeStyleCropEnabled(true) //裁剪框是否可拖拽
            .showCropGrid(false) //是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .isPreviewEggs(previewEggs) //是否预览大小
    }

    fun getPictureSelector(activity: Activity?): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .isWeChatStyle(true) // 是否开启微信图片选择风格
            .maxSelectNum(1) //最大选择
            .minSelectNum(1) //最小选择
            .setRequestedOrientation(1)
            .isNotPreviewDownload(false) //是否长按显示下载 设置为false
            .selectionMode(PictureConfig.SINGLE) // 单选
            .isPreviewImage(true) //是否预览
            .isCamera(true) //是否显示相机
            .isEnableCrop(true) //是否开启裁剪
            .isPreviewEggs(true) //是否预览大小
    }

    fun  openCamera(
        activity: Activity?,
        enableCrop: Boolean,
        circleDimmedLayer: Boolean,
        compress: Boolean,
        listener: OnResultCallbackListener<LocalMedia>?
    ) {
        PictureSelector.create(activity)
            .openCamera(PictureMimeType.ofImage())
            .isEnableCrop(enableCrop) //是否裁剪
            .circleDimmedLayer(circleDimmedLayer) //是否开启圆形裁剪
            .isDragFrame(enableCrop) ////是否可拖动裁剪框(固定)
            .isCompress(compress) //是否压缩
            .imageEngine(GlideEngine.createGlideEngine())
            .forResult(listener)
    }

    fun openCamera(activity: Activity?): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openCamera(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
    }

    fun getPictureSelector(
        activity: Activity?,
        chooseMode: Int,
        selectionMode: Int
    ): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(chooseMode) // 0 全部 1 图片 2 视频
            .isCamera(false)
            .selectionMode(if (selectionMode == 0) PictureConfig.SINGLE else PictureConfig.MULTIPLE)
            .maxSelectNum(if (selectionMode == 0) 1 else selectionMode)
            .minSelectNum(1)
            .previewVideo(true)
    }
}