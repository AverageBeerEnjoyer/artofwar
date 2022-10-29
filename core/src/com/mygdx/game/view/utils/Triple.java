package com.mygdx.game.view.utils;

public final class Triple<T, U, O> {

    public Triple(T first, U second, O third) {
        this.second = second;
        this.first = first;
        this.third=third;
    }

    public final T first;
    public final U second;
    public final O third;

    // Because 'pair()' is shorter than 'new Pair<>()'.
    // Sometimes this difference might be very significant (especially in a
    // 80-ish characters boundary). Sorry diamond operator.
    public static <T, U, O> Triple<T, U, O> triple(T first, U second, O third) {
        return new Triple<>(first, second, third);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", "+third+")";
    }
}
