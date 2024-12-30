package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta


@Composable
fun T01_PerguntaVF(
    isChecked: Boolean?,
    isCheckedInvalid: Boolean,
    onCheckedChange: (Boolean?) -> Unit
){
    Text("Solução:")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Verdadeiro")
            Checkbox(
                checked = isChecked == true,
                onCheckedChange = {
                    onCheckedChange(if (it) true else null)
                }
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Falso")
            Checkbox(
                checked = isChecked == false,
                onCheckedChange = {
                    onCheckedChange(if (it) false else null)
                }
            )
        }
    }
    if (isCheckedInvalid) {
        Text(
            text = "Por favor, selecione uma opção.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun T02_PerguntaEM(
    selected: Int,
    isChecked: Int?,
    selectedInvalid: Boolean,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onCheckedChange: (Int?) -> Unit,
    onNomeChange: (Int, String) -> Unit,
    onCountChange: (Int) -> Unit
) {
    val butoesLista = (2..6).toList()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BotoesLista(butoesLista, selected, onCountChange)
        Text("Solução:")
            T02_Opcoes(
                selected,
                isChecked,
                selectedInvalid,
                nomes,
                isNomeInvalidList,
                onCheckedChange,
                onNomeChange
            )
    }
}

@Composable
fun T03_PerguntaEM(
    selected: Int,
    isChecked: List<Int>,
    selectedInvalid: Boolean,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onCheckedChange: (List<Int>) -> Unit,
    onNomeChange: (Int, String) -> Unit,
    onCountChange: (Int) -> Unit
) {
    val butoesLista = (2..6).toList()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BotoesLista(butoesLista, selected, onCountChange)
        Text("Solução:")
        T03_Opcoes(
            selected,
            isChecked,
            selectedInvalid,
            nomes,
            isNomeInvalidList,
            onCheckedChange,
            onNomeChange
        )
    }
}

@Composable
fun BotoesLista(
    butoesLista: List<Int> = (2..6).toList(),
    selected: Int? = null,
    onCountChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        butoesLista.forEach { num ->
            Button(
                onClick = {
                    onCountChange(num)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(selected == num) Color.DarkGray else Color.LightGray
                )
            ) {
                Text(num.toString())
            }
        }
    }
}

@Composable
fun T04_PerguntaCorrespondecia(
    selected: Int,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onNomeChange: (Int, String) -> Unit,
    onCountChange: (Int) -> Unit
) {
    val butoesLista = (2..6).toList()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BotoesLista(butoesLista, selected, onCountChange)
        Text("Solução:")
        T04_Opcoes(
            selected,
            nomes,
            isNomeInvalidList,
            onNomeChange
        )
    }


}

@Composable
fun T04_Opcoes(
    countButton: Int,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onNomeChange: (Int, String) -> Unit
) {
    for (i in 0 until countButton){
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            OutlinedTextField(
                value = nomes.getOrElse(i) { "" },
                onValueChange = { newText -> onNomeChange(i, newText) },
                label = { Text("${i + 1}. ") },
                isError = isNomeInvalidList.getOrElse(i) { false },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                value = nomes.getOrElse(i + countButton) { "" },
                onValueChange = { newText -> onNomeChange(i + countButton, newText) },
                label = { Text("${('A' + i)}. ") },
                isError = isNomeInvalidList.getOrElse(i + countButton) { false },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun OutlinedTextField(
    text : String,
    i: Int,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onNomeChange: (Int, String) -> Unit
){
    OutlinedTextField(
        value = nomes[i],
        isError = isNomeInvalidList[i],
        label = { Text(text) },
        onValueChange = { newText ->
            onNomeChange(i, newText)
        },
        modifier = Modifier
            .fillMaxWidth()
    )

}

@Composable
fun T02_Opcoes(
    countButton: Int,
    selected: Int?,
    isCheckedInvalid: Boolean,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onCheckedChange: (Int?) -> Unit,
    onNomeChange: (Int, String) -> Unit
) {
    for (i in 0 until countButton) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected == i,
                onCheckedChange = {
                    onCheckedChange(if (it) i else null)
                }
            )
            OutlinedTextField("Resposta :",i, nomes, isNomeInvalidList, onNomeChange)
        }
        if (isCheckedInvalid && i == countButton - 1) {
            Text(
                text = "Por favor, selecione uma opção.",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Composable
fun T03_Opcoes(
    countButton: Int,
    selected: List<Int>,
    isCheckedInvalid: Boolean,
    nomes: List<String>,
    isNomeInvalidList: List<Boolean>,
    onCheckedChange: (List<Int>) -> Unit,
    onNomeChange: (Int, String) -> Unit
) {
    for (i in 0 until countButton) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected.contains(i),
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        onCheckedChange(selected + i)
                    } else {
                        onCheckedChange(selected.filter { it != i })
                    }
                }
            )
            OutlinedTextField("Resposta: ",i, nomes, isNomeInvalidList, onNomeChange)
        }

        if (isCheckedInvalid && i == countButton - 1) {
            Text(
                text = "Por favor, selecione uma opção.",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Composable
fun CriarPerguntaScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    tipoPerguntaSelecionada: Int
) {
    var nome by remember { mutableStateOf("") }
    var isNomeInvalid by remember { mutableStateOf(false) }
    // P01 -------------
    var isChecked01 by remember { mutableStateOf<Boolean?>(null) }
    var isCheckedInvalid01 by remember { mutableStateOf(false) }
    // P02---------------
    var isChecked02 by remember { mutableStateOf<Int?>(null) }
    var isCheckedInvalid02 by remember { mutableStateOf(false) }
    var countButton by remember { mutableIntStateOf(2) }
    val nomes = remember(countButton) { mutableStateListOf(*Array(countButton) { "" }) }
    val isNomeInvalidList = remember(countButton) { mutableStateListOf(*Array(countButton) { false }) }
    // P03---------------
    var isChecked03 by remember { mutableStateOf<List<Int>>(emptyList()) }
    // P04---------------
    val nomes04 = remember(countButton) { mutableStateListOf(*Array(countButton*2) { "" }) }
    val isNomeInvalidList04 = remember(countButton) { mutableStateListOf(*Array(countButton*2) { false }) }
    val errorMessage by viewModel.error
    var isEntradaValida by remember { mutableStateOf(false) }

    fun validarP01(): Boolean {
        var isValid = true

        if (isChecked01 == null) {
            isCheckedInvalid01 = true
            isValid = false
        } else {
            isCheckedInvalid01 = false
        }
        return isValid
    }

    fun validarP02(): Boolean {
        var isValid = true

        if (isChecked02 == null) {
            isCheckedInvalid02 = true
            isValid = false
        } else {
            isCheckedInvalid02 = false
        }
        for(i in 0 until countButton) {
            if (nomes[i].isEmpty()) {
                isNomeInvalidList[i] = true
                isValid = false
            } else {
                isNomeInvalidList[i] = false
            }
        }
        return isValid
    }

    fun validarP03(): Boolean{
        var isValid = true

        if (isChecked03.isEmpty()) {
            isCheckedInvalid02 = true
            isValid = false
        } else {
            isCheckedInvalid02 = false
        }
        for(i in 0 until countButton) {
            if (nomes[i].isEmpty()) {
                isNomeInvalidList[i] = true
                isValid = false
            } else {
                isNomeInvalidList[i] = false
            }
        }
        return isValid
    }

    fun validarP04(): Boolean{
        var isValid = true
        for(i in 0 until countButton*2) {
            if (nomes04[i].isEmpty()) {
                isNomeInvalidList04[i] = true
            } else {
                isNomeInvalidList04[i] = false
            }
        }
        return isValid
    }


    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = nome,
            isError = isNomeInvalid,
            label = { Text("Pergunta:") },
            onValueChange = { newText ->
                nome = newText
                isNomeInvalid = false
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        if (isNomeInvalid) {
            Text(
                text = "Este campo é obrigatório.",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        var isValid = true
        if (nome.isEmpty()) {
            isNomeInvalid = true
            isValid = false
        } else {
            isNomeInvalid = false
        }
        if (isValid) {
            when (tipoPerguntaSelecionada) {
                0 -> {
                    T01_PerguntaVF(
                        isChecked = isChecked01,
                        isCheckedInvalid = isCheckedInvalid01,
                        onCheckedChange = { novoValor -> isChecked01 = novoValor }
                    )
                }
                1 -> {
                    T02_PerguntaEM(
                        selected = countButton,
                        isChecked = isChecked02,
                        selectedInvalid = isCheckedInvalid02,
                        onCheckedChange = { novoValor -> isChecked02 = novoValor },
                        nomes = nomes,
                        isNomeInvalidList = isNomeInvalidList,
                        onNomeChange = { index, novoValor ->
                            nomes[index] = novoValor
                            isNomeInvalidList[index] = false
                        },
                        onCountChange = { novoValor -> countButton = novoValor }
                    )
                }
                2 -> {
                    T03_PerguntaEM(
                        selected = countButton,
                        isChecked = isChecked03,
                        selectedInvalid = isCheckedInvalid02,
                        onCheckedChange = { novoValor -> isChecked03 = novoValor },
                        nomes = nomes,
                        isNomeInvalidList = isNomeInvalidList,
                        onNomeChange = { index, novoValor ->
                            nomes[index] = novoValor
                            isNomeInvalidList[index] = false
                        },
                        onCountChange = { novoValor -> countButton = novoValor })
                }
                3 -> {
                    T04_PerguntaCorrespondecia(
                        selected = countButton,
                        nomes = nomes04,
                        isNomeInvalidList = isNomeInvalidList04,
                        onNomeChange = { index, novoValor ->
                            nomes04[index] = novoValor
                            isNomeInvalidList04[index] = false
                        },
                        onCountChange = { novoValor -> countButton = novoValor }
                    )
                }
                else -> {
                    Text("Tipo de pergunta desconhecido")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        var pergunta by remember { mutableStateOf<Pergunta?>(null) }
        Button(
            onClick = {
                when(tipoPerguntaSelecionada){
                    0 -> {
                        isEntradaValida = validarP01()
                        pergunta = Pergunta(
                            id = "",
                            titulo = nome,
                            imagem = "123",
                            respostas = listOf(""),
                            respostaCerta = listOf(isChecked01.toString()),
                            tipo = "P01"
                        )
                    }
                    1 -> {
                        isEntradaValida = validarP02()
                        pergunta = Pergunta(
                            id = "",
                            titulo = nome,
                            imagem = "123",
                            respostas = nomes,
                            respostaCerta = listOf(isChecked02.toString()),
                            tipo = "P02"
                        )
                    }
                    2 -> {
                        val respostasCertas = List(isChecked03.size) { "" }.toMutableList()

                        if (isChecked03.isNotEmpty()) {
                            isChecked03.forEachIndexed { index, item ->
                                respostasCertas[index] = isChecked03[index].toString()
                            }
                        }
                        isEntradaValida = validarP03()
                        pergunta = Pergunta(
                            id = "",
                            titulo = nome,
                            imagem = "123",
                            respostas = nomes,
                            respostaCerta = respostasCertas,
                            tipo = "P03"
                        )
                    }
                    3 -> {
                        val primeiraMetade = nomes04.take(nomes04.size / 2).shuffled()
                        val segundaMetade = nomes04.drop(nomes04.size / 2).shuffled()
                        val nomes04shuffled = primeiraMetade + segundaMetade
                        isEntradaValida = validarP04()
                        pergunta = Pergunta(
                            id = "",
                            titulo = nome,
                            imagem = "123",
                            respostas = nomes04shuffled,
                            respostaCerta = nomes04,
                            tipo = "P04"
                        )
                    }
                }

                if (isEntradaValida) {
                    pergunta?.let {
                        viewModel.addPerguntaToFirestore(
                            it
                        )
                    }
                    if(errorMessage == null) {
                        navController.navigate("criar-questionario") {
                            popUpTo("criar-questionario") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Guardar")
        }
    }
}


