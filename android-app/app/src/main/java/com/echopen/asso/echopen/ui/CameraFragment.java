/*
 * 
 */
package com.echopen.asso.echopen.ui;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.echopen.asso.echopen.MainActivity;

import java.io.IOException;
import java.util.List;

public class CameraFragment extends Fragment
{

	/** The  preview. */
	private Preview mPreview;
	
	/** The camera. */
	Camera mCamera;
	
	/** The number of cameras available in device. */
	int mNumberOfCameras;
	
	/** The id of current camera. */
	int mCurrentCamera; // Camera ID currently chosen
	
	/** The id of camera currently locked. */
	int mCameraCurrentlyLocked; // Camera ID that's actually acquired

	/** The default camera id. */
	int mDefaultCameraId;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Create a container that will hold a SurfaceView for camera previews
		mPreview = new Preview(getActivity());

		// Find the total number of cameras available
		mNumberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the rear-facing ("default") camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < mNumberOfCameras; i++)
		{
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK)
			{
				mCurrentCamera = mDefaultCameraId = i;
			}
		}
		setHasOptionsMenu(mNumberOfCameras > 1);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		mPreview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				((MainActivity) getActivity()).gesture.onTouchEvent(event);
				return false;
			}
		});
		return mPreview;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume()
	{
		super.onResume();

		// Use mCurrentCamera to select the camera desired to safely restore
		// the fragment after the camera has been changed
		mCamera = Camera.open(mCurrentCamera);
		mCameraCurrentlyLocked = mCurrentCamera;
		mPreview.setCamera(mCamera);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause()
	{
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null)
		{
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * A simple wrapper around a Camera and a SurfaceView that renders a
	 * centered preview of the Camera to the surface. We need to center the
	 * SurfaceView because not all devices have cameras that support preview
	 * sizes at the same aspect ratio as the device's display.
	 */
	class Preview extends ViewGroup implements SurfaceHolder.Callback
	{
		
		/** The tag. */
		private final String TAG = "Preview";

		/** The m surface view. */
		SurfaceView mSurfaceView;
		
		/** The m holder. */
		SurfaceHolder mHolder;
		
		/** The m preview size. */
		Size mPreviewSize;
		
		/** The m supported preview sizes. */
		List<Size> mSupportedPreviewSizes;
		
		/** The m camera. */
		Camera mCamera;
		
		/** The m surface created. */
		boolean mSurfaceCreated = false;

		/**
		 * Instantiates a new preview.
		 *
		 * @param context the context
		 */
		Preview(Context context)
		{
			super(context);

			mSurfaceView = new SurfaceView(context);
			addView(mSurfaceView);

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = mSurfaceView.getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		/**
		 * Sets the camera.
		 *
		 * @param camera the new camera
		 */
		public void setCamera(Camera camera)
		{
			mCamera = camera;
			if (mCamera != null)
			{
				mSupportedPreviewSizes = mCamera.getParameters()
						.getSupportedPreviewSizes();
				if (mSurfaceCreated)
					requestLayout();
			}
		}

		/* (non-Javadoc)
		 * @see android.view.View#onMeasure(int, int)
		 */
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			// We purposely disregard child measurements because act as a
			// wrapper to a SurfaceView that centers the camera preview instead
			// of stretching it.
			final int width = resolveSize(getSuggestedMinimumWidth(),
					widthMeasureSpec);
			final int height = resolveSize(getSuggestedMinimumHeight(),
					heightMeasureSpec);
			setMeasuredDimension(width, height);

			if (mSupportedPreviewSizes != null)
			{
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
						width, height);
			}

			if (mCamera != null)
			{
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setPreviewSize(mPreviewSize.width,
						mPreviewSize.height);

				mCamera.setParameters(parameters);
			}
		}

		/* (non-Javadoc)
		 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
		 */
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b)
		{
			if (getChildCount() > 0)
			{
				final View child = getChildAt(0);

				final int width = r - l;
				final int height = b - t;

				int previewWidth = width;
				int previewHeight = height;
				if (mPreviewSize != null)
				{
					previewWidth = mPreviewSize.height;
					previewHeight = mPreviewSize.width;
				}

				// Center the child SurfaceView within the parent.
				if (width * previewHeight > height * previewWidth)
				{
					final int scaledChildWidth = previewWidth * height
							/ previewHeight;
					child.layout((width - scaledChildWidth) / 2, 0,
							(width + scaledChildWidth) / 2, height);
				}
				else
				{
					final int scaledChildHeight = previewHeight * width
							/ previewWidth;
					child.layout(0, (height - scaledChildHeight) / 2, width,
							(height + scaledChildHeight) / 2);
				}
			}
		}

		/* (non-Javadoc)
		 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			// The Surface has been created, acquire the camera and tell it
			// where
			// to draw.
			try
			{
				if (mCamera != null)
				{
					mCamera.setPreviewDisplay(holder);
				}
			} catch (IOException exception)
			{
				Log.e(TAG, "IOException caused by setPreviewDisplay()",
						exception);
			}
			if (mPreviewSize == null)
				requestLayout();
			mSurfaceCreated = true;
		}

		/* (non-Javadoc)
		 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			// Surface will be destroyed when we return, so stop the preview.
			if (mCamera != null)
			{
				mCamera.stopPreview();
			}
		}

		/**
		 * Gets the optimal preview size.
		 *
		 * @param sizes the sizes
		 * @param w the w
		 * @param h the h
		 * @return the optimal preview size
		 */
		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h)
		{
			final double ASPECT_TOLERANCE = 0.1;
			double targetRatio = (double) h / w;

			if (sizes == null)
				return null;

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = h;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes)
			{
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff)
				{
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the
			// requirement
			if (optimalSize == null)
			{
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes)
				{
					if (Math.abs(size.height - targetHeight) < minDiff)
					{
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}
			return optimalSize;
		}

		/* (non-Javadoc)
		 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h)
		{
			// Now that the size is known, set up the camera parameters and
			// begin
			// the preview.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			// parameters.setRotation(90);
			mCamera.setDisplayOrientation(90);
			requestLayout();

			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

	}
}
