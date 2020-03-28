package cn.cbsd.dogtag.UI;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.Data.DogViolationBean;
import cn.cbsd.dogtag.R;
import cn.cbsd.dogtag.Tools.FileUtils;


public class ViolationUnitAdapter extends RecyclerView.Adapter<ViolationUnitAdapter.CPViewHolder> {

    private Context mContext;
    List<DogViolationBean> list;

    public ViolationUnitAdapter(Context context, List<DogViolationBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public ViolationUnitAdapter.CPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViolationUnitAdapter.CPViewHolder holder = new ViolationUnitAdapter.CPViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.adapter_violation_unit, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(ViolationUnitAdapter.CPViewHolder holder, final int position) {
        holder.tv_personName.setText(list.get(position).getPersonName());
        holder.tv_dogname.setText(list.get(position).getDogName());
        if(list.get(position).getViolation_message().length()>4){
            holder.tv_violation_message.setText(list.get(position).getViolation_message().substring(0,4)+"...");
        }else {
            holder.tv_violation_message.setText(list.get(position).getViolation_message());
        }
        holder.tv_dealStatus.setText(list.get(position).getDealStatus());
        if (list.get(position).getDealStatus().equals("待处理")){
            holder.tv_dealStatus.setTextColor(Color.RED);
        }
        holder.tv_datetime.setText(list.get(position).getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    ViolationUnitAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ViolationUnitAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    class CPViewHolder extends RecyclerView.ViewHolder {
        TextView tv_personName;
        TextView tv_dogname;
        TextView tv_violation_message;
        TextView tv_dealStatus;
        TextView tv_datetime;
        public CPViewHolder(View view) {
            super(view);
            tv_personName = (TextView) view.findViewById(R.id.tv_personName);
            tv_dogname = (TextView) view.findViewById(R.id.tv_dogname);
            tv_violation_message = (TextView) view.findViewById(R.id.tv_violation_message);
            tv_dealStatus = (TextView) view.findViewById(R.id.tv_dealStatus);
            tv_datetime = (TextView) view.findViewById(R.id.tv_datetime);

        }
    }
}
