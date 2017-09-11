package com.wei.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wei.demo.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wei} on 2017/6/9.
 */

public abstract class CommRecylerViewAdapter<T> extends RecyclerView.Adapter<ViewHolderHelper> {

    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();
    protected int mLayoutId;
    protected OnItemClickListener mOnItemClickListener;
    protected LayoutInflater mLayoutInflater;

    public CommRecylerViewAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        this.mDatas = datas;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolderHelper onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = mLayoutInflater.inflate(mLayoutId, parent, false);
        ViewHolderHelper holderHelper = ViewHolderHelper.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, holderHelper, viewType);
        return holderHelper;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolderHelper holder, int position) {
        holder.updatePosition(position);
        convert(holder, mDatas.get(position));
    }

    private static final String TAG = "debug_CommRecylerAdapter";

    @Override
    public int getItemCount() {
        Log.e(TAG, "65行...getItemCount: size = " + mDatas.size());
        return mDatas.size();
    }


    private void setListener(final ViewGroup parent, final ViewHolderHelper viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    public abstract void convert(ViewHolderHelper viewHolderHelper, T t);


    public void add(T elem) {
        mDatas.add(elem);
        notifyDataSetChanged();
    }

    public void addAt(int location, T elem) {
        mDatas.add(location, elem);
        notifyItemChanged(location);
    }

    public void addAll(List<T> elements) {
        mDatas.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAllAt(int location, List<T> elements) {
        mDatas.addAll(location, elements);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mDatas.remove(elem);
        notifyDataSetChanged();
    }

    public void removeAt(int location) {
        mDatas.remove(location);
        notifyDataSetChanged();
    }

    public void removeAll(List<T> elements) {
        mDatas.removeAll(elements);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void replace(T oldElem, T newElem) {
        replaceAt(mDatas.indexOf(oldElem), newElem);
    }

    public void replaceAt(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elements) {
        if (mDatas.size() > 0) {
            mDatas.clear();
        }
        mDatas.addAll(elements);
        notifyDataSetChanged();
    }

    public T get(int position) {
        if (position > mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }

    public List<T> getAll() {
        return mDatas;
    }

    public int getSize() {
        return mDatas.size();
    }


    public boolean constains(T elem) {
        return mDatas.contains(elem);
    }
}
