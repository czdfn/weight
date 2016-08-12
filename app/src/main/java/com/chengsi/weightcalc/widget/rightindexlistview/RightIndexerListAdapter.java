package com.chengsi.weightcalc.widget.rightindexlistview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.storage.JDStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RightIndexerListAdapter extends ArrayAdapter<HospitalBean> {

    private int resource; // store the resource layout id for 1 row
    private boolean inSearchMode = false;
    private List<HospitalBean> _items;
    private RightSectionIndexer indexer = null;

    public RightIndexerListAdapter(Context _context, int _resource,
                                   List<HospitalBean> _items) {
        super(_context, _resource, _items);
        resource = _resource;
        this._items = _items;
        if (_items != null && _items.size() >= 0) {
            Collections.sort(_items, new ItemComparator());
            setIndexer(new RightSectionIndexer(_items));
        }
    }

    // get the section textview from row view
    // the section view will only be shown for the first item
    public TextView getSectionTextView(View rowView) {
        TextView sectionTextView = (TextView) rowView
                .findViewById(R.id.sectionTextView);
        return sectionTextView;
    }

    public TextView getLableTextView(View rowView) {
        TextView sectionTextView = (TextView) rowView
                .findViewById(R.id.hos_lable);
        return sectionTextView;
    }

    public void showSectionViewIfFirstItem(View rowView,
                                           HospitalBean item, int position) {
        TextView sectionTextView = getSectionTextView(rowView);

        // if in search mode then dun show the section header
        if (inSearchMode) {
            sectionTextView.setVisibility(View.GONE);
        } else {
            // if first item then show the header

            if (indexer.isFirstItemInSection(position)) {

                String sectionTitle = indexer.getSectionTitle(item.getPinyin());
                sectionTextView.setText(sectionTitle);
                sectionTextView.setVisibility(View.VISIBLE);

            } else
                sectionTextView.setVisibility(View.GONE);
        }
    }

    public void showLableView(View rowView, List<HospitalBean> _items, int position) {
        if(!inSearchMode) {
            int count;
            for (count =0; count < _items.size(); count++) {
                String curCity = _items.get(count).getCity();
                String inprovince = JDStorage.getInstance().getStringValue("province", null);
                String incity = JDStorage.getInstance().getStringValue("city", null);
                boolean curbol = (curCity != null && incity != null) && (!(curCity.contains(inprovince)) || !(curCity.contains(incity)));
                if (curbol) {
                    break;
                }
            }
            if (count > 0) {
                if (position == 0) {
                    getLableTextView(rowView).setVisibility(View.VISIBLE);
                    getLableTextView(rowView).setText("距离您最近的生殖中心");
                }
            }
            if (count > 2) {
                    if (position == 2) {
                        getLableTextView(rowView).setVisibility(View.VISIBLE);
                        getLableTextView(rowView).setText("本省热门生殖中心");
                    } else if (position == count) {
                        getLableTextView(rowView).setVisibility(View.VISIBLE);
                        getLableTextView(rowView).setText("全部生殖中心");
                    }
            } else {
                if (position == count) {
                    getLableTextView(rowView).setVisibility(View.VISIBLE);
                    getLableTextView(rowView).setText("全部医院");
                }
            }
        }
    }

    // do all the data population for the row here
    // subclass overwrite this to draw more items
    public void populateDataForRow(View parentView, HospitalBean item,
                                   int position) {
        // default just draw the item only
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView nameView = (TextView) infoView.findViewById(R.id.cityName);
        nameView.setText(item.getCity());
    }

    // this should be override by subclass if necessary
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup parentView;

        HospitalBean item = getItem(position);

       if (convertView == null) {
                parentView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        inflater);
            vi.inflate(resource, parentView, true);
        } else {
            parentView = (LinearLayout) convertView;
        }

        showSectionViewIfFirstItem(parentView, item, position);
        Log.i("_item", "" + _items.size());
        showLableView(parentView, _items, position);
        populateDataForRow(parentView, item, position);

        return parentView;
    }

    public boolean isInSearchMode() {
        return inSearchMode;
    }

    public void setInSearchMode(boolean inSearchMode) {
        this.inSearchMode = true;
    }

    public RightSectionIndexer getIndexer() {
        return indexer;
    }

    public void setIndexer(RightSectionIndexer indexer) {
        this.indexer = indexer;
    }

    /**
     * 排序比较
     */
    class ItemComparator implements Comparator<HospitalBean> {

        @Override
        public int compare(HospitalBean lhs, HospitalBean rhs) {
            if (lhs.getPinyin() == null || rhs.getPinyin() == null)
                return -1;
            int k = (lhs.getPinyin().compareTo(rhs.getPinyin()));
            if (k == 0) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
            return k;

        }

    }

}
