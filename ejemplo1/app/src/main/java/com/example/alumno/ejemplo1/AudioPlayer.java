package com.example.alumno.ejemplo1;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by Alexander on 30/12/2015.
 */
public class AudioPlayer implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener {

    private View view;
    private MediaPlayer player;
    private MediaController controler;

    public AudioPlayer(View view, final Runnable onExit){
        this.view = view;
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        controler = new MediaController(view.getContext()){
            @Override
            public void hide(){

            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event){
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    release();
                    onExit.run();
                }
                return super.dispatchKeyEvent(event);
            }
        };
    }

    public void setUri(){
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(view.getContext(), Settings.System.DEFAULT_RINGTONE_URI);
            player.prepare();
        }catch(Exception e){
        }
        player.start();
    }

    public void release(){
        if(player!=null){
            player.stop();
            player.release();
            player=null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controler.setMediaPlayer(this);
        controler.setAnchorView(view);
        controler.show(0);
    }

    @Override
    public void start() {
        player.start();

    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
