package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.AlbumInfo;
import com.chengsi.weightcalc.manager.AlbumManager;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlbumListActivity extends BaseActivity {
	public static final String EXTRA_ALBUM_POSITION = "position";
	
	private int selectMode;
	public static final String DEFAULT_ALBUM = "default_album";
	private ListView listView;
	private SocialStreamAdapter adapter;
	private List<AlbumInfo> dataList = new ArrayList<AlbumInfo>();
	private int[] resource = {R.layout.album_list_item};
	private String[] from = {ImageLoaderUtils.IMAGE_CONTENT_PATH, "name", "count"};
	private int[] to = {R.id.album_list_item_image, R.id.album_list_item_name, R.id.album_list_item_count};
	private List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	private boolean isForZone;
	private boolean canRechoose;
	private AlbumManager.OnAlbumChangedListener onAlbumChangedListener = new AlbumManager.OnAlbumChangedListener() {
		@Override
		public void OnChanged(List<AlbumInfo> list) {
			data.clear();
			dataList.clear();
			dataList.addAll(list);
			for (AlbumInfo album : dataList) {
				data.add(parseData(album));
			}
			adapter.notifyDataSetChanged();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isInAbnormalState(savedInstanceState)) {
			return;
		}
		selectMode = getIntent().getIntExtra(AlbumManager.KEY_MODE, AlbumManager.MODE_MULTI);
		isForZone = getIntent().getBooleanExtra(AlbumManager.KEY_FOR_ZONE, false);
		canRechoose = getIntent().getBooleanExtra(AlbumManager.KEY_RECHOOSE, false);
		setContentView(R.layout.activity_album_list);
		setMyTitle(R.string.add_activity_albumSelect);
		initListView();
		application.albumManager.getImagesBucketList(true, new AlbumManager.OnGetAlbumListListener() {
			@Override
			public void OnFinished(List<AlbumInfo> list) {
				dataList.addAll(list);
				for (AlbumInfo album : dataList) {
					data.add(parseData(album));
				}
				adapter.notifyDataSetChanged();
			}
		});
		Intent intent = new Intent(AlbumListActivity.this, SelectPhotoActivity.class);
		intent.putExtra(AlbumListActivity.EXTRA_ALBUM_POSITION, 0);
		intent.putExtra(AlbumManager.KEY_MODE, selectMode);
		intent.putExtra(AlbumManager.KEY_RECHOOSE, canRechoose);
		startActivity(intent);
		application.albumManager.addOnAlbumChangedListener(onAlbumChangedListener);
		application.albumManager.registRelatedActivity(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.albumManager.unregistRelatedActivity(this);
	}
	protected View createLeftView() {
		View view = generateDefaultLeftView();
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isForZone) {
					application.albumManager.onCancel();
				}
				finish();
			}
		});
		return view;
	};
	@Override
	public void onBackPressed() {
		if (!isForZone) {
			application.albumManager.onCancel();
		}
		super.onBackPressed();
	}
	private void initListView(){
		listView = (ListView) findViewById(R.id.album_list_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AlbumListActivity.this, SelectPhotoActivity.class);
				intent.putExtra(AlbumManager.KEY_MODE, selectMode);
				intent.putExtra(AlbumManager.KEY_RECHOOSE, canRechoose);
				intent.putExtra(AlbumListActivity.EXTRA_ALBUM_POSITION, position);
				startActivity(intent);
			}
		});
		HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
		fromMap.put(resource[0], from);
		HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
		toMap.put(resource[0], to);
		adapter = new SocialStreamAdapter(this, data, resource, fromMap, toMap, UIUtils.convertDpToPixel(60,this), 0);
		listView.setAdapter(adapter);
	}
	private Map<String, Object> parseData(AlbumInfo album){
		Map<String, Object> map = new HashMap<String, Object>();
		if (!album.imageList.isEmpty()) {
			map.put(from[0], album.imageList.get(0).imagePath);
		}
		map.put(from[1], album.albumName);
		map.put(from[2], getString(R.string.page, album.count));
		return map;
	}
}
