package com.xky.emptyproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xky.emptyproject.widget.BodyDataIndexInfo;
import com.xky.emptyproject.widget.BodyPointInfo;
import com.xky.emptyproject.widget.MyLineView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyLineView myLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLineView.setNewData(getInfo());
            }
        });
        myLineView = findViewById(R.id.my_line_view);
//        try {
//            Thread.sleep(5000);
//            myLineView.setNewData(getInfo());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private BodyDataIndexInfo getInfo() {
        BodyDataIndexInfo info = new BodyDataIndexInfo();
        info.leftY = getLeftY();
        info.bottomX = getBottomX();
        info.pointList = getPointList();
        return info;
    }

    private List<String> getLeftY() {
        return Arrays.asList("40",
                "60",
                "80",
                "100",
                "120",
                "140"
        );
    }

    private List<String> getBottomX() {
        return Arrays.asList("02-11",
                "02-12",
                "02-13",
                "02-14",
                "02-15",
                "02-16"

//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16",
//
//                "02-11",
//                "02-12",
//                "02-13",
//                "02-14",
//                "02-15",
//                "02-16"
        );
    }

    private List<BodyPointInfo> getPointList() {
        List<BodyPointInfo> result = new ArrayList<>();
        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());

//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
//
//        result.add(new BodyPointInfo.Builder().setPointY("50").setTxtValue("02-11").build());
//        result.add(new BodyPointInfo.Builder().setPointY("40").setTxtValue("02-12").build());
//        result.add(new BodyPointInfo.Builder().setPointY("90").setTxtValue("02-13").build());
//        result.add(new BodyPointInfo.Builder().setPointY("80").setTxtValue("02-14").build());
//        result.add(new BodyPointInfo.Builder().setPointY("75").setTxtValue("02-15").build());
//        result.add(new BodyPointInfo.Builder().setPointY("60").setTxtValue("02-16").build());
        return result;
    }
}
