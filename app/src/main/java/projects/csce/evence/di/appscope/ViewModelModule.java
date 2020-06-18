package projects.csce.evence.di.appscope;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import daniel.perez.core.di.ViewModelFactory;
import daniel.perez.generateqrview.GenerateQrViewModel;
import projects.csce.evence.di.viewmodel.ViewModelKey;
import projects.csce.evence.viewmodel.MainViewModel;
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel;

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

    @Binds
    @IntoMap
    @ViewModelKey(QrReaderViewModel.class)
    abstract ViewModel qrReaderViewModel(QrReaderViewModel viewModel);
}
