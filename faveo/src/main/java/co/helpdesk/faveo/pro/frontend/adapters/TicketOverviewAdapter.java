package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.TicketOverview;

/**
 * This adapter is for the tickets(inbox,mytickets,trash...) page in the main page.
 */
public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
    private List<TicketOverview> ticketOverviewList;
    String subject;
    int length=0;
    private Context context;
    ArrayList<Integer> checked_items= new ArrayList<>();
    ArrayList<String> ticketSubject=new ArrayList<>();
    private SparseBooleanArray mSelectedItemsIds;
    private List<Integer> selectedIds = new ArrayList<>();


    public TicketOverviewAdapter(Context context,List<TicketOverview> ticketOverviewList) {
        this.ticketOverviewList = ticketOverviewList;
        this.context=context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getItemCount() {
        return ticketOverviewList.size();
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, final int i) {
        final TicketOverview ticketOverview = ticketOverviewList.get(i);
        String letter = String.valueOf(ticketOverview.clientName.charAt(0)).toUpperCase();
         TextDrawable.IBuilder mDrawableBuilder;

        Log.d("letter",letter);
//        if (selectedIds.contains(id)){
//            //if item is selected then,set foreground color of FrameLayout.
//            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#bdbdbd"));
//        }
//        else {
//            //else remove selected item color.
//            //holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context,android.R.color.transparent)));
//        }
        subject=ticketOverview.ticketSubject;
        if (subject.startsWith("=?UTF-8?Q?")&&subject.endsWith("?=")){
            String first=subject.replace("=?UTF-8?Q?","");
            String second=first.replace("_"," ");
            String third=second.replace("=C2=A0","");
            String fourth=third.replace("?=","");
            String fifth=fourth.replace("=E2=80=99","'");
            ticketViewHolder.textViewSubject.setText(fifth);
        }
        else{
            ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
        }

        if (ticketOverview.ticketAttachments.equals("0")) {
            ticketViewHolder.attachementView.setVisibility(View.GONE);
        } else {
            int color = Color.parseColor("#808080");
            ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
            ticketViewHolder.attachementView.setColorFilter(color);
        }
        if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
//            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

             if (Helper.compareDates(ticketOverview.dueDate) == 2) {
                ticketViewHolder.textViewduetoday.setVisibility(View.VISIBLE);
                ticketViewHolder.textViewduetoday.setText(R.string.due_today);
                 //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#FFD700"));
                 ((GradientDrawable)ticketViewHolder.textViewduetoday.getBackground()).setColor(Color.parseColor("#3da6d7"));
                 ticketViewHolder.textViewduetoday.setTextColor(Color.parseColor("#ffffff"));
                //ticketViewHolder.textViewOverdue.setBackgroundColor();

            }
            else  if (Helper.compareDates(ticketOverview.dueDate) == 1) {
            ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                 ticketViewHolder.textViewOverdue.setText(R.string.overdue);
                 //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#ef9a9a"));
//                GradientDrawable drawable = (GradientDrawable) context.getDrawable(ticketViewHolder.textViewOverdue);
//
////set color
//                 drawable.setColor(color);
                 ((GradientDrawable)ticketViewHolder.textViewOverdue.getBackground()).setColor(Color.parseColor("#3da6d7"));
                 ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#ffffff"));
             }
        else {
                ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
            }


        ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");

        ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
        if (ticketOverview.getClientName().startsWith("=?")){
            String clientName=ticketOverview.getClientName().replaceAll("=?UTF-8?Q?","");
            String newClientName=clientName.replaceAll("=E2=84=A2","");
            String finalName=newClientName.replace("=??Q?","");
            String name=finalName.replace("?=","");
            String newName=name.replace("_"," ");
            Log.d("new name",newName);
            ticketViewHolder.textViewClientName.setText(newName);
        }
        else{
            ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);

        }
        if (ticketOverview.ticketPriorityColor.equals("null")){
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
        }
        else if (ticketOverview.ticketPriorityColor != null) {
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
        }


//        else if (ticketOverview.ticketPriorityColor.equals("null")){
//            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
//        }
        if (!ticketOverview.ticketTime.equals("null")) {
            ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));
        }
        else{
            ticketViewHolder.textViewTime.setVisibility(View.GONE);
        }

if (!ticketOverview.countthread.equals("0")){
    ticketViewHolder.countThread.setText("("+ticketOverview.getCountthread()+")");
}
else{
    ticketViewHolder.countThread.setVisibility(View.GONE);
}

        switch (ticketOverview.sourceTicket) {
            case "chat": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.chat);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "web": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.web);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "agent": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "email": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "facebook": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.facebook);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "twitter": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.twitter);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            case "call": {
                int color = Color.parseColor("#808080");
                ticketViewHolder.source.setImageResource(R.drawable.ic_call_black_24dp);
                ticketViewHolder.source.setColorFilter(color);
                break;
            }
            default:
                ticketViewHolder.source.setVisibility(View.GONE);
                break;
        }
if (!ticketOverview.countcollaborator.equals("0")){

    int color = Color.parseColor("#808080");
    ticketViewHolder.countCollaborator.setImageResource(R.drawable.ic_group_black_24dp);
    ticketViewHolder.countCollaborator.setColorFilter(color);
}
else if (ticketOverview.countcollaborator.equals("0")){
ticketViewHolder.countCollaborator.setVisibility(View.GONE);
}

if (!ticketOverview.agentName.equals("Unassigned")){
    ticketViewHolder.agentAssignedImage.setVisibility(View.VISIBLE);
    ticketViewHolder.agentAssigned.setText(ticketOverview.getAgentName());
}
else{
    ticketViewHolder.agentAssigned.setText("Unassigned");
    ticketViewHolder.agentAssignedImage.setVisibility(View.GONE);
}

//else if (ticketOverview.getAgentName().equals("Unassigned")){
//    ticketViewHolder.agentAssignedImage.setVisibility(View.GONE);
//}


if (!ticketOverview.lastReply.equals("client")){
    int color=Color.parseColor("#e9e9e9");
ticketViewHolder.ticket.setBackgroundColor(color);
}



if (ticketOverview.clientPicture.equals("")){
    ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

}
else if (ticketOverview.clientPicture.contains(".jpg")){
    mDrawableBuilder = TextDrawable.builder()
            .round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
    Picasso.with(context).load(ticketOverview.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

    //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

}
else{
    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable drawable = TextDrawable.builder()
            .buildRound(letter,generator.getRandomColor());
    ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
    ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

}
//   else if (ticketOverview.clientPicture.startsWith("")){
////    Glide.with(context)
////            .load(imageUrl)
////            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
////            .dontTransform()
////            .placeholder(placeholder)
////            .into(imageView);
//
////    IImageLoader imageLoader = new PicassoLoader();
   //imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);
//    //imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);
//}

//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect("A", Color.RED, 10); // radius in px
//
//        TextDrawable drawable2 = TextDrawable.builder()
//                .buildRound("A", Color.RED);




//        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                Prefs.putString("cameFromNotification","none");
                Prefs.putString("ticketThread","");
                intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                Prefs.putString("TICKETid",ticketOverview.ticketID+"");
                Prefs.putString("ticketId",ticketOverview.ticketID+"");
                Prefs.putString("ticketstatus",ticketOverview.getTicketStatus());
                intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                Log.d("clicked","onRecyclerView");
                v.getContext().startActivity(intent);
            }
        });
//        ticketViewHolder.ticket.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
//                ticketViewHolder.checkBox1.setChecked(true);
//                length++;
//                Log.d("noofitems",""+length);
//                Prefs.putInt("NoOfItems",length);
//
////                ticketOverviewList.get(i).getTicketID();
////                Log.d("position",""+ticketOverviewList.get(i).getTicketID());
////                if (ticketViewHolder.checkBox1.isEnabled()){
////
////                }
////                else{
////                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
////                }
////                if (ticketViewHolder.checkBox1.isChecked()){
////
////                }else{
////                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
////                }
//                return true;
//            }
//        });


//        ticketViewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ticketViewHolder.checkBox1.isChecked())
//                    ticketViewHolder.checkBox1.setChecked(false);
//                else
//                    ticketViewHolder.checkBox1.setChecked(true);
//            }
//        });



//        ticketViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//
//                if (isChecked){
////                    ticketViewHolder.checkBox1.setChecked(true);
////                    Prefs.putString("checkboxstate","checked");
//
//                    stringBuffer.append(ticketOverview.getTicketID()+",");
//                    Log.d("ids",stringBuffer.toString());
//
//                }
//                else{
//                    Prefs.putString("checkboxstate","unchecked");
//                    stringBuffer.toString().replace(""+ticketOverview.getTicketID(),"");
//                    Log.d("ids",stringBuffer.toString());
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                    notifyDataSetChanged();
//                }
////
////                String state=Prefs.getString("checkboxstate",null);
////
////                if (state.equals("checked")){
////                    ticketViewHolder.checkBox1.setSelected(true);
////                    notifyDataSetChanged();
////                }
////                else{
////                    ticketViewHolder.checkBox1.setSelected(false);
////                    notifyDataSetChanged();
////                }
//
//
//            }
//        });

    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }
    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }


    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    public void setSelectedIds(ArrayList<Integer> checked_items) {
        this.checked_items = checked_items;
        notifyDataSetChanged();
    }
    public TicketOverview getItem(int position){
        return ticketOverviewList.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        public View ticket;
        public ImageView roundedImageViewProfilePic;
        public TextView textViewTicketID;
        public TextView textViewTicketNumber;
        public TextView textViewClientName;
        public TextView textViewSubject;
        public RelativeTimeTextView textViewTime;
        public TextView textViewOverdue;
        public View ticketPriority;
        // TextView ticketStatus;
        public ImageView attachementView;
        public CheckBox checkBox1;
        public ImageView countCollaborator;
        public ImageView source;
        public TextView countThread;
        public TextView agentAssigned;
        public ImageView agentAssignedImage;
        public TextView textViewduetoday;
        TicketViewHolder(View v) {
            super(v);
            ticket = v.findViewById(R.id.ticket);
            attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
            ticketPriority = v.findViewById(R.id.priority_view);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
            checkBox1= (CheckBox) v.findViewById(R.id.checkbox);
            countCollaborator= (ImageView) v.findViewById(R.id.collaborator);
            countThread= (TextView) v.findViewById(R.id.countthread);
            source= (ImageView) v.findViewById(R.id.source);
            agentAssigned= (TextView) v.findViewById(R.id.agentassigned);
            agentAssignedImage= (ImageView) v.findViewById(R.id.agentAssigned);
            textViewduetoday= (TextView) v.findViewById(R.id.duetoday);


        }

    }

}
