package projects.csce.evence.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import projects.csce.evence.di.viewmodel.ViewModelFactory;
import projects.csce.evence.di.viewmodel.ViewModelKey;
import projects.csce.evence.viewmodel.GenerateQrViewModel;
import projects.csce.evence.viewmodel.MainViewModel;

@Module
public abstract class ViewModelModule
{
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel mainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GenerateQrViewModel.class)
    abstract ViewModel generateQrViewModel(GenerateQrViewModel viewModel);
}
