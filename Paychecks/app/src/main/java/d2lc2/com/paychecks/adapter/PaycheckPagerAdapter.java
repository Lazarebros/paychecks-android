package d2lc2.com.paychecks.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.d2l2c.paycheck.util.bean.PaycheckDetail;
import com.d2l2c.paycheck.util.bean.PaycheckSummary;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import d2lc2.com.paychecks.R;

public class PaycheckPagerAdapter extends PagerAdapter {

//    protected String[] mMonths = new String[] {"Jan", "Fev", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    List<String> mMonths = Arrays.asList("Jan", "Fev", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

    private LayoutInflater layoutInflater;

    private Context context;
    private List<PaycheckSummary> paycheckSummaryList;

    //int[] colors = {R.color.colorAccent, R.color.green, R.color.yellow};

    public PaycheckPagerAdapter(Context context, List<PaycheckSummary> paycheckSummaryList)
    {
        this.context = context;
        this.paycheckSummaryList = paycheckSummaryList;
        Collections.sort(this.paycheckSummaryList, Collections.<PaycheckSummary>reverseOrder());
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paycheckSummaryList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.sumary_view, container, false);
        initView(view, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void initView(View view, int position) {
        PaycheckSummary paycheckSummary = paycheckSummaryList.get(position);

        ProgressBar progressBar = view.findViewById(R.id.year_progress);
        progressBar.setProgress(paycheckSummary.getYearProgress());

        TextView textViewProgressYear = view.findViewById(R.id.year_progress_text);
        textViewProgressYear.setText(paycheckSummary.getYearProgress() + "%");

        TextView textViewYear = view.findViewById(R.id.paycheck_year);
        textViewYear.setText("" + paycheckSummary.getYear());

        TextView textViewGross = view.findViewById(R.id.gross_amount);
        textViewGross.setText("" + paycheckSummary.getGrossAmount());

        TextView textViewNet = view.findViewById(R.id.net_amount);
        textViewNet.setText("" + paycheckSummary.getNetPayReal());

        TextView textViewReimbursement = view.findViewById(R.id.reimbursement);
        textViewReimbursement.setText("" + paycheckSummary.getReimbursement());

        TextView textViewGrossExpected = view.findViewById(R.id.gross_amount_expected);
        textViewGrossExpected.setText("" + paycheckSummary.getExpectedGrossAmount());

        TextView textViewGrossRemain = view.findViewById(R.id.gross_amount_remain);
        textViewGrossRemain.setText("" + paycheckSummary.getGrossAmountRemain());

        TextView textViewNetExpected = view.findViewById(R.id.net_amount_expected);
        textViewNetExpected.setText("" + paycheckSummary.getExpectedNetPay());

        TextView textViewNetRemain = view.findViewById(R.id.net_amount_remain);
        textViewNetRemain.setText("" + paycheckSummary.getNetPayRemain());

        TextView textViewNetMean = view.findViewById(R.id.net_amount_mean);
        textViewNetMean.setText("" + paycheckSummary.getNetPayRealMean());

        initViewCharts(view, paycheckSummary);

    }

    private void initViewCharts(View view, PaycheckSummary paycheckSummary) {
        Collections.sort(paycheckSummary.getPaycheckDetails());
        ArrayList<Entry> netPayEntries = new ArrayList<>();

        for (PaycheckDetail paycheckDetail : paycheckSummary.getPaycheckDetails()) {
            netPayEntries.add(new Entry(paycheckDetail.getMonth() - 1, paycheckDetail.getNetPayReal().floatValue()));
        }

        LineData lineData = this.generateLineData(netPayEntries);

        LineChart mChart = view.findViewById(R.id.chart_gross_amt);

        mChart.getDescription().setText(" ");

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths.get((int) value);
            }
        });

        mChart.setData(lineData);
        mChart.invalidate();

    }

    private LineData generateLineData(ArrayList<Entry> entries) {

        LineData data = new LineData();

        LineDataSet dataSet = new LineDataSet(entries, "Net Pay");
        dataSet.setColor(R.color.md_blue_900);
        dataSet.setLineWidth(2.5f);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(R.color.md_red_900);
        dataSet.setValueTextSize(10f);

        data.addDataSet(dataSet);

        return data;
    }

    private BarData generateBarData(ArrayList<BarEntry> entries) {
        BarData data = new BarData();
        float barWidth = 0.45f; // x2 dataset
        data.setBarWidth(barWidth);

        BarDataSet dataSet = new BarDataSet(entries, "Net");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.rgb(60, 220, 78));
        dataSet.setValueTextSize(10f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        data.addDataSet(dataSet);

        return data;
    }

}
