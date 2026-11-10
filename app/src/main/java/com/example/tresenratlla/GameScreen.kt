package com.example.tresenratlla


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {

    // collectAsState observa el StateFlow.
    // Cuando cambia el tablero, Compose actualiza la pantalla.
    val board by viewModel.board.collectAsState()


    Column(
        modifier = modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {


        Text(
            text = "Tres en ratlla",
            fontSize = 32.sp
        )


        Spacer(
            modifier = Modifier.height(30.dp)
        )


        // Mostramos un mensaje distinto
        // dependiendo del estado del juego
        when {

            board.winner != null -> {

                Text(
                    text = "Ha guanyat ${board.winner}!",
                    fontSize = 24.sp
                )
            }


            board.isDraw -> {

                Text(
                    text = "Empat!",
                    fontSize = 24.sp
                )
            }


            else -> {

                Text(
                    text = "Torn del jugador ${board.currentPlayer}",
                    fontSize = 22.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(30.dp)
        )


        BoardGrid(
            board = board,

            onCellClick = { position ->

                viewModel.onCellClick(position)
            }
        )


        Spacer(
            modifier = Modifier.height(30.dp)
        )


        Button(
            onClick = {

                viewModel.restartGame()
            }
        ) {

            Text(
                text = "Reiniciar"
            )
        }
    }
}


// Dibuja el tablero completo de 3x3
@Composable
fun BoardGrid(
    board: BoardModel,
    onCellClick: (Int) -> Unit
) {

    Column {

        // Creamos 3 filas
        for (row in 0..2) {

            Row {

                // Cada fila tiene 3 columnas
                for (column in 0..2) {

                    // Convertimos fila y columna
                    // en una posición de 0 a 8
                    val position =
                        row * 3 + column


                    CellItemView(
                        cell = board.cells[position],

                        gameOver = board.gameOver,

                        onClick = {

                            onCellClick(position)
                        }
                    )
                }
            }
        }
    }
}


// Representa visualmente una casilla
@Composable
fun CellItemView(
    cell: CellModel,
    gameOver: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        // No se puede pulsar si está ocupada
        // o si la partida ya ha terminado
        enabled =
            cell.symbol == "" &&
                    !gameOver,

        modifier = Modifier
            .size(100.dp)
    ) {

        Text(
            text = cell.symbol,
            fontSize = 40.sp
        )
    }
}