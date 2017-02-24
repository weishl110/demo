package com.wei.demo.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wei.demo.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by wei on 2016/10/9.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> implements ICallBackSwopLocation{

    private static final String TAG = "zpy_MyAdapter";
    private ArrayList<String> list;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.layout_item, null);

//        MyHolder myHolder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent,false));
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onMoveItem(int srcPosition, int targetPosition) {
        Collections.swap(list,srcPosition,targetPosition);
        notifyItemMoved(srcPosition,targetPosition);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
