package projects.csce.evence.service.repository;

import projects.csce.evence.service.model.DataObject;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitClient
{
    @POST()
    DataObject postDataToServer(@Body String data);
}