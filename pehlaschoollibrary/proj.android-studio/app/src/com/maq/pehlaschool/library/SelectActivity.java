package com.maq.pehlaschool.library;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maq.kitkitlogger.KitKitLoggerActivity;

import kitkitschool.DownloadExpansionFile;

import static kitkitschool.DownloadExpansionFile.xAPKS;

/**
 * Created by ingtellect on 9/7/17.
 */

public class SelectActivity extends KitKitLoggerActivity {
    public static String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("ExpansionFile", MODE_PRIVATE);
        int defaultFileVersion = 0;

        // render text based on the calling application
        Intent libraryIntent = getIntent();
        Bundle extras = libraryIntent.getExtras();
        if (extras != null) {
            String intentValue = extras.getString("locale");
            if (intentValue != null) {
                locale = intentValue.toLowerCase();
                // clear the library intent by removing the extended data from the intent
                // this is done to get the latest extended data of the intent
                libraryIntent.removeExtra("locale");
                setIntent(libraryIntent);
            }
        } else {
            // set the default value of the variable on successive calls
            locale = "hindi";
        }

        // Retrieve the stored values of main and patch file version
        int storedMainFileVersion = sharedPref.getInt(getString(R.string.mainFileVersion), defaultFileVersion);
        int storedPatchFileVersion = sharedPref.getInt(getString(R.string.patchFileVersion), defaultFileVersion);
        boolean isExtractionRequired = isExpansionExtractionRequired(storedMainFileVersion, storedPatchFileVersion);

        if (storedMainFileVersion == 0 && storedPatchFileVersion == 0) {
            // Set main and patch file version to 0, if the extractions takes place for the first time
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.mainFileVersion), 0);
            editor.putInt(getString(R.string.patchFileVersion), 0);
            editor.commit();
            startSplashScreenActivity();
        } else if (isExtractionRequired) {
            // If main or patch file is updated, the extraction process needs to be performed again
            startSplashScreenActivity();
        }

        super.onCreate(savedInstanceState);

        String TAG = "SelectActivity";
        Log.d(TAG, "onCreate()");
        Util.hideSystemUI(this);

        setContentView(R.layout.activity_select);

        TextView titleName = findViewById(R.id.toolbar_title);
        TextView videoTabName = findViewById(R.id.video_textView);
        TextView booksTabName = findViewById(R.id.book_textView);

        switch (locale) {
            case "english":
                titleName.setText(getResources().getString(R.string.app_name));
                videoTabName.setText(getResources().getString(R.string.tab_video));
                booksTabName.setText(getResources().getString(R.string.tab_book));
                break;
            case "urdu":
                titleName.setText(getResources().getString(R.string.app_name_urdu));
                videoTabName.setText(getResources().getString(R.string.tab_video_urdu));
                booksTabName.setText(getResources().getString(R.string.tab_book_urdu));
                break;
            case "bengali":
                titleName.setText(getResources().getString(R.string.app_name_bengali));
                videoTabName.setText(getResources().getString(R.string.tab_video_bengali));
                booksTabName.setText(getResources().getString(R.string.tab_book_bengali));
                break;
            default: // Do nothing as Hindi text is set by default
                break;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.library_icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void startSplashScreenActivity() {
        Intent intent = new Intent(SelectActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isExpansionExtractionRequired(int storedMainFileVersion, int storedPatchFileVersion) {
        for (DownloadExpansionFile.XAPKFile xf : xAPKS) {
            // If main or patch file is updated set isExtractionRequired to true
            if (xf.mIsMain && xf.mFileVersion != storedMainFileVersion || !xf.mIsMain && xf.mFileVersion != storedPatchFileVersion) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            Util.hideSystemUI(this);
        }
    }

    public void onClickBook(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 1);
        startActivity(intent);
    }

    public void onClickVideo(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 0);
        startActivity(intent);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void showTutorialVideo(View v) {
        VideoDialogFragment fragment = new VideoDialogFragment();
        fragment.show(getFragmentManager(), "video");
    }


    static public class VideoDialogFragment extends DialogFragment {

        TutorialVideoPopupView popupView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.dialog_video, container, false);
            popupView = view.findViewById(R.id.video_view);

            String filename = "How to use the tablet.mp4";
            popupView.setVideo(filename);

            ImageButton closeBtn = view.findViewById(R.id.btn_close);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
            return view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Dialog dialog = new Dialog(getActivity(), R.style.DialogFullScreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


                    popupView.start();
                    Dialog dialogObj = (Dialog) dialogInterface;
                    dialogObj.getWindow().getDecorView().setSystemUiVisibility(
                            getActivity().getWindow().getDecorView().getSystemUiVisibility());
                    dialogObj.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                    MediaPlayer player = popupView.getPlayer();

                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!mediaPlayer.isPlaying()) {
                                dialog.dismiss();
                            }
                        }
                    });

                }
            });
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            return dialog;
        }

        @Override
        public void onResume() {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);

            super.onResume();
        }

    }

}