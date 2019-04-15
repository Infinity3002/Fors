package proglife.fora.bank.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class BasePresenter<V: MvpView>: MvpPresenter<V>() {

    private val cd: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        cd.clear()
        super.onDestroy()
    }

    protected fun unSubscribeOnDestroy(disposable: Disposable) {
        cd.add(disposable)
    }

    protected fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { this.unSubscribeOnDestroy(it)}
        }
    }

}