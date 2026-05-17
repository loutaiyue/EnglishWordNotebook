package com.example.wordnotebook.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordnotebook.data.local.WordLocalRepository
import com.example.wordnotebook.data.local.WordNotebookDatabase
import com.example.wordnotebook.data.remote.BaiduTranslateRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WordItem(
    val id: Int,
    val word: String,
    val phonetic: String = "",
    val meaning: String,
    val isFavorite: Boolean = false
)

data class WordNotebookUiState(
    val inputWord: String = "",
    val currentWord: WordItem? = null,
    val favoriteWords: List<WordItem> = emptyList(),
    val recentQueries: List<WordItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDarkMode: Boolean = false
)

class WordNotebookViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val remoteRepository = BaiduTranslateRepository()
    private val localRepository = WordLocalRepository(
        WordNotebookDatabase.getInstance(application).wordRecordDao()
    )

    private val _uiState = MutableStateFlow(WordNotebookUiState())
    val uiState: StateFlow<WordNotebookUiState> = _uiState.asStateFlow()

    private var queryJob: Job? = null

    init {
        observeLocalRecords()
    }

    fun onWordInputChanged(newWord: String) {
        _uiState.update {
            it.copy(
                inputWord = newWord,
                errorMessage = null
            )
        }

        queryJob?.cancel()
        if (newWord.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentWord = null,
                    errorMessage = null
                )
            }
            return
        }

        queryJob = viewModelScope.launch {
            delay(450)
            translate(newWord.trim())
        }
    }

    fun onToggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun onToggleFavorite() {
        val current = uiState.value.currentWord ?: return

        viewModelScope.launch {
            val isFavoriteNow = localRepository.toggleFavorite(
                word = current.word,
                fallbackMeaning = current.meaning,
                fallbackPhonetic = current.phonetic
            )
            _uiState.update { state ->
                state.currentWord?.let { cw ->
                    state.copy(currentWord = cw.copy(isFavorite = isFavoriteNow))
                } ?: state
            }
        }
    }

    private fun observeLocalRecords() {
        viewModelScope.launch {
            localRepository.observeFavorites().collect { entities ->
                val favorites = entities.map { entity ->
                    WordItem(
                        id = entity.word.hashCode(),
                        word = entity.word,
                        phonetic = entity.phonetic,
                        meaning = entity.meaning,
                        isFavorite = entity.isFavorite
                    )
                }
                _uiState.update { state ->
                    val currentWord = state.currentWord
                    if (currentWord != null) {
                        val currentFavorite = favorites.any { it.word.equals(currentWord.word, ignoreCase = true) }
                        state.copy(
                            favoriteWords = favorites,
                            currentWord = currentWord.copy(isFavorite = currentFavorite)
                        )
                    } else {
                        state.copy(favoriteWords = favorites)
                    }
                }
            }
        }

        viewModelScope.launch {
            localRepository.observeRecentQueries(limit = 3).collect { entities ->
                val recent = entities.map { entity ->
                    WordItem(
                        id = entity.word.hashCode(),
                        word = entity.word,
                        phonetic = entity.phonetic,
                        meaning = entity.meaning,
                        isFavorite = entity.isFavorite
                    )
                }
                _uiState.update { it.copy(recentQueries = recent) }
            }
        }
    }

    private suspend fun translate(word: String) {
        if (!word.all { it.isLetter() || it.isWhitespace() || it == '-' || it == '\'' }) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "请输入英文单词") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        remoteRepository.translateEnToZh(word)
            .onSuccess { translated ->
                localRepository.saveQuery(word = word, meaning = translated)
                val isFavorite = localRepository.isFavorite(word)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        currentWord = WordItem(
                            id = word.hashCode(),
                            word = word,
                            meaning = translated,
                            isFavorite = isFavorite
                        )
                    )
                }
            }
            .onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "查询失败，请稍后重试"
                    )
                }
            }
    }
}
