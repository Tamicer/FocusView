package com.lyk.focusview_master;


import java.util.ArrayList;
import java.util.HashMap;




import modle.FocusItemModle;
import modle.DemoModle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * List of support level, vertical and horizontal vertical, two-way list list.
 * 
 * @author lyk.
 * 
 *  */

@SuppressLint("NewApi")
public class FocusView extends  ViewGroup {
	
	private static final String TAG = "FocusView";
	private FocusAdapter mAdapter;

	public static enum OrientationType {
		All, Vertical, Horizontal
	};
	/** items mOnItemSelectedListener*/
	@SuppressWarnings("rawtypes")
	private OnItemSelectedListener mOnItemSelectedListener;
	/**  items mOnItemClickListener */
	private OnItemClickListener mOnItemClickListener;
	protected int mNextX;
	private int mMaxX = Integer.MAX_VALUE;

	private static final LayoutParams FILL_FILL = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ROWS = 5;
	
	private static final int DEF_VISIBLE_COLS = 2;
	
	private static final int PADDING = 10;

	private OrientationType mOrientation = OrientationType.Horizontal;
	
	/**
	 * ROWS.
	 */
	protected int visibleRows = DEF_VISIBLE_ROWS;
	/**
	 *COLS.
	 */
	protected int visibleCols = DEF_VISIBLE_COLS;

	private Scroller mScroller;
	
	private VelocityTracker mVelocityTracker;
	/**
	 * The current item left upper corner of the screen
	 */
	private int mCurRow = 0, mCurCol = 0;
	/**
	 * Total number of rows or Cols,
	 */
	protected int mRowsCount = 0, mColsCount = 0;
	
	private static final int TOUCH_STATE_REST = 0;
	
	private static final int TOUCH_STATE_SCROLLING = 1;

	private static final int SNAP_VELOCITY = 600;

	private int mTouchState = TOUCH_STATE_REST;
	
	private int mTouchSlop;
	
	private float mLastMotionX, mLastMotionY;

	private FocusListener mFocusListener;
	/**
	 * mRowHeight, mColWidth
	 */
	private int mRowHeight, mColWidth;

	/**
	 * mGapWidth
	 */
	private int mGapWidth, mGapHeight;
	/**
	 * The view for animation effect of focus and the lost focus event
	 */
	
	private int mSreenWidth;
	
	private int mSreenHight;
	
	private int mOnX;
	private int mOnY;
	private AnimationFocusMetroManager mAnimationFocusController;
	@SuppressWarnings("rawtypes")
	protected ArrayList<FocusItemModle> mFocusItems = new ArrayList<FocusItemModle>();
	
	protected HashMap< View, FocusItemModle<DemoModle>> maps = new HashMap<View, FocusItemModle<DemoModle>>();
	
	public FocusView(Context context) {
		this(context, null);
	}

	public FocusView(Context context, AttributeSet attrs) {
		
		
		this(context, attrs, 0);
	}

	public FocusView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		getDefDisplay();
		initViewGroup(context);
	}
	
	private void getDefDisplay() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wmManager=(WindowManager) ((Activity) getContext()). getSystemService(Context.WINDOW_SERVICE);  
	
		  mSreenWidth = wmManager.getDefaultDisplay().getWidth();

		  mSreenHight = wmManager.getDefaultDisplay().getHeight();

		
	}

	public void setFocusListener(FocusListener focusListener) {
		this.mFocusListener = focusListener;
	}
	
	@SuppressWarnings("rawtypes")
	public OnItemSelectedListener<?> getmOnItemSelectedListener() {
		return mOnItemSelectedListener;
	}

	public void setOnItemSelectedListener(
			OnItemSelectedListener mOnItemSelectedListener) {
		this.mOnItemSelectedListener =  mOnItemSelectedListener;
	}

	
	private void initViewGroup(Context context) {
		
		mScroller = new Scroller(context);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		// set Animation
		mAnimationFocusController = new AnimationFocusMetroManager(getContext());
		
	}
	public FocusAdapter getAdapter() {
		return mAdapter;
	}

	public void setAdapter(FocusAdapter mAdapter) {
		this.mAdapter = mAdapter;
		
		 if (mAdapter == null ) {
	           return;
	        }
		 
		 int row = 0, col = 0;
		 int rowspan = 1, colspan = 1 ;
		
		 for (int i = 0; i < mAdapter.getCount(); i++) {
		
			View mView =  mAdapter.getView(i, null, null);
			
			LinearLayout  layout =  new LinearLayout(getContext());
			layout.setFocusable(true);
			layout.setFocusableInTouchMode(true);
			layout.setBackgroundResource(android.R.drawable.btn_default);
			layout.setPadding(PADDING, PADDING, PADDING, PADDING);
			if (mView != null) {
				layout.addView(mView, FILL_FILL);		
			}
			FocusItemModle<DemoModle>  mItemModle = null;
			if (i < 15) {
			   if (i < 5) {
				   row = 0;
				   col = i;
				   colspan = 1;
				   rowspan = 2;
				   mItemModle = new FocusItemModle<DemoModle>(layout,mAdapter.getItem(i), row,  rowspan,  col,
							 colspan);
				   
			   } else if (5 <= i && i < 10) {
				   row = 2;
				   col = i-5 ;
				   colspan = 1;
				   rowspan = 2;
				   mItemModle = new FocusItemModle<DemoModle>(layout ,mAdapter.getItem(i), row,  rowspan,  col,
							 colspan);
			   }
			   else {
				   row = 4;
				   col = i-10;
				   colspan = 1;
				   rowspan = 2;
				   mItemModle = new FocusItemModle<DemoModle>(layout ,mAdapter.getItem(i), row,  rowspan,  col,
							 colspan);
			   }
			   addFocusItem(mItemModle);
			}
			else {
				
				if (i < 20) {
					   row = 0;
					   col = i-10;
					   colspan = 1;
					   rowspan = 2;
					   mItemModle = new FocusItemModle<DemoModle>(layout,mAdapter.getItem(i), row,  rowspan,  col,
								 colspan);
					   
				   } else if (20 <= i && i < 25) {
					   row = 2;
					   col = i-15 ;
					   colspan = 1;
					   rowspan = 2;
					   mItemModle = new FocusItemModle<DemoModle>(layout ,mAdapter.getItem(i), row,  rowspan,  col,
								 colspan);
				   }
				   else {
					   row = 4;
					   col = i-20;
					   colspan = 1;
					   rowspan = 2;
					   mItemModle = new FocusItemModle<DemoModle>(layout ,mAdapter.getItem(i), row,  rowspan,  col,
								 colspan);
				   }
				   addFocusItem(mItemModle);
			}
			
		
		}

	}
	
	 public LinearLayout getLinearLayout() {
			LinearLayout layout = new LinearLayout(getContext());
			layout.setFocusable(true);
			layout.setFocusableInTouchMode(true);
			layout.setBackgroundResource(android.R.drawable.btn_default);
			layout.setPadding(5, 5, 5, 5);
			
			return layout;
		}

	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	public void setOnItemClickListener(
			OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	/**
	 * setGap DeF:0
	 * @param gap
	 */
	public void setGap(int gap) {
		this.mGapHeight = this.mGapWidth = gap;
	}
	/**
	 * Def:0
	 * @param gapWidth
	 * @param gapHeight
	 */
	public void setGap(int gapWidth, int gapHeight) {
		this.mGapHeight = gapHeight;
		this.mGapWidth = gapWidth;
	}
	/**
	 * Focus on animation must pass the animation resource Xml ID.
	 * @param in Focus disappears animation.
	 * @param out Get focus animation.
	 */
	public void setAnimation(int in, int out) {
		mAnimationFocusController.setAnimation(in, out);
		
		
		setClipChildren(this);
	}
	
	/**
	 * @param view
	 */
	private void setClipChildren(ViewParent view) {
		
		if(view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)view;
			vg.setClipChildren(false);
			vg.setClipToPadding(false);
			
			if(!vg.equals(vg.getParent())) {
				setClipChildren(view.getParent());
			}
		}else {
			return;
		}
	}
	
	/**
	 * 
	 * @param FocusLock true  else  ,false DEF:false
	 */
	public void setAnimationFocusLock(boolean lock) {
		mAnimationFocusController.setAnimationFocusLock(lock);
	}
	
	/**
	 * set row and column count for visible item
	 * 0 equals to not change current value
	 * others equal to 
	 * 
	 * @param rowCount
	 * @param colCount
	 */
	public void setVisibleItems(int rowCount, int colCount) {

		if (rowCount < 0 || colCount < 0)
			throw new IllegalArgumentException("visible count < 0");

		if (rowCount != 0)
			visibleRows = rowCount;

		if (colCount != 0)
			visibleCols = colCount;
	}
	
	public int getVisibleRows() {
		return visibleRows;
	}

	public int getVisibleCols() {
		return visibleCols;
	}

	public void addFocusItem(FocusItemModle<DemoModle> item) {
		mFocusItems.add(item);
		addView(item.getFocusView(), FILL_FILL);

		adjustRowCol(item);
		
		OnFocusChangeListener l = item.getFocusView().getOnFocusChangeListener();
		if(l != null) {
			mAnimationFocusController.add(item.getFocusView(), l);
		}
		item.getFocusView().setOnFocusChangeListener(mAnimationFocusController);
	}

	public boolean deleteFocusItem(FocusItemModle item) {

		boolean ret = false;

		if (mFocusItems.contains(item)) {
			mFocusItems.remove(item);
			removeView(item.getFocusView());
			ret = true;
		}

		mRowsCount = 0;
		mColsCount = 0;

		for (FocusItemModle mi : mFocusItems) {
			adjustRowCol(mi);
		}
		
		mAnimationFocusController.delete(item.getFocusView());

		return ret;
	}

	private void adjustRowCol(FocusItemModle item) {
		// adjust rows count
		if (mRowsCount < item.getRow() + item.getRowSpan())
			mRowsCount = item.getRow() + item.getRowSpan();

		// adjust columns count
		if (mColsCount < item.getCol() + item.getColSpan())
			mColsCount = item.getCol() + item.getColSpan();
	}

	public void clearFocusItem() {
		mFocusItems.clear();
		removeAllViews();

		mRowsCount = 0;
		mColsCount = 0;
		mAnimationFocusController.clear();
	}

	public void setOrientation(OrientationType orientation) {
		mOrientation = orientation;
	}

	public OrientationType getOrientation() {
		return mOrientation;
	}

	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean dipatchFlg = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			dipatchFlg = onKeyDown(event.getKeyCode(), event);
		}else if(event.getAction() == KeyEvent.ACTION_UP) {
			dipatchFlg = onKeyUp(event.getKeyCode(), event);
		}
		if(!dipatchFlg) {
			dipatchFlg = super.dispatchKeyEvent(event);
		}
		return dipatchFlg;
	}
	
	private FocusItemModle getFocusMetroItem() {
		View focus = getFocusedChild();
		for (int i = 0; i < mFocusItems.size(); i++) {
			if(mFocusItems.get(i).getFocusView() == focus) {
				return mFocusItems.get(i);
			} 
		}
		return null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		FocusItemModle<DemoModle> focusItem = getFocusMetroItem();
		Log.i(TAG, "in onKeyUp focus.row="+keyCode);
		if(focusItem == null) {
			return false;
		}
		
		
		int leftCol = mCurCol;
		int topRow = mCurRow;
		int rightCol = mCurCol + visibleCols;
		int buttomRow = mCurRow + visibleRows;
		
		//change page
		switch(keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(mOrientation != OrientationType.Vertical) {
				if(focusItem.getCol() < leftCol) {
					snapTo(mCurRow, focusItem.getCol() + focusItem.getColSpan() - visibleCols);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(mOrientation != OrientationType.Vertical) {
				if(focusItem.getCol() + focusItem.getColSpan() > rightCol) {
					snapTo(mCurRow, focusItem.getCol());
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(mOrientation != OrientationType.Horizontal) {
				if(focusItem.getRow() < topRow) {
					snapTo(focusItem.getRow() + focusItem.getRowSpan() - visibleRows, mCurCol);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(mOrientation != OrientationType.Horizontal) {
				if(focusItem.getRow() + focusItem.getRowSpan() > buttomRow) {
					snapTo(focusItem.getRow(), mCurCol);
				}
			}

			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			
			if(mOnItemClickListener != null) {
				
			    mOnItemClickListener.onItemClick(this,
					focusItem.getFocusView(), focusItem, focusItem.getPostion(), focusItem.getRow(),
					focusItem.getCol(), focusItem.getId());
			}
			
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		final int itemCount = mFocusItems.size();

		if (itemCount != getChildCount())
			throw new IllegalArgumentException("contain unrecorded child");

		for (int i = 0; i < itemCount; i++) {
			final FocusItemModle item = mFocusItems.get(i);
			final View childView = item.getFocusView();
			
			maps.put(childView ,item);

			if (childView.getVisibility() != View.GONE) {
				final int childLeft = getPaddingLeft() + (mColWidth + mGapWidth) * item.getCol();
				final int childTop = getPaddingTop() + (mRowHeight + mGapHeight) * item.getRow();
				final int childWidth = (mColWidth + mGapWidth) * item.getColSpan() - mGapWidth;
				final int childHeight = (mRowHeight + mGapHeight) * item.getRowSpan() - mGapHeight;

				childView.layout(childLeft, childTop, childLeft + childWidth,
						childTop + childHeight);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);

		mRowHeight = (height - (visibleRows - 1) * mGapHeight - getPaddingTop() - getPaddingBottom()) / visibleRows;
		mColWidth = (width - (visibleCols - 1) * mGapWidth - getPaddingLeft() - getPaddingRight()) / visibleCols;

		// The children are given the same width and height as the scrollLayout
		final int itemCount = mFocusItems.size();

		for (int i = 0; i < itemCount; i++) {

			final FocusItemModle item = mFocusItems.get(i);
			final View childView = item.getFocusView();

			final int childWidth = MeasureSpec.makeMeasureSpec(
					(mColWidth + mGapWidth) * item.getColSpan() - mGapWidth, MeasureSpec.EXACTLY);
			final int childHeight = MeasureSpec.makeMeasureSpec(
					(mRowHeight + mGapHeight) * item.getRowSpan() - mGapHeight, MeasureSpec.EXACTLY);
			
			item.setmWigth(childWidth);
			item.setmHight(childHeight);
			childView.measure(childWidth, childHeight);
		}
		

		scrollTo((mColWidth + mGapWidth) * mCurCol, (mRowHeight + mGapHeight) * mCurRow);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		Log.d(TAG, "snapToDestination(): col = mCurCol");
		final int destRow = (getScrollY() + (mRowHeight + mGapHeight) / 2) / (mRowHeight + mGapHeight);
		final int destCol = (getScrollX() + (mColWidth + mGapWidth) / 2) / (mColWidth + mGapWidth);
		snapTo(destRow, destCol);
	}

	public void snapTo(int whichRow, int whichCol) {

		if (whichRow < 0)
			whichRow = 0;

		if (whichCol < 0)
			whichCol = 0;

		Log.d(TAG, String.format("snap to row:%d, col:%d", whichRow, whichCol));

		boolean needRedraw = false;

		if (mOrientation == OrientationType.Horizontal) {
			whichRow = 0;
			if (whichCol + visibleCols > mColsCount)
				whichCol = Math.max(mColsCount - visibleCols, 0);
		} else if (mOrientation == OrientationType.Vertical) {
			whichCol = 0;
			if (whichRow + visibleRows > mRowsCount)
				whichRow = Math.max(mRowsCount - visibleRows, 0);
		} else if (mOrientation == OrientationType.All) {
			if (whichRow + visibleRows > mRowsCount)
				whichRow = Math.max(mRowsCount - visibleRows, 0);
			if (whichCol + visibleCols > mColsCount)
				whichCol = Math.max(mColsCount - visibleCols, 0);
		}

		int deltaX = whichCol * (mColWidth + mGapWidth);
		int deltaY = whichRow * (mRowHeight + mGapHeight);
		Log.e(TAG, "end snapTo whichRow="+whichRow+" whichCol="+whichCol +" getScrollX()="+getScrollX()+" getScrollY()="+getScrollY());
		// get the valid layout page
		if (getScrollX() != deltaX) {
			deltaX = deltaX - getScrollX();
			needRedraw = true;
		}else {
			deltaX = 0;
		}

		if (getScrollY() != deltaY) {
			deltaY = deltaY - getScrollY();
			needRedraw = true;
		}else {
			deltaY = 0;
		}
		
		if (needRedraw) {
			mScroller.startScroll(getScrollX(), getScrollY(), deltaX, deltaY,
					Math.max(Math.abs(deltaX)/2, Math.abs(deltaY/2)) * 2);
			mCurRow = whichRow;
			mCurCol = whichCol;
			invalidate();
		}
	}

	public int getCurRow() {
		return mCurRow;
	}
	
	public int getCurCol() {
		return mCurCol;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		mOnX = (int) event.getRawX();
		mOnY = (int) event.getRawY();

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

		if (mOrientation == OrientationType.Horizontal)
			y = 0;
		else if (mOrientation == OrientationType.Vertical)
			x = 0;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			int deltaY = (int) (mLastMotionY - y);

			mLastMotionX = x;
			mLastMotionY = y;

			scrollBy(deltaX, deltaY);
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);

			int velocityX = (int) velocityTracker.getXVelocity();
			int velocityY = (int) velocityTracker.getYVelocity();

			int row = mCurRow;
			int col = mCurCol;

			if (velocityX > SNAP_VELOCITY && mCurCol > 0) {
				// Fling enough to move left
				col--;
			} else if (velocityX < -SNAP_VELOCITY && mCurCol < mColsCount - 1) {
				// Fling enough to move right
				col++;
			}

			if (velocityY > SNAP_VELOCITY && mCurRow > 0) {
				// Fling enough to move up
				row--;
			} else if (velocityY < -SNAP_VELOCITY && mCurRow < mRowsCount - 1) {
				// Fling enough to move down
				row++;
			}
			

			if (row == mCurRow && col == mCurCol) {
				if (velocityX == 0 && velocityY == 0) {
					snapToDestination();
				}
				
				else {
					
					mOnX = (int) event.getRawX();
					mOnY = (int) event.getRawY();
					Log.e(TAG, "rawX =" +mOnX + "  rawY ="+ mOnY);
					sendMotionEvent(mOnX,mOnY, KeyEvent.ACTION_UP);
					
					View onClickItem = getViewAtViewGroup(mOnX,mOnY);
					FocusItemModle<DemoModle> onClickItemModle = maps.get(onClickItem);
					
					if(mOnItemClickListener != null && onClickItem != null) {
						Log.e(TAG, "View 2=" +mOnX + "  rawY 2 ="+ mOnY);
						
						 row =  onClickItemModle.getCol();
						 col =  onClickItemModle.getCol();
						mOnItemClickListener.onItemClick(this,
								  onClickItem, onClickItemModle,
								onClickItemModle.getRow(), onClickItemModle.getPostion(), onClickItemModle.getCol(), onClickItemModle.getId());
						
						snapTo(row, col);
						
					}

				}
			}
				
			else {
				snapTo(row, col);
				if (mFocusListener != null)
					mFocusListener.scrollto(row, col);
			} 

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}
	
	private View getOnClickItemView(int x, int y){
		
		return  getViewAtViewGroup(x, y);
		
	}

	public interface FocusListener {

		public void scrollto(int row, int col);
	}
	
	/**
	 * FocusView
	 * Interface definition for a callback to be invoked when an item in this
      * FocusView item has been clicked.
	 * @author liuyongkui.
	 * @param <T>
	 */
	public interface OnItemClickListener<T> {
        
        /**
         * @param mFocusView
         *   FoucusView.
         * @param view
         *   focus View item.
         * @param col
         *    col Num.
         * @param row
         *    row Num.
         * @param id
         *    item id.
         */

		void onItemClick(FocusView mFocusView, View focusView,
				FocusItemModle<DemoModle> focusItem, int Postion, int row, int col, long id);

		
      
    }
	
	/**
	 * FocusView
	 * Interface definition for a callback to be invoked when an item in this
     * FocusView item has been Selected.
	 * @author liuyongkui.
	 * @param <T>
	 */
	public interface OnItemSelectedListener <T> {
		
		 /**
         * @param <T>
		 * @param mFocusView
         *   FoucusView.
         * @param view
         *   focus View item.
         * @param col
         *    col Num.
         * @param row
         *    row Num.
         * @param id
         *    item id.
         */
		public void onItemSelected(FocusView metroView, View view, T modle, int col, int row, long id);
	}
 
    /**
     * get Onclicked ItemView
     * 
     * 
     * @param X
     * @param Y
     * @return getView
     */
    public View getViewAtViewGroup(int x, int y) {
        return findViewByXY(this, x, y);
    }
 
    /**
     * 
     * @param view
     * @param x
     * @param y
     * @return
     */
    private View findViewByXY(View view, int x, int y) {
        View targetView = null;
        if (view instanceof ViewGroup) {
            // 父容器,遍历子控件
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
            	Log.e(TAG, "v.getChildCount()=" + v.getChildCount());
                targetView = findViewByXY(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        return targetView;
 
    }
 
    /**
     * @param view
     * @param x
     * @param y
     * @return
     */
    private View getTouchTarget(View view, int x, int y) {
        View targetView = null;
        
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
            	Log.e(TAG, "child=" + child.getId());
                targetView = child;
                break;
            }
        }
        return targetView;
    }
 
    /** check is view isTouchPoint
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }
    
	@SuppressLint({ "InlinedApi", "NewApi" })
	private void sendMotionEvent(int x, int y, int action) {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		int metaState = 0;
		MotionEvent motionEvent = MotionEvent.obtain(
				downTime, 
				eventTime, 
				action, 
				x, 
				y, 
				metaState
				);
		if(action == MotionEvent.ACTION_HOVER_MOVE) {
			motionEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);
			FocusView .this.dispatchGenericMotionEvent(motionEvent);
		} else if (action == KeyEvent.ACTION_UP) {
			
			KeyEvent keyEvent =new KeyEvent(action, KeyEvent.KEYCODE_DPAD_CENTER);
			
			FocusView .this.dispatchKeyEvent(keyEvent);
		} else {
			FocusView .this.dispatchTouchEvent(motionEvent);
		}
	}

	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			return FocusView.this.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return FocusView.this.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			synchronized(FocusView.this){
				mNextX += (int)distanceX;
			}
			requestLayout();
			
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Rect viewRect = new Rect();
			for (int i=0;i < getChildCount();i++) {
				View child = getChildAt(i);
				FocusItemModle<DemoModle> onClickItemModle = maps.get(child);
				int left = child.getLeft();
				int right = child.getRight();
				int top = child.getTop();
				int bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if(viewRect.contains((int)e.getX(), (int)e.getY())){
					if(mOnItemClickListener != null){
						mOnItemClickListener.onItemClick(FocusView.this, child, onClickItemModle, onClickItemModle.getPostion(),onClickItemModle.getRow()
								,onClickItemModle.getCol(), onClickItemModle.getId() );
					}
					/*if(mOnItemSelected != null){
						mOnItemSelected.onItemSelected(HorizontialListViewe.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}*/
					break;
				}
				
			}
			return true;
		}
		
		
		
	};
	private Drawable color;
	
	
	protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		synchronized(FocusView.this){
		mScroller.fling(mNextX, 0, (int)-velocityX, 0, 0, mMaxX, 0, 0);
	}
	requestLayout();
	
	return true;
	}


	protected boolean onDown(MotionEvent e) {
	
		mScroller.forceFinished(true);
	return true;
	
}



	
}
