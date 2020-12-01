package com.md.dzbp.utils.hcUtils;

public interface PlayerCallback {
    void onPlayStart();

    void onPlayPause();

    void onPlayStop();

    void onPlaying(int progress);

    void onSeekComplete();
}
