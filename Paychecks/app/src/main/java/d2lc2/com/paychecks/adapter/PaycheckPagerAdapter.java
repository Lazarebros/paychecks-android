package d2lc2.com.paychecks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.d2l2c.paycheck.util.bean.PaycheckSummary;

import java.util.List;

import d2lc2.com.paychecks.R;

public class PaycheckPagerAdapter extends PagerAdapter {

    LayoutInflater layoutInflater;

    private Context context;
    private List<PaycheckSummary> paycheckSummaryList;

    //int[] colors = {R.color.colorAccent, R.color.green, R.color.yellow};

    //int[] layouts = {R.layout.one, R.layout.two, R.layout.three};

    public PaycheckPagerAdapter(Context context, List<PaycheckSummary> paycheckSummaryList)
    {
        this.context = context;
        this.paycheckSummaryList = paycheckSummaryList;
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
        //view.findViewById(R.id.view1).setBackgroundResource(colors[position]);
        initView(view, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void initView(View view, int position) {
        TextView textViewYear = view.findViewById(R.id.paycheck_year);
        textViewYear.setText("" + paycheckSummaryList.get(position).getYear());

        TextView textViewGross = view.findViewById(R.id.gross_amount);
        textViewGross.setText("" + paycheckSummaryList.get(position).getGrossAmount());

        TextView textViewNet = view.findViewById(R.id.net_amount);
        textViewNet.setText("" + paycheckSummaryList.get(position).getNetPayReal());

        TextView textViewReimbursement = view.findViewById(R.id.reimbursement);
        textViewReimbursement.setText("" + paycheckSummaryList.get(position).getReimbursement());

        TextView textViewGrossExpected = view.findViewById(R.id.gross_amount_expected);
        textViewGrossExpected.setText("" + paycheckSummaryList.get(position).getExpectedGrossAmount());

        TextView textViewGrossRemain = view.findViewById(R.id.gross_amount_remain);
        textViewGrossRemain.setText("" + paycheckSummaryList.get(position).getGrossAmountRemain());

        TextView textViewNetExpected = view.findViewById(R.id.net_amount_expected);
        textViewNetExpected.setText("" + paycheckSummaryList.get(position).getExpectedNetPay());

    }
}
