package projects.csce.evence.di.appscope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import daniel.perez.core.di.ViewModelFactory
import daniel.perez.generateqrview.GenerateQrViewModel
import daniel.perez.qrcameraview.viewmodel.QrReaderViewModel
import projects.csce.evence.di.viewmodel.ViewModelKey
import projects.csce.evence.viewmodel.MainViewModel

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GenerateQrViewModel::class)
    abstract fun generateQrViewModel(viewModel: GenerateQrViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(QrReaderViewModel::class)
    abstract fun qrReaderViewModel(viewModel: QrReaderViewModel?): ViewModel?
}