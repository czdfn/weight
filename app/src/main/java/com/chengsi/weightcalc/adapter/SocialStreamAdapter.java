package com.chengsi.weightcalc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.utils.imageloader.WSImageDisplayOpts;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SocialStreamAdapter extends BaseAdapter
{
	public static interface AdapterType
	{
		public static final int NO_CHANGED = 0;
		public static final int SET_SELECTION_FROM_TOP = 1;
	}
	public static interface CommonDataKey
	{
		public static final String USER_ID = "userId";
		public static final String ACCOUNT_ID = "accountId";
		public static final String USER_NAME = "userName";
		public static final String EVENT_ID = "eventId";
		public static final String EVENT_TYPE = "eventType";
		public static final String ITEM_TYPE = "itemType";
		public static final String ITEM_ENABLE_STATUS = "itemEnableStatus";
		public static final String SNS_ID = "snsId";
	}

	public static interface ContentDataKey
	{
		public static final String CONTENT_LAYOUT_KEY = "content layout";
		public static final String CONTENT_DATA_KEY = "content data";
	}
	public static final String TAG = "SocialStreamAdapter";

	public static final String LINK_ADD_EXTRA_KEY = "linkAddExtra";
	public static final String LINK_STRING_KEY = "linkString";
	public static final String BROWSWE_URL_KEY = "Browser URL";
	public static final String CONTENT_KEY = "content";
	
	private boolean mIsGCMannually = true;
	protected boolean mIsFling = false;
	private Map<Integer, String[]> mFrom;
	protected Map<Integer, int[]> mTo;
	private ViewBinder mViewBinder;
	private ConvertViewResetLister  convertViewResetLister;
	public static final String IMAGE_CACHE_DIR = "thumbs";
	protected List<Map<String, Object>> mData;

	protected int []mResource;        //more than one layout to choose
	protected LayoutInflater mInflater;

	private Context mContext;
//	private Map<Integer, OnClickListener> mOnClickMap; 
	private Map<Integer, ArrayList<TextViewLinkListener>> mLinkkMap; 
	private DisplayImageOptions options;
	private int mEmptyUriImageResource = 0;
	
	private int mSize = 0, mRadius = 0;

	private int firstItemMarginTop = 0;
	
	public float getFirstItemTopMargin() {
		return firstItemMarginTop;
	}

	public void setFirstItemTopMargin(int firstItemTopMargin) {
		this.firstItemMarginTop = firstItemTopMargin;
	}

	private Handler mEmotionInitCompleteHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d("com.mediatek.widget", "mEmotionInitCompleteHandler   received \"inti complete\" message");
			if(SocialStreamAdapter.this != null)
			{
				Log.d("com.mediatek.widget", "adapter is not null, so to notify this adapter");
				SocialStreamAdapter.this.notifyDataSetChanged();
			}
		}

	};

	public static void throwExceptionForDebug(String msg)
	{
		throw new RuntimeException(msg);
	}

	
	public SocialStreamAdapter(Context context, List<Map<String, Object>> data,
			int []resource,Map<Integer, String[]> from, Map<Integer, int[]> to,int size,int radius) {
		mContext = context.getApplicationContext();
		mInflater = LayoutInflater.from(context);
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mSize = size;
		mRadius = radius;
		options = new WSImageDisplayOpts(mEmptyUriImageResource != 0 ? mEmptyUriImageResource : R.drawable.ic_launcher, mRadius)
		.setImageHeight(size).setImageWidth(size).toImageLoaderOpts();
        //options.setDefImage(R.drawable.talk_def_pic);
		
	}

	public SocialStreamAdapter(Context context, List<Map<String, Object>> data,
			int []resource,Map<Integer, String[]> from, Map<Integer, int[]> to,int size,int radius, DisplayImageOptions options) {
		mContext = context.getApplicationContext();
		mInflater = LayoutInflater.from(context);
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mSize = size;
		mRadius = radius;
		this.options = options;
	}

	public void setEmptyUriImageResource (int drawableResource) {
		mEmptyUriImageResource = drawableResource;
		options = new WSImageDisplayOpts(mEmptyUriImageResource != 0 ? mEmptyUriImageResource : R.drawable.ic_launcher, mRadius)
		.setImageHeight(mSize).setImageWidth(mSize).toImageLoaderOpts();
	}

	public void setData(List<Map<String, Object>> mData)
	{
		this.mData = mData;
	}
	
	/**
	 * @return The data used to fill the ListView
	 */
	public List<Map<String, Object>> getData() {
		return mData;
	}


	/**
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	public int getItemViewType(int position) {
		if(position >= mData.size())
		{
			Log.e("error", "SocialStreanAdapter  getItemViewType  position is bigger than the list! position is "+position+", list size is "+mData.size());
			return 0;
		}
		Integer result = (Integer)mData.get(position).get(CommonDataKey.ITEM_TYPE);
		int iRet = 0;
		if(result == null)
		{
			iRet = 0;
		}
		else
		{
			try
			{
				iRet = result.intValue();
			}
			catch(Exception e)
			{
				iRet = 0;
				Log.d("com.mediatek.widget", "result.intValue() happened exception!!");
				e.printStackTrace();
			}
		}
		return iRet;
	}

	/** 
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	public int getViewTypeCount() {
		return this.mResource.length;
	}

//	public void addViewOnClickListener(int resId, OnClickListener l)
//	{
//		if(this.mOnClickMap == null)
//		{
//			this.mOnClickMap = new HashMap<Integer, OnClickListener>();
//		}
//		if(!mOnClickMap.containsKey(resId))
//		{
//			Log.d(TAG, "mOnClickMap has contained this resId and its listener");
//		}
//		mOnClickMap.put(resId, l);
//	}
	
	private class TextViewLinkListener
	{
		Pattern pattern;
		Intent intent;
		Integer snsId; //the SnsId to parse LinkListener
	}
	
	public void addTextViewLinkListener(int resId, Pattern pattern, Intent intent)
	{
		addTextViewLinkListener(resId, pattern, intent,null);
	}
	
	public void addTextViewLinkListener(int resId, Pattern pattern, Intent intent, Integer snsId)
	{
		if(pattern==null || intent==null)
		{
			throwExceptionForDebug("pattern is "+pattern+", and intent is "+intent);
		}
		if(this.mLinkkMap == null)
		{
			this.mLinkkMap = new HashMap<Integer, ArrayList<TextViewLinkListener>>();
		}
		
		TextViewLinkListener listener = new TextViewLinkListener();
		listener.pattern = pattern;
		listener.intent = intent;
		listener.snsId = snsId;
		
		if(mLinkkMap.containsKey(resId))
		{
			ArrayList<TextViewLinkListener> list = mLinkkMap.get(resId);
			if(list == null)
			{
				throwExceptionForDebug("list is null");
				return;
			}
			list.add(listener);
			Log.d(TAG, "mLinkkMap has contained this resId and its listener");
		}
		else
		{
			ArrayList<TextViewLinkListener> list = new ArrayList<TextViewLinkListener>();
			list.add(listener);
			mLinkkMap.put(resId, list);
		}
	}
	
	
//	public void addViewOnClickListener(Map<Integer, OnClickListener> map)
//	{
//		this.mOnClickMap = map;
//	}

	/**
	 * Get the item number of this ListView.
	 * 
	 * @return the item number
	 */
	public int getCount() 
	{
		return mData.size();
	}

	/**
	 *	Get an item by custom position
	 *
	 * 	@param position The item position
	 *  @return The data at the specified position.
	 */
	public Object getItem(int position) 
	{
		if(position >= mData.size())
		{
			Log.e("error", "SocialStreamAdapter getItem  position >= mData.size().  position is "+position+", and mData size is "+mData.size());
			return null;
		}
		if(mData != null)
			return mData.get(position);
		else
		{
			throwExceptionForDebug("mData is null");
			return 0;
		}
		
	}

	/**
	 * Get an item id by custom position
	 * 
	 * @param position The item position
	 * @return The id of the item at the specified position.
	 */
	public long getItemId(int position) 
	{
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View ret=createViewFromResource(position, convertView, parent);
		checkFirsrItemMargin(ret, position);
		return ret;
	}

	protected void checkFirsrItemMargin(View ret, int position) {
		if(firstItemMarginTop > 0.0) {
			ViewGroup vg = (ViewGroup)ret;
			View v = vg.getChildAt(0);
			ViewGroup.LayoutParams params = v.getLayoutParams();
			if(params != null && params instanceof RelativeLayout.LayoutParams) {
				RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) params;
				if(position == 0) {
					rl.topMargin = firstItemMarginTop;
				}
				else {
					rl.topMargin = 0;
				}
				v.setLayoutParams(rl);
			}
		}
	}

	public static interface ViewFactory {
		public View createView(Map<String, Object> map);
	}
	private ViewFactory mViewFactory = null;
	public void setViewFactory (ViewFactory factory) {
		this.mViewFactory = factory;
	}
	/**
	 * @param position The position of view to create
	 * @param convertView The view which is obtained by system
	 * @param parent A ViewGroup which is the parent of this view
	 * @return The view created by data
	 */
	protected View createViewFromResource(int position, View convertView,ViewGroup parent) 
	{
		if(position >= mData.size())
		{
			Log.e("error", "SocialStreamAdapter createViewFromResource position >= mData.size().  position is "+position+", and mData size is "+mData.size());
			return null;
		}
		View v = null;
		
		if (mViewFactory != null) {
			Map<String, Object> map = mData.get(position);
			View view = mViewFactory.createView(map);
			if (view != null) {
				return view;
			}
		}
		
		if (convertView == null)
		{
			if(mInflater != null)
			{
				v = mInflater.inflate(mResource[getItemViewType(position)], parent, false);
			}
			if(v == null)
			{
				throwExceptionForDebug("view inflated from XML file is null");
				return null;
			}
			v.setTag(mResource);
			final int[] to = mTo.get(mResource[getItemViewType(position)]);
			if(to == null)
			{
				throwExceptionForDebug("the to parameter is null");
				return null;
			}
			final int count = to.length;
			final View[] holder = new View[count];

			for (int i = 0; i < count; i++) 
			{
				holder[i] = v.findViewById(to[i]);
			}
			v.setTag(holder);
		}
		else
		{
			v = convertView;
			if(convertViewResetLister != null)
			{
				convertViewResetLister.setConvertViewResetLister(v);
			}
			
		}
		bindView(position, v);

		return v;
	}
	

	protected void bindView(int position, View view) {
		if(position >= mData.size())
		{
			Log.e("error", "SocialStreamAdapter bindView position >= mData.size().  position is "+position+", and mData size is "+mData.size());
			return;
		}
		final Map<String, Object> dataSet = mData.get(position);

		if (dataSet == null || view==null) {
			throwExceptionForDebug("dataSet is "+dataSet+", and view is "+view);
			return;
		}
		//for onItemClickListener
//		view.setTag(view.getId(), dataSet);
		final ViewBinder binder = mViewBinder;
		final View[] holder = (View[]) view.getTag();
		if(holder == null)
			return;
		//get the from and to
		final String[] from = mFrom.get(mResource[getItemViewType(position)]);
		final int[] to = mTo.get(mResource[getItemViewType(position)]);
		if(from==null || to==null)
		{
			throwExceptionForDebug("from is "+from+", and to is "+to);
			return;
		}
		if(holder.length != to.length)
		{
			Log.e(TAG, "#################################################################");
			Log.e(TAG, "here is wrong, holder.length("+holder.length+")!= to.length("+to.length+"), and the position is "+position+", and view type is "+getItemViewType(position));
			Log.e(TAG, "#################################################################");
		}
		final int count = holder.length;
		for (int i = 0; i < count; i++) 
		{
			final View v = holder[i];
			if (v != null) 
			{
				if(!dataSet.containsKey(from[i])) {
					continue;
				}
				final Object data = dataSet.get(from[i]);
				boolean bound = false;
				if (binder != null) 
				{
					bound = binder.setViewValue(v, data, dataSet);
				}
				//将这段代码放到binder运行之后
				if(data == null)
					continue;
				if (!bound) 
				{
					//add the response
//					if(this.mOnClickMap!=null && mOnClickMap.containsKey(v.getId()))
//					{
//						OnClickListener l = mOnClickMap.get(v.getId());
//						if(l != null)
//						{
//							v.setOnClickListener(l);
//							v.setTag(v.getId(), dataSet);
//						}
//					}
					if(data instanceof OnTouchListener)
					{
//						v.setTag(v.getId(), dataSet);
						v.setOnTouchListener((OnTouchListener)data);
						continue;
					}
					if(data instanceof OnClickListener)
					{
//						v.setTag(v.getId(), dataSet);
						v.setOnClickListener((OnClickListener) data);
						continue;
					}
					if(data instanceof OnLongClickListener)
					{
						v.setOnLongClickListener((OnLongClickListener) data);
						continue;
					}
					if (v instanceof Checkable) 
					{
						if (data instanceof Boolean)
						{
							((Checkable) v).setChecked((Boolean) data);
						} 
						else 
						{
							throw new IllegalStateException(v.getClass().getName() +
									" should be bound to a Boolean, not a " + data.getClass());
						}
					} 
//					else if (v instanceof Button) 
//					{
////						v.setTag(v.getId(), dataSet);
//						//setViewText((TextView) v, (CharSequence)data, dataSet);
//					} 
					else if(data instanceof Boolean)
					{
						Boolean isVisible = (Boolean) data;
						if(isVisible)
						{
							v.setVisibility(View.VISIBLE);
						}
						else
						{
							v.setVisibility(View.GONE);
						}
						
					}
					else if (!(v instanceof Button) && v instanceof TextView && data instanceof CharSequence) 
					{
						setViewText((TextView) v, (CharSequence)data, dataSet);
					}
					else if (v instanceof ImageView) 
					{
						setImageView(v, data, dataSet);
					}
				
					else {
						throw new IllegalStateException(v.getClass().getName() + " is not a " +
						" view that can be bounds with value("+data+") by this MTKAdapter key is " + from[i] + " view.id is=" + Integer.toHexString(v.getId()));
					}
				}
			}
		}
	}
	
	protected void setImageView(View v, Object data, Map<String,Object> dataMap)
	{
		//resource Bitmap can be cached here
		if (data instanceof Integer) 
		{
			int d = (Integer)data;
			ImageView iv = (ImageView)v;
			iv.setImageResource(d);
		}
		else if(data instanceof Bitmap) 
		{
			ImageView iv = (ImageView)v;
			iv.setImageBitmap((Bitmap)data);
		} else if(data instanceof String) 
		{
			ImageView iv = (ImageView)v;
			if(((String) data).contains("http:") || ((String) data).contains("https:") ){
				ImageLoaderUtils.displayImageForIv(iv, (String) data, options);
			}else{
				String path =  ImageDownloader.Scheme.FILE.wrap((String)data);
				ImageLoaderUtils.displayImageForIv(iv, path, options);
			}
		} else if(data instanceof Uri) 
		{
			ImageView iv = (ImageView)v;
			ImageLoaderUtils.displayImageForIv(iv, data.toString(), options);
		} 
		else
		{
			Log.d("com.mediatek.widget", " the type of ImageView is not supported! We only support sourceId, bitmap object and Uri. this is " + data.getClass().getName() + " : " + data.toString());
			return;
		}
	}
	
	/**
	 * Returns the {@link SocialStreamAdapter.ViewBinder} used to bind data to views.
	 *
	 * @return a ViewBinder or null if the binder does not exist
	 *
	 */
	public ViewBinder getViewBinder() {
		return mViewBinder;
	}

	/**
	 * Sets the binder used to bind data to views.
	 *
	 * @param viewBinder the binder used to bind data to views, can be null to
	 *        remove the existing binder
	 *
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to an ImageView.
	 *
	 * This method is called instead of {@link #setViewImage(android.widget.ImageView, String)}
	 * if the supplied data is an int or Integer.
	 *
	 * @param v ImageView to receive an image
	 * @param value the value retrieved from the data set
	 *
	 * @see #setViewImage(android.widget.ImageView, String)
	 */
	public void setViewImage(ImageView v, int value) {
		if(v != null)
			v.setImageResource(value);
		else
			throwExceptionForDebug("setViewImage   v is null");
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to an ImageView.
	 *
	 * By default, the value will be treated as an image resource. If the
	 * value cannot be used as an image resource, the value is used as an
	 * image Uri.
	 *
	 * This method is called instead of {@link #setViewImage(android.widget.ImageView, int)}
	 * if the supplied data is not an int or Integer.
	 *
	 * @param v ImageView to receive an image
	 * @param value the value retrieved from the data set
	 *
	 * @see #setViewImage(android.widget.ImageView, int)
	 */
	public void setViewImage(ImageView v, String value) {
		if(v == null)
		{
			throwExceptionForDebug("setViewImage   v is null");
			return;
		}
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to an TextView.
	 *
	 * @param v TextView to receive text
	 * @param text the text to be set for the TextView
	 * @param dataSet The emotion text type
	 */
	public void setViewText(TextView v, final CharSequence text, Map<String, Object> dataSet) 
	{
		v.setText(text);
		
//		if(v==null || text==null || dataSet==null)
//		{
//			throwExceptionForDebug("v is "+v+", text is "+text+", dataSet is "+dataSet);
//			return;
//		}
//		
//		v.setClickable(false);
//		v.setFocusable(false);
//		v.setLongClickable(false);
//		//this two conditions is to set text directly
//		if(!this.mIsEmotionParsing || this.mIsFling )
//		{
//			v.setText(text);
//		}
//		else
//		{
//			boolean result = setTextViewLink(v, text, dataSet);
//			if(!result)
//			{
//				if(EmotionParser.getEmotionParserState()<=0)
//				{
//					v.setText(text);
//				}
//				else
//				{
//					Object obj = dataSet.get(CommonDataKey.SNS_ID);
//					if(obj == null)
//					{
////						throwExceptionForDebug("snsId is not declared in the dataSet");
//						Log.e(TAG, "The snsId from data is null");
//						v.setText(text);
//					}
//					else
//					{
//						int snsId = (Integer)obj;
//						CharSequence cs = parserEmotion(text, snsId);
//						if(cs != null)
//						{
//							v.setText(cs);
//						}
//						else
//						{
//							throwExceptionForDebug("the parsed CharSequence is null");
//						}
//					}
//				}
//			}
//		}
	}
	

	private boolean setTextViewLink(TextView v, final CharSequence text,final Map<String, Object> dataSet)
	{
		boolean flag = false;
		if(this.mLinkkMap!=null && this.mLinkkMap.containsKey(v.getId()))
		{
			final ArrayList<TextViewLinkListener> listenerList = mLinkkMap.get(v.getId());
			if(listenerList != null)
			{
				SpannableStringBuilder ss = new SpannableStringBuilder(text);
				for(final TextViewLinkListener listener:listenerList)
				{
					Object objSnsId = dataSet.get(CommonDataKey.SNS_ID);
					if(objSnsId==null || !(objSnsId instanceof Integer))
					{
						Log.e(TAG, "objSnsId is null");
						return false;
					}
					int snsId = (Integer)objSnsId;
					// snsId is not matched!
					if(null != listener.snsId && snsId != listener.snsId)
					{
						continue;
					}
					Pattern p = listener.pattern;
					if(p != null)
					{
						Matcher m=p.matcher(text);
						while(m.find())
						{
							flag = true;
							final int start = m.start();
							final int end = m.end();
							ss.setSpan(new ClickableSpan() {
								public void onClick(View widget) {
									Intent it = listener.intent;
									if(it != null)
									{
										it.putExtra(LINK_STRING_KEY, text.subSequence(start, end).toString());
										ArrayList<String> list = it.getStringArrayListExtra(LINK_ADD_EXTRA_KEY);
										if(list!=null && list.size()>0)
										{
											for(String key:list)
											{
												if(key.equals(BROWSWE_URL_KEY))
												{
													it.setData(Uri.parse(text.subSequence(start, end).toString()));
												}
												else
												{
													Object value = dataSet.get(key);
													if(value != null)
													{
														if(value instanceof String)													
															it.putExtra(key, (String)value);													
														else if(value instanceof Integer)
															it.putExtra(key, (Integer)value);
														else if(value instanceof Long)
															it.putExtra(key, (Long)value);
														else
															throwExceptionForDebug("the value's type ("+value.getClass().getCanonicalName()+") is not be support.");
													}
													else
													{
														throwExceptionForDebug("the value get by "+key+" is null");
													}
												}
												
											}
										}
										mContext.startActivity(it);
									}
									else
									{
										Log.e(TAG, "intent is null");
									}
								}
							}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
					else
					{
						Log.e(TAG, "pattern is null");
					}
				}
				if(flag)
				{
					((TextView)v).setText(ss);
					v.setMovementMethod(SocialStreamLinkMovementMethod.getInstance());
					//cause these three property be set true in the setMovementMethod
					v.setFocusable(false);
				}
			}
		}
		
		return flag;
	}	


	/**
	 * This class can be used by external clients of SimpleAdapter to bind
	 * values to views.
	 *
	 * You should use this class to bind values to views that are not
	 * directly supported by SimpleAdapter or to change the way binding
	 * occurs for views supported by SimpleAdapter.
	 *
	 */
	public static interface ViewBinder {
		/**
		 * Binds the specified data to the specified view.
		 *
		 * When binding is handled by this ViewBinder, this method must return true.
		 * If this method returns false, SimpleAdapter will attempts to handle
		 * the binding on its own.
		 *
		 * @param view the view to bind the data to
		 * @param data the data to bind to the view
		 * @param  comment a safe String representation of the supplied data:
		 *        it is either the result of data.toString() or an empty String but it
		 *        is never null
		 *
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setViewValue(View view, Object data, Object comment);
	}
	/**
	 * 当 ConvertView 不为空时候调用
	 * @author cccc
	 *
	 */
	public static interface ConvertViewResetLister {
		boolean setConvertViewResetLister(View view);
	}

	
	public ConvertViewResetLister getConvertViewResetLister() {
		return convertViewResetLister;
	}


	public void setConvertViewResetLister(
			ConvertViewResetLister convertViewResetLister) {
		this.convertViewResetLister = convertViewResetLister;
	}


	public boolean isFling() {
		return mIsFling;
	}

	public void setFling(boolean isFling)
	{
		setFling(isFling,0,0);
	}

	public void setFling(boolean isFling,int start, int num) {
		this.mIsFling = isFling;
		if(isFling)
		{
			onFlingStart(start, num);
		}
		else
		{
			onFlingStop(start, num);
		}
	}

	public void onFlingStart(int start, int num)
	{
		
	}
	
	public void onFlingStop(int start, int num)
	{
		
	}
	
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		Boolean result = false;
		try {
			result = (Boolean) mData.get(position).get(CommonDataKey.ITEM_ENABLE_STATUS);
		}
		catch(Exception e ) {
			return true;
		}
		boolean bRet = true;
		if(result == null)
		{
			bRet = true;
		}
		else
		{
			try
			{
				bRet = result.booleanValue();
			}
			catch(Exception e)
			{
				bRet = true;
				Log.d("com.mediatek.widget", "result.intValue() happened exception!!");
				e.printStackTrace();
			}
		}
		return bRet;
	}

	public boolean isGCMannually() {
		return mIsGCMannually;
	}


	public void setGCMannually(boolean isGCMannually) {
		this.mIsGCMannually = isGCMannually;
	}

	public void removeCachebyData(Map<String,Object> map)
	{
		if(map.get(ImageLoaderUtils.IMAGE_HEAD_PATH) != null){
			
			String key =  Uri.fromFile(new File((String) map.get(ImageLoaderUtils.IMAGE_HEAD_PATH))).toString();
			String memoryCacheKeyPre = new StringBuilder(key).toString();
			for(String k : ImageLoader.getInstance().getMemoryCache().keys()){
				if(k.startsWith(memoryCacheKeyPre)){
					ImageLoader.getInstance().getMemoryCache().remove(k);
				}
			}
			File file = ImageLoader.getInstance().getDiskCache().get(key);
			if(file != null && file.exists())file.delete();
		}
	}
	
	public Context getContext()
	{
		return mContext;
	}
}
