package com.maubis.scarlet.base.export.sheet

import android.app.Dialog
import com.facebook.litho.ComponentContext
import com.github.bijoysingh.starter.util.IntentUtils
import com.maubis.scarlet.base.MainActivity
import com.maubis.scarlet.base.R
import com.maubis.scarlet.base.config.ApplicationBase
import com.maubis.scarlet.base.export.activity.ImportNoteActivity
import com.maubis.scarlet.base.export.support.PermissionUtils
import com.maubis.scarlet.base.main.sheets.EnterPincodeBottomSheet
import com.maubis.scarlet.base.settings.sheet.SecurityOptionsBottomSheet
import com.maubis.scarlet.base.support.sheets.LithoOptionBottomSheet
import com.maubis.scarlet.base.support.sheets.LithoOptionsItem
import com.maubis.scarlet.base.support.sheets.openSheet
import com.maubis.scarlet.base.support.ui.ThemedActivity
import com.maubis.scarlet.base.support.utils.Flavor

class BackupSettingsOptionsBottomSheet : LithoOptionBottomSheet() {
  override fun title(): Int = R.string.home_option_backup_options

  override fun getOptions(componentContext: ComponentContext, dialog: Dialog): List<LithoOptionsItem> {
    val activity = context as MainActivity
    val options = ArrayList<LithoOptionsItem>()
    options.add(LithoOptionsItem(
        title = R.string.home_option_install_from_store,
        subtitle = R.string.home_option_install_from_store_subtitle,
        icon = R.drawable.ic_action_play,
        listener = {
          IntentUtils.openAppPlayStore(context)
          dismiss()
        },
        visible = ApplicationBase.instance.appFlavor() == Flavor.NONE
    ))
    options.add(LithoOptionsItem(
        title = R.string.home_option_export,
        subtitle = R.string.home_option_export_subtitle,
        icon = R.drawable.ic_export,
        listener = {
          val manager = PermissionUtils().getStoragePermissionManager(activity)
          val hasAllPermissions = manager.hasAllPermissions()
          when (hasAllPermissions) {
            true -> {
              openExportSheet(activity)
              dismiss()
            }
            false -> {
              openSheet(activity, PermissionBottomSheet())
            }
          }
        }
    ))
    options.add(LithoOptionsItem(
        title = R.string.home_option_import,
        subtitle = R.string.home_option_import_subtitle,
        icon = R.drawable.ic_import,
        listener = {
          val manager = PermissionUtils().getStoragePermissionManager(activity)
          val hasAllPermissions = manager.hasAllPermissions()
          when (hasAllPermissions) {
            true -> {
              IntentUtils.startActivity(activity, ImportNoteActivity::class.java)
              dismiss()
            }
            false -> {
              openSheet(activity, PermissionBottomSheet())
            }
          }
        }
    ))
    options.add(LithoOptionsItem(
        title = R.string.import_export_layout_folder_sync,
        subtitle = R.string.import_export_layout_folder_sync_details,
        icon = R.drawable.icon_folder_sync,
        listener = {
          val manager = PermissionUtils().getStoragePermissionManager(activity)
          val hasAllPermissions = manager.hasAllPermissions()
          when (hasAllPermissions) {
            true -> {
              openSheet(activity, ExternalFolderSyncBottomSheet())
            }
            false -> openSheet(activity, PermissionBottomSheet())
          }
        }
    ))
    return options
  }

  private fun openExportSheet(activity: MainActivity) {
    if (!SecurityOptionsBottomSheet.hasPinCodeEnabled()) {
      openSheet(activity, ExportNotesBottomSheet())
      return
    }
    EnterPincodeBottomSheet.openUnlockSheet(
        activity as ThemedActivity,
        object : EnterPincodeBottomSheet.PincodeSuccessListener {
          override fun onFailure() {
            openExportSheet(activity)
          }

          override fun onSuccess() {
            openSheet(activity, ExportNotesBottomSheet())
          }
        })
  }
}