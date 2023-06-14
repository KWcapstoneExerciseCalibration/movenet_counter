package org.tensorflow.lite.examples.poseestimation.database.calendarDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Calender {             //달력(날짜) 관련
        @PrimaryKey
        private int date;

        private String note;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        //당일 진행한 커리큘럼 정보
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

