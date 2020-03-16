package com.xky.emptyproject.widget;

public class BodyPointInfo {
    public String txtValue;
    public String pointX;
    public String pointY;

    public BodyPointInfo(Builder builder) {
        this.txtValue = builder.txtValue;
        this.pointX = builder.pointX;
        this.pointY = builder.pointY;
    }

    public static class Builder {
        private String txtValue;
        private String pointX;
        private String pointY;

        public Builder setTxtValue(String txtValue) {
            this.txtValue = txtValue;
            return this;
        }

        public Builder setPointX(String pointX) {
            this.pointX = pointX;
            return this;
        }

        public Builder setPointY(String pointY) {
            this.pointY = pointY;
            return this;
        }

        public BodyPointInfo build() {
            return new BodyPointInfo(this);
        }
    }
}
