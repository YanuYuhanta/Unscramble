/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.yanuyuhantaunscramble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.yanuyuhantaunscramble.R
import com.example.android.yanuyuhantaunscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    // Kode program dibawah akan mengikat objek binding dengan akses ke tampilan di tata letak game_fragment.xml
    private lateinit var binding: GameFragmentBinding
    private val viewModel: GameViewModel by viewModels()

    //onCreateView() digunakan untuk meng-inflate XML tata letak game_fragment menggunakan objek binding.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    //pada kode program onViewCreated berfungsi untuk menyiapkan proses klik button dan
    // mengupdate ui
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setel viewModel untuk pengikatan data - ini memungkinkan akses tata letak terikat
        // ke semua data di ViewModel
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAKS_NAMA
        // Tentukan tampilan fragmen sebagai pemilik siklus proses pengikatan.
        // Ini digunakan agar pengikatan dapat mengamati pembaruan LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // pada kode program dibawah digunakan untuk mengatur tombol kirim dan lewati
        // dalam game
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
    }

    /*
     * kode funcction onSubmitWord digunakan untuk mengatur menampilkan
     * kata berikutnya yang ejaannya sudah diacak, serta menghapus kolom teks jika
     * pengejaan kata salah maupun benar dan untuk meningkatkan skor serta jumlah kata
     * tanpa memvalidasi kata pemain
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
     *  Pada function onSkipWord akan mengatur tombol lewati yang akan mengupdate
     *  kata dan menghapus kolom teks, namun tidak akan mengubah skor pemain
     */
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    /*
     * pada function showFinalScoreDialog digunakan untuk menampilkan skor dan
     * tombol untuk memulai ulang game atau keluar dari game
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.congratulations))
                .setMessage(getString(R.string.you_scored, viewModel.score.value))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.exit)) { _, _ ->
                    exitGame()
                }
                .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                    restartGame()
                }
                .show()
    }

    /*
     * pada function restartGame digunakan untuk mengisikan ulang data di ViewModel dan memperbarui
     * tampilan dengan data baru, untuk mulai ulang permainan.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    /*
     * pada function exitGame digunakan untuk mengatur agar permainan berakhir
     * dan keluar dari app
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * function setErrorTextField digunakan untuk mengatur penghapusan text
    * pada kolom dan mereset status error, serta digunakan untuk pertanda
    * jika pemain salah memasukkan ejaan yang benar maka akan muncul kata "coba lagi"
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
}
