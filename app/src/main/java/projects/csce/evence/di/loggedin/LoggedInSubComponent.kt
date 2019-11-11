package projects.csce.evence.di.loggedin

import dagger.Subcomponent
import projects.csce.evence.view.ui.SecondActivity

@SignedInScope
@Subcomponent(modules = [LoggedInModule::class])
interface LoggedInSubComponent
{
	fun inject(activity: SecondActivity?)
}