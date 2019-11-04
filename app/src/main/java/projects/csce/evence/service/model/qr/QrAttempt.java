package projects.csce.evence.service.model.qr;


import android.graphics.Bitmap;

public abstract class QrAttempt
{
    public static class Success extends QrAttempt
    {
        public Bitmap bitmap;
        public Success(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }
    }

    public static class Failure extends QrAttempt
    {
        public Throwable e;
        public Failure(Throwable e)
        {
            this.e = e;
        }
    }
}