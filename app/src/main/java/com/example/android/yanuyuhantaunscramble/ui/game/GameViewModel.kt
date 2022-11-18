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

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 *  ViewModel berisi data aplikasi dan metode untuk memproses data
 */
class GameViewModel : ViewModel() {
    // Pada kode program MutableLiveData digunakan untuk mengantisipasi perubahan logika
    // dari repository atau mengamati perubahan value dari class GameViewModel secara dinamis
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    // kode dibawah mengimplementasikan daftar kata kata yang digunakan dalam game
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        getNextWord()
    }

    /*
     * function getNextWord digunakan untk mengupdate kata yang diacak
     * dengan kata yang diacak berikutnya
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordsList.add(currentWord)
        }
    }

    /*
     * function dibawah berfungsi sebagai proses dalam function restartGame di
     * GameFragment yang akan melakukan update ulang data game untuk memulai ulang game
     */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    /*
    * function increaseScore berfungsi untuk mengupdate skor dalam game
    */
    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    /*
    * function dibawah berfungsi untuk melakukan validasi kata yang dimasukkan
    * benar atau salah jika benar maka skor akan ditambahkan.
    */
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /*
    * fuction dibawah berfungsi untuk melanjutkan ke kata berikutnya
    * jika ejaan kata benar
    */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAKS_NAMA) {
            getNextWord()
            true
        } else false
    }
}
