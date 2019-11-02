package projects.csce.evence.di.loggedin;

import dagger.Subcomponent;
import projects.csce.evence.view.SecondActivity;

@SignedInScope
@Subcomponent(modules = {LoggedInModule.class})
public interface LoggedInSubComponent
{
    void inject(SecondActivity activity);
}
