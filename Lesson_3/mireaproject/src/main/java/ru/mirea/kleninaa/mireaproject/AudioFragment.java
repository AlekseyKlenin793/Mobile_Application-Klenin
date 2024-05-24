package ru.mirea.kleninaa.mireaproject;

import android.media.MediaPlayer;
import android.Manifest;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ru.mirea.kleninaa.mireaproject.databinding.FragmentAudioBinding;

public class AudioFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String fileName = null;

    private Button startRecordButton;
    private Button stopRecordButton;
    private Button playButton;
    private Button stopButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio, container, false);

        startRecordButton = root.findViewById(R.id.start_record_button);
        stopRecordButton = root.findViewById(R.id.stop_record_button);
        playButton = root.findViewById(R.id.start_play_button);
        stopButton = root.findViewById(R.id.stop_play_button);

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        startRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });

        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
            }
        });

        fileName = getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audio_record.3gp";

        return root;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AudioFragment", "prepare() failed");
        }

        recorder.start();
        Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(getActivity(), "Recording stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioFragment", "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(getActivity(), "Audio stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlaying();
        stopRecording();
    }
}