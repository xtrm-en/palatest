package me.xtrm.paladium.palatest.client.ui.animation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Animations implements IAnimation {
    LINEAR (percentage -> percentage),
    IN_QUAD (percentage -> (float) Math.pow(percentage, 2)),
    OUT_QUAD (percentage -> -percentage * (percentage - 2)),

    OUT_QUINT (
        percentage -> (float) (Math.pow(percentage - 1, 5) + 1)
    ),
    OUT_EXP(
        percentage -> (float) (-(Math.pow(2f, -10 * percentage)) + 1)
    );

    private final IAnimation underlying;

    @Override
    public float getValue(float percentage) {
        return underlying.getValue(percentage);
    }
}
