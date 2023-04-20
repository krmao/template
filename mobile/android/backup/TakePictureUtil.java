package com.xxx.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.task.Priority;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TakePictureUtil {
    private Context context;
    private ProgressDialog progressDialog;
    private AlertDialog uploadCompletelyDialog;
    private Handler handle;
    private Callback.Cancelable uploadCancelable;
    public static final int REQUEST_CODE_TAKE_PICTURE = 100;
    private final boolean PROGRESS_STYLE_HORIZONTAL = true;
    private String takePictureOrderId, takePictureOrderClothesId;

    @SuppressLint("HandlerLeak")
    public TakePictureUtil(Context context) {
        if (context != null) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMax(100); // Progress Dialog Max Value
            progressDialog.setTitle("图片上传提示"); // Setting Title
            progressDialog.setMessage("上传中..."); // Setting Message
            progressDialog.setProgressStyle(PROGRESS_STYLE_HORIZONTAL ? ProgressDialog.STYLE_HORIZONTAL : ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if (PROGRESS_STYLE_HORIZONTAL) {
                handle = new Handler() {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (progressDialog != null) {
                            progressDialog.setProgress(msg.what);
                            progressDialog.setSecondaryProgress(msg.what);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                };
            }
        }
    }

    // for fragment
    public void openTakePicture(android.support.v4.app.Fragment fragment, String takePictureOrderId, String takePictureOrderClothesId) {
        if (!(context != null && (context instanceof Activity && !((Activity) context).isFinishing()))) {
            return;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            showUploadCompletelyDialog(null, "未检测到相机, 上传失败");
            return;
        }
        boolean isCameraParamsCorrect = true;
        Gson gson = new Gson();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CameraManager manager = null;
            try {
                manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (manager != null) {
                String[] cameraIds;
                try {
                    cameraIds = manager.getCameraIdList();
                } catch (Exception e) {
                    e.printStackTrace();
                    cameraIds = null;
                }
                if (cameraIds == null || cameraIds.length <= 0) {
                    showUploadCompletelyDialog(null, "未检测到有效相机, 上传失败\n\n(注意:连接/断开相机需重启应用)");
                    return;
                }
                try {
                    for (String cameraId : cameraIds) {
                        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                        // show all info
                        // for (CameraCharacteristics.Key<?> key :  characteristics.getKeys()) Log.i("TakePictureUtil", "cameraId=" + cameraId + ", " + key.getName() + "='" + gson.toJson(characteristics.get(key)) + "'");

                        String SENSOR_INFO_PIXEL_ARRAY_SIZE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE));
                        //noinspection ConstantConditions
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_PIXEL_ARRAY_SIZE.equalsIgnoreCase("{\"mHeight\":480,\"mWidth\":640}"); // {"mHeight":5472,"mWidth":7296}  像素阵列大小
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_PIXEL_ARRAY_SIZE='" + SENSOR_INFO_PIXEL_ARRAY_SIZE + "'");

                        String SENSOR_INFO_ACTIVE_ARRAY_SIZE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_ACTIVE_ARRAY_SIZE.equalsIgnoreCase("{\"bottom\":480,\"left\":0,\"right\":640,\"top\":0}"); // {"bottom":5472,"left":0,"right":7296,"top":0}
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_ACTIVE_ARRAY_SIZE='" + SENSOR_INFO_ACTIVE_ARRAY_SIZE + "'");

                        String SENSOR_INFO_COLOR_FILTER_ARRANGEMENT = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_COLOR_FILTER_ARRANGEMENT.equalsIgnoreCase("null");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_COLOR_FILTER_ARRANGEMENT='" + SENSOR_INFO_COLOR_FILTER_ARRANGEMENT + "'");

                        String SENSOR_INFO_EXPOSURE_TIME_RANGE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_EXPOSURE_TIME_RANGE.equalsIgnoreCase("null"); // {"mLower":10000,"mUpper":1000000000}
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_EXPOSURE_TIME_RANGE='" + SENSOR_INFO_EXPOSURE_TIME_RANGE + "'");

                        String SENSOR_INFO_MAX_FRAME_DURATION = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_MAX_FRAME_DURATION.equalsIgnoreCase("221811200"); // 9000000000
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_MAX_FRAME_DURATION='" + SENSOR_INFO_MAX_FRAME_DURATION + "'");

                        String SENSOR_INFO_PHYSICAL_SIZE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_PHYSICAL_SIZE.equalsIgnoreCase("{\"mHeight\":6.1242065,\"mWidth\":6.1242065}"); // 9000000000
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_PHYSICAL_SIZE='" + SENSOR_INFO_PHYSICAL_SIZE + "'");

                        String SENSOR_INFO_SENSITIVITY_RANGE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_SENSITIVITY_RANGE.equalsIgnoreCase("null"); // {"mLower":50,"mUpper":3500}
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_SENSITIVITY_RANGE='" + SENSOR_INFO_SENSITIVITY_RANGE + "'");

                        String SENSOR_INFO_TIMESTAMP_SOURCE = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_TIMESTAMP_SOURCE.equalsIgnoreCase("0");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_TIMESTAMP_SOURCE='" + SENSOR_INFO_TIMESTAMP_SOURCE + "'");

                        String SENSOR_INFO_WHITE_LEVEL = gson.toJson(characteristics.get(CameraCharacteristics.SENSOR_INFO_WHITE_LEVEL));
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SENSOR_INFO_WHITE_LEVEL.equalsIgnoreCase("null");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SENSOR_INFO_WHITE_LEVEL='" + SENSOR_INFO_WHITE_LEVEL + "'");

                        String LENS_INFO_AVAILABLE_APERTURES = gson.toJson(characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)); // 孔径
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = LENS_INFO_AVAILABLE_APERTURES.equalsIgnoreCase("null");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", LENS_INFO_AVAILABLE_APERTURES='" + LENS_INFO_AVAILABLE_APERTURES + "'");

                        String LENS_INFO_AVAILABLE_FOCAL_LENGTHS = gson.toJson(characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)); // 焦距
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = LENS_INFO_AVAILABLE_FOCAL_LENGTHS.equalsIgnoreCase("[35.0]");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", LENS_INFO_AVAILABLE_FOCAL_LENGTHS='" + LENS_INFO_AVAILABLE_FOCAL_LENGTHS + "'");

                        String SCALER_AVAILABLE_MAX_DIGITAL_ZOOM = gson.toJson(characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)); // 最大数字缩放
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = SCALER_AVAILABLE_MAX_DIGITAL_ZOOM.equalsIgnoreCase("3.0");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", SCALER_AVAILABLE_MAX_DIGITAL_ZOOM='" + SCALER_AVAILABLE_MAX_DIGITAL_ZOOM + "'");

                        String REQUEST_AVAILABLE_CAPABILITIES = gson.toJson(characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)); // 相机功能
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = REQUEST_AVAILABLE_CAPABILITIES.equalsIgnoreCase("[0]");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", REQUEST_AVAILABLE_CAPABILITIES='" + REQUEST_AVAILABLE_CAPABILITIES + "'");

                        String CONTROL_AVAILABLE_SCENE_MODES = gson.toJson(characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES)); // 场景模式
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = CONTROL_AVAILABLE_SCENE_MODES.equalsIgnoreCase("[0]");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", CONTROL_AVAILABLE_SCENE_MODES='" + CONTROL_AVAILABLE_SCENE_MODES + "'");

                        String STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES = gson.toJson(characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)); // 人脸检测模式
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES.equalsIgnoreCase("[0]");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES='" + STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES + "'");

                        String HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES = gson.toJson(characteristics.get(CameraCharacteristics.HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES)); // 热像素模式
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES.equalsIgnoreCase("null");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES='" + HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES + "'");

                        String FLASH_INFO_AVAILABLE = gson.toJson(characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)); //  闪光灯
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = FLASH_INFO_AVAILABLE.equalsIgnoreCase("false");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", FLASH_INFO_AVAILABLE='" + FLASH_INFO_AVAILABLE + "'");

                        String COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES = gson.toJson(characteristics.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES)); // 像差模式(关/快速/高质量)
                        if (isCameraParamsCorrect)
                            isCameraParamsCorrect = COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES.equalsIgnoreCase("[1]");
                        Log.d("TakePictureUtil", "cameraId=" + cameraId + ", COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES='" + COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES + "'");

                        if (!isCameraParamsCorrect) break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //noinspection UnusedAssignment
                    isCameraParamsCorrect = false;
                    showUploadCompletelyDialog(null, "相机访问错误, 上传失败\n\n(注意:连接/断开相机需重启应用)");
                    return;
                }
            } else {
                //noinspection UnusedAssignment
                isCameraParamsCorrect = false;
                showUploadCompletelyDialog(null, "设备获取错误, 上传失败");
                return;
            }
        } else {
            //noinspection UnusedAssignment
            isCameraParamsCorrect = false;
            showUploadCompletelyDialog(null, "系统版本低于5, 上传失败");
            return;
        }

        if (!isCameraParamsCorrect) {
            showUploadCompletelyDialog(null, "不支持相机型号, 上传失败");
            return;
        }

        reset();
        // open camera
        this.takePictureOrderId = takePictureOrderId;
        this.takePictureOrderClothesId = takePictureOrderClothesId;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPictureFile()));
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } else {
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        }
    }

    // for activity
    public void openTakePicture(String takePictureOrderId, String takePictureOrderClothesId) {
        openTakePicture(null, takePictureOrderId, takePictureOrderClothesId);
    }

    @NonNull
    private File getPictureFile() {
        return new File(WasherApp.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/test.jpg");
    }

    private void removePictureFile() {
        File pictureFile = getPictureFile();
        if (pictureFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pictureFile.delete();
        }
    }

    @SuppressWarnings("unused")
    public void onActivityResultForTakePicture(int requestCode, int resultCode, Intent data) {
        if (!(context != null && (context instanceof Activity && !((Activity) context).isFinishing()))) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            uploadPicture(getPictureFile());
        }
    }

    private void reset() {
        if (uploadCancelable != null) uploadCancelable.cancel();
        if (handle != null) handle.removeCallbacks(null);
        dismissProgressDialog();
        dismissUploadCompletelyDialog();
        removePictureFile();
        takePictureOrderId = null;
        takePictureOrderClothesId = null;
    }

    public void onDestroy() {
        reset();
        this.uploadCancelable = null;
        this.handle = null;
        this.context = null;
        this.progressDialog = null;
        this.uploadCompletelyDialog = null;
    }

    private void updateProgressDialogProgress(int progress) {
        if (PROGRESS_STYLE_HORIZONTAL && handle != null && context != null && (context instanceof Activity && !((Activity) context).isFinishing())) {
            Message message = new Message();
            message.what = progress;
            handle.sendMessage(message);
        }
    }

    /**
     * @param doCompress false 跳过压缩, true 执行压缩
     */
    @SuppressWarnings("SameParameterValue")
    private byte[] compressPicture(File pictureFile, float scale, boolean doCompress) {
        if (pictureFile == null || !pictureFile.exists()) return null;

        byte[] bytes = null;
        Bitmap fullBitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
        Bitmap scaledBitmap = null;
        ByteArrayOutputStream baos = null;
        try {
            scaledBitmap = !doCompress ? fullBitmap : Bitmap.createScaledBitmap(fullBitmap, (int) (fullBitmap.getWidth() * scale), (int) (fullBitmap.getHeight() * scale), true);
            baos = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scaledBitmap != null) scaledBitmap.recycle();
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytes;
    }

    private void uploadPicture(File pictureFile) {
        byte[] pictureByteArray = compressPicture(pictureFile, 0.5f, false);
        if (pictureFile == null || !pictureFile.exists() || pictureByteArray == null || pictureByteArray.length <= 0) {
            showUploadCompletelyDialog(false, "未检测到有效图片, 上传失败");
            return;
        }

        RequestParams params = new RequestParams(Constants.baseUrl + Constants.uploadOrderClothesPicture);
        params.setConnectTimeout(1000 * 15);
        params.setMaxRetryCount(3);
        params.setPriority(Priority.BG_TOP);
        params.setMultipart(true);
        params.addBodyParameter("orderId", takePictureOrderId);
        params.addBodyParameter("orderClothesId", takePictureOrderClothesId);
        params.addBodyParameter("picture", pictureByteArray, null);
        if (uploadCancelable != null) uploadCancelable.cancel();
        uploadCancelable = x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                dismissProgressDialog();
                showUploadCompletelyDialog(null);
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                dismissProgressDialog();
                showUploadCompletelyDialog(false);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(File arg0) {
                dismissProgressDialog();
                showUploadCompletelyDialog(true);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                int progress = (int) (current / (float) total * 100.0f);
                Log.w("TakePictureUtil", "onLoading total=" + total + ", current=" + current + ", isDownloading=" + isDownloading + ", progress=" + progress);
                updateProgressDialogProgress(progress);
            }

            @Override
            public void onStarted() {
                showProgressDialog();
            }

            @Override
            public void onWaiting() {
            }
        });
    }

    private void showProgressDialog() {
        dismissProgressDialog();
        if (progressDialog != null) {
            progressDialog.show();// Display Progress Dialog
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    private void showUploadCompletelyDialog(Boolean uploadSuccess) {
        showUploadCompletelyDialog(uploadSuccess, null);
    }

    private void showUploadCompletelyDialog(Boolean uploadSuccess, String message) {
        dismissProgressDialog();
        dismissUploadCompletelyDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("图片上传提示").setMessage(TextUtils.isEmpty(message) ? (uploadSuccess == null ? "上传已取消" : (uploadSuccess ? "上传成功" : "上传失败")) : message).setNegativeButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //BUTTON_POSITIVE 好的 继续拍照上传
        //BUTTON_NEGATIVE 好的
        if (uploadSuccess != null && uploadSuccess) {
            builder.setPositiveButton("继续拍照上传", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openTakePicture(takePictureOrderId, takePictureOrderClothesId);
                }
            });
        }

        uploadCompletelyDialog = builder.show();
    }

    private void dismissUploadCompletelyDialog() {
        if (uploadCompletelyDialog != null) uploadCompletelyDialog.dismiss();
    }

}
