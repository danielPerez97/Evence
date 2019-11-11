package projects.csce.evence.service.model.qr

import android.graphics.Bitmap

sealed class QrAttempt
{
	class Success(var bitmap: Bitmap) : QrAttempt()
	class Failure(var e: Throwable) : QrAttempt()
}