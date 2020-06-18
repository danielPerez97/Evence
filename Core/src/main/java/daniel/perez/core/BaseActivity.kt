package daniel.perez.core

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity: AppCompatActivity()
{
    val disposables = CompositeDisposable()
}