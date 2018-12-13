//package co.helpdesk.faveo.pro.frontend.adapters;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import co.helpdesk.faveo.pro.R;
//import co.helpdesk.faveo.pro.model.DataModel;
//
///**
// * Created by Lenovo on 6/29/2017.
// */
//
//public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {
//    Context mContext;
//    int layoutResourceId;
//    DataModel data[] = null;
//
//    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {
//
//        super(mContext, layoutResourceId, data);
//        this.layoutResourceId = layoutResourceId;
//        this.mContext = mContext;
//        this.data = data;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        View listItem = convertView;
//
//        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//        listItem = inflater.inflate(layoutResourceId, parent, false);
//
//        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageView2);
//        TextView textViewName = (TextView) listItem.findViewById(R.id.inboxtv);
//        TextView countNumber= (TextView) listItem.findViewById(R.id.inbox_count);
//
//        DataModel folder = data[position];
//
////        listItem.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Log.d("clickedOnInbox","clickedon"+position);
////
////            }
////        });
//
//        imageViewIcon.setImageResource(folder.getIcon());
//        textViewName.setText(folder.getName());
//        countNumber.setText(folder.getCount());
//
//        return listItem;
//    }
//
//}
