package edu.uaic.fii.wad.edec.fragment;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.camera.CameraPreview;
import edu.uaic.fii.wad.edec.service.scan.ProductInfo;
import net.sourceforge.zbar.*;

public class ScanProductFragment extends Fragment {

    private static Camera mCamera;
    private static CameraPreview mPreview;
    private static Handler autoFocusHandler;
    private static ImageScanner scanner;

    private static boolean previewing = true;

    public static boolean previewStopped = false;
    private static boolean tapped = false;

    public static PageFragmentListener scanPageListener;


    public ScanProductFragment(PageFragmentListener listener) {
        scanPageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.scan_product_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) getActivity().findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        ImageView scanButton = (ImageView) getActivity().findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!tapped) {
                    tapped = true;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    public static Camera getCameraInstance(){
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return camera;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    private static Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    private Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {

            if (tapped) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);

                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    previewStopped = true;

                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        Toast.makeText(getActivity().getApplicationContext(),
                               "Scan Result: " + sym.getData(), Toast.LENGTH_LONG).show();

                        new ProductInfo(sym.getData()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        tapped = false;
                    }
                }
            }
        }
    };

    private static Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 100);
        }
    };

    public static void stop() {
        if (!previewStopped) {
            mCamera.stopPreview();
            previewStopped = true;
        }
    }

    public static void start() {
        if (previewStopped) {
            mCamera.startPreview();
            new Runnable() {
                public void run() {
                    if (previewing)
                        mCamera.autoFocus(autoFocusCB);
                }
            };
            previewStopped = false;
        }
    }
}