package proglife.fora.bank.di

import javax.inject.Scope

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class GlobalFeatureScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureScope