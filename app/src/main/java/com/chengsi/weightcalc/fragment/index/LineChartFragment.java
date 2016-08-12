package com.chengsi.weightcalc.fragment.index;


import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.XController;
import com.db.chart.view.animation.Animation;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.CheckResultBean;
import com.chengsi.weightcalc.utils.JDUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;

/**
 */
public class LineChartFragment extends android.support.v4.app.Fragment {

    private int position;
    private LineChartView lineChartView;
    private String[] dateArray;
    private TextView tvTitle;

    public LineChartFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null){
            this.position = getArguments().getInt("position", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_ovalation, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_line1);
        lineChartView = (LineChartView) view.findViewById(R.id.line_chat);
        produceChartView(lineChartView, tvTitle, position);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && lineChartView != null){
            produceChartView(lineChartView, tvTitle, position);
        }
    }

    private float[] generateChartValues(int position) {

        List<CheckResultBean> checkResultBeanList = new ArrayList<>();
        int max = 0;
        switch (position) {
            case 0:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getBigOvares() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 1:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getB() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getE2() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getP() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); ++i) {
                    if (TreatFragment.checkResultBeanList.get(i).getLh() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getFsh() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 6:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getPrl() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
            case 7:
                for (int i = 0; i < TreatFragment.checkResultBeanList.size(); i ++) {
                    if (TreatFragment.checkResultBeanList.get(i).getT() != null){
                        checkResultBeanList.add(TreatFragment.checkResultBeanList.get(i));
                    }
                }
                break;
        }
        dateArray = new String[checkResultBeanList.size()];
        if (!ListUtils.isEmpty(checkResultBeanList)) {
            XController.xPointArray = new int[checkResultBeanList.size()];
            XController.xPointArray[0] = 0;
            for (int i = 1; i < checkResultBeanList.size(); i++) {
                XController.xPointArray[i] = (int) ((checkResultBeanList.get(i).getCheckDate().getTime() - checkResultBeanList.get(0).getCheckDate().getTime()) / 24 / 3600 / 1000);
            }
            max = XController.xPointArray[XController.xPointArray.length - 1];
            float diff = max / (float) checkResultBeanList.size();
            int k = 0;
            for (int i = 0; i < checkResultBeanList.size(); i++) {
                if (k * diff * 3 <= XController.xPointArray[i]) {
                    k++;
                    dateArray[i] = JDUtils.formatDate(checkResultBeanList.get(i).getCheckDate(), "MM-dd");
                } else {
                    dateArray[i] = "";
                }
                if (i == checkResultBeanList.size() - 1 || i == 0) {
                    dateArray[i] = JDUtils.formatDate(checkResultBeanList.get(i).getCheckDate(), "MM-dd");
                }
            }
        } else {
            XController.xPointArray = null;
        }
        List<Float> list = new ArrayList<>();
        switch (position) {
            case 0:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    list.add((float)checkResultBeanList.get(i).getBigOvares());
                }
                break;
            case 1:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getB() != null){
                        list.add(checkResultBeanList.get(i).getB());
                    }
                }
                break;
            case 2:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getE2() != null){
                        list.add(checkResultBeanList.get(i).getE2());
                    }
                }
                break;
            case 3:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getP() != null){
                        list.add(checkResultBeanList.get(i).getP());
                    }
                }
                break;
            case 4:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getLh() != null){
                        list.add(checkResultBeanList.get(i).getLh());
                    }
                }
                break;
            case 5:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getFsh() != null){
                        list.add(checkResultBeanList.get(i).getFsh());
                    }
                }
                break;
            case 6:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getPrl() != null){
                        list.add(checkResultBeanList.get(i).getPrl());
                    }
                }
                break;
            case 7:
                for (int i = 0; i < checkResultBeanList.size(); ++i) {
                    if (checkResultBeanList.get(i).getT() != null){
                        list.add(checkResultBeanList.get(i).getT());
                    }
                }
                break;
        }
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i ++){
            array[i] = list.get(i);
        }
        return array;
    }

    private void produceChartView(LineChartView chart, TextView line1Label, int position) {

        float[] dataArray = generateChartValues(position);
        double max = 0; int step;
        for (float f : dataArray){
            if (max < f){
                max = Math.ceil(f);
            }
        }
        if (max <= 1){
            max = 1;
        }

        if(max < 5){
            step = 1;
        }else{
            step = (int) (max / 4);
        }

        LineSet dataset = new LineSet(dateArray, dataArray);
        dataset.setColor(getResources().getColor(R.color.color_theme)).setDotsColor(getResources().getColor(R.color.color_theme))
                .setDotsRadius(Tools.fromDpToPx(2)).setThickness(Tools.fromDpToPx(2));
        chart.addData(dataset);

//        dataset = new LineSet(dateArray, generateChartValues(position * 2 + 1));
//        dataset.setColor(getResources().getColor(R.color.color_theme)).setDotsColor(getResources().getColor(R.color.color_theme))
//                .setSmooth(true).setDotsRadius(Tools.fromDpToPx(2)).setThickness(Tools.fromDpToPx(2));
        if (position == 1){
            dataset.setFill(getResources().getColor(R.color.color_theme)).setThickness(1).setAlpha(0.7f);
        }
//        chart.addData(dataset);
        Tooltip tip = new Tooltip(getActivity(), R.layout.linechart_three_tooltip, R.id.value);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));

            tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f));
        }
        Paint gridPaint = new Paint();
        gridPaint.setAntiAlias(true);
        gridPaint.setColor(getResources().getColor(R.color.transparent));
        chart.setTooltips(tip);
        chart.setGrid(ChartView.GridType.VERTICAL, gridPaint)
                .setAxisBorderValues(0, (int) max, step)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#e08b36"))
                .setAxisColor(Color.parseColor("#e08b36"))
                .setXAxis(true)
                .setYAxis(true);
        if (dataArray.length > 1){
            Animation anim = new Animation().setStartPoint(-1, 1);
            chart.show(anim);
        }else{
            chart.show();
        }
        switch (position) {
            case 0:
                line1Label.setText("大卵泡个数");
                break;
            case 1:
                line1Label.setText("子宫内膜厚度");
                break;
            case 2:
                line1Label.setText("雌二醇");
                break;
            case 3:
                line1Label.setText("孕酮");
                break;
            case 4:
                line1Label.setText("黄体生成激素");
                break;
            case 5:
                line1Label.setText("卵泡生成激素");
                break;
            case 6:
                line1Label.setText("催乳激素");
                break;
            case 7:
                line1Label.setText("睾酮");
                break;
        }
    }
}
