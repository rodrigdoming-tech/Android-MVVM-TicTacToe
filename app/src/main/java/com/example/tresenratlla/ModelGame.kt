package com.example.tresenratlla


// Representa una casilla del tablero
data class CellModel(
    val id: Int,
    val position: Int,
    val symbol: String = ""
)


// Representa todo el estado del tablero
data class BoardModel(

    val cells: List<CellModel>,

    val currentPlayer: String = "X",

    val winner: String? = null,

    val isDraw: Boolean = false,

    val gameOver: Boolean = false
) {


    companion object {

        // Crea el tablero inicial con 9 casillas vacías
        fun createInitialBoard(): BoardModel {

            val initialCells = List(9) { position ->

                CellModel(
                    id = position,
                    position = position,
                    symbol = ""
                )
            }

            return BoardModel(
                cells = initialCells
            )
        }
    }


    // Realiza una jugada en la posición indicada
    fun makeMove(position: Int): BoardModel {

        // Si el juego ha terminado no permitimos jugar
        if (gameOver) {
            return this
        }


        // Comprobamos que la posición exista
        if (position !in cells.indices) {
            return this
        }


        // Si la casilla ya está ocupada no hacemos nada
        if (cells[position].symbol != "") {
            return this
        }


        // Creamos una nueva lista de casillas
        // cambiando solamente la que se ha pulsado
        val newCells = cells.map { cell ->

            if (cell.position == position) {

                cell.copy(
                    symbol = currentPlayer
                )

            } else {

                cell
            }
        }


        // Creamos temporalmente un tablero con la nueva jugada
        val boardAfterMove = copy(
            cells = newCells
        )


        // Comprobamos si hay ganador
        val newWinner = boardAfterMove.checkWinner()


        // Hay empate si todas las casillas están llenas
        // y no existe ganador
        val draw =
            newWinner == null &&
                    newCells.all { cell ->
                        cell.symbol != ""
                    }


        // Comprobamos si la partida ha terminado
        val finished =
            newWinner != null || draw


        // Cambiamos el jugador solamente si la partida sigue
        val nextPlayer =

            if (finished) {

                currentPlayer

            } else if (currentPlayer == "X") {

                "O"

            } else {

                "X"
            }


        // copy() crea una nueva versión del tablero
        // sin modificar directamente el anterior
        return boardAfterMove.copy(

            currentPlayer = nextPlayer,

            winner = newWinner,

            isDraw = draw,

            gameOver = finished
        )
    }


    // Comprueba las posibles combinaciones ganadoras
    fun checkWinner(): String? {

        // Posiciones que permiten ganar
        val winningCombinations = listOf(

            // Filas
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),

            // Columnas
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),

            // Diagonales
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )


        for (combination in winningCombinations) {

            val position1 = combination[0]
            val position2 = combination[1]
            val position3 = combination[2]


            val symbol1 = cells[position1].symbol
            val symbol2 = cells[position2].symbol
            val symbol3 = cells[position3].symbol


            // Si las tres casillas tienen el mismo símbolo
            // y no están vacías, tenemos ganador
            if (
                symbol1 != "" &&
                symbol1 == symbol2 &&
                symbol2 == symbol3
            ) {

                return symbol1
            }
        }


        // Si ninguna combinación gana devolvemos null
        return null
    }
}