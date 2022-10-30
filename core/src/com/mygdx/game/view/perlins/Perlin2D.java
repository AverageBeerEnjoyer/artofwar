package com.mygdx.game.cur_project.perlins;

import java.util.Random;

public class Perlin2D {
    private Random random;

    public Perlin2D(double seed) {
        random = new Random((long)seed);
    }
    public void setRandom(double seed){
        this.random = new Random((long) seed);
    }

    public float getNoiseStandart(float x, float y) {
        int left = (int)x;
        int top = (int)y;

        float localX = x - left;
        float localY = y - top;

        // ��������� ����������� ������� ��� ���� ������ ��������:
        Vector topLeftGradient     = getPseudoRandomGradientVector(left,   top  );
        Vector topRightGradient    = getPseudoRandomGradientVector(left+1, top  );
        Vector bottomLeftGradient  = getPseudoRandomGradientVector(left,   top+1);
        Vector bottomRightGradient = getPseudoRandomGradientVector(left+1, top+1);

        // ������� �� ������ �������� �� ����� ������ ��������:
        Vector distanceToTopLeft     = new Vector(localX,   localY);
        Vector distanceToTopRight    = new Vector(localX-1, localY);
        Vector distanceToBottomLeft  = new Vector(localX,   localY-1);
        Vector distanceToBottomRight = new Vector(localX-1, localY-1);

        // ������� ��������� ������������ ����� �������� ����� ���������������
        float tx1 = dot(distanceToTopLeft,     topLeftGradient);
        float tx2 = dot(distanceToTopRight,    topRightGradient);
        float bx1 = dot(distanceToBottomLeft,  bottomLeftGradient);
        float bx2 = dot(distanceToBottomRight, bottomRightGradient);

        // ����������, ������������:
        float tx = lerp(tx1, tx2, localX);
        float bx = lerp(bx1, bx2, localX);
        float tb = lerp(tx, bx, localY);

        return tb;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private float dot(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }

    private Vector getPseudoRandomGradientVector(int x, int y) {
        // ������-��������� ����� �� 0 �� 3 ������� ������ ��������� ��� ������ x � y
        int v = random.nextInt(4);

        switch (v)
        {
            case 0:  return new Vector(1, 0);
            case 1:  return new Vector(-1, 0);
            case 2:  return new Vector(0, 1);
            default: return new Vector(0,-1);
        }
    }

    private static class Vector {
        float x;
        float y;
        Vector(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

    public float getNoiseUpdate(float fx, float fy, int octaves, float persistence) {
        float amplitude = 1;
        float max = 0;
        float result = 0;

        while (octaves-- > 0)
        {
            max += amplitude;
            result += getNoiseStandart(fx, fy) * amplitude;
            amplitude *= persistence;
            fx *= 2;
            fy *= 2;
        }
        return result/max;
    }
}