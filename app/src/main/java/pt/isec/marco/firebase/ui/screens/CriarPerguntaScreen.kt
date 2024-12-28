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
fun T01_PerguntaVF(){
    var isChecked by remember { mutableStateOf<Boolean?>(null) }
    var isCheckedInvalid by remember { mutableStateOf(false) }
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
                    isChecked = if (it) true else null
                    isCheckedInvalid = false
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
                    isChecked = if (it) false else null
                    isCheckedInvalid = false
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
fun T02_PerguntaEM() {
    var selected by remember { mutableIntStateOf(2) }
    var selectedInvalid by remember { mutableStateOf(false) }
    val butoesLista = (2..6).toList()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            butoesLista.forEach { num ->
                Button(
                    onClick = {
                        selected = num
                        selectedInvalid = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(selected == num) Color.DarkGray else Color.LightGray
                    )
                ) {
                    Text(num.toString())
                }
            }
        }
        Text("Solução:")
        T02_Opcoes(selected)
    }
}

@Composable
fun T02_Opcoes(
    countButton: Int
) {
    var selected by remember { mutableStateOf<Int?>(null) }

    val nomes = remember(countButton) { mutableStateListOf(*Array(countButton) { "" }) }
    val isNomeInvalidList = remember(countButton) { mutableStateListOf(*Array(countButton) { false }) }
    var isCheckedInvalid by remember { mutableStateOf(false) }


    for (i in 0 until countButton) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selected == i,
                onCheckedChange = {
                    selected = if (it) i else null
                    isCheckedInvalid = false
                }
            )

            OutlinedTextField(
                value = nomes[i],
                isError = isNomeInvalidList[i],
                label = { Text("Resposta:") },
                onValueChange = { newText ->
                    nomes[i] = newText
                    isNomeInvalidList[i] = newText.isEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        if (isCheckedInvalid) {
            Text(
                text = "Por favor, selecione uma opção.",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}



@Composable
fun T03_PerguntaMC() {
    var selected by remember { mutableStateOf<Int?>(null) }
    var selectedInvalid by remember { mutableStateOf(false) }
    Text("Solução:")
}


@Composable
fun CriarPerguntaScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    tipoPerguntaSelecionada: Int
) {
    var nome by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf<Boolean?>(null) }
    var isNomeInvalid by remember { mutableStateOf(false) }
    var isCheckedInvalid by remember { mutableStateOf(false) }
    val errorMessage by viewModel.error


    // Função de validação que atualiza os estados de erro
    fun validarEntradas(): Boolean {
        var isValid = true
        if (nome.isEmpty()) {
            isNomeInvalid = true
            isValid = false
        } else {
            isNomeInvalid = false
        }

        if (isChecked == null) {
            isCheckedInvalid = true
            isValid = false
        } else {
            isCheckedInvalid = false
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

        when (tipoPerguntaSelecionada) {
            0 -> {
                T01_PerguntaVF()
            }
            1 -> {
                T02_PerguntaEM()
            }
            2 -> {
                T03_PerguntaMC()
            }
            else -> {
                Text("Tipo de pergunta desconhecido")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = {
//                fun getValidacaoParaTipo(tipo: Int): Boolean {
//                    return when (tipo) {
//                        0 -> validarP01()
//                        1 -> validarP02()
//                        2 -> validarP03()
//                        3 -> validarP04()
//                        4 -> validarP05()
//                        5 -> validarP06()
//                        6 -> validarP07()
//                        7 -> validarP08()
//                        else -> false
//                    }
//                }


                if (validarEntradas()) {
                    val tipoPergunta = when (tipoPerguntaSelecionada) {
                        0 -> "P01"
                        1 -> "P02"
                        2 -> "P03"
                        3 -> "P04"
                        4 -> "P05"
                        5 -> "P06"
                        6 -> "P07"
                        7 -> "P08"
                        else -> "-1"
                    }
                    viewModel.addPerguntaToFirestore(
                        Pergunta(
                            id = "",
                            titulo = nome,
                            imagem = "123",
                            respostas = listOf(""),
                            respostaCerta = listOf(isChecked.toString()),
                            tipo = tipoPergunta
                        )
                    )
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
