package cn.cbsd.dogtag.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.cbsd.dogtag.R;
import cn.cbsd.dogtag.Tools.FileUtils;

public class DogPicAdapter extends ArrayAdapter<String> {

    private int recourceId;

    List<String> images;

    public DogPicAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.recourceId = resource;
        this.images = objects;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String string_image = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(recourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dogPic = view.findViewById(R.id.iv_dogPic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.dogPic.setImageBitmap(FileUtils.base64ToBitmap(string_image));
        return view;
    }

    class ViewHolder {
        ImageView dogPic;
    }
}
