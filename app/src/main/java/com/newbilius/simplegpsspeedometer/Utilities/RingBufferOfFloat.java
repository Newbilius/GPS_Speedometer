package com.newbilius.simplegpsspeedometer.Utilities;

public class RingBufferOfFloat {
    private float[] array;
    private int maxSize;
    private int writePointer = -1;
    private int currentSize = 0;

    public RingBufferOfFloat(int maxSize) {
        array = new float[maxSize];
        this.maxSize = maxSize;
    }

    public void add(float value) {
        synchronized (this) {
            currentSize++;
            if (currentSize > maxSize)
                currentSize = maxSize;

            writePointer++;
            if (writePointer == maxSize)
                writePointer = 0;
            array[writePointer] = value;
        }
    }

    public float[] getArray() {
        synchronized (this) {
            if (currentSize == maxSize)
                return array.clone();
            else {
                float[] newArray = new float[currentSize];
                System.arraycopy(array, 0, newArray, 0, currentSize);
                return newArray;
            }
        }
    }

    public void clear() {
        synchronized (this) {
            writePointer = -1;
            currentSize = 0;
            array = new float[maxSize];
        }
    }
}