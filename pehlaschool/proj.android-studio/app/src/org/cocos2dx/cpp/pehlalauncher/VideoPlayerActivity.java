package org.cocos2dx.cpp.pehlalauncher;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.maq.kitkitlogger.KitKitLoggerActivity;
import com.maq.pehlaschool.R;

/**
 * Created by yshong on 2018. 4. 13..
 */

public class VideoPlayerActivity extends KitKitLoggerActivity implements SurfaceHolder.Callback {
    public String video = "";
    ImageButton mVideoCloseButton;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mVideoView;
    private int mCurrentPosition;
    private boolean mbPause;
    private String mVideoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Util.hideSystemUI(this);
        setContentView(R.layout.activity_video);

        mVideoView = findViewById(R.id.videoSurface);
        mVideoCloseButton = findViewById(R.id.videoCloseButton);

        mVideoCloseButton.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            video = bundle.getString("video", "");
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mVideoCloseButton.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return false;
            }
        });

        mCurrentPosition = 0;
        mbPause = false;

        mVideoView.getHolder().addCallback(this);

        if (video.equals("writing_board")) {
            if (appLanguage == null || !appLanguage.equals("sw-TZ")) {
                mVideoFileName = "tutorial_writingboard_en.mp4";
            }
        } else {
            if (appLanguage == null || !appLanguage.equals("sw-TZ")) {
                mVideoFileName = "en_vdo_tut_welcome.m4v";
            }
        }

        playVideo(mVideoFileName);


        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (video.equals("writing_board")) {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        mVideoCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoCloseButton.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mbPause = true;
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            if (mCurrentPosition > 10) {
                mCurrentPosition -= 10;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mbPause == true) {
            playVideo(mVideoFileName);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Util.hideSystemUI(this);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    private void playVideo(String fileName) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            AssetFileDescriptor fd = getAssets().openFd(fileName);
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mMediaPlayer.prepare();

            if (mbPause) {
                mMediaPlayer.seekTo(mCurrentPosition);
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Adjust the size of the video
                    // so it fits on the screen
                    int videoWidth = mMediaPlayer.getVideoWidth();
                    int videoHeight = mMediaPlayer.getVideoHeight();
                    float videoProportion = (float) videoWidth / (float) videoHeight;
                    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
                    float screenProportion = (float) screenWidth / (float) screenHeight;
                    android.view.ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();

                    if (videoProportion > screenProportion) {
                        lp.width = screenWidth;
                        lp.height = (int) ((float) screenWidth / videoProportion);
                    } else {
                        lp.width = (int) (videoProportion * (float) screenHeight);
                        lp.height = screenHeight;
                    }
                    mVideoView.setLayoutParams(lp);

                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    }
                }
            });


        } catch (Exception e) {
            Log.e("myLog", "" + e);
            e.printStackTrace();
            finish();
        }
    }
}
