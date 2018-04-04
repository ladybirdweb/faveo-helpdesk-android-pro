package co.helpdesk.faveo.pro.frontend.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.model.AttachmentModel;

/**
 * Created by Sayar Samanta on 3/19/2018.
 */

public class AttachmnetAdapter extends ArrayAdapter<AttachmentModel> {

    private Context mContext;
    private List<AttachmentModel> moviesList = new ArrayList<>();

    public AttachmnetAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<AttachmentModel> list) {
        super(context, 0 , list);
        mContext = context;
        moviesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_item_row,parent,false);

        AttachmentModel currentMovie = moviesList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.label);
        name.setText(currentMovie.getName());
        return listItem;
    }
}
