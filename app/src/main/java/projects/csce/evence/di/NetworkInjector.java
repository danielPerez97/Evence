package projects.csce.evence.di;

import javax.inject.Singleton;

import dagger.Component;
import projects.csce.evence.MainActivity;
import projects.csce.evence.SecondActivity;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkInjector
{
    void inject(MainActivity mainActivity);
    void inject(SecondActivity secondActivity);
}
