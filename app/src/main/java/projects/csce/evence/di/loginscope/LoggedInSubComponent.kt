package projects.csce.evence.di.loginscope

import dagger.Subcomponent
import projects.csce.evence.di.loginscope.LoggedInModule
import projects.csce.evence.di.loginscope.SignedInScope
import projects.csce.evence.view.ui.SecondActivity

@SignedInScope
@Subcomponent(modules = [LoggedInModule::class])
interface LoggedInSubComponent
{
	fun inject(activity: SecondActivity?)
}