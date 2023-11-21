package com.apero.realistic.classes.router

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.classes.bottomsheet.GenerateSettingBottomSheet
import com.apero.realistic.classes.bottomsheet.GenerateSettingUI
import com.apero.realistic.classes.bottomsheet.PromptHistoryBottomSheet
import com.apero.realistic.classes.bottomsheet.editprompt.EditPromptBottomSheet
import com.apero.realistic.classes.dialog.ArtworkPreviewDialog
import com.apero.realistic.classes.dialog.ConfirmDialog
import com.apero.realistic.classes.dialog.LoadingDialog
import com.apero.realistic.classes.dialog.SpeedToFutureDialog
import com.apero.realistic.classes.language.LanguageActivity
import com.apero.realistic.classes.main.MainActivity
import com.apero.realistic.classes.profile.ProfileActivity
import com.apero.realistic.classes.result.GenerateResultActivity
import com.apero.realistic.classes.setting.SettingActivity

object Router {
    fun makeUILanguage(from: BaseActivity<*>, includeClearTask: Boolean) {
        val intent = Intent(from, LanguageActivity::class.java)
        if (includeClearTask) intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        from.startActivity(intent)
    }

    fun makeUIMainClearTask(from: BaseActivity<*>) {
        val intent = Intent(from, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        from.startActivity(intent)
    }

    fun makeUISetting(from: BaseActivity<*>) {
        val intent = Intent(from, SettingActivity::class.java)
        from.startActivity(intent)
    }

    fun makeUIResult(from: BaseActivity<*>) {
        val intent = Intent(from, GenerateResultActivity::class.java)
        from.startActivity(intent)
    }

    fun makeUIProfile(from: BaseActivity<*>) {
        val intent = Intent(from, ProfileActivity::class.java)
        from.startActivity(intent)
    }

    fun makeUIBottomSheetPromptHistory(from: BaseFragment<*>) {
        val promptHistory = PromptHistoryBottomSheet()
        promptHistory.show(from.childFragmentManager, promptHistory.tag)
    }

    fun makeUIBottomSheetGenerateSetting(
        from: BaseFragment<*>,
        settings: ArrayList<GenerateSettingUI>
    ) {
        val generateSetting = GenerateSettingBottomSheet()
        generateSetting.settings = settings
        generateSetting.show(from.childFragmentManager, generateSetting.tag)
    }

    fun makeUIBottomSheetEditPrompt(from: BaseActivity<*>) {
        val editPrompt = EditPromptBottomSheet()
        editPrompt.show(from.supportFragmentManager, editPrompt.tag)
    }

    fun makeUIDialogLoading(from: BaseActivity<*>, gotoPremium: (() -> Unit)? = null) {
        val dialogLoading = LoadingDialog(gotoPremium = gotoPremium)
        dialogLoading.show(from.supportFragmentManager, dialogLoading.tag)
    }

    fun makeUIDialogTryArtwork(from: BaseFragment<*>, onTryThisCompletion: (() -> Unit)? = null) {
        val dialog = ArtworkPreviewDialog()
        dialog.onTryThisCompletion = onTryThisCompletion
        dialog.show(from.childFragmentManager, dialog.tag)
    }

    fun makeUIDialogConfirm(
        manager: FragmentManager,
        onOkListener: (() -> Unit)? = null,
        onDismissCompletion: (() -> Unit)? = null
    ) {
        val confirmDialog = ConfirmDialog(onOkListener = {
            onOkListener?.invoke()
        })
        confirmDialog.onDismissCompletion = onDismissCompletion
        confirmDialog.show(manager, confirmDialog.tag)
    }

    fun makeUIActiveSpeed(
        manager: FragmentManager,
        onActiveListener: (() -> Unit)? = null,
        onDismissCompletion: (() -> Unit)? = null
    ) {
        val speedToFutureDialog = SpeedToFutureDialog(onActiveListener = onActiveListener)
        speedToFutureDialog.onDismissCompletion = onDismissCompletion
        speedToFutureDialog.show(manager, speedToFutureDialog.tag)
    }
}
