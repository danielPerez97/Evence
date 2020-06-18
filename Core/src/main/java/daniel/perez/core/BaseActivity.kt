package daniel.perez.core

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity(), DialogClosable
{
    override fun finishActivity()
    {
        finish()
    }

}