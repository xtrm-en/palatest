package me.xtrm.paladium.palatest.client.ui.animation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimationTrack {
    private static final long DELAY = 1000L / 60L;
    private final Timer timer = new Timer();

    private final IAnimation animationStrategy;
    private final int totalFrames;
    private final int totalDelayFrames;

    private int frame;
    private int delayFrame;

    public AnimationTrack(IAnimation animationStrategy, int totalFrames, int totalDelayFrames) {
        this.animationStrategy = animationStrategy;
        this.totalFrames = totalFrames;
        this.totalDelayFrames = totalDelayFrames;
    }

    public void update() {
        update(false);
    }

    public void update(boolean reverse) {
        if (!timer.hasReached(DELAY)) return;
        timer.reset();

        if (reverse) {
            if (delayFrame > 0) delayFrame--;
            else if (frame > 0) frame--;
        } else {
            if (delayFrame < totalDelayFrames) delayFrame++;
            else if (frame < totalFrames) frame++;
        }
    }

    public float getPercentage() {
        return ((float) frame / (float) totalFrames);
    }

    public double getValue() {
        return this.getValue(false);
    }

    public double getValue(boolean reverse) {
        float val = animationStrategy.getValue(getPercentage());
        val *= 100;
        val = Math.round(val);
        return reverse ? 1F - (val / 100F) : (val / 100F);
    }

    public void skipDelay() {
        this.delayFrame = this.totalDelayFrames;
    }

    public boolean isFinished() {
        return frame == totalFrames;
    }

    private static class Timer {
        private long lastMS = 0L;

        public long getCurrentMS() {
            return System.nanoTime() / 1000000L;
        }

        public long getDiff() {
            return getCurrentMS() - lastMS;
        }

        public void setLastMS(long lastMS) {
            this.lastMS = System.currentTimeMillis();
        }

        public int convertToMS(int perSecond) {
            return 1000 / perSecond;
        }

        public boolean hasReached(long milliseconds) {
            return getDiff() >= milliseconds;
        }

        public void reset() {
            lastMS = getCurrentMS();
        }

        public void reset(long offset) {
            lastMS = getCurrentMS() - offset;
        }
    }
}
