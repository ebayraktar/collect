package org.odk.collect.android.projects

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import org.odk.collect.android.R
import org.odk.collect.android.activities.ActivityUtils
import org.odk.collect.android.activities.MainMenuActivity
import org.odk.collect.android.configure.qr.AppConfigurationGenerator
import org.odk.collect.android.databinding.ManualProjectCreatorDialogLayoutBinding
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.android.utilities.DialogUtils
import org.odk.collect.android.utilities.SoftKeyboardController
import org.odk.collect.android.utilities.ToastUtils
import org.odk.collect.material.MaterialFullScreenDialogFragment
import javax.inject.Inject

class ManualProjectCreatorDialog : MaterialFullScreenDialogFragment() {

    @Inject
    lateinit var projectCreator: ProjectCreator

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    @Inject
    lateinit var softKeyboardController: SoftKeyboardController

    private lateinit var binding: ManualProjectCreatorDialogLayoutBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerUtils.getComponent(context).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ManualProjectCreatorDialogLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        binding.urlInputText.doOnTextChanged { text, _, _, _ ->
            binding.addButton.isEnabled = !text.isNullOrBlank()
        }

        binding.urlInputText.post {
            softKeyboardController.showSoftKeyboard(binding.urlInputText)
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.addButton.setOnClickListener {
            handleAddingNewProject()
        }

        binding.gdrive.setOnClickListener {
            showGdrivePlaceholderDialog()
        }
    }

    override fun onCloseClicked() {
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    private fun setUpToolbar() {
        toolbar.setTitle(R.string.add_project)
        toolbar.navigationIcon = null
    }

    private fun handleAddingNewProject() {
        val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
            binding.urlInputText.text?.trim().toString(),
            binding.usernameInputText.text?.trim().toString(),
            binding.passwordInputText.text?.trim().toString()
        )

        projectCreator.createNewProject(settingsJson)
        ActivityUtils.startActivityAndCloseAllOthers(activity, MainMenuActivity::class.java)
        ToastUtils.showLongToast(getString(org.odk.collect.projects.R.string.new_project_created))
    }

    private fun showGdrivePlaceholderDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Google Drive configuration coming soon")
            .setMessage("Google Drive configuration is not fully implemented in this beta!")
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()
        DialogUtils.showDialog(dialog, activity)
    }
}
