package projects.csce.evence.service.repository;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DriveService
{
    @POST("/upload/drive/v3/files?uploadType=media")
    Observable<Response<String>> uploadFile(File file);

    @PUT("/upload/drive/v3/files?uploadType=media")
    Observable<Response<String>> updateFile(File file);
}
