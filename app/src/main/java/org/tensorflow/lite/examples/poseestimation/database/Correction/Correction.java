package org.tensorflow.lite.examples.poseestimation.database.Correction;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Correction {
    @PrimaryKey
    private int temp;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    static class SitUp{
        class Head{
            private double x;
            private double y;
            private double z;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getZ() {
                return z;
            }

            public void setZ(double z) {
                this.z = z;
            }
        }

        class LeftArm{
            private double x;
            private double y;
            private double z;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public double getZ() {
                return z;
            }

            public void setZ(double z) {
                this.z = z;
            }
        }
    }

    static class PushUp{

    }

    static class Squat{

    }
}

