package com.maubis.scarlet.base.settings.sheet

import android.app.Dialog
import com.facebook.litho.ComponentContext
import com.maubis.scarlet.base.MainActivity
import com.maubis.scarlet.base.R
import com.maubis.scarlet.base.support.sheets.LithoOptionBottomSheet
import com.maubis.scarlet.base.support.sheets.LithoOptionsItem
import com.maubis.scarlet.base.support.utils.*

class InternalSettingsOptionsBottomSheet : LithoOptionBottomSheet() {
  override fun title(): Int = R.string.internal_settings_title

  override fun getOptions(componentContext: ComponentContext, dialog: Dialog): List<LithoOptionsItem> {
    val activity = context as MainActivity
    val options = ArrayList<LithoOptionsItem>()
    options.add(LithoOptionsItem(
        title = R.string.internal_settings_enable_log_exceptions_title,
        subtitle = R.string.internal_settings_enable_log_exceptions_description,
        icon = R.drawable.ic_note_white_48dp,
        listener = {
          sInternalLogTracesToNote = !sInternalLogTracesToNote
          reset(activity, dialog)
        },
        isSelectable = true,
        selected = sInternalLogTracesToNote
    ))
    options.add(LithoOptionsItem(
        title = R.string.internal_settings_enable_show_exceptions_title,
        subtitle = R.string.internal_settings_enable_show_exceptions_description,
        icon = R.drawable.icon_add_list,
        listener = {
          sInternalShowTracesInSheet = !sInternalShowTracesInSheet
          reset(activity, dialog)
        },
        isSelectable = true,
        selected = sInternalShowTracesInSheet
    ))
    options.add(LithoOptionsItem(
        title = R.string.internal_settings_enable_throw_exceptions_title,
        subtitle = R.string.internal_settings_enable_throw_exceptions_description,
        icon = R.drawable.ic_whats_new,
        listener = {
          sInternalThrowOnException = !sInternalThrowOnException
          sInternalThrownExceptionCount = 0
          reset(activity, dialog)
        },
        isSelectable = true,
        selected = sInternalThrowOnException
    ))
    options.add(LithoOptionsItem(
        title = R.string.internal_settings_fake_exceptions_title,
        subtitle = R.string.internal_settings_fake_exceptions_description,
        icon = R.drawable.ic_info,
        listener = {
          maybeThrow(activity, RuntimeException("Fake Exception for Testing"))
        }
    ))
    return options
  }
}