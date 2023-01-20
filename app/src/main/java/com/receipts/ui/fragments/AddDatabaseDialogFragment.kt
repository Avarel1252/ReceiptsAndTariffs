package com.receipts.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.receipts.R
import com.receipts.databinding.DialogAddDbBinding
import com.receipts.models.Repositories
import com.receipts.models.receipts.MainDb
import com.receipts.ui.lists.database.DatabasesViewModel


class AddDatabaseDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddDbBinding
    private val dbsViewModel: DatabasesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddDbBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            btnAdd.setOnClickListener {
                val newDatabase = "${etNameDb.text}.db"
                if (!Repositories.dbsRepository.databasesList.contains(newDatabase)) {
                    dbsViewModel.selectOrAdd(newDatabase)
                    dismiss()
                } else {
                    Toast.makeText(context, R.string.newDB_error, Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 2000L)
                }

            }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}