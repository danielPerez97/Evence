package projects.csce.evence.http;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitClient
{
    @POST()
    DataObject postDataToServer(@Body String data);
}