package org.tensorflow.lite.examples.poseestimation.database.User;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
        @PrimaryKey
        private int temp;

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
        private String name;

        private int age;
        private double exp;    // Level로 환산하는 함수 필요

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getExp() {
            return exp;
        }

        public void setExp(double exp) {
            this.exp = exp;
        }

        //신체 정보
        private double weight;
        private double height;
        public double BMI;

        public void bmiCalc(){
            this.BMI = this.weight/(this.height/100);
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        //운동 커리큘럼 진행 상황
        private int sitUp;
        private int pushUp;
        private int squat;

        public int getSitUp() {
            return sitUp;
        }

        public void setSitUp(int sitUp) {
            this.sitUp = sitUp;
        }

        public int getPushUp() {
            return pushUp;
        }

        public void setPushUp(int pushUp) {
            this.pushUp = pushUp;
        }

        public int getSquat() {
            return squat;
        }

        public void setSquat(int squat) {
            this.squat = squat;
        }

}



