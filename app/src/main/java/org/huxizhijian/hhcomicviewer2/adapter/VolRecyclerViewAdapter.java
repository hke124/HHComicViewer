package org.huxizhijian.hhcomicviewer2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.huxizhijian.hhcomicviewer2.R;

import java.util.List;

/**
 * 对应显示vol列表的适配器
 * Created by wei on 2016/8/25.
 */
public class VolRecyclerViewAdapter extends RecyclerView.Adapter<VolRecyclerViewAdapter.VolViewHolder> {

    private List<String> mVolName;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int mCapturePosition = 0;
    private boolean mIsReaded = false;
    private List<String> mFinishedComicCaptureList;  //下载好的章节

    private Bitmap mBitmap_ok = null; //下载好的章节右下角显示完成的图片

    public VolRecyclerViewAdapter(Context context, List<String> volName) {
        this.mVolName = volName;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public VolRecyclerViewAdapter(Context context, List<String> volName, List<String> finishedComicCaptures) {
        this.mVolName = volName;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mFinishedComicCaptureList = finishedComicCaptures;
    }

    public VolRecyclerViewAdapter(Context context, List<String> volName,
                                  int capturePosition, List<String> finishedComicCaptures) {
        this.mVolName = volName;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mCapturePosition = capturePosition;
        this.mIsReaded = true;
        this.mFinishedComicCaptureList = finishedComicCaptures;
    }

    @Override
    public VolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_vol_recycler_view, parent, false);
        return new VolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VolViewHolder holder, int position) {
        holder.tv.setText(mVolName.get(position));
        if (mIsReaded && position == mCapturePosition) {
            //标记上次阅读的章节
            holder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.main_color));
        } else {
            holder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        if (mFinishedComicCaptureList != null) {
            //标记下载好的章节
            if (mFinishedComicCaptureList.contains(mVolName.get(position))) {
                //进行标记
                if (mBitmap_ok == null) {
                    //进行图片的初始化
                    mBitmap_ok = BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.ic_check_green_18dp);
                }
                holder.iv.setImageBitmap(mBitmap_ok);
                holder.iv.setVisibility(View.VISIBLE);
            }
        } else {
            holder.iv.setVisibility(View.GONE);
        }
        setUpItemEvent(holder);
    }

    protected void setUpItemEvent(final VolViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });

            //longClick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setFinishedComicCaptureList(List<String> finishedComicCaptureList) {
        mFinishedComicCaptureList = finishedComicCaptureList;
    }

    @Override
    public int getItemCount() {
        return mVolName.size();
    }

    public void setReadCapture(int capturePosition) {
        int prePosition = this.mCapturePosition;
        this.mCapturePosition = capturePosition;
        mIsReaded = true;
        notifyItemChanged(prePosition);
        notifyItemChanged(mCapturePosition);
    }

    class VolViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        CardView cv;
        ImageView iv;

        public VolViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv_vol_name_item);
            cv = (CardView) itemView.findViewById(R.id.cardView_vol_item);
            iv = (ImageView) itemView.findViewById(R.id.imageView_vol_item);
        }
    }
}
