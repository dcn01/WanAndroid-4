package com.tainzhi.android.wanandroid.ui.login

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.common.util.toast
import com.tainzhi.android.wanandroid.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterFragment : BaseVMFragment<LoginViewModel>(useBinding = true) {

    override fun getLayoutResId() = R.layout.fragment_register

    override fun initVM(): LoginViewModel {
        val viewModel = requireActivity().getViewModel<LoginViewModel>()
        return viewModel
    }

    override fun initView() {
        toolbar.setTitle(R.string.register)
        toolbar.setNavigationOnClickListener { onBack() }
    
        requireActivity().onBackPressedDispatcher.addCallback { onBack() }
    
    }

    override fun initData() {
        (mBinding as FragmentRegisterBinding).viewModel = mViewModel
    }

    override fun startObserve() {
        mViewModel.apply {
            registerUser.observe(viewLifecycleOwner, Observer {
                it?.run {
                    mViewModel.register()
                }
            })

            uiState.observe(viewLifecycleOwner, Observer {
                if (it.showProgress) {
                    dismissKeyboard(userNameEt.windowToken)
                }

                it.showSuccess?.let {
                    activity?.toast(R.string.register_success)
                    onBackToMain()
                }

                it.showError?.let { err ->
                    activity?.toast(err)
                }
            })
        }
    }

    private fun onBack() {
        findNavController().navigateUp()
    }

    private fun onBackToMain() {
        findNavController().popBackStack(R.id.tabHostFragment, false)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}
