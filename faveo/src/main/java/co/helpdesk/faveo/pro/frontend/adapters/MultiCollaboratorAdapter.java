package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.model.MultiCollaborator;

/**
 * Created by Sayar Samanta on 4/6/2018.
 */

public class MultiCollaboratorAdapter extends ArrayAdapter<MultiCollaborator> {
    ArrayList<MultiCollaborator> customers, tempCustomer, suggestions ;
    Context context;
    public MultiCollaboratorAdapter(Context context, ArrayList<MultiCollaborator> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects ;
        this.tempCustomer =new ArrayList<MultiCollaborator>(objects);
        this.suggestions =new ArrayList<MultiCollaborator>(objects);
        this.context=context;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MultiCollaborator customer = getItem(position);
        IImageLoader imageLoader;
        //String letter= String.valueOf(customer.getFirst_name().charAt(0));
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collaborator_row, parent, false);
        }
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.textView_collaborator_name);
        ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.imageView_collaborator);
        TextView textViewEmail= (TextView) convertView.findViewById(R.id.textView_client_email);

        if (txtCustomer != null&&customer.getFirst_name()!=null)
            txtCustomer.setText(customer.getFirst_name() + " " + customer.getLast_name());

        if (ivCustomerImage != null && customer.getProfile_pic() != null) {
            if (customer.getProfile_pic().equals(".jpg")||customer.getProfile_pic().equals(".jpeg")||customer.getProfile_pic().equals(".png")) {
                Picasso.with(context).load(customer.getProfile_pic()).transform(new CircleTransform()).into(ivCustomerImage);
            }
            else{
                Log.d("cameInThisBlock","true");
                Picasso.with(context).load(customer.getProfile_pic()).transform(new CircleTransform()).into(ivCustomerImage);
            }
        }

        if (textViewEmail!=null&&customer.getEmail()!=null)
            textViewEmail.setText(customer.getEmail());

        // Now assign alternate color for rows
//        if (position % 2 == 0)
//            convertView.setBackgroundColor(getContext().getColor(R.color.odd));
//        else
//            convertView.setBackgroundColor(getContext().getColor(R.color.even));

        return convertView;
    }
}
