package com.example.mynailproject.signin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.mynailproject.R


class VerifyEmaillDialogFragment : DialogFragment() {

    private var desc: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        desc = "Чтобы продолжить, необходимо подверить аккаунт. Вам на почту отправлено сообщение. Перейдите по ссылке, чтобы подвердить вашу электронную почту."
        return builder
            .setTitle("Подтвержение аккаунта")
            .setIcon(R.drawable.ic_baseline_mark_email_read_24)
            .setMessage(desc)
            .setPositiveButton("OK", null)
            .create();

    }
}