package projects.csce.evence.di.loggedin

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

@Scope
@Retention(value = RetentionPolicy.RUNTIME)
annotation class SignedInScope
