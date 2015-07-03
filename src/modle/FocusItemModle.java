package modle;

import android.view.View;

public class FocusItemModle<T> {
	private View mFocusView = null;

	private T mModle;
	/**
	 * 起点行数
	 */
	private int mRow = 0;
	/**
	 * view占据行数
	 */
	private int mRowSpan = 1;
	/**
	 * 起点列数
	 */
	private int mCol = 0;
	/**
	 * View占据列数
	 */
	private int mColSpan = 1;
	/**
	 * View Id
	 */
	private long mId = 0X22;
	/**
	 * View 所占高度
	 */
	private int mHight;
	/**
	 * View 宽度
	 */
	private int mWigth;
	/**
	 * View 宽度
	 */
	private int mPostion;
	

	/**
	 * @param v
	 * @param row
	 * @param col
	 */
	public FocusItemModle(View v, int row, int col) {
		this(v, row, 1, col, 1);
	}

	/**
	 * @param v
	 * @param row
	 * @param rowspan
	 * @param col
	 * @param colspan
	 */
	public FocusItemModle(View v, int row, int rowspan, int col, int colspan) {
		mFocusView = v;

		setPosition(row, col);

		setId(Integer.parseInt(row + "0" + col));

		if (rowspan < 1)
			throw new IllegalArgumentException("rowspan < 1");
		mRowSpan = rowspan;

		if (colspan < 1)
			throw new IllegalArgumentException("colspan < 1");
		mColSpan = colspan;
	}

	/**
	 * @param v
	 * @param row
	 * @param rowspan
	 * @param col
	 * @param colspan
	 */
	public FocusItemModle(View v, T mModle, int row, int rowspan, int col,
			int colspan) {
		mFocusView = v;

		setPosition(row, col);

		setmModle(mModle);

		setId(Integer.parseInt(row + "0" + col));

		if (rowspan < 1)
			throw new IllegalArgumentException("rowspan < 1");
		mRowSpan = rowspan;

		if (colspan < 1)
			throw new IllegalArgumentException("colspan < 1");
		mColSpan = colspan;
	}

	/**
	 * @return
	 */
	public View getFocusView() {
		return mFocusView;
	}

	public int getRow() {
		return mRow;
	}

	public int getRowSpan() {
		return mRowSpan;
	}

	public int getCol() {
		return mCol;
	}

	public int getColSpan() {
		return mColSpan;
	}

	public void setPosition(int row, int col) {
		if (row < 0)
			throw new IllegalArgumentException("row < 0");
		mRow = row;

		if (col < 0)
			throw new IllegalArgumentException("col < 0");
		mCol = col;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public void setmModle(T mModle) {
		this.mModle = mModle;
	}

	public T getModle() {

		return mModle;
	}

	public int getmHight() {
		return mHight;
	}

	public void setmHight(int mHight) {
		this.mHight = mHight;
	}

	public int getmWigth() {
		return mWigth;
	}

	public void setmWigth(int mWigth) {
		this.mWigth = mWigth;
	}

	public int getPostion() {
		return mPostion;
	}

	public void setPostion(int mPostion) {
		this.mPostion = mPostion;
	}
	

}
