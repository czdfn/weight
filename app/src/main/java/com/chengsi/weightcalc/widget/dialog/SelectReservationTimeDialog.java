package com.chengsi.weightcalc.widget.dialog;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.OutDoctorArrangeBean;
import com.chengsi.weightcalc.bean.ReservationTime;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;

import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;

public class SelectReservationTimeDialog extends BaseSelectDialog {
	private List dataSource;
	private boolean isOutReservation = false;
	private AdapterView.OnItemClickListener onItemClickListener;

	protected static SelectReservationTimeDialog mDialog;
	public SelectReservationTimeDialog() {
	}
	
	public static SelectReservationTimeDialog getInstance(){
		return new SelectReservationTimeDialog();
	}
	@Override
	protected int getContentViewId() {
		return R.layout.view_select_dialog;
	}

	@Override
	protected void initViews() {
		super.initViews();
		listView.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return ListUtils.isEmpty(dataSource) ? 0 : dataSource.size();
			}

			@Override
			public Object getItem(int position) {
				return dataSource.get(position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_reservation_time, parent, false);
				TextView tvTime = (TextView) view.findViewById(R.id.tv_reservation_time);
				TextView tvNum = (TextView) view.findViewById(R.id.tv_reservation_num);
				if (dataSource.get(position) instanceof ReservationTime){
					ReservationTime reservationTime = (ReservationTime) dataSource.get(position);
					if (reservationTime.getNum() <= 0){
						tvNum.setText("0");
						tvNum.setTextColor(getResources().getColor(R.color.text_5e));
					}else{
						tvNum.setText(String.valueOf(reservationTime.getNum()));
						tvNum.setTextColor(getResources().getColor(R.color.text_reservation_num));
					}
					tvTime.setText(reservationTime.getTimeValue());
				}else if (dataSource.get(position) instanceof OutDoctorArrangeBean){
					OutDoctorArrangeBean reservationTime = (OutDoctorArrangeBean) dataSource.get(position);
					if (reservationTime.getSurplus_numbers() <= 0){
						tvNum.setText("0");
						tvNum.setTextColor(getResources().getColor(R.color.text_5e));
					}else{
						tvNum.setText(String.valueOf(reservationTime.getSurplus_numbers()));
						tvNum.setTextColor(getResources().getColor(R.color.text_reservation_num));
					}
					tvTime.setText(reservationTime.getTimeName());
				}

				return view;
			}
		});
		if (!ListUtils.isEmpty(dataSource) && dataSource.size() <= 6){
			JDUtils.setListViewHeightBasedOnChildren(listView);
		}else{
			listView.getLayoutParams().height = UIUtils.convertDpToPixel(320, getActivity());
		}
		listView.setOnItemClickListener(onItemClickListener);
	}

	public void showSelect(FragmentManager manager, List<ReservationTime> list, AdapterView.OnItemClickListener onItemClickListener){
		this.dataSource = list;
		isOutReservation = false;
		this.onItemClickListener = onItemClickListener;
		show(manager, list + SelectReservationTimeDialog.class.getName());
	}

	public void showOutSelect(FragmentManager manager, List<OutDoctorArrangeBean> list, AdapterView.OnItemClickListener onItemClickListener){
		this.dataSource = list;
		isOutReservation = true;
		this.onItemClickListener = onItemClickListener;
		show(manager, list + SelectReservationTimeDialog.class.getName());
	}
}
