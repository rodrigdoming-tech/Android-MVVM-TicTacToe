# Android MVVM TicTacToe

## 1. Anàlisi i documentació de l'arquitectura MVVM

### 1.1. Components de l'arquitectura MVVM

#### Model

El Model és la part que guarda les dades de l'aplicació i la lògica principal del joc.

En el nostre Tres en ratlla, el Model guarda les 9 caselles del tauler, el jugador que té el torn, el guanyador, si hi ha empat i si la partida ha acabat.

També conté la lògica per fer una jugada i comprovar si algun jugador ha guanyat.

#### ViewModel

El ViewModel és la part que comunica el Model amb la View.

La seva funció és mantenir l'estat del joc i oferir a la interfície els mètodes que necessita, per exemple quan un jugador prem una casella o quan vol reiniciar la partida.

En el nostre projecte utilitzem `MutableStateFlow` per modificar l'estat intern i `StateFlow` perquè la View pugui observar-lo sense modificar-lo directament.

#### View

La View és la part visual de l'aplicació.

En aquest projecte està feta amb Jetpack Compose i mostra el tauler de 3x3, el torn actual, el guanyador o empat i el botó per reiniciar la partida.

La View llegeix l'estat del ViewModel amb `collectAsState()` i quan l'usuari prem una casella crida el mètode corresponent del ViewModel.

---

### 1.2. Comparació entre MVVMSimple i MVVM-separatModel

#### Estructura i separació de responsabilitats

A `MVVMSimple`, el Model és molt senzill i el ViewModel conté pràcticament tota la lògica.

En canvi, a `MVVM-separatModel`, el Model està separat i conté la seva pròpia lògica de negoci.

Això permet que el ViewModel sigui més senzill i només s'encarregui de gestionar l'estat i comunicar el Model amb la View.

En el nostre Tres en ratlla hem utilitzat aquesta segona estructura.

#### On s'executa la lògica?

A `MVVMSimple`, gran part de la lògica es troba dins del ViewModel.

A `MVVM-separatModel`, la lògica principal es troba dins del Model.

En el nostre projecte, els mètodes `makeMove()` i `checkWinner()` estan dins de `BoardModel`.

#### Immutabilitat i `.copy()`

En el projecte no modifiquem directament els objectes antics.

Utilitzem `.copy()` per crear una nova versió de l'objecte amb els valors que han canviat.

Per exemple, quan un jugador prem una casella es crea una còpia de la casella amb el símbol `"X"` o `"O"`.

Això és útil perquè Compose detecta els canvis d'estat i actualitza la pantalla.

#### Avantatges de separar el Model

Separar la lògica del Model del ViewModel fa que el codi sigui més ordenat.

També permet modificar la interfície sense haver de canviar la lògica del joc i facilita trobar errors.

---

### 1.3. StateFlow i mutableStateOf

#### StateFlow / MutableStateFlow

`MutableStateFlow` permet guardar un estat que pot anar canviant.

El ViewModel modifica aquest estat i la View l'observa amb `collectAsState()`.

En el nostre projecte utilitzem:

`MutableStateFlow<BoardModel>`

per guardar l'estat del tauler.

#### mutableStateOf / State

`mutableStateOf` també permet guardar un estat reactiu, però està més relacionat directament amb Jetpack Compose.

Quan el valor canvia, Compose torna a dibuixar les parts de la pantalla que depenen d'aquest valor.

#### Diferència

Els dos serveixen per gestionar estat reactiu.

`mutableStateOf` és més simple per aplicacions petites, mentre que `StateFlow` és útil quan volem separar millor el ViewModel de la interfície.

Per aquest projecte hem utilitzat `StateFlow` perquè és el sistema que es demana a l'enunciat i encaixa bé amb l'arquitectura MVVM.

## 2. Arquitectura utilitzada en el Tres en Ratlla

Per desenvolupar el Tres en Ratlla he utilitzat l'arquitectura MVVM amb el Model separat.

El projecte està dividit principalment en quatre fitxers:

### ModelGame.kt

Aquest fitxer conté el Model del joc.

`CellModel` representa una casella del tauler i guarda el seu identificador, la posició i el símbol que conté (`X`, `O` o buit).

`BoardModel` guarda l'estat complet de la partida: les 9 caselles, el jugador actual, el guanyador, si hi ha empat i si el joc ha acabat.

El mètode `createInitialBoard()` crea un tauler inicial amb les 9 caselles buides.

El mètode `makeMove()` permet fer una jugada en una casella. També comprova que la casella estigui buida i que la partida no hagi acabat.

El mètode `checkWinner()` comprova les files, columnes i diagonals per saber si algun jugador ha guanyat.

### GameViewModel.kt

El `GameViewModel` comunica el Model amb la interfície.

Utilitza un `MutableStateFlow` privat per guardar l'estat del tauler i l'exposa com un `StateFlow` perquè la View el pugui consultar però no modificar directament.

`onCellClick()` s'executa quan el jugador prem una casella.

`restartGame()` crea un tauler nou i permet començar una altra partida.

### GameScreen.kt

Aquest fitxer conté la interfície feta amb Jetpack Compose.

`GameScreen()` és la pantalla principal i utilitza `collectAsState()` per observar els canvis del tauler.

`BoardGrid()` crea les 3 files i 3 columnes del tauler.

`CellItemView()` representa cadascuna de les caselles i permet que el jugador les premi.

També es mostra el torn actual, el guanyador, l'empat i el botó per reiniciar la partida.

### MainActivity.kt

És el punt d'entrada de l'aplicació.

Crea el `GameViewModel` amb `by viewModels()` i utilitza `setContent` per mostrar la interfície creada amb Jetpack Compose.

### Funcionament general

Quan el jugador prem una casella, `GameScreen` crida `onCellClick()` del ViewModel.

El ViewModel demana al `BoardModel` que faci la jugada amb `makeMove()`.

El Model retorna un nou estat i el ViewModel actualitza el `StateFlow`.

`GameScreen` detecta el canvi amb `collectAsState()` i Compose actualitza la pantalla.

### Captura de l'aplicació

![captura_tres_en_ratlla.png](C:\Users\xro9097\AndroidStudioProjects\tresEnRatlla\captura_tres_en_ratlla.png)

---

## 3. Conceptes i Mètodes Apresos

Durant aquesta pràctica he utilitzat alguns conceptes que no havia utilitzat abans.

### MutableStateFlow

`MutableStateFlow` permet guardar un valor que pot anar canviant durant l'execució de l'aplicació.

En aquest projecte guarda l'estat actual del tauler.

Exemple:

```kotlin
private val _board =
    MutableStateFlow(BoardModel.createInitialBoard())
```

Quan assignem un nou valor:

```kotlin
_board.value = newBoard
```

la View pot detectar el canvi.

### StateFlow i asStateFlow()

`StateFlow` permet que la interfície pugui observar l'estat.

Utilitzem `asStateFlow()` perquè des de fora del ViewModel no es pugui modificar directament el `MutableStateFlow`.

Exemple:

```kotlin
val board: StateFlow<BoardModel> =
    _board.asStateFlow()
```

D'aquesta manera, el ViewModel pot modificar `_board`, però la View només pot consultar `board`.

### collectAsState()

`collectAsState()` serveix per llegir un `StateFlow` des de Jetpack Compose.

Exemple:

```kotlin
val board by viewModel.board.collectAsState()
```

Quan canvia el valor de `board`, Compose actualitza automàticament la part de la interfície que utilitza aquest valor.

### copy()

Les `data class` de Kotlin tenen el mètode `.copy()`.

Permet crear un objecte nou a partir d'un altre canviant només alguns valors.

Exemple:

```kotlin
val newCell = cell.copy(
    symbol = "X"
)
```

La casella original no es modifica directament, sinó que obtenim una nova casella amb el símbol `X`.

En aquesta pràctica és útil perquè treballem amb estats immutables.

### @Composable

`@Composable` indica que una funció forma part de la interfície creada amb Jetpack Compose.

Per exemple:

```kotlin
@Composable
fun GameScreen() {
    Text("Tres en ratlla")
}
```

Compose utilitza aquestes funcions per crear i actualitzar la pantalla.

### by viewModels()

`by viewModels()` permet obtenir un ViewModel associat a una Activity.

En el projecte l'hem utilitzat així:

```kotlin
private val viewModel:
    GameViewModel by viewModels()
```

Així `MainActivity` pot passar el mateix ViewModel a `GameScreen`.

### Conclusió

Amb aquesta pràctica he après millor com separar una aplicació utilitzant MVVM.

El Model conté les dades i la lògica del joc, el ViewModel gestiona l'estat i comunica les diferents parts, i la View mostra les dades i envia les accions de l'usuari.