package com.example.tresenratlla


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameViewModel : ViewModel() {


    // Estado interno modificable del juego
    private val _board =
        MutableStateFlow(
            BoardModel.createInitialBoard()
        )


    // Estado público de solo lectura
    // La View puede observarlo pero no modificarlo directamente
    val board: StateFlow<BoardModel> =
        _board.asStateFlow()


    // Se ejecuta cuando el usuario pulsa una casilla
    fun onCellClick(position: Int) {

        val currentBoard = _board.value

        // Pedimos al Model que realice la jugada
        val newBoard =
            currentBoard.makeMove(position)

        // Guardamos el nuevo estado
        _board.value = newBoard
    }


    // Reinicia completamente la partida
    fun restartGame() {

        _board.value =
            BoardModel.createInitialBoard()
    }
}