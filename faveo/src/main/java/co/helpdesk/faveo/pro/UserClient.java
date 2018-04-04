package co.helpdesk.faveo.pro;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Sayar Samanta on 3/22/2018.
 */

public interface UserClient {

    @Multipart
    @POST("/sayarnew/public/api/v1/helpdesk/create?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9qYW1ib3JlZWJsaXNzLmNvbS9zYXlhcm5ldy9wdWJsaWMvYXBpL3YxL2F1dGhlbnRpY2F0ZSIsImlhdCI6MTUyMjE0NTM1NCwiZXhwIjoxNTIyMTQ1NTk0LCJuYmYiOjE1MjIxNDUzNTQsImp0aSI6IjFLMkdDdUt2azBrbUdTNzAifQ.44BTWBT2gYel-iUrivvQ04o1WM1_Lu95-tmmGKbEgKk\" +\n" +
            "            \"&user_id=1&subject=hgghhhhhh&body=ggggghhhhhhhhhhh&help_topic=1\" +\n" +
            "            \"&priority=2&first_name=gggg&last_name=gggg&email=asmita.k@gmail.com&assigned=1&phone=965286666null&code=91&mobile=685214582")
    Call<ResponseBody> createTicket(
            @Part ("description")RequestBody description,
            @Part MultipartBody.Part file);
}

