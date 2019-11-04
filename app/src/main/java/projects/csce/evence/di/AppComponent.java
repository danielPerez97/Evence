package projects.csce.evence.di;

import javax.inject.Singleton;

import dagger.Component;
import projects.csce.evence.di.loggedin.LoggedInModule;
import projects.csce.evence.di.loggedin.LoggedInSubComponent;
import projects.csce.evence.view.main.MainActivity;

@Singleton
@Component(modules = {NetworkModule.class, LoginModule.class})
public interface AppComponent
{
    void inject(MainActivity mainActivity);

    LoggedInSubComponent newLoggedInSubComponent(LoggedInModule module);
}
