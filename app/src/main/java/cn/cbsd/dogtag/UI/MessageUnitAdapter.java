package cn.cbsd.dogtag.UI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cbsd.dogtag.Data.DogMessageBean;
import cn.cbsd.dogtag.R;
import cn.cbsd.dogtag.Tools.FileUtils;


public class MessageUnitAdapter extends RecyclerView.Adapter<MessageUnitAdapter.CPViewHolder> {

    private Context mContext;
    List<DogMessageBean> list;

    public MessageUnitAdapter(Context context, List<DogMessageBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public MessageUnitAdapter.CPViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MessageUnitAdapter.CPViewHolder holder = new MessageUnitAdapter.CPViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.adapter_message_unit, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(MessageUnitAdapter.CPViewHolder holder, final int position)
    {
        holder.tv_personName.setText(list.get(position).getPersonName());
        holder.tv_dogname.setText(list.get(position).getDogName());
        holder.tv_dogType.setText(list.get(position).getDogType());
        holder.iv_dogPic.setImageBitmap(FileUtils.base64ToBitmap(list.get(position).getBitmaps().get(0)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    MessageUnitAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MessageUnitAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    class CPViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_personName;
        TextView tv_dogname;
        TextView tv_dogType;
        ImageView iv_dogPic;


        public CPViewHolder(View view)
        {
            super(view);
            tv_personName = (TextView) view.findViewById(R.id.tv_personName) ;
            tv_dogname = (TextView) view.findViewById(R.id.tv_dogname);
            tv_dogType = (TextView) view.findViewById(R.id.tv_dogType);
            iv_dogPic = (ImageView)view.findViewById(R.id.iv_dogPic);

        }
    }
}
