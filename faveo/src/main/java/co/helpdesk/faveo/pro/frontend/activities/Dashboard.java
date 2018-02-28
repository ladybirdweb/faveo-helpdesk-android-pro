package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class Dashboard extends AppCompatActivity {
String content;
TextView textView;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        textView= (TextView) findViewById(R.id.dashboardcreate);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,TicketsRelated.class);
                startActivity(intent);
            }
        });
        content="<b>Requestor/email id</b>: You can enter the mail id if the account already exists in Faveo. If you are a new user then click on the “+” button to create a new account. \n" +
                "You can enter the Requestor <b>maid id</b>, <b>First name</b>, <b>Last Name</b> and <b>Phone number</b> when you are creating a new account.<br />" +
                "<b>CC</b>: You can add collaborator by entering the name of the agents.<br />" +
                "<b>First Name</b>: Enter the first name.<br />" +
                "<b>Last Name</b>: Enter the last name.<br />" +
                "<b>Mobile</b>: Select the country and enter the mobile number.<br />" +
                "<b>Phone</b>: Enter an additional number, this is optional.<br />" +
                "<b>Subject</b>: Enter the subject of the ticket.<br />" +
                "<b>Priority</b>: You can choose the priority from the drop down box.<br />" +
                "You can assign this ticket to a particular agent on Faveo Helpdesk." +
                "After filling out the details click on the check mark at the top right to create a ticket.\n";
        textView.setText(Html.fromHtml(content));
    }
}
