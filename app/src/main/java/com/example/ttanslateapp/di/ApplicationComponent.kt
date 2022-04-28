package com.example.ttanslateapp.di

import android.app.Application
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(modifyWordFragment: ModifyWordFragment)

    @Component.Factory
    interface ApplicationFactory {
        fun create(
            @BindsInstance context: Application
        ): ApplicationComponent
    }
}